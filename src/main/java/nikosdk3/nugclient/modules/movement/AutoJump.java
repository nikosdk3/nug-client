package nikosdk3.nugclient.modules.movement;

import com.google.common.eventbus.Subscribe;
import nikosdk3.nugclient.events.event.TickEvent;
import nikosdk3.nugclient.modules.Category;
import nikosdk3.nugclient.modules.Module;
import nikosdk3.nugclient.settings.Setting;
import nikosdk3.nugclient.settings.EnumSetting;

public class AutoJump extends Module {
    public enum JumpIf {
        Sprinting,
        Walking,
        Always
    }

    private Setting<JumpIf> jumpIf = addSetting(new EnumSetting.Builder<JumpIf>()
            .name("jump-if")
            .description("Jump if.")
            .defaultValue(JumpIf.Always)
            .build()
    );

    public AutoJump() {
        super(Category.Movement, "auto-jump", "Automatically jumps.");
    }

    private boolean jump() {
        switch (jumpIf.get()) {
            case Sprinting:
                return mc.player.isSprinting() && (mc.player.forwardSpeed != 0 || mc.player.sidewaysSpeed != 0);
            case Walking:
                return !mc.player.isSprinting() && (mc.player.forwardSpeed != 0 || mc.player.sidewaysSpeed != 0);
            case Always:
                return true;
            default:
                return false;
        }
    }

    @Subscribe
    private void onTick(TickEvent event) {
        if (!mc.player.isOnGround() || mc.player.isSneaking()) return;

        if (jump()) mc.player.jump();
    }
}
