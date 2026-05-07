package net.pastek.cobblemonextrastructures.registers;


import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.pastek.cobblemonextrastructures.ExtraStructures;

public class EXBlocks {

    public static final Block POKEMON_SPAWNER = registerBlock("pokemon_spawner", new PokemonSpawnerBlock(BlockBehaviour.Properties.of().strength(5f).noLootTable().sound(SoundType.STONE)));
    public static final Block RANDOM_POKEMON_SPAWNER = registerBlock("random_pokemon_spawner", new RandomPokemonSpawnerBlock(BlockBehaviour.Properties.of().strength(5f).noLootTable().sound(SoundType.STONE)));
    public static final Block CONDITIONAL_POKEMON_SPAWNER = registerBlock("conditional_pokemon_spawner", new ConditionalPokemonSpawnerBlock(BlockBehaviour.Properties.of().strength(5f).noLootTable().sound(SoundType.STONE)));
    public static final Block BELLSPROUT_STATUE = registerBlock("bellsprout_statue", new BellsproutStatue(BlockBehaviour.Properties.of().strength(2f).sound(SoundType.COPPER).requiresCorrectToolForDrops().noOcclusion()));
    public static final Block STRANGE_ROCK_REGICE = registerBlock("strange_rock_regice", new StrangeRock(BlockBehaviour.Properties.of().strength(5f).sound(SoundType.DRIPSTONE_BLOCK).noLootTable().noOcclusion()));
    public static final Block STRANGE_ROCK_REGIROCK = registerBlock("strange_rock_regirock", new StrangeRock(BlockBehaviour.Properties.of().strength(5f).sound(SoundType.DRIPSTONE_BLOCK).noLootTable().noOcclusion()));
    public static final Block STRANGE_ROCK_REGISTEEL = registerBlock("strange_rock_registeel", new StrangeRock(BlockBehaviour.Properties.of().strength(5f).sound(SoundType.DRIPSTONE_BLOCK).noLootTable().noOcclusion()));

    private static <T extends Block> T registerBlock(String name, T block) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(ExtraStructures.MOD_ID, name);
        T registeredBlock = Registry.register(BuiltInRegistries.BLOCK, id, block);
        EXItems.registerBlockItem(id, registeredBlock);
        return registeredBlock;
    }

    public static void register() {
    }
}