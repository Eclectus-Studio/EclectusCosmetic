package com.eclectusstudio.eclectuscosmetic.packet.capes;

import com.eclectusstudio.eclectuscosmetic.client.ClientCapeCache;
import com.eclectusstudio.eclectuscosmetic.packet.record.CapeData;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public class CapeClientPayloadHandler {
    public static void handleDataOnMain(final CapeData data, final IPayloadContext ctx){
        ClientCapeCache.set(UUID.fromString(data.playerUUID()), ResourceLocation.parse(data.capeLocation()));
    }
}
