package nikosdk3.nugclient.modules.misc;

import com.google.common.eventbus.Subscribe;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.gui.screen.ingame.AbstractSignEditScreen;
import net.minecraft.network.packet.c2s.play.UpdateSignC2SPacket;
import nikosdk3.nugclient.events.event.OpenScreenEvent;
import nikosdk3.nugclient.events.event.SendPacketEvent;
import nikosdk3.nugclient.modules.Category;
import nikosdk3.nugclient.modules.Module;

import java.lang.reflect.Field;

public class AutoSign extends Module {
    private String[] text;

    public AutoSign() {
        super(Category.Misc, "auto-sign", "Automatically writes signs. Write the first sign with the text you want to use.");
    }

    @Override
    public void onDeactivate() {
        text = null;
    }

    @Subscribe
    private void onSendPacket(SendPacketEvent event) {
        if (!(event.packet instanceof UpdateSignC2SPacket)) return;

        text = ((UpdateSignC2SPacket) event.packet).getText();
    }

    @Subscribe
    private void onOpenScreen(OpenScreenEvent event) {
        if (!(event.screen instanceof AbstractSignEditScreen) || text == null) return;

        try {
            Field field = AbstractSignEditScreen.class.getDeclaredField("blockEntity");
            field.setAccessible(true);
            SignBlockEntity sign = (SignBlockEntity) field.get(event.screen);

            mc.player.networkHandler.sendPacket(new UpdateSignC2SPacket(sign.getPos(), true, text[0], text[1], text[2], text[3]));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        event.setCancelled(true);
    }
}
