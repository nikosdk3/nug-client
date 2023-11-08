package nikosdk3.nugclient.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.world.ClientWorld;
import nikosdk3.nugclient.NugClient;
import nikosdk3.nugclient.events.EventStore;
import nikosdk3.nugclient.events.event.OpenScreenEvent;
import nikosdk3.nugclient.mixininterface.IMinecraftClient;
import nikosdk3.nugclient.utils.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin implements IMinecraftClient {
    @Shadow
    private static MinecraftClient instance;

    @Shadow
    public ClientWorld world;

    @Shadow
    private int itemUseCooldown;

    @Shadow
    protected abstract void doItemUse();

    @Shadow
    protected abstract boolean doAttack();

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(RunArgs args, CallbackInfo info) {
        NugClient.instance.onInitializeClient();
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void onTick(CallbackInfo info) {
        if (Utils.canUpdate()) {
            world.getProfiler().swap("nug-client_update");
            NugClient.eventBus.post(EventStore.tickEvent());
        }
    }

    @Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
    private void onOpenScreen(Screen screen, CallbackInfo info) {
        OpenScreenEvent event = EventStore.openScreenEvent(screen);
        NugClient.eventBus.post(event);

        if(event.isCancelled()) info.cancel();
    }

    @Override
    public void leftClick() {
        doAttack();
    }

    @Override
    public void rightClick() {
        doItemUse();
    }

    @Override
    public void setItemUseCooldown(int cooldown) {
        itemUseCooldown = cooldown;
    }
}