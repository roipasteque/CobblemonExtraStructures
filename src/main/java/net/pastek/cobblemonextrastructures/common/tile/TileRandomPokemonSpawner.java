package net.pastek.cobblemonextrastructures.common.tile;

import com.cobblemon.mod.common.CobblemonEntities;
import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.abilities.Abilities;
import com.cobblemon.mod.common.api.abilities.Ability;
import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.moves.MoveTemplate;
import com.cobblemon.mod.common.api.moves.Moves;
import com.cobblemon.mod.common.api.pokemon.Natures;
import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import com.cobblemon.mod.common.api.pokemon.stats.Stat;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.pastek.cobblemonextrastructures.ExtraStructures;
import net.pastek.cobblemonextrastructures.prefab.configuration.EXConfigurationHandler;
import net.pastek.cobblemonextrastructures.registers.EXTiles;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TileRandomPokemonSpawner extends BlockEntity {

    private double triggerRadius = 5.0;
    private List<SpawnEntry> spawnEntries = new ArrayList<>();

    private UUID spawnedPokemonUUID = null;
    private boolean hasSpawnedOnce = false;
    private int currentCooldown = 0;

    public TileRandomPokemonSpawner(BlockPos pos, BlockState state) {
        super(EXTiles.RANDOM_POKEMON_SPAWNER, pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, TileRandomPokemonSpawner entity) {
        if (level.isClientSide) return;

        int configCooldown = EXConfigurationHandler.getInstance().random_spawner_cooldown;

        if (configCooldown == -1 && entity.hasSpawnedOnce) {
            return;
        }

        if (entity.currentCooldown > 0) {
            entity.currentCooldown--;
            return;
        }

        if (entity.spawnEntries.isEmpty()) return;

        AABB triggerBox = new AABB(pos).inflate(entity.triggerRadius);
        List<Player> nearbyPlayers = level.getEntitiesOfClass(Player.class, triggerBox,
                player -> !player.isCreative() && !player.isSpectator());

        if (!nearbyPlayers.isEmpty()) {
            if (entity.isSpawnAlive((ServerLevel) level)) {
                entity.currentCooldown = 200;
                return;
            }

            entity.spawnRandomPokemon();
            entity.hasSpawnedOnce = true;
            entity.currentCooldown = Math.max(0, configCooldown);
            entity.setChanged();
        }
    }

    private boolean isSpawnAlive(ServerLevel serverLevel) {
        if (this.spawnedPokemonUUID == null) return false;
        Entity existing = serverLevel.getEntity(this.spawnedPokemonUUID);
        return existing != null && existing.isAlive();
    }

    private void spawnRandomPokemon() {
        if (level == null || level.isClientSide || spawnEntries.isEmpty()) return;

        int totalWeight = spawnEntries.stream().mapToInt(e -> Math.max(0, e.weight)).sum();
        if (totalWeight <= 0) return;

        int randomValue = level.random.nextInt(totalWeight);
        SpawnEntry selectedEntry = null;

        for (SpawnEntry entry : spawnEntries) {
            randomValue -= Math.max(0, entry.weight);
            if (randomValue < 0) {
                selectedEntry = entry;
                break;
            }
        }

        if (selectedEntry == null) return;

        try {
            PokemonProperties props = PokemonProperties.Companion.parse(selectedEntry.species);
            Pokemon pokemon = props.create();

            if (selectedEntry.pokemonLevel <= 0) {
                pokemon.setLevel(level.random.nextInt(100) + 1);
            } else {
                pokemon.setLevel(selectedEntry.pokemonLevel);
            }

            if (!selectedEntry.ability.isEmpty()) {
                ResourceLocation abilityId = ResourceLocation.fromNamespaceAndPath("cobblemon", selectedEntry.ability.toLowerCase().replace(" ", ""));
                var abilityTemplate = Abilities.INSTANCE.get(abilityId.getPath());
                if (abilityTemplate != null) {
                    pokemon.setAbility$common(new Ability(abilityTemplate, false, Priority.LOW));
                }
            }

            String[] customMoves = {selectedEntry.move1, selectedEntry.move2, selectedEntry.move3, selectedEntry.move4};
            boolean hasCustomMoves = false;
            for (String m : customMoves) if (!m.isEmpty()) hasCustomMoves = true;

            if (hasCustomMoves) {
                pokemon.getMoveSet().clear();
                int slot = 0;
                for (String moveName : customMoves) {
                    if (!moveName.isEmpty() && slot < 4) {
                        String formattedMove = moveName.toLowerCase().replace(" ", "").replace("-", "");
                        MoveTemplate moveTemplate = Moves.getByName(formattedMove);

                        if (moveTemplate != null) {
                            pokemon.getMoveSet().setMove(slot, new Move(moveTemplate, moveTemplate.getPp(), 0));
                            slot++;
                        }
                    }
                }
            }

            pokemon.setShiny(level.random.nextInt(selectedEntry.shinyLuck) == 0);

            if (!selectedEntry.nature.isEmpty()) {
                var natureObj = Natures.getNature(ResourceLocation.fromNamespaceAndPath("cobblemon", selectedEntry.nature.toLowerCase()));
                if (natureObj != null) {
                    pokemon.setNature(natureObj);
                }
            }

            for (Stat stat : Stats.values()) {
                String id = stat.getIdentifier().getPath();
                if (id.equals("hp") || id.equals("attack") || id.equals("defence") ||
                        id.equals("special_attack") || id.equals("special_defence") || id.equals("speed")) {
                    int randomIv = selectedEntry.minIv + level.random.nextInt((selectedEntry.maxIv - selectedEntry.minIv) + 1);
                    pokemon.getIvs().set(stat, randomIv);
                }
            }

            if (!selectedEntry.heldItem.isEmpty()) {
                Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(selectedEntry.heldItem));
                if (item != Items.AIR) {
                    pokemon.setHeldItem$common(new ItemStack(item));
                }
            }

            PokemonEntity pokemonEntity = CobblemonEntities.POKEMON.create(level);
            if (pokemonEntity != null) {
                pokemonEntity.setPokemon(pokemon);
                pokemonEntity.setPos(
                        worldPosition.getX() + selectedEntry.spawnOffsetX,
                        worldPosition.getY() + selectedEntry.spawnOffsetY,
                        worldPosition.getZ() + selectedEntry.spawnOffsetZ
                );
                pokemonEntity.setPersistenceRequired();
                level.addFreshEntity(pokemonEntity);
                this.spawnedPokemonUUID = pokemonEntity.getUUID();
            }

        } catch (Exception e) {
            ExtraStructures.LOGGER.error("Failed to spawn Cobblemon from Random Spawner Block: {}", e.getMessage());
        }
    }

    public void resetSpawner() {
        this.currentCooldown = 0;
        this.hasSpawnedOnce = false;
        this.spawnedPokemonUUID = null;
        this.setChanged();
        if (this.level != null) {
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
        }
    }

    public List<SpawnEntry> getSpawnEntries() { return spawnEntries; }
    public void setSpawnEntries(List<SpawnEntry> entries) {
        this.spawnEntries = new ArrayList<>(entries);
        this.setChanged();
        if (this.level != null) {
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
        }
    }
    
    public double getTriggerRadius() { return triggerRadius; }
    public void setTriggerRadius(double radius) { this.triggerRadius = radius; this.setChanged(); }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, provider);
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putDouble("TriggerRadius", triggerRadius);
        tag.putBoolean("HasSpawnedOnce", this.hasSpawnedOnce);
        tag.putInt("CurrentCooldown", currentCooldown);
        if (this.spawnedPokemonUUID != null) {
            tag.putUUID("SpawnedUUID", this.spawnedPokemonUUID);
        }

        ListTag listTag = new ListTag();
        for (SpawnEntry entry : spawnEntries) {
            CompoundTag entryTag = new CompoundTag();
            entry.save(entryTag);
            listTag.add(entryTag);
        }
        tag.put("SpawnEntries", listTag);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        if (tag.contains("TriggerRadius")) triggerRadius = tag.getDouble("TriggerRadius");
        if (tag.contains("HasSpawnedOnce")) this.hasSpawnedOnce = tag.getBoolean("HasSpawnedOnce");
        if (tag.contains("CurrentCooldown")) currentCooldown = tag.getInt("CurrentCooldown");
        if (tag.hasUUID("SpawnedUUID")) {
            this.spawnedPokemonUUID = tag.getUUID("SpawnedUUID");
        }

        this.spawnEntries.clear();
        if (tag.contains("SpawnEntries", Tag.TAG_LIST)) {
            ListTag listTag = tag.getList("SpawnEntries", Tag.TAG_COMPOUND);
            for (int i = 0; i < listTag.size(); i++) {
                this.spawnEntries.add(new SpawnEntry(listTag.getCompound(i)));
            }
        }
    }
}