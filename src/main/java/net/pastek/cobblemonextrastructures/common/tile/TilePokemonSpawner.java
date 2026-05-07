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
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.api.pokemon.stats.Stat;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.pastek.cobblemonextrastructures.ExtraStructures;
import net.pastek.cobblemonextrastructures.prefab.configuration.EXConfigurationHandler;
import net.pastek.cobblemonextrastructures.registers.EXTiles;

import java.util.List;
import java.util.UUID;

public class TilePokemonSpawner extends BlockEntity {

    private double triggerRadius = 5.0;
    private String species = "porygon";
    private int minIv = 0;
    private int maxIv = 31;
    private int pokemonLevel = -1;
    private int shinyLuck = 4096;
    private String nature = "";
    private String heldItem = "";
    private String ability = "";
    private String move1 = "", move2 = "", move3 = "", move4 = "";
    private double spawnOffsetX = 0.5;
    private double spawnOffsetY = 1.0;
    private double spawnOffsetZ = 0.5;

    private UUID spawnedPokemonUUID = null;
    private boolean hasSpawnedOnce = false;
    private int currentCooldown = 0;

    public TilePokemonSpawner(BlockPos pos, BlockState state) {
        this(EXTiles.POKEMON_SPAWNER.get(), pos, state);
    }

    protected TilePokemonSpawner(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, TilePokemonSpawner entity) {
        if (level.isClientSide) return;

        int configCooldown = EXConfigurationHandler.SPAWNER_COOLDOWN.get();

        if (configCooldown == -1 && entity.hasSpawnedOnce) {
            return;
        }

        if (entity.currentCooldown > 0) {
            entity.currentCooldown--;
            return;
        }

        AABB triggerBox = new AABB(pos).inflate(entity.triggerRadius);
        List<Player> nearbyPlayers = level.getEntitiesOfClass(Player.class, triggerBox,
                player -> !player.isCreative() && !player.isSpectator());

        if (!nearbyPlayers.isEmpty()) {
            if (entity.isSpawnAlive((ServerLevel) level)) {
                entity.currentCooldown = 200;
                return;
            }

            entity.spawnPokemon();
            entity.hasSpawnedOnce = true;
            entity.currentCooldown = Math.max(0, configCooldown);
            entity.setChanged();
        }
    }

    protected boolean isSpawnAlive(ServerLevel serverLevel) {
        if (this.spawnedPokemonUUID == null) return false;

        Entity existing = serverLevel.getEntity(this.spawnedPokemonUUID);
        return existing != null && existing.isAlive();
    }

