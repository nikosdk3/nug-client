package nikosdk3.nugclient.mixin;

import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import nikosdk3.nugclient.modules.ModuleManager;
import nikosdk3.nugclient.modules.render.AntiFog;
import nikosdk3.nugclient.modules.render.XRay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {
    @Inject(method = "applyFog", at = @At("HEAD"), cancellable = true)
    private static void onApplyFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo info) {
        if(ModuleManager.get(AntiFog.class).isActive() || ModuleManager.get(XRay.class).isActive())
            info.cancel();
    }
}
