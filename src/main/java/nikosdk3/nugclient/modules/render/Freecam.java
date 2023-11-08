package nikosdk3.nugclient.modules.render;

import com.google.common.eventbus.Subscribe;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;
import nikosdk3.nugclient.events.event.SendPacketEvent;
import nikosdk3.nugclient.events.event.TickEvent;
import nikosdk3.nugclient.modules.Category;
import nikosdk3.nugclient.modules.Module;
import nikosdk3.nugclient.settings.DoubleSetting;
import nikosdk3.nugclient.settings.Setting;

public class Freecam extends Module {
    private final Setting<Double> speed = addSetting(new DoubleSetting.Builder()
            .name("speed")
            .description("Speed")
            .defaultValue(1.0)
            .min(0.0)
            .build()
    );

    private Entity origCamera;
    private OtherClientPlayerEntity newCamera;
    private OtherClientPlayerEntity dummy;

    public Freecam() {
        super(Category.Render, "freecam", "Navigate the world as a spectator.");
    }

    @Override
    public void onActivate() {
        origCamera = mc.cameraEntity;

        newCamera = new OtherClientPlayerEntity(mc.world, mc.player.getGameProfile());
        newCamera.copyPositionAndRotation(mc.player);
        newCamera.horizontalCollision = false;
        newCamera.verticalCollision = false;

        dummy = new OtherClientPlayerEntity(mc.world, mc.player.getGameProfile());
        dummy.copyPositionAndRotation(mc.player);
        dummy.setBoundingBox(dummy.getBoundingBox().expand(0.1));

        mc.world.addEntity(newCamera.getId(), newCamera);
        mc.world.addEntity(dummy.getId(), dummy);

        mc.cameraEntity = newCamera;
    }

    @Override
    public void onDeactivate() {
        mc.cameraEntity = origCamera;

        mc.world.removeEntity(newCamera.getId(), Entity.RemovalReason.DISCARDED);
        mc.world.removeEntity(dummy.getId(), Entity.RemovalReason.DISCARDED);
    }

    @Subscribe
    private void onSendPacket(SendPacketEvent event) {
        if (event.packet instanceof ClientCommandC2SPacket || event.packet instanceof PlayerMoveC2SPacket || event.packet instanceof PlayerInputC2SPacket)
            event.cancel();
    }

    @Subscribe
    private void onTick(TickEvent event) {
        newCamera.setVelocity(0, 0, 0);

        newCamera.setYaw(mc.player.getYaw());
        newCamera.setPitch(mc.player.getPitch());
        newCamera.setHeadYaw(mc.player.headYaw);
        newCamera.setBodyYaw(mc.player.bodyYaw);
        newCamera.elytraYaw = mc.player.elytraYaw;
        newCamera.elytraPitch = mc.player.elytraPitch;

        double speed = this.speed.get() / 2;
        Vec3d velocity = newCamera.getVelocity();
        Vec3d forward = new Vec3d(0, 0, speed).rotateY(-(float) Math.toRadians(newCamera.headYaw));
        Vec3d strafe = forward.rotateY((float) Math.toRadians(90));

        if (mc.options.forwardKey.isPressed()) velocity = velocity.add(forward.x, 0, forward.z);
        if (mc.options.backKey.isPressed()) velocity = velocity.subtract(forward.x, 0, forward.z);
        if (mc.options.leftKey.isPressed()) velocity = velocity.add(strafe.x, 0, strafe.z);
        if (mc.options.rightKey.isPressed()) velocity = velocity.subtract(strafe.x, 0, strafe.z);
        if (mc.options.jumpKey.isPressed()) velocity = velocity.add(0, speed, 0);
        if (mc.options.sneakKey.isPressed()) velocity = velocity.subtract(0, speed, 0);

        newCamera.setPos(newCamera.getX() + velocity.x, newCamera.getY() + velocity.y, newCamera.getZ() + velocity.z);
    }
}
