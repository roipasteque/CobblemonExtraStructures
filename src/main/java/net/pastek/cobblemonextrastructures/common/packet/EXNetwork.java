package net.pastek.cobblemonextrastructures.common.packet;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.pastek.cobblemonextrastructures.ExtraStructures;
import net.pastek.cobblemonextrastructures.common.tile.TileConditionalPokemonSpawner;
import net.pastek.cobblemonextrastructures.common.tile.TilePokemonSpawner;
import net.pastek.cobblemonextrastructures.common.tile.TileRandomPokemonSpawner;

@EventBusSubscriber(modid = ExtraStructures.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class EXNetwork {

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(ExtraStructures.MOD_ID);

        registrar.playToServer(
            UpdateSpawnerPayload.TYPE,
            UpdateSpawnerPayload.STREAM_CODEC,
            EXNetwork::handleUpdateSpawner
        );

        registrar.playToServer(
                UpdateRandomSpawnerPayload.TYPE,
                UpdateRandomSpawnerPayload.STREAM_CODEC,
                EXNetwork::handleUpdateRandomSpawner
        );

        registrar.playToServer(
                UpdateConditionalSpawnerPayload.TYPE,
                UpdateConditionalSpawnerPayload.STREAM_CODEC,
                EXNetwork::handleUpdateConditonalSpawner
        );
    }

    private static void handleUpdateSpawner(final UpdateSpawnerPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            if (!player.isCreative()) return;

            if (player.level().getBlockEntity(payload.pos()) instanceof TilePokemonSpawner spawner) {
                spawner.updateSettings(
                    payload.radius(), payload.species(), payload.minIv(),
                    payload.maxIv(), payload.level(), payload.shinyLuck(),
                    payload.nature(), payload.heldItem(), payload.ability(),
                    payload.m1(), payload.m2(), payload.m3(), payload.m4(),
                    payload.offX(), payload.offY(), payload.offZ()
                );

                if (payload.reset()) {
                    spawner.resetSpawner();
                }
            }
        });
    }

    private static void handleUpdateRandomSpawner(final UpdateRandomSpawnerPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            if (!player.isCreative()) return;

            if (player.level().getBlockEntity(payload.pos()) instanceof TileRandomPokemonSpawner spawner) {
                spawner.setTriggerRadius(payload.radius());
                spawner.setSpawnEntries(payload.entries());

                if (payload.reset()) {
                    spawner.resetSpawner();
                }
            }
        });
    }

    private static void handleUpdateConditonalSpawner(final UpdateConditionalSpawnerPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            if (!player.isCreative()) return;
            if (player.level().getBlockEntity(payload.pos()) instanceof TileConditionalPokemonSpawner spawner) {
                spawner.updateSettings(payload.radius(), payload.species(), payload.minIv(), payload.maxIv(),
                        payload.level(), payload.shinyLuck(), payload.nature(), payload.heldItem(), payload.ability(),
                        payload.m1(), payload.m2(), payload.m3(), payload.m4(), payload.offX(), payload.offY(), payload.offZ());

                spawner.updateConditions(payload.reqLevel(), payload.reqSpecies(), payload.reqItem(), payload.reqAdv());

                if (payload.reset()) spawner.resetSpawner();

                player.level().sendBlockUpdated(payload.pos(), spawner.getBlockState(), spawner.getBlockState(), 3);
            }
        });
    }
}