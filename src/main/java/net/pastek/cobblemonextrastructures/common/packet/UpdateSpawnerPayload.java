package net.pastek.cobblemonextrastructures.common.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.pastek.cobblemonextrastructures.ExtraStructures;

public record UpdateSpawnerPayload(
        BlockPos pos, double radius, String species, int minIv, int maxIv, 
        int level, int shinyLuck, String nature, String heldItem,
        String ability, String m1, String m2, String m3, String m4,
        double offX, double offY, double offZ, boolean reset
        ) implements CustomPacketPayload {

    public static final Type<UpdateSpawnerPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ExtraStructures.MOD_ID, "update_spawner"));

    public static final StreamCodec<FriendlyByteBuf, UpdateSpawnerPayload> STREAM_CODEC = StreamCodec.ofMember(
            UpdateSpawnerPayload::write, UpdateSpawnerPayload::new
    );

    public UpdateSpawnerPayload(FriendlyByteBuf buffer) {
        this(
                buffer.readBlockPos(), buffer.readDouble(), buffer.readUtf(),
                buffer.readInt(), buffer.readInt(), buffer.readInt(),
                buffer.readInt(), buffer.readUtf(), buffer.readUtf(256),
                buffer.readUtf(), buffer.readUtf(), buffer.readUtf(),
                buffer.readUtf(), buffer.readUtf(), buffer.readDouble(),
                buffer.readDouble(), buffer.readDouble(), buffer.readBoolean()
        );
    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeDouble(radius);
        buffer.writeUtf(species);
        buffer.writeInt(minIv);
        buffer.writeInt(maxIv);
        buffer.writeInt(level);
        buffer.writeInt(shinyLuck);
        buffer.writeUtf(nature);
        buffer.writeUtf(heldItem, 256);
        buffer.writeUtf(ability);
        buffer.writeUtf(m1);
        buffer.writeUtf(m2);
        buffer.writeUtf(m3);
        buffer.writeUtf(m4);
        buffer.writeDouble(offX);
        buffer.writeDouble(offY);
        buffer.writeDouble(offZ);
        buffer.writeBoolean(reset);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}