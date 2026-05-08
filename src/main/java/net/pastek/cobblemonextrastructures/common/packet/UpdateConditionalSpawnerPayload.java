package net.pastek.cobblemonextrastructures.common.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.pastek.cobblemonextrastructures.ExtraStructures;

public record UpdateConditionalSpawnerPayload(
        BlockPos pos,
        double radius, String species, int minIv, int maxIv, int level, int shinyLuck, 
        String nature, String heldItem, String ability, String m1, String m2, String m3, String m4,
        double offX, double offY, double offZ,
        int reqLevel, String reqSpecies, String reqItem, String reqAdv,
        boolean reset
) implements CustomPacketPayload {

    public static final Type<UpdateConditionalSpawnerPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ExtraStructures.MOD_ID, "update_conditional_spawner"));

    public static final StreamCodec<FriendlyByteBuf, UpdateConditionalSpawnerPayload> STREAM_CODEC = StreamCodec.ofMember(
            UpdateConditionalSpawnerPayload::write, UpdateConditionalSpawnerPayload::new
    );

    public UpdateConditionalSpawnerPayload(FriendlyByteBuf buf) {
        this(buf.readBlockPos(), buf.readDouble(), buf.readUtf(), buf.readInt(), buf.readInt(), buf.readInt(), 
             buf.readInt(), buf.readUtf(), buf.readUtf(256), buf.readUtf(), buf.readUtf(), buf.readUtf(),
             buf.readUtf(), buf.readUtf(), buf.readDouble(), buf.readDouble(), buf.readDouble(),
             buf.readInt(), buf.readUtf(256), buf.readUtf(256), buf.readUtf(256), buf.readBoolean());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeDouble(radius);
        buf.writeUtf(species);
        buf.writeInt(minIv);
        buf.writeInt(maxIv);
        buf.writeInt(level);
        buf.writeInt(shinyLuck);
        buf.writeUtf(nature);
        buf.writeUtf(heldItem, 256);
        buf.writeUtf(ability);
        buf.writeUtf(m1);
        buf.writeUtf(m2);
        buf.writeUtf(m3);
        buf.writeUtf(m4);
        buf.writeDouble(offX);
        buf.writeDouble(offY);
        buf.writeDouble(offZ);
        buf.writeInt(reqLevel);
        buf.writeUtf(reqSpecies, 256);
        buf.writeUtf(reqItem, 256);
        buf.writeUtf(reqAdv, 256);
        buf.writeBoolean(reset);
    }

    @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
}