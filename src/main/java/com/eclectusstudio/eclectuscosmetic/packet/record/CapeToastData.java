package com.eclectusstudio.eclectuscosmetic.packet.record;

import com.eclectusstudio.eclectuscosmetic.EclectusCosmetic;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record CapeToastData(String capeLocation, String capeName) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<CapeToastData> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(EclectusCosmetic.MODID, "unlock_cape_networking"));

    public static final StreamCodec<ByteBuf, CapeToastData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, CapeToastData::capeLocation,
            ByteBufCodecs.STRING_UTF8, CapeToastData::capeName,
            CapeToastData::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
