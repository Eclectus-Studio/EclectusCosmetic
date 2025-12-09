package com.eclectusstudio.eclectuscosmetic.packet.record;

import com.eclectusstudio.eclectuscosmetic.EclectusCosmetic;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record CapeData(String playerUUID, String capeLocation) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<CapeData> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(EclectusCosmetic.MODID, "set_cape_networking"));

    public static final StreamCodec<ByteBuf, CapeData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            CapeData::playerUUID,
            ByteBufCodecs.STRING_UTF8,
            CapeData::capeLocation,
            CapeData::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
