package nikosdk3.nugclient.mixin;

import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import nikosdk3.nugclient.NugClient;
import nikosdk3.nugclient.events.event.CharTypedEvent;
import nikosdk3.nugclient.events.EventStore;
import nikosdk3.nugclient.events.event.KeyEvent;
import nikosdk3.nugclient.utils.Utils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public abstract class KeyboardMixin {
    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "onKey", at = @At("HEAD"), cancellable = true)
    public void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo info) {
        if (Utils.canUpdate() && !client.isPaused() && client.currentScreen == null) {
            KeyEvent event = EventStore.keyEvent(key, action == 1);
            NugClient.eventBus.post(event);

            if (event.isCancelled()) info.cancel();
        }
    }

    @Inject(method = "onChar", at = @At("HEAD"), cancellable = true)
    private void onChar(long window, int codePoint, int modifiers, CallbackInfo info) {
        if (Utils.canUpdate() && !client.isPaused() && client.currentScreen == null) {
            CharTypedEvent event = EventStore.charTypedEvent((char) codePoint);
            NugClient.eventBus.post(event);

            if (event.isCancelled()) info.cancel();
        }
    }
}
