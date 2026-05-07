package net.pastek.cobblemonextrastructures.registers;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.pastek.cobblemonextrastructures.ExtraStructures;

public class EXCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ExtraStructures.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXTRA_STRUCTURES_TAB = CREATIVE_MODE_TAB.register("extrastructures_tab", () -> CreativeModeTab.builder().icon(() -> new ItemStack(EXBlocks.POKEMON_SPAWNER.get())).title(Component.translatable("creativetab.cobblemonextrastructures")).displayItems((itemDisplayParameters, output) -> {

        output.accept(EXBlocks.POKEMON_SPAWNER);
        output.accept(EXBlocks.RANDOM_POKEMON_SPAWNER);
        output.accept(EXBlocks.CONDITIONAL_POKEMON_SPAWNER);
        output.accept(EXBlocks.BELLSPROUT_STATUE);
        output.accept(EXBlocks.STRANGE_ROCK_REGIROCK);
        output.accept(EXBlocks.STRANGE_ROCK_REGISTEEL);
        output.accept(EXBlocks.STRANGE_ROCK_REGICE);



    }).build());
}