package net.pastek.cobblemonextrastructures.registers;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.pastek.cobblemonextrastructures.ExtraStructures;
import net.pastek.cobblemonextrastructures.common.tile.TileConditionalPokemonSpawner;
import net.pastek.cobblemonextrastructures.common.tile.TilePokemonSpawner;
import net.pastek.cobblemonextrastructures.common.tile.TileRandomPokemonSpawner;

public class EXTiles {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ExtraStructures.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TilePokemonSpawner>> POKEMON_SPAWNER =
            BLOCK_ENTITY_TYPES.register("pokemon_spawner",
                    () -> BlockEntityType.Builder.of(TilePokemonSpawner::new, EXBlocks.POKEMON_SPAWNER.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileRandomPokemonSpawner>> RANDOM_POKEMON_SPAWNER =
            BLOCK_ENTITY_TYPES.register("random_pokemon_spawner",
                    () -> BlockEntityType.Builder.of(TileRandomPokemonSpawner::new, EXBlocks.RANDOM_POKEMON_SPAWNER.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileConditionalPokemonSpawner>> CONDITIONAL_POKEMON_SPAWNER =
            BLOCK_ENTITY_TYPES.register("conditional_pokemon_spawner",
                    () -> BlockEntityType.Builder.of(TileConditionalPokemonSpawner::new, EXBlocks.CONDITIONAL_POKEMON_SPAWNER.get()).build(null));

}
