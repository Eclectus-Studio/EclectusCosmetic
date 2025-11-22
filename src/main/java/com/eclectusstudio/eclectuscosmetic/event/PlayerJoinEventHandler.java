package com.eclectusstudio.eclectuscosmetic.event;

import com.eclectusstudio.eclectuscosmetic.data.cape.Capes;
import com.eclectusstudio.eclectuscosmetic.packet.EclectusCosmeticNetworking;
import com.eclectusstudio.eclectuscosmetic.packet.record.CapeData;
import com.eclectusstudio.eclectuscosmetic.registry.CapeRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;
import java.util.stream.Collectors;


public class PlayerJoinEventHandler {

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayer joiningPlayer = (ServerPlayer) event.getEntity();
        // Get the server instance
        var server = joiningPlayer.getServer();
        if (server == null) return;

        // ---------------------------
        // 1) All other players except the joining player
        // ---------------------------
        List<ServerPlayer> otherPlayers = server.getPlayerList().getPlayers().stream()
                .filter(player -> !player.getUUID().equals(joiningPlayer.getUUID()))
                .collect(Collectors.toList());

        for (ServerPlayer player : otherPlayers) {
            if(CapeRegistry.hasCape(player.getUUID())){
                PacketDistributor.sendToPlayer(player, new CapeData(player.getStringUUID(), Capes.INSTANCE.getCape(CapeRegistry.getCape(player.getUUID())).texture));
            }
        }

        // ---------------------------
        // 2) All players including the joining player
        // ---------------------------
        if(CapeRegistry.hasCape(joiningPlayer.getUUID())){
            PacketDistributor.sendToAllPlayers(new CapeData(joiningPlayer.getStringUUID(), Capes.INSTANCE.getCape(CapeRegistry.getCape(joiningPlayer.getUUID())).texture));
        }
    }
}
