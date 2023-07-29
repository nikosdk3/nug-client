package nikosdk3.nugclient.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import nikosdk3.nugclient.CommandDispatcher;
import nikosdk3.nugclient.Config;
import nikosdk3.nugclient.NugClient;
import nikosdk3.nugclient.events.EventStore;
import nikosdk3.nugclient.events.event.SendPacketEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {

    @Shadow @Final private MinecraftClient client;

    @Inject(at = @At("TAIL"), method = "onGameJoin")
    private void onGameJoin(GameJoinS2CPacket packet, CallbackInfo info) {
        NugClient.loadConfig();
    }

    @Inject(at = @At("HEAD"), method = "sendPacket(Lnet/minecraft/network/packet/Packet;)V", cancellable = true)
    public void onSendPacket(Packet packet, CallbackInfo info) {
        SendPacketEvent packetEvent = EventStore.sendPacketEvent(packet);
        NugClient.eventBus.post(packetEvent);
        if(packetEvent.isCancelled()) info.cancel();
    }

    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    private void onSendChatMessage(String content, CallbackInfo info) {
        if(content.startsWith(Config.instance.prefix)) {
            CommandDispatcher.run(content.substring(Config.instance.prefix.length()));
            client.inGameHud.getChatHud().addToMessageHistory(content);
            info.cancel();
        }
    }
}
