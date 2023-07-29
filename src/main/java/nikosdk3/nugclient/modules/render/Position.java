package nikosdk3.nugclient.modules.render;

import com.google.common.eventbus.Subscribe;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.world.dimension.DimensionTypes;
import nikosdk3.nugclient.events.event.Render2DEvent;
import nikosdk3.nugclient.modules.Category;
import nikosdk3.nugclient.modules.Module;
import nikosdk3.nugclient.utils.Color;
import nikosdk3.nugclient.utils.Utils;

import java.text.DecimalFormat;

public class Position extends Module {
    private DecimalFormat decimalFormat = new DecimalFormat(".#");

    public Position() {
        super(Category.Render, "position", "Displays your position.");
    }

    private void drawPosition(DrawContext context, int screenWidth, String dimension, int yy, double x, double y, double z) {
        String coords = decimalFormat.format(x) + " " + decimalFormat.format(y) + " " + decimalFormat.format(z);
        int spacing = Utils.getTextWidth(dimension) + 2;
        Utils.drawText(context, dimension, 2, yy, Color.fromRGBA(255, 255, 255, 255), false);
        Utils.drawText(context, coords, spacing, yy, Color.fromRGBA(185, 185, 185, 255), false);
    }

    @Subscribe
    private void onRender2D(Render2DEvent event) {
        int y = event.screenHeight - Utils.getTextHeight() - 2;

        if (mc.player.getWorld().getDimensionKey() == DimensionTypes.OVERWORLD) {
            drawPosition(event.drawContext, event.screenWidth, "Nether Pos: ", y, mc.player.getX() / 8.0, mc.player.getY() / 8.0, mc.player.getZ() / 8.0);
            y -= Utils.getTextHeight() + 2;
            drawPosition(event.drawContext, event.screenWidth, "Pos: ", y, mc.player.getX(), mc.player.getY(), mc.player.getZ());
        } else if (mc.player.getWorld().getDimensionKey() == DimensionTypes.THE_NETHER) {
            drawPosition(event.drawContext, event.screenWidth, "Overworld Pos: ", y, mc.player.getX() / 8.0, mc.player.getY() / 8.0, mc.player.getZ() / 8.0);
            y -= Utils.getTextHeight() + 2;
            drawPosition(event.drawContext, event.screenWidth, "Pos: ", y, mc.player.getX(), mc.player.getY(), mc.player.getZ());
        } else if (mc.player.getWorld().getDimensionKey() == DimensionTypes.THE_END) {
            drawPosition(event.drawContext, event.screenWidth, "Pos: ", y, mc.player.getX(), mc.player.getY(), mc.player.getZ());
        }
    }
}
