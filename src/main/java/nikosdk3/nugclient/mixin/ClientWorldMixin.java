package nikosdk3.nugclient.mixin;

import net.minecraft.client.world.ClientWorld;
import nikosdk3.nugclient.NugClient;
import nikosdk3.nugclient.modules.ModuleManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin {
    @Inject(at = @At("TAIL"), method = "disconnect")
    private void onDisconnect(CallbackInfo info) {
        NugClient.saveConfig();
        ModuleManager.deactivateAll();
    }
}
