package com.eclectusstudio.eclectuscosmetic.packet.capes;

import com.eclectusstudio.eclectuscosmetic.client.ClientCapeCache;
import com.eclectusstudio.eclectuscosmetic.data.cape.Cape;
import com.eclectusstudio.eclectuscosmetic.data.cape.Capes;
import com.eclectusstudio.eclectuscosmetic.packet.EclectusCosmeticNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;

import java.util.UUID;

public class CapeUserGet {
    private final UUID target;

    public CapeUserGet(UUID target) {
        this.target = target;
    }

    public CapeUserGet(FriendlyByteBuf buf) {
        this.target = buf.readUUID();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(target);
    }

    public void handle(CustomPayloadEvent.Context context) {
        System.out.println("Cape user get received");
        context.enqueueWork(() -> {
            ServerPlayer sender = context.getSender();
            if (sender == null) return;
            System.out.println("sender is not null");
            var capeId = ClientCapeCache.get(target);
            if (capeId == null) return;
            System.out.println("Requested cape isnt null");

            Cape cape = Capes.INSTANCE.getCape(capeId);
            if (cape == null) return;
            System.out.println("Cape is in registry");

            EclectusCosmeticNetworking.sendToPlayer(new CapeUserSend(target, cape.texture), sender);
        });
        context.setPacketHandled(true);
    }
}
