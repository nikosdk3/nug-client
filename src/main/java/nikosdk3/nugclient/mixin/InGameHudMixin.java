package nikosdk3.nugclient.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import nikosdk3.nugclient.NugClient;
import nikosdk3.nugclient.events.EventStore;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Shadow
    private int scaledWidth;

    @Shadow
    private int scaledHeight;

    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(at = @At("TAIL"), method = "render")
    private void onRender(DrawContext context, float tickDelta, CallbackInfo ci) {
        client.getProfiler().push(NugClient.MOD_ID + "_render");
        NugClient.eventBus.post(EventStore.render2DEvent(context, scaledWidth, scaledHeight, tickDelta));
        client.getProfiler().pop();
    }
}
