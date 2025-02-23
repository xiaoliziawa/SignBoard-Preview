package net.prizowo.signboardpreview;

import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.prizowo.signboardpreview.event.SignDisplayEvents;

@Mod(Signboardpreview.MODID)
public class Signboardpreview {
    public static final String MODID = "signboardpreview";

    public Signboardpreview() {
        NeoForge.EVENT_BUS.register(new SignDisplayEvents());
    }
}
