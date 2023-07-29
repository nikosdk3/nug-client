package nikosdk3.nugclient.modules.player;

import com.google.common.eventbus.Subscribe;
import net.minecraft.item.FishingRodItem;
import nikosdk3.nugclient.events.event.KeyEvent;
import nikosdk3.nugclient.events.event.TickEvent;
import nikosdk3.nugclient.events.event.PlaySoundEvent;
import nikosdk3.nugclient.modules.Category;
import nikosdk3.nugclient.modules.Module;
import nikosdk3.nugclient.settings.Setting;
import nikosdk3.nugclient.settings.BoolSetting;
import nikosdk3.nugclient.settings.IntSetting;
import nikosdk3.nugclient.utils.Utils;

public class AutoFish extends Module {
    private Setting<Boolean> autoCast = addSetting(new BoolSetting.Builder()
            .name("auto-fish")
            .description("Automatically casts when activated")
            .defaultValue(true)
            .build()
    );

    private Setting<Integer> ticksCatch = addSetting(new IntSetting.Builder()
            .name("ticks-catch")
            .description("Ticks to wait before catching the fish")
            .defaultValue(6)
            .min(2)
            .build()
    );

    private Setting<Integer> ticksCast = addSetting(new IntSetting.Builder()
            .name("ticks-cast")
            .description("Ticks to wait before casting rod")
            .defaultValue(14)
            .min(2)
            .build()
    );

    private boolean ticksEnabled;
    private boolean waitForFish;
    private int ticksToRightClick;

    public AutoFish() {
        super(Category.Player, "auto-fish", "Automatically fishes.");
    }

    @Override
    public void onActivate() {
        ticksEnabled = false;
        if (autoCast.get() && mc.player.getMainHandStack().getItem() instanceof FishingRodItem)
            Utils.rightClick();
    }

    @Subscribe
    private void onPlaySound(PlaySoundEvent event) {
        if (event.sound.getId().getPath().equals("entity.fishing_bobber.splash")) {
            ticksEnabled = true;
            ticksToRightClick = ticksCatch.get();
            waitForFish = false;
        }
    }

    @Subscribe
    private void onTick(TickEvent event) {
        if (ticksEnabled && ticksToRightClick <= 0) {
            if (!waitForFish) {
                Utils.rightClick();
                ticksToRightClick = ticksCast.get();
                waitForFish = true;
            } else {
                Utils.rightClick();
                ticksEnabled = false;
            }
        }
        ticksToRightClick--;
    }

    @Subscribe
    private void onKey(KeyEvent event) {
        if (mc.options.useKey.isPressed()) ticksEnabled = false;
    }
}
