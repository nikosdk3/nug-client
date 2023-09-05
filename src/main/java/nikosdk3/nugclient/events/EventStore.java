package nikosdk3.nugclient.events;


import net.minecraft.block.BlockState;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.network.packet.Packet;
import nikosdk3.nugclient.events.event.*;

public class EventStore {
    private static final ActiveModulesChangedEvent activeModulesChangedEvent = new ActiveModulesChangedEvent();
    private static final BlockShouldDrawSideEvent blockShouldDrawSideEvent = new BlockShouldDrawSideEvent();
    private static final ChangeChatLengthEvent changeChatLengthEvent = new ChangeChatLengthEvent();
    private static final CharTypedEvent charTypedEvent = new CharTypedEvent();
    private static final KeyEvent keyEvent = new KeyEvent();
    private static final ModuleBindChangedEvent moduleBindChangedEvent = new ModuleBindChangedEvent();
    private static final ModuleVisibilityChangedEvent moduleVisibilityChangedEvent = new ModuleVisibilityChangedEvent();
    private static final PlaySoundEvent playSoundEvent = new PlaySoundEvent();
    private static final Render2DEvent render2DEvent = new Render2DEvent();
    private static final RenderEvent renderEvent = new RenderEvent();
    private static final SendPacketEvent sendPacketEvent = new SendPacketEvent();
    private static final TickEvent tickEvent = new TickEvent();

    public static ActiveModulesChangedEvent activeModulesChangedEvent() {
        return activeModulesChangedEvent;
    }

    public static BlockShouldDrawSideEvent blockShouldDrawSideEvent(BlockState state) {
        blockShouldDrawSideEvent.setCancelled(false);
        blockShouldDrawSideEvent.state = state;
        blockShouldDrawSideEvent.shouldRenderSide = false;
        return blockShouldDrawSideEvent;
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

    public static RenderEvent renderEvent() {
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
