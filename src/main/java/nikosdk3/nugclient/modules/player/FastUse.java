package nikosdk3.nugclient.modules.player;

import com.google.common.eventbus.Subscribe;
import nikosdk3.nugclient.events.event.TickEvent;
import nikosdk3.nugclient.mixininterface.IMinecraftClient;
import nikosdk3.nugclient.modules.Category;
import nikosdk3.nugclient.modules.Module;

public class FastUse extends Module {
    public FastUse() {
        super(Category.Player, "fast-use", "Use items faster.");
    }

    @Subscribe
    private void onTick(TickEvent event) {
        ((IMinecraftClient) mc).setItemUseCooldown(0);
    }
}
