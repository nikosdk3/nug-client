package nikosdk3.nugclient.modules.player;

import com.google.common.eventbus.Subscribe;
import net.minecraft.client.MinecraftClient;
import nikosdk3.nugclient.events.event.TickEvent;
import nikosdk3.nugclient.modules.Category;
import nikosdk3.nugclient.modules.Module;

import java.lang.reflect.Field;

public class FastUse extends Module {
    private Field itemUseCooldown;

    public FastUse() {
        super(Category.Player, "fast-use", "Use items faster.");

        try {
            itemUseCooldown = MinecraftClient.class.getDeclaredField("itemUseCooldown");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    @Subscribe
    private void onTick(TickEvent event) {
        try {
            itemUseCooldown.set(mc, 0);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
