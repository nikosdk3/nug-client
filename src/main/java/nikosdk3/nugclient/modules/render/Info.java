package nikosdk3.nugclient.modules.render;

import com.google.common.collect.Iterables;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.gui.DrawContext;
import nikosdk3.nugclient.events.event.Render2DEvent;
import nikosdk3.nugclient.modules.Category;
import nikosdk3.nugclient.modules.Module;
import nikosdk3.nugclient.settings.BoolSetting;
import nikosdk3.nugclient.settings.Setting;
import nikosdk3.nugclient.utils.Color;
import nikosdk3.nugclient.utils.Utils;

public class Info extends Module {
    private Setting<Boolean> fps = addSetting(new BoolSetting.Builder()
            .name("fps")
            .description("Displays fps.")
            .defaultValue(true)
            .build()
    );

    private Setting<Boolean> entities = addSetting(new BoolSetting.Builder()
            .name("entities")
            .description("Displays number of entities.")
            .defaultValue(true)
            .build()
    );

    public Info() {
        super(Category.Render, "info", "Displays various info.");
    }

    private void drawInfo(DrawContext context, String text1, String text2, int y) {
        Utils.drawText(context, text1, 2, y, Color.fromRGBA(255, 255, 255, 255), true);
        Utils.drawText(context, text2, 2 + Utils.getTextWidth(text1), y, Color.fromRGBA(185, 185, 185, 255), true);
    }

    @Subscribe
    private void onRender2D(Render2DEvent event) {
        if (mc.options.debugEnabled) return;
        int y = 2;

        if (fps.get()) {
            drawInfo(event.drawContext, "FPS: ", mc.getCurrentFps() + "", y);
            y += Utils.getTextHeight() + 2;
        }

        if (entities.get()) {
            drawInfo(event.drawContext, "Entities: ", Iterables.size(mc.world.getEntities()) + "", y);
            y += Utils.getTextHeight() + 2;
        }
    }
}
