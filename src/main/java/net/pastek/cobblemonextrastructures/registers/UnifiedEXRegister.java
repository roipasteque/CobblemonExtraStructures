package net.pastek.cobblemonextrastructures.registers;

public class UnifiedEXRegister {
    public static void register() {
        EXBlocks.register();
        EXItems.register();
        EXTiles.register();
        EXMenuTypes.register();
        EXMapDecorations.register();
        EXCreativeTabs.register();
        EXTags.register(); 
    }
}