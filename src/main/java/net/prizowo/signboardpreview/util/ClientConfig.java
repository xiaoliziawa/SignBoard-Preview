package net.prizowo.signboardpreview.util;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ClientConfig {
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.BooleanValue SHOW_PREVIEW;
    
    public static final ModConfigSpec.DoubleValue TEXT_SCALE_REGULAR;
    public static final ModConfigSpec.DoubleValue TEXT_SCALE_HANGING;
    
    public static final ModConfigSpec.DoubleValue Z_OFFSET_FRONT;
    public static final ModConfigSpec.DoubleValue Y_OFFSET_STANDING;
    public static final ModConfigSpec.DoubleValue Y_OFFSET_WALL;
    public static final ModConfigSpec.DoubleValue Y_OFFSET_HANGING;
    
    public static final ModConfigSpec.IntValue LINE_HEIGHT;
    public static final ModConfigSpec.IntValue LINE_SPACING;
    
    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        
        builder.comment("SignBoard Preview Settings").push("general");
        
        SHOW_PREVIEW = builder
            .comment("是否显示告示牌文本预览 | Whether to show sign text preview")
            .define("showPreview", true);
            
        builder.pop();
        
        // 文本外观 Text Appearance
        builder.comment("文本外观设置 | Text Appearance Settings").push("appearance");
        
        LINE_HEIGHT = builder
            .comment("文本行高 | Text line height")
            .defineInRange("lineHeight", 10, 5, 20);
            
        LINE_SPACING = builder
            .comment("文本行间距 | Text line spacing")
            .defineInRange("lineSpacing", -2, -10, 10);
            
        builder.pop();
        
        // 位置设置 Position Settings
        builder.comment("位置和大小设置 | Position and Size Settings").push("position");
        
        TEXT_SCALE_REGULAR = builder
            .comment("普通告示牌文本缩放比例 | Regular sign text scale")
            .defineInRange("textScaleRegular", 0.025, 0.01, 0.1);
            
        TEXT_SCALE_HANGING = builder
            .comment("悬挂式告示牌文本缩放比例 | Hanging sign text scale")
            .defineInRange("textScaleHanging", 0.0225, 0.01, 0.1);
            
        Z_OFFSET_FRONT = builder
            .comment("正面文本Z轴偏移量 | Front text Z-axis offset")
            .defineInRange("zOffsetFront", 0.03, 0.01, 0.1);
            
        Y_OFFSET_STANDING = builder
            .comment("立式告示牌Y轴偏移量 | Standing sign Y-axis offset")
            .defineInRange("yOffsetStanding", 1.8, 0.5, 3.0);
            
        Y_OFFSET_WALL = builder
            .comment("墙式告示牌Y轴偏移量 | Wall sign Y-axis offset")
            .defineInRange("yOffsetWall", 1.5, 0.5, 3.0);
            
        Y_OFFSET_HANGING = builder
            .comment("悬挂式告示牌Y轴偏移量 | Hanging sign Y-axis offset")
            .defineInRange("yOffsetHanging", -0.9, -2.0, 0.0);
            
        builder.pop();
        
        SPEC = builder.build();
    }
} 