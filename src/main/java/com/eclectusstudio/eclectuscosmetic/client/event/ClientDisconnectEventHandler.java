package com.eclectusstudio.eclectuscosmetic.client.event;

import com.eclectusstudio.eclectuscosmetic.client.ClientCapeCache;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;

public class ClientDisconnectEventHandler {

    @SubscribeEvent
    public void onClientLogout(ClientPlayerNetworkEvent.LoggingOut event) {
        ClientCapeCache.clear();
    }
}
