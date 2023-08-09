package nikosdk3.nugclient.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import nikosdk3.nugclient.NugClient;
import nikosdk3.nugclient.events.EventStore;
import nikosdk3.nugclient.events.event.BlockShouldDrawSideEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class BlockMixin {
    @Inject(at = @At("HEAD"), method = "shouldDrawSide", cancellable = true)
    private static void onShouldDrawSide(BlockState state, BlockView world, BlockPos pos, Direction side, BlockPos otherPos, CallbackInfoReturnable<Boolean> info) {
        BlockShouldDrawSideEvent event = EventStore.blockShouldDrawSideEvent(state);
        NugClient.eventBus.post(event);

        if(event.isCancelled()) info.setReturnValue(event.shouldRenderSide);
    }
}
