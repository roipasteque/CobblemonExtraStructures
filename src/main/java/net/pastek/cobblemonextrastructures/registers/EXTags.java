package net.pastek.cobblemonextrastructures.registers;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.pastek.cobblemonextrastructures.ExtraStructures;

public class EXTags {
    public static class Blocks {

        private static TagKey<Block> createTag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(ExtraStructures.MOD_ID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> TRANSFORMABLE_ITEMS = createTag("transformable_items");

        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(ExtraStructures.MOD_ID, name));
        }
    }

    public static class Structures {
        public static final TagKey<Structure> SPROUT_TOWER_TAG = createTag("sprout_tower");

        private static TagKey<Structure> createTag(String name) {
            return TagKey.create(Registries.STRUCTURE, ResourceLocation.fromNamespaceAndPath(ExtraStructures.MOD_ID, name));
        }
    }
}