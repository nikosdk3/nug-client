package nikosdk3.nugclient.modules.combat;

import com.google.common.eventbus.Subscribe;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import nikosdk3.nugclient.events.event.SendPacketEvent;
import nikosdk3.nugclient.mixininterface.IPlayerInteractEntityC2SPacket;
import nikosdk3.nugclient.modules.Category;
import nikosdk3.nugclient.modules.Module;
import nikosdk3.nugclient.settings.BoolSetting;
import nikosdk3.nugclient.settings.Setting;

public class Criticals extends Module {
    private final Setting<Boolean> onlyOnGround = addSetting(new BoolSetting.Builder()
            .name("only-on-ground")
            .description("Attacks crit only on ground.")
            .defaultValue(false)
            .build()
    );

    public Criticals() {
        super(Category.Combat, "criticals", "Makes every hit crit.");
    }

    @Subscribe
    private void onSendPacket(SendPacketEvent event) {
        if (event.packet instanceof IPlayerInteractEntityC2SPacket packet && packet.getType() == PlayerInteractEntityC2SPacket.InteractType.ATTACK) {
            if (!doCrit())
                return;

            double x = mc.player.getX();
            double y = mc.player.getY();
            double z = mc.player.getZ();

            PlayerMoveC2SPacket upPacket = new PlayerMoveC2SPacket.PositionAndOnGround(x, y + 0.0625, z, false);
            PlayerMoveC2SPacket downPacket = new PlayerMoveC2SPacket.PositionAndOnGround(x, y, z, false);

            mc.player.networkHandler.sendPacket(upPacket);
            mc.player.networkHandler.sendPacket(downPacket);
        }
    }

    private boolean doCrit() {
        boolean b = !mc.player.isSubmergedInWater() && !mc.player.isInLava() && !mc.player.isClimbing();
        if (onlyOnGround.get()) return b && mc.player.isOnGround();
        return b;
    }
}
