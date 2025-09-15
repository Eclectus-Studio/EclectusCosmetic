package com.eclectusstudio.eclectuscosmetic.event;

import com.eclectusstudio.eclectuscosmetic.packet.EclectusCosmeticNetworking;
import com.eclectusstudio.eclectuscosmetic.packet.capes.CapeUserSend;
import com.eclectusstudio.eclectuscosmetic.registry.CapeRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber
public class PlayerJoinEventHandler {

    @SubscribeEvent
    public static void onPlayerJoin(PlayerLoggedInEvent event) {
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
                EclectusCosmeticNetworking.sendToPlayer(new CapeUserSend(player.getUUID(), CapeRegistry.getCape(player.getUUID()).toString()), joiningPlayer);
            }

        }

        // ---------------------------
        // 2) All players including the joining player
        // ---------------------------
        List<ServerPlayer> allPlayers = server.getPlayerList().getPlayers();
        for (ServerPlayer player : allPlayers) {
            if(CapeRegistry.hasCape(joiningPlayer.getUUID())){
                EclectusCosmeticNetworking.sendToPlayer(new CapeUserSend(joiningPlayer.getUUID(), CapeRegistry.getCape(player.getUUID()).toString()), player);
            }
        }
    }
}
