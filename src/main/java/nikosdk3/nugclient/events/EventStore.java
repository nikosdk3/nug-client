package nikosdk3.nugclient.events;


import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.network.packet.Packet;
import nikosdk3.nugclient.events.event.*;

public class EventStore {
    private static ActiveModulesChangedEvent activeModulesChangedEvent = new ActiveModulesChangedEvent();
    private static ChangeChatLengthEvent changeChatLengthEvent = new ChangeChatLengthEvent();
    private static CharTypedEvent charTypedEvent = new CharTypedEvent();
    private static KeyEvent keyEvent = new KeyEvent();
    private static ModuleBindChangedEvent moduleBindChangedEvent = new ModuleBindChangedEvent();
    private static ModuleVisibilityChangedEvent moduleVisibilityChangedEvent = new ModuleVisibilityChangedEvent();
    private static PlaySoundEvent playSoundEvent = new PlaySoundEvent();
    private static Render2DEvent render2DEvent = new Render2DEvent();
    private static RenderEvent renderEvent = new RenderEvent();
    private static SendPacketEvent sendPacketEvent = new SendPacketEvent();
    private static TickEvent tickEvent = new TickEvent();

    public static ActiveModulesChangedEvent activeModulesChangedEvent() {
        return activeModulesChangedEvent;
    }

    public static ChangeChatLengthEvent changeChatLengthEvent(int length) {
        changeChatLengthEvent.length = length;
        return changeChatLengthEvent;
    }

    public static CharTypedEvent charTypedEvent(char c) {
        charTypedEvent.c = c;
        return charTypedEvent;
    }

    public static KeyEvent keyEvent(int key, boolean push) {
        keyEvent.setCancelled(false);
        keyEvent.key = key;
        keyEvent.push = push;
        return keyEvent;
    }

    public static ModuleBindChangedEvent moduleBindChangedEvent() {
        return moduleBindChangedEvent;
    }

    public static ModuleVisibilityChangedEvent moduleVisibilityChangedEvent() {
        return moduleVisibilityChangedEvent;
    }

    public static PlaySoundEvent playSoundEvent(SoundInstance sound) {
        playSoundEvent.sound = sound;
        return playSoundEvent;
    }

    public static Render2DEvent render2DEvent(DrawContext drawContext, int screenWidth, int screenHeight, float tickDelta) {
        render2DEvent.drawContext = drawContext;
        render2DEvent.screenWidth = screenWidth;
        render2DEvent.screenHeight = screenHeight;
        render2DEvent.tickDelta = tickDelta;
        return render2DEvent;
    }

    public static RenderEvent renderEvent(float tickDelta) {
        renderEvent.tickDelta = tickDelta;
        return renderEvent;
    }

    public static SendPacketEvent sendPacketEvent(Packet<?> packet) {
        sendPacketEvent.setCancelled(false);
        sendPacketEvent.packet = packet;
        return sendPacketEvent;
    }

    public static TickEvent tickEvent() {
        return tickEvent;
    }
}