package net.pastek.cobblemonextrastructures.registers;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.pastek.cobblemonextrastructures.ExtraStructures;
import net.pastek.cobblemonextrastructures.common.inventory.container.ConditionalPokemonSpawnerMenu;
import net.pastek.cobblemonextrastructures.common.inventory.container.PokemonSpawnerMenu;
import net.pastek.cobblemonextrastructures.common.inventory.container.RandomPokemonSpawnerMenu;

public class EXMenuTypes {

    public static final MenuType<PokemonSpawnerMenu> POKEMON_SPAWNER_MENU = Registry.register(
            BuiltInRegistries.MENU,
            ResourceLocation.fromNamespaceAndPath(ExtraStructures.MOD_ID, "pokemon_spawner_menu"),
            new ExtendedScreenHandlerType<>(PokemonSpawnerMenu::new) // Fabric's equivalent for network-aware menus
    );

    public static final MenuType<RandomPokemonSpawnerMenu> RANDOM_POKEMON_SPAWNER_MENU = Registry.register(
            BuiltInRegistries.MENU,
            ResourceLocation.fromNamespaceAndPath(ExtraStructures.MOD_ID, "random_pokemon_spawner_menu"),
            new ExtendedScreenHandlerType<>(RandomPokemonSpawnerMenu::new)
    );

    public static final MenuType<ConditionalPokemonSpawnerMenu> CONDITIONAL_POKEMON_SPAWNER_MENU = Registry.register(
            BuiltInRegistries.MENU,
            ResourceLocation.fromNamespaceAndPath(ExtraStructures.MOD_ID, "conditional_pokemon_spawner_menu"),
            new ExtendedScreenHandlerType<>(ConditionalPokemonSpawnerMenu::new)
    );

    public static void register() {}
}