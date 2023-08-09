package nikosdk3.nugclient.modules.movement;

import com.google.common.eventbus.Subscribe;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import nikosdk3.nugclient.events.event.SendPacketEvent;
import nikosdk3.nugclient.mixininterface.IPlayerMoveC2SPacket;
import nikosdk3.nugclient.modules.Category;
import nikosdk3.nugclient.modules.Module;

public class NoFall extends Module {
    public NoFall() {
        super(Category.Movement, "no-fall", "Protects you from fall damage.");
    }

    @Subscribe
    private void onSendPacket(SendPacketEvent event) {
        if (mc.player.fallDistance > 0.0 && event.packet instanceof PlayerMoveC2SPacket) {
            ((IPlayerMoveC2SPacket) event.packet).setOnGround(true);
        }
    }
}
