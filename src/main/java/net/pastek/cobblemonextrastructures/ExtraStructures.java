package net.pastek.cobblemonextrastructures;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.pastek.cobblemonextrastructures.prefab.configuration.EXConfigurationHandler;
import net.pastek.cobblemonextrastructures.registers.UnifiedEXRegister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ExtraStructures.MOD_ID)
public class ExtraStructures {
    public static final String MOD_ID = "cobblemonextrastructures";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public ExtraStructures(IEventBus EventBus, ModContainer modContainer) {
        EventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.register(this);
        UnifiedEXRegister.register(EventBus);
        modContainer.registerConfig(ModConfig.Type.COMMON, EXConfigurationHandler.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

        }
    }
}