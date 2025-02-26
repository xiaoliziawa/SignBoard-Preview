package net.prizowo.signboardpreview;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.prizowo.signboardpreview.util.ClientConfig;

@Mod(Signboardpreview.MODID)
public class Signboardpreview {
    public static final String MODID = "signboardpreview";

    public Signboardpreview(ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
    }
}