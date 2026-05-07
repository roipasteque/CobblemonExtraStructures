package net.pastek.cobblemonextrastructures.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.pastek.cobblemonextrastructures.ExtraStructures;

public class ItemModelProvider extends net.neoforged.neoforge.client.model.generators.ItemModelProvider {
    public ItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ExtraStructures.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

    }
}
