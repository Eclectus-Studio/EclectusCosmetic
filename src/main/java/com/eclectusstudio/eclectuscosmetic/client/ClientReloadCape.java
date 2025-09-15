package com.eclectusstudio.eclectuscosmetic.client;

import com.eclectusstudio.eclectuscosmetic.packet.EclectusCosmeticNetworking;
import com.eclectusstudio.eclectuscosmetic.packet.capes.CapeUserGet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.player.Player;


public class ClientReloadCape {
    public static void reload() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        // Iterate over all players that the client knows about
        for (Player player : mc.level.players()) {
            // Request the server to send cape info for each
            EclectusCosmeticNetworking.sendToServer(new CapeUserGet(player.getUUID()));
        }
    }
}
