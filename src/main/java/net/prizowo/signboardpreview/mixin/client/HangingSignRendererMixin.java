package net.prizowo.signboardpreview.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.AbstractSignRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.prizowo.signboardpreview.util.ClientConfig;
import net.prizowo.signboardpreview.util.SignRenderHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSignRenderer.class)
public class HangingSignRendererMixin {
    @Inject(method = "render(Lnet/minecraft/world/level/block/entity/SignBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V", at = @At("RETURN"))
    private void renderHangingSignPreview(SignBlockEntity signBlockEntity, float partialTick, PoseStack poseStack,
                                          MultiBufferSource buffer, int packedLight, int packedOverlay, CallbackInfo ci) {
        if (!(signBlockEntity instanceof HangingSignBlockEntity)) return;
        if (!ClientConfig.SHOW_PREVIEW.get()) return;

        renderHangingSignText(signBlockEntity, poseStack, buffer, packedLight, packedOverlay);
    }

    private void renderHangingSignText(SignBlockEntity signBlockEntity, PoseStack poseStack,
                                       MultiBufferSource buffer, int packedLight, int packedOverlay) {
        var signText = signBlockEntity.getFrontText();
        if (SignRenderHelper.getPreviewTextLines(signText).isEmpty()) return;

        poseStack.pushPose();

        BlockState blockState = signBlockEntity.getBlockState();
        boolean isCeilingSign = blockState.getBlock() instanceof CeilingHangingSignBlock;
        boolean isWallSign = blockState.getBlock() instanceof WallHangingSignBlock;

        float zOffset = SignRenderHelper.getZOffsetFront();

        poseStack.translate(0.5f, ClientConfig.Y_OFFSET_HANGING.get().floatValue(), 0.5f);

        if (isWallSign) {
            Direction facing = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);
            switch (facing) {
                case NORTH -> poseStack.translate(0, 0, zOffset);
                case SOUTH -> poseStack.translate(0, 0, -zOffset);
                case WEST -> poseStack.translate(zOffset, 0, 0);
                case EAST -> poseStack.translate(-zOffset, 0, 0);
            }
        } else if (isCeilingSign) {
            poseStack.translate(0, 0, zOffset);
        }

        SignRenderHelper.renderText(
                poseStack,
                buffer,
                signText,
                SignRenderHelper.getTextScaleHanging(),
                true,
                packedLight,
                packedOverlay
        );

        poseStack.popPose();
    }
} 