    protected void spawnPokemon() {
        if (level == null || level.isClientSide) return;

        try {
            PokemonProperties props = PokemonProperties.Companion.parse(this.species);
            Pokemon pokemon = props.create();

            if (this.pokemonLevel <= 0) {
                pokemon.setLevel(level.random.nextInt(100) + 1);
            } else {
                pokemon.setLevel(this.pokemonLevel);
            }

            if (!this.ability.isEmpty()) {
                ResourceLocation abilityId = ResourceLocation.fromNamespaceAndPath("cobblemon", this.ability.toLowerCase().replace(" ", ""));
                var abilityTemplate = Abilities.INSTANCE.get(abilityId.getPath());

                if (abilityTemplate != null) {
                    Ability abilityInstance = new Ability(abilityTemplate, false, Priority.LOW);
                    pokemon.setAbility$common(abilityInstance);
                }
            }

            String[] customMoves = {move1, move2, move3, move4};
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
                            Move moveInstance = new Move(moveTemplate, moveTemplate.getPp(), 0);

                            pokemon.getMoveSet().setMove(slot, moveInstance);
                            slot++;
                        } else {
                            ExtraStructures.LOGGER.debug("Spawner: Move template NOT found for: {}", formattedMove);
                        }
                    }
                }
            }

            pokemon.setShiny(level.random.nextInt(this.shinyLuck) == 0);

            if (!this.nature.isEmpty()) {
                var natureObj = Natures.getNature(ResourceLocation.fromNamespaceAndPath("cobblemon", this.nature.toLowerCase()));
                if (natureObj != null) {
                    pokemon.setNature(natureObj);
                }
            }

            for (Stat stat : Stats.values()) {
                String id = stat.getIdentifier().getPath();
                if (id.equals("hp") || id.equals("attack") || id.equals("defence") ||
                        id.equals("special_attack") || id.equals("special_defence") || id.equals("speed")) {

                    int randomIv = this.minIv + level.random.nextInt((this.maxIv - this.minIv) + 1);
                    pokemon.getIvs().set(stat, randomIv);
                }
            }

            if (!this.heldItem.isEmpty()) {
                Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(this.heldItem));
                if (item != Items.AIR) {
                    pokemon.setHeldItem$common(new ItemStack(item));
                }
            }

            PokemonEntity pokemonEntity = CobblemonEntities.POKEMON.create(level);
            if (pokemonEntity != null) {
                pokemonEntity.setPokemon(pokemon);

                pokemonEntity.setPos(
                        worldPosition.getX() + this.spawnOffsetX,
                        worldPosition.getY() + this.spawnOffsetY,
                        worldPosition.getZ() + this.spawnOffsetZ
                );

                pokemonEntity.setPersistenceRequired();

                level.addFreshEntity(pokemonEntity);
                this.spawnedPokemonUUID = pokemonEntity.getUUID();
            }

        } catch (Exception e) {
            ExtraStructures.LOGGER.debug("Failed to spawn Cobblemon from Spawner Block: {}", e.getMessage());
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
        tag.putString("Species", species);
        tag.putInt("MinIV", minIv);
        tag.putInt("MaxIV", maxIv);
        tag.putInt("PokemonLevel", pokemonLevel);
        tag.putInt("ShinyLuck", shinyLuck);
        tag.putString("Nature", nature);
        tag.putString("HeldItem", heldItem);
        tag.putString("Ability", ability);
        tag.putString("Move1", move1);
        tag.putString("Move2", move2);
        tag.putString("Move3", move3);
        tag.putString("Move4", move4);
        tag.putDouble("OffsetX", spawnOffsetX);
        tag.putDouble("OffsetY", spawnOffsetY);
        tag.putDouble("OffsetZ", spawnOffsetZ);

        tag.putBoolean("HasSpawnedOnce", this.hasSpawnedOnce);
        tag.putInt("CurrentCooldown", currentCooldown);

        if (this.spawnedPokemonUUID != null) {
            tag.putUUID("SpawnedUUID", this.spawnedPokemonUUID);
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        if (tag.contains("TriggerRadius")) triggerRadius = tag.getDouble("TriggerRadius");
        if (tag.contains("Species")) species = tag.getString("Species");
        if (tag.contains("MinIV")) minIv = tag.getInt("MinIV");
        if (tag.contains("MaxIV")) maxIv = tag.getInt("MaxIV");
        if (tag.contains("PokemonLevel")) pokemonLevel = tag.getInt("PokemonLevel");
        if (tag.contains("ShinyLuck")) shinyLuck = tag.getInt("ShinyLuck");
        if (tag.contains("Nature")) nature = tag.getString("Nature");
        if (tag.contains("HeldItem")) heldItem = tag.getString("HeldItem");
        if (tag.contains("Ability")) ability = tag.getString("Ability");
        if (tag.contains("Move1")) move1 = tag.getString("Move1");
        if (tag.contains("Move2")) move2 = tag.getString("Move2");
        if (tag.contains("Move3")) move3 = tag.getString("Move3");
        if (tag.contains("Move4")) move4 = tag.getString("Move4");
        if (tag.contains("OffsetX")) spawnOffsetX = tag.getDouble("OffsetX");
        if (tag.contains("OffsetY")) spawnOffsetY = tag.getDouble("OffsetY");
        if (tag.contains("OffsetZ")) spawnOffsetZ = tag.getDouble("OffsetZ");

        if (tag.contains("HasSpawnedOnce")) this.hasSpawnedOnce = tag.getBoolean("HasSpawnedOnce");
        if (tag.contains("CurrentCooldown")) currentCooldown = tag.getInt("CurrentCooldown");

        if (tag.hasUUID("SpawnedUUID")) {
            this.spawnedPokemonUUID = tag.getUUID("SpawnedUUID");
        }
    }

    public void updateSettings(double radius, String species, int minIv, int maxIv, int level, int shinyLuck, String nature, String heldItem, String ability, String m1, String m2, String m3, String m4, double offX, double offY, double offZ) {
        this.triggerRadius = radius;
        this.species = species;
        this.minIv = minIv;
        this.maxIv = maxIv;
        this.pokemonLevel = level;
        this.shinyLuck = shinyLuck;
        this.nature = nature;
        this.heldItem = heldItem;
        this.ability = ability;
        this.move1 = m1; this.move2 = m2; this.move3 = m3; this.move4 = m4;
        this.spawnOffsetX = offX;
        this.spawnOffsetY = offY;
        this.spawnOffsetZ = offZ;
        this.setChanged();

        if (this.level != null) {
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
        }
    }

    public double getTriggerRadius() { return triggerRadius; }
    public int getCurrentCooldown() { return this.currentCooldown; }
    public void setCooldown(int ticks) { this.currentCooldown = ticks; }
    public void tickCooldown() { if (this.currentCooldown > 0) this.currentCooldown--; }
    public void setHasSpawnedOnce(boolean state) { this.hasSpawnedOnce = state; }
    public String getSpecies() { return species; }
    public int getMinIv() { return minIv; }
    public int getMaxIv() { return maxIv; }
    public int getPokemonLevel() { return pokemonLevel; }
    public int getShinyLuck() { return shinyLuck; }
    public String getNature() { return nature; }
    public String getHeldItem() { return heldItem; }
    public String getAbility() { return ability; }
    public String getMove1() { return move1; }
    public String getMove2() { return move2; }
    public String getMove3() { return move3; }
    public String getMove4() { return move4; }
    public double getOffsetX() { return spawnOffsetX; }
    public double getOffsetY() { return spawnOffsetY; }
    public double getOffsetZ() { return spawnOffsetZ; }
}