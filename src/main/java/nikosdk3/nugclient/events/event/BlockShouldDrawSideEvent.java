package nikosdk3.nugclient.events.event;

import net.minecraft.block.BlockState;
import nikosdk3.nugclient.events.Cancellable;

public class BlockShouldDrawSideEvent extends Cancellable {
    public BlockState state;
    public boolean shouldRenderSide;
}
