package net.pastek.cobblemonextrastructures.common.inventory.container;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.pastek.cobblemonextrastructures.common.tile.TileConditionalPokemonSpawner;
import net.pastek.cobblemonextrastructures.registers.EXMenuTypes;

public class ConditionalPokemonSpawnerMenu extends AbstractContainerMenu {
    private final TileConditionalPokemonSpawner blockEntity;

    public ConditionalPokemonSpawnerMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, (TileConditionalPokemonSpawner) inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public ConditionalPokemonSpawnerMenu(int containerId, Inventory inv, TileConditionalPokemonSpawner entity) {
        super(EXMenuTypes.CONDITIONAL_POKEMON_SPAWNER_MENU, containerId);
        this.blockEntity = entity;
    }

    public TileConditionalPokemonSpawner getBlockEntity() {
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