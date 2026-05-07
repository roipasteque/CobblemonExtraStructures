package net.pastek.cobblemonextrastructures.prefab.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.pastek.cobblemonextrastructures.ExtraStructures;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class EXConfigurationHandler {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "cobblemonextrastructures.json");

    public int static_spawner_cooldown = 168000;
    public int random_spawner_cooldown = 24000;

    private static EXConfigurationHandler INSTANCE = new EXConfigurationHandler();

    public static EXConfigurationHandler getInstance() {
        return INSTANCE;
    }

    public static void load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                INSTANCE = GSON.fromJson(reader, EXConfigurationHandler.class);
            } catch (IOException e) {
                ExtraStructures.LOGGER.error("Failed to load config, using defaults", e);
            }
        } else {
            save();
        }
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(INSTANCE, writer);
        } catch (IOException e) {
            ExtraStructures.LOGGER.error("Failed to save config", e);
        }
    }
}