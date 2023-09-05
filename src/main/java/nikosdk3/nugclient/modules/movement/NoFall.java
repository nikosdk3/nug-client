package nikosdk3.nugclient.modules.movement;

import com.google.common.eventbus.Subscribe;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import nikosdk3.nugclient.events.event.SendPacketEvent;
import nikosdk3.nugclient.mixin.PlayerMoveC2SPacketAccessor;
import nikosdk3.nugclient.modules.Category;
import nikosdk3.nugclient.modules.Module;

public class NoFall extends Module {
    public NoFall() {
        super(Category.Movement, "no-fall", "Protects you from fall damage.");
    }

    @Subscribe
    private void onTick(SendPacketEvent event) {
        if (!(event.packet instanceof PlayerMoveC2SPacket) || mc.player.getVelocity().y > -0.5 || mc.player.getAbilities().creativeMode)
            return;
        ((PlayerMoveC2SPacketAccessor) event.packet).setOnGround(true);
    }
}