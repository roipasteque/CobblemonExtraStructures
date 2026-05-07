package net.pastek.cobblemonextrastructures.common.inventory.container;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.pastek.cobblemonextrastructures.common.tile.TileRandomPokemonSpawner;
import net.pastek.cobblemonextrastructures.registers.EXMenuTypes;

public class RandomPokemonSpawnerMenu extends AbstractContainerMenu {
    private final TileRandomPokemonSpawner blockEntity;

    public RandomPokemonSpawnerMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, (TileRandomPokemonSpawner) inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public RandomPokemonSpawnerMenu(int containerId, Inventory inv, TileRandomPokemonSpawner entity) {
        super(EXMenuTypes.RANDOM_POKEMON_SPAWNER_MENU.get(), containerId);
        this.blockEntity = entity;
    }

    public TileRandomPokemonSpawner getBlockEntity() {
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