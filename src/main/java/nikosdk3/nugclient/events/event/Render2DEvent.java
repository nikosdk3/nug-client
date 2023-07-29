package nikosdk3.nugclient.events.event;

import net.minecraft.client.gui.DrawContext;

public class Render2DEvent {
    public DrawContext drawContext;
    public int screenWidth, screenHeight;
    public float tickDelta;
}
