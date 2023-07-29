package nikosdk3.nugclient.events.event;

import net.minecraft.network.packet.Packet;
import nikosdk3.nugclient.events.Cancellable;

public class SendPacketEvent extends Cancellable {
    public Packet<?> packet;
}
