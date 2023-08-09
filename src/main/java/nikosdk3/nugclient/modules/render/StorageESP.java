package nikosdk3.nugclient.modules.render;

import com.google.common.eventbus.Subscribe;
import net.minecraft.block.entity.*;
import nikosdk3.nugclient.events.event.RenderEvent;
import nikosdk3.nugclient.modules.Category;
import nikosdk3.nugclient.modules.Module;
import nikosdk3.nugclient.settings.ColorSetting;
import nikosdk3.nugclient.settings.EnumSetting;
import nikosdk3.nugclient.settings.Setting;
import nikosdk3.nugclient.utils.Color;
import nikosdk3.nugclient.utils.RenderUtils;
import nikosdk3.nugclient.utils.Utils;

public class StorageESP extends Module {
    public enum Mode {
        Lines,
        Sides,
        Both
    }

    private Setting<Mode> mode = addSetting(new EnumSetting.Builder<Mode>()
            .name("mode")
            .description("Rendering mode.")
            .defaultValue(Mode.Both)
            .build()
    );

    private Setting<Color> standard = addSetting(new ColorSetting.Builder()
            .name("standard")
            .description("Color of chests, barrels, and shulker blocks.")
            .defaultValue(new Color(255, 160, 0, 255))
            .build()
    );

    private Setting<Color> enderChest = addSetting(new ColorSetting.Builder()
            .name("ender-chest")
            .description("Color of ender chests.")
            .defaultValue(new Color(130, 0, 255, 255))
            .build()
    );

    private Setting<Color> other = addSetting(new ColorSetting.Builder()
            .name("other")
            .description("Color of furnaces, dispensers, droppers and hoppers")
            .defaultValue(new Color(125, 125, 125, 255))
            .build()
    );

    private final Color lineColor = new Color(0, 0, 0, 0);
    private final Color sideColor = new Color(0, 0, 0, 0);
    private boolean render;

    public StorageESP() {
        super(Category.Render, "storage-esp", "Highlights storage blocks.");
    }

    @Subscribe
    public void onRender(RenderEvent event) {
        for (BlockEntity blockEntity : Utils.blockEntities()) {
            getBlockEntityColor(blockEntity);
            if (render) {
                int x = blockEntity.getPos().getX();
                int y = blockEntity.getPos().getY();
                int z = blockEntity.getPos().getZ();

                if (mode.get() == StorageESP.Mode.Lines) {
                    RenderUtils.blockEdges(x, y, z, lineColor);
//                    System.out.println(lineColor.r + " " + lineColor.g + " " + lineColor.b + " ");
                }
                else if (mode.get() == StorageESP.Mode.Sides)
                    RenderUtils.blockSides(x, y, z, sideColor);
                else {
//                    System.out.println(x + " " + y + " " + z);
                    RenderUtils.blockEdges(x, y, z, lineColor);
                    RenderUtils.blockSides(x, y, z, sideColor);
                }
            }
        }
    }

    private void getBlockEntityColor(BlockEntity blockEntity) {
        render = true;

        if (blockEntity instanceof ChestBlockEntity || blockEntity instanceof BarrelBlockEntity || blockEntity instanceof ShulkerBoxBlockEntity)
            lineColor.set(standard.get());
        else if (blockEntity instanceof EnderChestBlockEntity)
            lineColor.set(enderChest.get());
        else if (blockEntity instanceof FurnaceBlockEntity || blockEntity instanceof DispenserBlockEntity || blockEntity instanceof DropperBlockEntity || blockEntity instanceof HopperBlockEntity)
            lineColor.set(other.get());
        else render = false;

        if (mode.get() == StorageESP.Mode.Sides || mode.get() == StorageESP.Mode.Both) {
            sideColor.set(lineColor);
            sideColor.a -= 225;
            if (sideColor.a < 0) sideColor.a = 0;
        }
    }
}
