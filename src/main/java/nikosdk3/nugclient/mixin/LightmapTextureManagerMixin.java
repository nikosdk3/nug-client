package nikosdk3.nugclient.mixin;

import net.minecraft.client.render.LightmapTextureManager;
import nikosdk3.nugclient.modules.ModuleManager;
import nikosdk3.nugclient.modules.render.FullBright;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LightmapTextureManager.class)
public abstract class LightmapTextureManagerMixin {
    @ModifyArg(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/NativeImage;setColor(III)V"), index = 2)
    private int update(int color) {
        if (ModuleManager.get(FullBright.class).isActive()) return 0xFFFFFFFF;
        else return color;
    }
}
