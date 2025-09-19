package com.eclectusstudio.eclectuscosmetic.mixin.client;

import com.eclectusstudio.eclectuscosmetic.EclectusCosmetic;
import com.eclectusstudio.eclectuscosmetic.client.ClientCapeCache;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(value = AbstractClientPlayer.class, priority = 500)
public class AbstractClientPlayerEntityMixin {
    @Shadow @Nullable private PlayerInfo playerInfo;

    static {
        EclectusCosmetic.LOGGER.info("Mixin AbstractClientPlayerEntityMixin loaded!");
    }

    @Inject(method = "getSkin", at = @At("RETURN"), cancellable = true,order=100000)
    private void getSkin(CallbackInfoReturnable<PlayerSkin> cir) {
        if(ClientCapeCache.has(playerInfo.getProfile().getId())){
            ResourceLocation cape = ClientCapeCache.get(playerInfo.getProfile().getId());
            var texture = cir.getReturnValue();
            var newTexture = new PlayerSkin(
                    texture.texture(),
                    texture.textureUrl(),
                    cape,
                    texture.elytraTexture(),
                    texture.model(),
                    texture.secure()
            );
            cir.setReturnValue(newTexture);
            cir.cancel();
        }
    }
}
