package nikosdk3.nugclient.modules.render;

import com.google.common.eventbus.Subscribe;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
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
import nikosdk3.nugclient.utils.Utils;

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

    private final Setting<Boolean> storage = addSetting(new BoolSetting.Builder()
            .name("storage")
            .description("Trace storage blocks.")
            .defaultValue(false)
            .build()
    );

    private Vec3d vec1;

    public Tracers() {
        super(Category.Render, "tracers", "Renders lines to entities.");
    }

    public void render(Entity entity) {
        Vec3d vec2 = entity.getPos().add(0, entity.getEyeHeight(entity.getPose()), 0);
        RenderUtils.line(vec1.x, vec1.y, vec1.z, vec2.x, vec2.y, vec2.z, color.get());
    }

    @Subscribe
    public void onRender(RenderEvent event) {
        vec1 = new Vec3d(0, 0, 100)
                .rotateX(-(float) Math.toRadians(mc.player.getPitch()))
                .rotateY(-(float) Math.toRadians(mc.player.getYaw()))
                .add(mc.cameraEntity.getPos()
                        .add(0, mc.cameraEntity.getEyeHeight(mc.cameraEntity.getPose()), 0));
        for (Entity entity : mc.world.getEntities()) {
            if (players.get() && EntityUtils.isPlayer(entity) && entity != mc.player) render(entity);
            if (mobs.get() && EntityUtils.isMob(entity)) render(entity);
            if (animals.get() && EntityUtils.isAnimal(entity)) render(entity);
            if (storage.get()) {
                for (BlockEntity blockEntity : Utils.blockEntities()) {
                    if (blockEntity instanceof ChestBlockEntity || blockEntity instanceof ShulkerBoxBlockEntity || blockEntity instanceof BarrelBlockEntity) {
                        BlockPos pos = blockEntity.getPos();
                        RenderUtils.line(vec1.x, vec1.y, vec1.z, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, color.get());
                    }
                }
            }
        }
    }
}
