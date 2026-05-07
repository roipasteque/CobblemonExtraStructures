package net.pastek.cobblemonextrastructures.registers;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import net.pastek.cobblemonextrastructures.ExtraStructures;

public class EXMapDecorations {

    public static final MapDecorationType SPROUT_TOWER = Registry.register(
            BuiltInRegistries.MAP_DECORATION_TYPE,
            ResourceLocation.fromNamespaceAndPath(ExtraStructures.MOD_ID, "sprout_tower"),
            new MapDecorationType(ResourceLocation.fromNamespaceAndPath(ExtraStructures.MOD_ID, "map/decorations/sprout_tower"), true, 0xAA00AA, false, true)
    );

    public static final MapDecorationType DESERT_RUINS = Registry.register(
            BuiltInRegistries.MAP_DECORATION_TYPE,
            ResourceLocation.fromNamespaceAndPath(ExtraStructures.MOD_ID, "desert_ruins"),
            new MapDecorationType(ResourceLocation.fromNamespaceAndPath(ExtraStructures.MOD_ID, "map/decorations/desert_ruins"), true, 0xFF9742, false, true)
    );

    public static final MapDecorationType ANCIENT_TOMB = Registry.register(
            BuiltInRegistries.MAP_DECORATION_TYPE,
            ResourceLocation.fromNamespaceAndPath(ExtraStructures.MOD_ID, "ancient_tomb"),
            new MapDecorationType(ResourceLocation.fromNamespaceAndPath(ExtraStructures.MOD_ID, "map/decorations/ancient_tomb"), true, 0xFF565A, false, true)
    );

    public static final MapDecorationType ISLAND_CAVE = Registry.register(
            BuiltInRegistries.MAP_DECORATION_TYPE,
            ResourceLocation.fromNamespaceAndPath(ExtraStructures.MOD_ID, "island_cave"),
            new MapDecorationType(ResourceLocation.fromNamespaceAndPath(ExtraStructures.MOD_ID, "map/decorations/island_cave"), true, 0xFFFF6A, false, true)
    );

    public static void register() {}
}