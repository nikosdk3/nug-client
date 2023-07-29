package nikosdk3.nugclient.modules.movement;

import com.google.common.eventbus.Subscribe;
import nikosdk3.nugclient.events.event.TickEvent;
import nikosdk3.nugclient.modules.Category;
import nikosdk3.nugclient.modules.Module;
import nikosdk3.nugclient.settings.EnumSetting;
import nikosdk3.nugclient.settings.Setting;

public class Flight extends Module {
    public enum Mode {
        Vanilla
    }

    private Setting<Mode> mode = addSetting(new EnumSetting.Builder<Mode>()
            .name("mode")
            .description("Mode.")
            .defaultValue(Mode.Vanilla)
            .build()
    );

    public Flight() {
        super(Category.Movement, "flight", "Allows you to fly.");
    }

    @Override
    public void onActivate() {
        if (mode.get() == Mode.Vanilla) {
            mc.player.getAbilities().flying = true;
            if (mc.player.getAbilities().creativeMode) return;
            mc.player.getAbilities().allowFlying = true;
        }
    }

    @Override
    public void onDeactivate() {
        if (mode.get() == Mode.Vanilla) {
            mc.player.getAbilities().flying = false;
            mc.player.getAbilities().setFlySpeed(0.05f);
            if (mc.player.getAbilities().creativeMode) return;
            mc.player.getAbilities().allowFlying = false;
        }
    }

    @Subscribe
    private void onTick(TickEvent event) {
        switch (mode.get()) {
            case Vanilla:
                mc.player.getAbilities().setFlySpeed(0.1f);
                mc.player.getAbilities().flying = true;
                if (mc.player.getAbilities().creativeMode) return;
                mc.player.getAbilities().allowFlying = true;
                break;
        }
    }
}
