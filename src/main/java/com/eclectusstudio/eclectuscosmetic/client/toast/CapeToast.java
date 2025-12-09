package com.eclectusstudio.eclectuscosmetic.client.toast;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class CapeToast implements Toast {

    private static final ResourceLocation TOAST_BG = ResourceLocation.withDefaultNamespace("toast/advancement");

    private final Component title;
    private final ResourceLocation capeIcon;
    private long startedTime = 0;

    public CapeToast(Component title, ResourceLocation capeIcon) {
        this.title = title;
        this.capeIcon = capeIcon;
    }

    @Override
    public Visibility render(GuiGraphics gfx, ToastComponent component, long time) {

        if (startedTime == 0)
            startedTime = time;

        // === Draw Background ===
        gfx.blitSprite(TOAST_BG, 0, 0, this.width(), this.height());

        // === Draw Icon (front of the cape region 1,1 to 10,15) ===
        gfx.blit(
                capeIcon,
                8, 8,               // x,y on toast
                1, 1,               // u,v texture start
                10, 15,             // width, height of region you want
                64, 32              // full icon size
        );

        // === Draw Title ===
        gfx.drawString(
                Minecraft.getInstance().font,
                title,
                30, 12,
                0xFFFFFF,
                false
        );

        // 5-second display time
        if (time - startedTime < 5000)
            return Visibility.SHOW;

        return Visibility.HIDE;
    }

    @Override
    public int width() {
        return 160;
    }

    @Override
    public int height() {
        return 32;
    }
}
