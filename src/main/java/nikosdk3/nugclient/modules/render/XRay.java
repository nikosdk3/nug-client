package nikosdk3.nugclient.modules.render;

import com.google.common.eventbus.Subscribe;
import net.minecraft.block.Block;
import net.minecraft.text.Text;
import nikosdk3.nugclient.events.event.BlockShouldDrawSideEvent;
import nikosdk3.nugclient.modules.Category;
import nikosdk3.nugclient.modules.Module;

public class XRay extends Module {

    public XRay() {
        super(Category.Render, "xray", "See only specific blocks.");
    }

    @Override
    public void onActivate() {
        mc.worldRenderer.reload();
    }

    @Override
    public void onDeactivate() {
        mc.worldRenderer.reload();
    }

    @Subscribe
    private void onBlockShouldDrawSide(BlockShouldDrawSideEvent event) {
        event.shouldRenderSide = isVisible(event.state.getBlock());
        event.setCancelled(true);
    }

    private boolean isVisible(Block block) {
        return block.getName().contains(Text.of("_ore"));
    }
}
