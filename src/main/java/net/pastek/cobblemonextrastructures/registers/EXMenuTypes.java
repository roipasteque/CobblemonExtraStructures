package net.pastek.cobblemonextrastructures.registers;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.pastek.cobblemonextrastructures.ExtraStructures;
import net.pastek.cobblemonextrastructures.common.inventory.container.ConditionalPokemonSpawnerMenu;
import net.pastek.cobblemonextrastructures.common.inventory.container.PokemonSpawnerMenu;
import net.pastek.cobblemonextrastructures.common.inventory.container.RandomPokemonSpawnerMenu;

public class EXMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
        DeferredRegister.create(Registries.MENU, ExtraStructures.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<PokemonSpawnerMenu>> POKEMON_SPAWNER_MENU =
        MENU_TYPES.register("pokemon_spawner_menu", () -> IMenuTypeExtension.create(PokemonSpawnerMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<RandomPokemonSpawnerMenu>> RANDOM_POKEMON_SPAWNER_MENU =
            MENU_TYPES.register("random_pokemon_spawner_menu", () -> IMenuTypeExtension.create(RandomPokemonSpawnerMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<ConditionalPokemonSpawnerMenu>> CONDITIONAL_POKEMON_SPAWNER_MENU =
            MENU_TYPES.register("conditional_pokemon_spawner_menu", () -> IMenuTypeExtension.create(ConditionalPokemonSpawnerMenu::new));
}