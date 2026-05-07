package net.pastek.cobblemonextrastructures.registers;

import net.neoforged.bus.api.IEventBus;

public class UnifiedEXRegister {
    public static void register(IEventBus eventBus) {
        EXBlocks.BLOCKS.register(eventBus);
        EXTiles.BLOCK_ENTITY_TYPES.register(eventBus);
        EXItems.ITEMS.register(eventBus);
        EXMenuTypes.MENU_TYPES.register(eventBus);
        EXMapDecorations.MAP_DECORATION_TYPES.register(eventBus);
        EXCreativeTabs.CREATIVE_MODE_TAB.register(eventBus);
    }
}
