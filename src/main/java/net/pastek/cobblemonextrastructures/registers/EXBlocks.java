package net.pastek.cobblemonextrastructures.registers;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.pastek.cobblemonextrastructures.ExtraStructures;
import net.pastek.cobblemonextrastructures.common.block.*;

import java.util.function.Supplier;

public class EXBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(ExtraStructures.MOD_ID);

    public static final DeferredBlock<Block> POKEMON_SPAWNER = registerBlock("pokemon_spawner", () -> new PokemonSpawnerBlock(BlockBehaviour.Properties.of().strength(5f).noLootTable().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> RANDOM_POKEMON_SPAWNER = registerBlock("random_pokemon_spawner", () -> new RandomPokemonSpawnerBlock(BlockBehaviour.Properties.of().strength(5f).noLootTable().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> CONDITIONAL_POKEMON_SPAWNER = registerBlock("conditional_pokemon_spawner", () -> new ConditionalPokemonSpawnerBlock(BlockBehaviour.Properties.of().strength(5f).noLootTable().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> BELLSPROUT_STATUE = registerBlock("bellsprout_statue", () -> new BellsproutStatue(BlockBehaviour.Properties.of().strength(2f).sound(SoundType.COPPER).requiresCorrectToolForDrops().noOcclusion()));
    public static final DeferredBlock<Block> STRANGE_ROCK_REGICE = registerBlock("strange_rock_regice", () -> new StrangeRock(BlockBehaviour.Properties.of().strength(5f).sound(SoundType.DRIPSTONE_BLOCK).noLootTable().noOcclusion()));
    public static final DeferredBlock<Block> STRANGE_ROCK_REGIROCK = registerBlock("strange_rock_regirock", () -> new StrangeRock(BlockBehaviour.Properties.of().strength(5f).sound(SoundType.DRIPSTONE_BLOCK).noLootTable().noOcclusion()));
    public static final DeferredBlock<Block> STRANGE_ROCK_REGISTEEL = registerBlock("strange_rock_registeel", () -> new StrangeRock(BlockBehaviour.Properties.of().strength(5f).sound(SoundType.DRIPSTONE_BLOCK).noLootTable().noOcclusion()));


    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        EXItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
}