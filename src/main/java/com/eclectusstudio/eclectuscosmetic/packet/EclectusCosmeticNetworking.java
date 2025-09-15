package com.eclectusstudio.eclectuscosmetic.packet;

import com.eclectusstudio.eclectuscosmetic.EclectusCosmetic;
import com.eclectusstudio.eclectuscosmetic.client.ClientReloadCape;
import com.eclectusstudio.eclectuscosmetic.packet.capes.CapeReload;
import com.eclectusstudio.eclectuscosmetic.packet.capes.CapeUserGet;
import com.eclectusstudio.eclectuscosmetic.packet.capes.CapeUserSend;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;

public class EclectusCosmeticNetworking {
    private static final SimpleChannel CHANNEL = ChannelBuilder.named(
                     ResourceLocation.fromNamespaceAndPath(EclectusCosmetic.MODID, "main"))
            .networkProtocolVersion(1)
            .serverAcceptedVersions((status, version) -> true)
            .clientAcceptedVersions((status, version) -> true)
            .simpleChannel();

    public static void register() {
        CHANNEL.messageBuilder(CapeReload.class, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(CapeReload::encode)
                .decoder(CapeReload::new)
                .consumerMainThread((msg, ctx) -> {
                    ClientReloadCape.reload(); // Do client-only stuff here
                    ctx.setPacketHandled(true);
                })
                .add();

        CHANNEL.messageBuilder(CapeUserGet.class, NetworkDirection.PLAY_TO_SERVER)
                .encoder(CapeUserGet::encode)
                .decoder(CapeUserGet::new)
                .consumerMainThread(CapeUserGet::handle)
                .add();

        CHANNEL.messageBuilder(CapeUserSend.class, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(CapeUserSend::encode)
                .decoder(CapeUserSend::new)
                .consumerMainThread(CapeUserSend::handle)
                .add();

    }

    public static void sendToAllClients(Object msg) {
        CHANNEL.send(msg, PacketDistributor.ALL.noArg());
    }

    public static void sendToServer(Object msg) {
        CHANNEL.send(msg, PacketDistributor.SERVER.noArg());
    }

    public static void sendToPlayer(Object msg, net.minecraft.server.level.ServerPlayer player) {
        CHANNEL.send(msg, PacketDistributor.PLAYER.with(player));
    }

}
