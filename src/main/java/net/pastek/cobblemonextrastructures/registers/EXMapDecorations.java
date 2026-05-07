package net.pastek.cobblemonextrastructures.registers;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.pastek.cobblemonextrastructures.ExtraStructures;

public class EXMapDecorations {
    public static final DeferredRegister<MapDecorationType> MAP_DECORATION_TYPES =
        DeferredRegister.create(Registries.MAP_DECORATION_TYPE, ExtraStructures.MOD_ID);

    public static final DeferredHolder<MapDecorationType, MapDecorationType> SPROUT_TOWER =
        MAP_DECORATION_TYPES.register("sprout_tower", () -> new MapDecorationType(
            ResourceLocation.fromNamespaceAndPath(ExtraStructures.MOD_ID, "map/decorations/sprout_tower"),
            true,
            0xAA00AA,
            false,
            true
        ));

    public static final DeferredHolder<MapDecorationType, MapDecorationType> DESERT_RUINS =
            MAP_DECORATION_TYPES.register("desert_ruins", () -> new MapDecorationType(
                    ResourceLocation.fromNamespaceAndPath(ExtraStructures.MOD_ID, "map/decorations/desert_ruins"),
                    true,
                    0xFF9742,
                    false,
                    true
            ));

    public static final DeferredHolder<MapDecorationType, MapDecorationType> ANCIENT_TOMB =
            MAP_DECORATION_TYPES.register("ancient_tomb", () -> new MapDecorationType(
                    ResourceLocation.fromNamespaceAndPath(ExtraStructures.MOD_ID, "map/decorations/ancient_tomb"),
                    true,
                    0xFF565A,
                    false,
                    true
            ));

    public static final DeferredHolder<MapDecorationType, MapDecorationType> ISLAND_CAVE =
            MAP_DECORATION_TYPES.register("island_cave", () -> new MapDecorationType(
                    ResourceLocation.fromNamespaceAndPath(ExtraStructures.MOD_ID, "map/decorations/island_cave"),
                    true,
                    0xFFFF6A,
                    false,
                    true
            ));
}