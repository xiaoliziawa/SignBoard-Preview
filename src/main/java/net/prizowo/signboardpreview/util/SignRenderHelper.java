package net.prizowo.signboardpreview.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.level.block.entity.SignText;

import java.util.ArrayList;
import java.util.List;

public class SignRenderHelper {
    public static float getTextScaleRegular() {
        return ClientConfig.TEXT_SCALE_REGULAR.get().floatValue();
    }
    
    public static float getTextScaleHanging() {
        return ClientConfig.TEXT_SCALE_HANGING.get().floatValue();
    }
    
    public static float getZOffsetFront() {
        return ClientConfig.Z_OFFSET_FRONT.get().floatValue();
    }
    
    public static float getZOffsetBack() {
        return -ClientConfig.Z_OFFSET_FRONT.get().floatValue();
    }
    
    public static int getBackgroundColor() {
        return ClientConfig.BACKGROUND_COLOR.get();
    }
    
    public static int getLineHeight() {
        return ClientConfig.LINE_HEIGHT.get();
    }
    
    public static int getLineSpacing() {
        return ClientConfig.LINE_SPACING.get();
    }

    public static void renderText(PoseStack poseStack, MultiBufferSource buffer, 
                                  SignText signText, float textScale, boolean isFront,
                                  int packedLight, int packedOverlay) {
        List<String> textLines = getPreviewTextLines(signText);
        if (textLines.isEmpty()) return;
        
        Minecraft mc = Minecraft.getInstance();
        Camera camera = mc.gameRenderer.getMainCamera();
        Font font = mc.font;
        
        float yRot = -camera.getYRot();
        if (!isFront) {
            yRot += 180.0f;
        }
        
        float xRot = camera.getXRot();
        
        poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
        poseStack.mulPose(Axis.XP.rotationDegrees(xRot));
        
        poseStack.scale(-textScale, -textScale, textScale);
        
        Style style = prepareTextStyle(signText);
        
        int lineHeight = getLineHeight();
        int lineSpacing = getLineSpacing();
        int totalHeight = textLines.size() * lineHeight + (textLines.size() - 1) * lineSpacing;
        int yStart = -totalHeight / 2;
        
        for (int i = 0; i < textLines.size(); i++) {
            String line = textLines.get(i);
            Component textComponent = Component.literal(line).withStyle(style);
            int textWidth = font.width(textComponent);
            
            font.drawInBatch(textComponent, -textWidth / 2, yStart + i * lineHeight, 
                            0xFFFFFF, false, poseStack.last().pose(), buffer, 
                            Font.DisplayMode.NORMAL, getBackgroundColor(), packedLight);
        }
    }
    
    private static Style prepareTextStyle(SignText signText) {
        Style style = Style.EMPTY;
        if (signText.getColor() != null) {
            style = style.withColor(signText.getColor().getTextColor());
        }
        if (signText.hasGlowingText()) {
            style = style.withBold(true);
        }
        return style;
    }
    
    public static List<String> getPreviewTextLines(SignText signText) {
        List<String> lines = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            String line = signText.getMessage(i, false).getString().trim();
            if (!line.isEmpty()) {
                lines.add(line);
            }
        }
        return lines;
    }
}