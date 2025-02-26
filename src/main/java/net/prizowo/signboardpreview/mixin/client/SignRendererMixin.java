package net.prizowo.signboardpreview.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.AbstractSignRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.prizowo.signboardpreview.util.ClientConfig;
import net.prizowo.signboardpreview.util.SignRenderHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSignRenderer.class)
public class SignRendererMixin {
    @Inject(method = "render(Lnet/minecraft/world/level/block/entity/SignBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V", at = @At("RETURN"))
    private void renderPreview(SignBlockEntity signBlockEntity, float partialTick, PoseStack poseStack,
                               MultiBufferSource buffer, int packedLight, int packedOverlay, CallbackInfo ci) {
        if (signBlockEntity instanceof HangingSignBlockEntity) return;
        if (!ClientConfig.SHOW_PREVIEW.get()) return;

        renderSignText(signBlockEntity, poseStack, buffer, packedLight, packedOverlay);
    }
    
    private void renderSignText(SignBlockEntity signBlockEntity, PoseStack poseStack, 
                                MultiBufferSource buffer, int packedLight, int packedOverlay) {
        var signText = signBlockEntity.getFrontText();
        if (SignRenderHelper.getPreviewTextLines(signText).isEmpty()) return;

        poseStack.pushPose();

        BlockState blockState = signBlockEntity.getBlockState();
        boolean isStandingSign = blockState.getBlock() instanceof StandingSignBlock;
        
        float yOffset = isStandingSign ? 
                ClientConfig.Y_OFFSET_STANDING.get().floatValue() : 
                ClientConfig.Y_OFFSET_WALL.get().floatValue();
        float zOffset = SignRenderHelper.getZOffsetFront();

        poseStack.translate(0.5f, yOffset, 0.5f);
        
        if (!isStandingSign) {
            Direction facing = blockState.getValue(WallSignBlock.FACING);
            switch (facing) {
                case NORTH -> poseStack.translate(0, 0, zOffset);
                case SOUTH -> poseStack.translate(0, 0, -zOffset);
                case WEST -> poseStack.translate(zOffset, 0, 0);
                case EAST -> poseStack.translate(-zOffset, 0, 0);
            }
        } else {
            poseStack.translate(0, 0, zOffset);
        }

        SignRenderHelper.renderText(
            poseStack, 
            buffer, 
            signText, 
            SignRenderHelper.getTextScaleRegular(), 
            true, 
            packedLight, 
            packedOverlay
        );

        poseStack.popPose();
    }
} 