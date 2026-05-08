package net.pastek.cobblemonextrastructures.common.inventory.container;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.pastek.cobblemonextrastructures.common.tile.TilePokemonSpawner;
import net.pastek.cobblemonextrastructures.registers.EXMenuTypes;

public class PokemonSpawnerMenu extends AbstractContainerMenu {
    private final TilePokemonSpawner blockEntity;

    public PokemonSpawnerMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, (TilePokemonSpawner) inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public PokemonSpawnerMenu(int containerId, Inventory inv, TilePokemonSpawner entity) {
        super(EXMenuTypes.POKEMON_SPAWNER_MENU, containerId);
        this.blockEntity = entity;
    }

    public TilePokemonSpawner getBlockEntity() {
        return blockEntity;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(blockEntity, player);
    }
}