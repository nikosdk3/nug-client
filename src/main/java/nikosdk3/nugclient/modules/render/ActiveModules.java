package nikosdk3.nugclient.modules.render;

import com.google.common.eventbus.Subscribe;
import nikosdk3.nugclient.events.event.ActiveModulesChangedEvent;
import nikosdk3.nugclient.events.event.ModuleVisibilityChangedEvent;
import nikosdk3.nugclient.events.event.Render2DEvent;
import nikosdk3.nugclient.modules.Category;
import nikosdk3.nugclient.modules.Module;
import nikosdk3.nugclient.modules.ModuleManager;
import nikosdk3.nugclient.utils.Color;
import nikosdk3.nugclient.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ActiveModules extends Module {
    private List<Module> modules = new ArrayList<>();
    private int infoColor = Color.fromRGBA(175, 175, 175, 255);

    public ActiveModules() {
        super(Category.Render, "active-modules", "Displays active modules.", false, false);
    }

    @Override
    public void onActivate() {
        recalculate();
    }

    private void recalculate() {
        modules.clear();

        for (Module module : ModuleManager.getActive()) {
            if (module.isVisible())
                modules.add(module);
        }

        modules.sort((o1, o2) -> {
            int a = Integer.compare(
                    o1.getInfoString() == null ? Utils.getTextWidth(o1.title) : Utils.getTextWidth(o1.title + " " + o1.getInfoString()),
                    o2.getInfoString() == null ? Utils.getTextWidth(o2.title) : Utils.getTextWidth(o2.title + " " + o2.getInfoString())
            );
            if (a == 0) return 0;
            return a < 0 ? 1 : -1;
        });
    }

    @Subscribe
    private void activeModulesChanged(ActiveModulesChangedEvent event) {
        recalculate();
    }

    @Subscribe
    private void onModuleVisibiltyChanged(ModuleVisibilityChangedEvent event) {
        if (event.module.isActive()) recalculate();
    }

    @Subscribe
    private void onRender2D(Render2DEvent event) {
        int y = 1;
        for (Module module : modules) {
            String infoString = module.getInfoString();
            if (infoString == null) {
                int x = event.screenWidth - Utils.getTextWidth(module.title) - 2;
                Utils.drawText(event.drawContext, module.title, x, y, module.color, true);
                y += Utils.getTextHeight() + 1;
            } else {
                int x = event.screenWidth - Utils.getTextWidth(module.title + " " + infoString) - 2;
                Utils.drawText(event.drawContext, module.title, x, y, module.color, true);
                Utils.drawText(event.drawContext, module.getInfoString(), x + Utils.getTextWidth(module.title + " "), y, infoColor, true);
                y += Utils.getTextHeight() + 1;
            }
        }
    }
}
