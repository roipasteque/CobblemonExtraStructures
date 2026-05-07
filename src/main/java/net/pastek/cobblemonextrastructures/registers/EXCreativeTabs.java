package net.pastek.cobblemonextrastructures.registers;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.pastek.cobblemonextrastructures.ExtraStructures;

public class EXCreativeTabs {

    public static final CreativeModeTab EXTRA_STRUCTURES_TAB = Registry.register(
            BuiltInRegistries.CREATIVE_MODE_TAB,
            ResourceLocation.fromNamespaceAndPath(ExtraStructures.MOD_ID, "extrastructures_tab"),
            FabricItemGroup.builder()
                    .icon(() -> new ItemStack(EXBlocks.POKEMON_SPAWNER))
                    .title(Component.translatable("creativetab.cobblemonextrastructures"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(EXBlocks.POKEMON_SPAWNER);
                        output.accept(EXBlocks.RANDOM_POKEMON_SPAWNER);
                        output.accept(EXBlocks.CONDITIONAL_POKEMON_SPAWNER);
                        output.accept(EXBlocks.BELLSPROUT_STATUE);
                        output.accept(EXBlocks.STRANGE_ROCK_REGIROCK);
                        output.accept(EXBlocks.STRANGE_ROCK_REGISTEEL);
                        output.accept(EXBlocks.STRANGE_ROCK_REGICE);
                    }).build()
    );

    public static void register() {}
}