package net.pastek.cobblemonextrastructures.registers;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.pastek.cobblemonextrastructures.ExtraStructures;
import net.pastek.cobblemonextrastructures.common.tile.TileConditionalPokemonSpawner;
import net.pastek.cobblemonextrastructures.common.tile.TilePokemonSpawner;
import net.pastek.cobblemonextrastructures.common.tile.TileRandomPokemonSpawner;

public class EXTiles {

    public static final BlockEntityType<TilePokemonSpawner> POKEMON_SPAWNER = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(ExtraStructures.MOD_ID, "pokemon_spawner"),
            BlockEntityType.Builder.of(TilePokemonSpawner::new, EXBlocks.POKEMON_SPAWNER).build(null)
    );

    public static final BlockEntityType<TileRandomPokemonSpawner> RANDOM_POKEMON_SPAWNER = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(ExtraStructures.MOD_ID, "random_pokemon_spawner"),
            BlockEntityType.Builder.of(TileRandomPokemonSpawner::new, EXBlocks.RANDOM_POKEMON_SPAWNER).build(null)
    );

    public static final BlockEntityType<TileConditionalPokemonSpawner> CONDITIONAL_POKEMON_SPAWNER = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(ExtraStructures.MOD_ID, "conditional_pokemon_spawner"),
            BlockEntityType.Builder.of(TileConditionalPokemonSpawner::new, EXBlocks.CONDITIONAL_POKEMON_SPAWNER).build(null)
    );

    public static void register() {}
}