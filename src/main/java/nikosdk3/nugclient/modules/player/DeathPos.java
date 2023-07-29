package nikosdk3.nugclient.modules.player;

import com.google.common.eventbus.Subscribe;
import nikosdk3.nugclient.events.event.TickEvent;
import nikosdk3.nugclient.modules.Category;
import nikosdk3.nugclient.modules.Module;
import nikosdk3.nugclient.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DeathPos extends Module {
    private boolean isDead;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    public DeathPos() {
        super(Category.Player, "death-pos", "Sends your last death position to your chat.");
    }

    @Subscribe
    private void onTick(TickEvent event) {
        if (mc.player.getHealth() <= 0 && !isDead) {
            Utils.sendMessage("#yellowDied at #blue%.1f#yellow, #blue%.1f#yellow, #blue%.1f#yellow at #blue%s#yellow.", mc.player.getX(), mc.player.getY(), mc.player.getZ(), dateFormat.format(new Date()));
            isDead = true;
        }
        if (mc.player.getHealth() > 0 && isDead) isDead = false;
    }
}
