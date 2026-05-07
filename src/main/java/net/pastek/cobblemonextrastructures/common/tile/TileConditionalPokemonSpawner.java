package net.pastek.cobblemonextrastructures.common.tile;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.pastek.cobblemonextrastructures.registers.EXTiles;

import java.util.Arrays;
import java.util.List;

public class TileConditionalPokemonSpawner extends TilePokemonSpawner {

    private int reqMinPartyLevel = 0;
    private String reqPartySpecies = "";
    private String reqItem = "";
    private String reqAdvancement = "";

    public TileConditionalPokemonSpawner(BlockPos pos, BlockState state) {
        super(EXTiles.CONDITIONAL_POKEMON_SPAWNER.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, TileConditionalPokemonSpawner entity) {
        if (level.isClientSide) return;

        if (entity.getCurrentCooldown() > 0) {
            entity.tickCooldown();
            return;
        }

        AABB triggerBox = new AABB(pos).inflate(entity.getTriggerRadius());
        List<Player> nearbyPlayers = level.getEntitiesOfClass(Player.class, triggerBox,
                player -> !player.isCreative() && !player.isSpectator());

        for (Player player : nearbyPlayers) {
            if (entity.checkConditions((ServerPlayer) player)) {
                if (entity.isSpawnAlive((ServerLevel) level)) {
                    entity.setCooldown(200);
                    return;
                }

                entity.spawnPokemon();
                entity.setHasSpawnedOnce(true);
                entity.setCooldown(200);
                entity.setChanged();
                break;
            }
        }
    }

    private boolean checkConditions(ServerPlayer player) {
        PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(player);
        if (reqMinPartyLevel > 0) {
            boolean foundLevel = false;
            for (Pokemon p : party) {
                if (p.getLevel() >= reqMinPartyLevel) {
                    foundLevel = true;
                    break;
                }
            }
            if (!foundLevel) return false;
        }

        if (!reqPartySpecies.isEmpty()) {
            List<String> requiredList = Arrays.asList(reqPartySpecies.toLowerCase().split(","));
            for (String req : requiredList) {
                boolean hasSpecies = false;
                for (Pokemon p : party) {
                    if (p.getSpecies().getName().equalsIgnoreCase(req.trim())) {
                        hasSpecies = true;
                        break;
                    }
                }
                if (!hasSpecies) return false;
            }
        }

        if (!reqItem.isEmpty()) {
            Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(reqItem));
            if (!player.getInventory().contains(new ItemStack(item))) {
                return false;
            }
        }

        if (!reqAdvancement.isEmpty()) {
            AdvancementHolder advancement = player.getServer().getAdvancements().get(ResourceLocation.parse(reqAdvancement));
            if (advancement == null || !player.getAdvancements().getOrStartProgress(advancement).isDone()) {
                return false;
            }
        }

        return true;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putInt("ReqMinLevel", reqMinPartyLevel);
        tag.putString("ReqSpecies", reqPartySpecies);
        tag.putString("ReqItem", reqItem);
        tag.putString("ReqAdvancement", reqAdvancement);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        reqMinPartyLevel = tag.getInt("ReqMinLevel");
        reqPartySpecies = tag.getString("ReqSpecies");
        reqItem = tag.getString("ReqItem");
        reqAdvancement = tag.getString("ReqAdvancement");
    }

    public void updateConditions(int level, String species, String item, String adv) {
        this.reqMinPartyLevel = level;
        this.reqPartySpecies = species;
        this.reqItem = item;
        this.reqAdvancement = adv;
        this.setChanged();
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag tag = super.getUpdateTag(provider);
        saveAdditional(tag, provider);
        return tag;
    }

    public int getReqMinPartyLevel() { return reqMinPartyLevel; }
    public String getReqPartySpecies() { return reqPartySpecies; }
    public String getReqItem() { return reqItem; }
    public String getReqAdvancement() { return reqAdvancement; }
}