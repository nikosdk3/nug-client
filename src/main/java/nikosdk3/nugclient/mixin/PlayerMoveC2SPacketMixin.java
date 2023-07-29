package nikosdk3.nugclient.mixin;

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import nikosdk3.nugclient.mixininterface.IPlayerMoveC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerMoveC2SPacket.class)
public class PlayerMoveC2SPacketMixin implements IPlayerMoveC2SPacket {
    @Shadow protected boolean onGround;

    @Override
    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }
}
