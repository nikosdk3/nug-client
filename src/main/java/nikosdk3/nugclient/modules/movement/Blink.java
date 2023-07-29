package nikosdk3.nugclient.modules.movement;

import com.google.common.eventbus.Subscribe;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import nikosdk3.nugclient.events.event.SendPacketEvent;
import nikosdk3.nugclient.events.event.TickEvent;
import nikosdk3.nugclient.modules.Category;
import nikosdk3.nugclient.modules.Module;

import java.util.ArrayList;
import java.util.List;

public class Blink extends Module {
    public Blink() {
        super(Category.Movement, "blink", "Suspends all motion updates while enabled.");
    }

    private List<PlayerMoveC2SPacket> packets = new ArrayList<>();
    private int timer = 0;

    @Override
    public void onDeactivate() {
        packets.forEach(p -> mc.player.networkHandler.sendPacket(p));
        packets.clear();
        timer = 0;
    }

    @Subscribe
    private void onTick(TickEvent event) {
        timer++;
    }

    @Subscribe
    private void onSendPacket(SendPacketEvent event) {
        if (!(event.packet instanceof PlayerMoveC2SPacket)) return;
        event.setCancelled(true);

        PlayerMoveC2SPacket p = (PlayerMoveC2SPacket) event.packet;
        PlayerMoveC2SPacket prev = packets.size() == 0 ? null : packets.get(packets.size() - 1);

        if (prev != null &&
                p.isOnGround() == prev.isOnGround() &&
                p.getYaw(-1) == prev.getYaw(-1) &&
                p.getPitch(-1) == prev.getPitch(-1) &&
                p.getX(-1) == prev.getPitch(-1) &&
                p.getY(-1) == prev.getY(-1) &&
                p.getZ(-1) == prev.getZ(-1)
        ) return;

        packets.add(p);
    }

    @Override
    public String getInfoString() {
        return String.format("%.1f", timer / 20f);
    }
}
