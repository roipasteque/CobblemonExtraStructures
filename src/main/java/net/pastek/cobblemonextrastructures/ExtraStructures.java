package net.pastek.cobblemonextrastructures;

import net.fabricmc.api.ModInitializer;
import net.pastek.cobblemonextrastructures.prefab.configuration.EXConfigurationHandler;
import net.pastek.cobblemonextrastructures.registers.UnifiedEXRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExtraStructures implements ModInitializer {
    public static final String MOD_ID = "cobblemonextrastructures";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        EXConfigurationHandler.load();
        UnifiedEXRegister.register();
    }
}