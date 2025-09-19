package com.eclectusstudio.eclectuscosmetic.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.Deadmau5EarsLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Deadmau5EarsLayer.class)
public abstract class Deadmau5EarsLayerMixin extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public Deadmau5EarsLayerMixin(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent) {
        super(parent);
    }

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void injectRender(PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                              AbstractClientPlayer player, float limbSwing, float limbSwingAmount,
                              float partialTicks, float ageInTicks, float netHeadYaw, float headPitch,
                              CallbackInfo ci) {
        // Deadmau5 = vanilla ears
        if ("deadmau5".equals(player.getName().getString()) && !player.isInvisible()) {
            return; // keep vanilla logic
        }

        // Everyone else: custom ears
        if (!player.isInvisible()) {
            VertexConsumer vertexConsumer =
                    buffer.getBuffer(RenderType.entitySolid(ResourceLocation.fromNamespaceAndPath("eclectuscosmetic", "textures/ears/ears.png")));
            int overlayCoords = LivingEntityRenderer.getOverlayCoords(player, 0.0F);

            for (int i = 0; i < 2; ++i) {
                float yaw = Mth.lerp(partialTicks, player.yRotO, player.getYRot()) -
                        Mth.lerp(partialTicks, player.yBodyRotO, player.yBodyRot);
                float pitch = Mth.lerp(partialTicks, player.xRotO, player.getXRot());

                poseStack.pushPose();
                poseStack.mulPose(Axis.YP.rotationDegrees(yaw));
                poseStack.mulPose(Axis.XP.rotationDegrees(pitch));
                poseStack.translate(0.375F * (float)(i * 2 - 1), 0.0F, 0.0F);
                poseStack.translate(0.0F, -0.375F, 0.0F);
                poseStack.mulPose(Axis.XP.rotationDegrees(-pitch));
                poseStack.mulPose(Axis.YP.rotationDegrees(-yaw));
                poseStack.scale(1.3333334F, 1.3333334F, 1.3333334F);

                ((PlayerModel<?>) this.getParentModel()).renderEars(poseStack, vertexConsumer, packedLight, overlayCoords);
                poseStack.popPose();
            }

            // Cancel vanilla so only our custom render runs
            ci.cancel();
        }
    }
}
