package net.pastek.cobblemonextrastructures.prefab.configuration;

import net.neoforged.neoforge.common.ModConfigSpec;

public class EXConfigurationHandler {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue SPAWNER_COOLDOWN;
    public static final ModConfigSpec.IntValue RANDOM_SPAWNER_COOLDOWN;

    static {
        BUILDER.push("spawner_settings");

        SPAWNER_COOLDOWN = BUILDER
                .comment("Cooldown for static spawners (e.g., Legendaries).", "Default: 168000 ticks (7 days)")
                .defineInRange("static_spawner_cooldown", 168000, -1, 1000000);

        RANDOM_SPAWNER_COOLDOWN = BUILDER
                .comment("Cooldown for randomized spawners (e.g., Dungeons).", "Default: 24000 ticks (1 day)")
                .defineInRange("random_spawner_cooldown", 24000, -1, 1000000);

        BUILDER.pop();
    }

    public static final ModConfigSpec SPEC = BUILDER.build();
}