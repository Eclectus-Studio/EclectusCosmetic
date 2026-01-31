package com.eclectusstudio.eclectuscosmetic.packet.capes;

import com.eclectusstudio.eclectuscosmetic.client.ClientCapeCache;
import com.eclectusstudio.eclectuscosmetic.packet.record.ClearPlayerCapeData;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public class UnequipPlayerCapePayloadHandler {
    public static void handleDataOnMain(final ClearPlayerCapeData data, final IPayloadContext ctx){
        ClientCapeCache.clearPlayer(UUID.fromString(data.playerUUID()));
    }
}
