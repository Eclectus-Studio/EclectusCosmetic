package com.eclectusstudio.eclectuscosmetic.packet.capes;

import com.eclectusstudio.eclectuscosmetic.client.toast.CapeToast;
import com.eclectusstudio.eclectuscosmetic.packet.record.CapeToastData;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class CapeToastClientPayloadHandler {
    public static void handleDataOnMain(final CapeToastData data, final IPayloadContext ctx){
        Minecraft.getInstance().getToasts().addToast(new CapeToast(
                Component.literal(data.capeName()),
                ResourceLocation.parse(data.capeLocation())
        ));
    }
}
