package com.eclectusstudio.eclectuscosmetic.packet;

import com.eclectusstudio.eclectuscosmetic.packet.capes.CapeClientPayloadHandler;
import com.eclectusstudio.eclectuscosmetic.packet.capes.CapeToastClientPayloadHandler;
import com.eclectusstudio.eclectuscosmetic.packet.capes.UnequipPlayerCapePayloadHandler;
import com.eclectusstudio.eclectuscosmetic.packet.record.CapeData;
import com.eclectusstudio.eclectuscosmetic.packet.record.CapeToastData;
import com.eclectusstudio.eclectuscosmetic.packet.record.ClearPlayerCapeData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class EclectusCosmeticNetworking {

    @SubscribeEvent
    public void register(final RegisterPayloadHandlersEvent event) {

        final PayloadRegistrar registrar = event.registrar("1.2");

        // Client-bound: CapeUserSend
        registrar.playToClient(
                CapeData.TYPE,
                CapeData.STREAM_CODEC,
                CapeClientPayloadHandler::handleDataOnMain
        );

        registrar.playToClient(
                CapeToastData.TYPE,
                CapeToastData.STREAM_CODEC,
                CapeToastClientPayloadHandler::handleDataOnMain
        );

        registrar.playToClient(
                ClearPlayerCapeData.TYPE,
                ClearPlayerCapeData.STREAM_CODEC,
                UnequipPlayerCapePayloadHandler::handleDataOnMain
        );
    }
}
