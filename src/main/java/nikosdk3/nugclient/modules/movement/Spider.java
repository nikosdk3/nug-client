package nikosdk3.nugclient.modules.movement;

import com.google.common.eventbus.Subscribe;
import net.minecraft.util.math.Vec3d;
import nikosdk3.nugclient.events.event.TickEvent;
import nikosdk3.nugclient.modules.Category;
import nikosdk3.nugclient.modules.Module;
import nikosdk3.nugclient.settings.DoubleSetting;
import nikosdk3.nugclient.settings.Setting;

public class Spider extends Module {
    private Setting<Double> speed = addSetting(new DoubleSetting.Builder()
            .name("speed")
            .description("Speed.")
            .defaultValue(0.4)
            .min(0.0)
            .max(2.0)
            .build()
    );

    public Spider() {
        super(Category.Movement, "spider", "Allows you to climb walls.");
    }

    @Subscribe
    private void onTick(TickEvent event){
        if(!mc.player.horizontalCollision) return;

        Vec3d velocity = mc.player.getVelocity();
        if(velocity.y >= 0.2) return;

        mc.player.setVelocity(velocity.x, speed.get(), velocity.z);
    }
}
