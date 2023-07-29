package nikosdk3.nugclient.modules.movement;

import com.google.common.eventbus.Subscribe;
import nikosdk3.nugclient.events.event.TickEvent;
import nikosdk3.nugclient.mixininterface.IKeyBinding;
import nikosdk3.nugclient.modules.Category;
import nikosdk3.nugclient.modules.Module;
import nikosdk3.nugclient.settings.EnumSetting;
import nikosdk3.nugclient.settings.Setting;
//import nikosdk3.nugclient.utils.GoalDirection;

public class AutoWalk extends Module {
    public enum Mode {
        Simple,
        Smart
    }

    private Setting<Mode> mode = addSetting(new EnumSetting.Builder<Mode>()
            .name("mode")
            .description("Walking mode.")
            .defaultValue(Mode.Simple)
            .build()
    );

    private int timer = 0;
//    private GoalDirection goal;

    public AutoWalk() {
        super(Category.Movement, "auto-walk", "Automatically walks.");
    }

    @Override
    public void onActivate() {
        if (mode.get() == Mode.Smart) {
            timer = 0;
            //goal = new GoalDirection(mc.player.getPos(),mc.player.getYaw());
            //BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(goal);
        }
    }

    @Override
    public void onDeactivate() {
        if (mode.get() == Mode.Simple) {
            ((IKeyBinding) mc.options.forwardKey).setPressed(false);
        } else {
            //BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().cancelEverything();
        }
    }

    @Subscribe
    public void onTick(TickEvent event) {
        if (mode.get() == Mode.Simple) {
            ((IKeyBinding) mc.options.forwardKey).setPressed(true);
        } else {
            if (timer > 20) {
//                goal.recalculate(mc.player.getPos());
            }
            timer++;
        }
    }
}
