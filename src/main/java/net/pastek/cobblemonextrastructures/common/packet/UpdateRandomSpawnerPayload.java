package net.pastek.cobblemonextrastructures.common.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.pastek.cobblemonextrastructures.ExtraStructures;
import net.pastek.cobblemonextrastructures.common.tile.SpawnEntry;

import java.util.List;

public record UpdateRandomSpawnerPayload(
        BlockPos pos, double radius, List<SpawnEntry> entries, boolean reset
) implements CustomPacketPayload {

    public static final Type<UpdateRandomSpawnerPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ExtraStructures.MOD_ID, "update_random_spawner"));

    public static final StreamCodec<FriendlyByteBuf, UpdateRandomSpawnerPayload> STREAM_CODEC = StreamCodec.ofMember(
            UpdateRandomSpawnerPayload::write, UpdateRandomSpawnerPayload::new
    );

    public UpdateRandomSpawnerPayload(FriendlyByteBuf buffer) {
        this(
                buffer.readBlockPos(),
                buffer.readDouble(),
                buffer.readList(buf -> {
                    SpawnEntry entry = new SpawnEntry();
                    entry.weight = buf.readInt();
                    entry.species = buf.readUtf();
                    entry.minIv = buf.readInt();
                    entry.maxIv = buf.readInt();
                    entry.pokemonLevel = buf.readInt();
                    entry.shinyLuck = buf.readInt();
                    entry.nature = buf.readUtf();
                    entry.heldItem = buf.readUtf(256);
                    entry.ability = buf.readUtf();
                    entry.move1 = buf.readUtf();
                    entry.move2 = buf.readUtf();
                    entry.move3 = buf.readUtf();
                    entry.move4 = buf.readUtf();
                    entry.spawnOffsetX = buf.readDouble();
                    entry.spawnOffsetY = buf.readDouble();
                    entry.spawnOffsetZ = buf.readDouble();
                    return entry;
                }),
                buffer.readBoolean()
        );
    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeDouble(radius);
        buffer.writeCollection(entries, (buf, entry) -> {
            buf.writeInt(entry.weight);
            buf.writeUtf(entry.species);
            buf.writeInt(entry.minIv);
            buf.writeInt(entry.maxIv);
            buf.writeInt(entry.pokemonLevel);
            buf.writeInt(entry.shinyLuck);
            buf.writeUtf(entry.nature);
            buf.writeUtf(entry.heldItem, 256);
            buf.writeUtf(entry.ability);
            buf.writeUtf(entry.move1);
            buf.writeUtf(entry.move2);
            buf.writeUtf(entry.move3);
            buf.writeUtf(entry.move4);
            buf.writeDouble(entry.spawnOffsetX);
            buf.writeDouble(entry.spawnOffsetY);
            buf.writeDouble(entry.spawnOffsetZ);
        });
        buffer.writeBoolean(reset);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}