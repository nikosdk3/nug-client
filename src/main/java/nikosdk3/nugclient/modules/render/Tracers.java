package nikosdk3.nugclient.modules.render;

import com.google.common.eventbus.Subscribe;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import nikosdk3.nugclient.events.event.RenderEvent;
import nikosdk3.nugclient.modules.Category;
import nikosdk3.nugclient.modules.Module;
import nikosdk3.nugclient.settings.BoolSetting;
import nikosdk3.nugclient.settings.ColorSetting;
import nikosdk3.nugclient.settings.Setting;
import nikosdk3.nugclient.utils.Color;
import nikosdk3.nugclient.utils.EntityUtils;
import nikosdk3.nugclient.utils.RenderUtils;

public class Tracers extends Module {
    private final Setting<Color> color = addSetting(new ColorSetting.Builder()
            .name("color")
            .description("Color.")
            .defaultValue(new Color(205, 205, 205, 255))
            .build()
    );

    private final Setting<Boolean> players = addSetting(new BoolSetting.Builder()
            .name("players")
            .description("Trace players.")
            .defaultValue(true)
            .build()
    );

    private final Setting<Boolean> mobs = addSetting(new BoolSetting.Builder()
            .name("mobs")
            .description("Trace mobs.")
            .defaultValue(false)
            .build()
    );

    private final Setting<Boolean> animals = addSetting(new BoolSetting.Builder()
            .name("animals")
            .description("Trace animals.")
            .defaultValue(false)
            .build()
    );

    public Tracers() {
        super(Category.Render, "tracers", "Renders lines to entities.");
    }

    public void render(Entity entity) {
        Vec3d vec1 = new Vec3d(0, 0, 100)
                .rotateX(-(float) Math.toRadians(mc.player.getPitch()))
                .rotateY(-(float) Math.toRadians(mc.player.getYaw()))
                .add(mc.cameraEntity.getPos()
                        .add(0, mc.cameraEntity.getEyeHeight(mc.cameraEntity.getPose()), 0));
        Vec3d vec2 = entity.getPos().add(0, entity.getEyeHeight(entity.getPose()), 0);

        RenderUtils.line(vec1.x, vec1.y, vec1.z, vec2.x, vec2.y, vec2.z, color.get());
    }

    @Subscribe
    public void onRender(RenderEvent event) {
        for (Entity entity : mc.world.getEntities()) {
            if (players.get() && EntityUtils.isPlayer(entity) && entity != mc.player) render(entity);
            if (mobs.get() && EntityUtils.isMob(entity)) render(entity);
            if (animals.get() && EntityUtils.isAnimal(entity)) render(entity);
        }
    }
}
