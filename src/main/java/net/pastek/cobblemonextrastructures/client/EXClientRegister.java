package net.pastek.cobblemonextrastructures.client;

import net.pastek.cobblemonextrastructures.ExtraStructures;
import net.pastek.cobblemonextrastructures.client.screen.ConditionalPokemonSpawnerScreen;
import net.pastek.cobblemonextrastructures.client.screen.PokemonSpawnerScreen;
import net.pastek.cobblemonextrastructures.client.screen.RandomPokemonSpawnerScreen;
import net.pastek.cobblemonextrastructures.registers.EXMenuTypes;

@EventBusSubscriber(modid = ExtraStructures.MOD_ID, value = {Dist.CLIENT})
public class EXClientRegister {

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(EXMenuTypes.POKEMON_SPAWNER_MENU.get(), PokemonSpawnerScreen::new);
        event.register(EXMenuTypes.RANDOM_POKEMON_SPAWNER_MENU.get(), RandomPokemonSpawnerScreen::new);
        event.register(EXMenuTypes.CONDITIONAL_POKEMON_SPAWNER_MENU.get(), ConditionalPokemonSpawnerScreen::new);
    }
}
