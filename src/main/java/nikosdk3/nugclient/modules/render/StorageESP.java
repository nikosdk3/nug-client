package nikosdk3.nugclient.modules.render;

import com.google.common.eventbus.Subscribe;
import net.minecraft.block.entity.*;
import net.minecraft.client.util.math.MatrixStack;
import nikosdk3.nugclient.events.event.RenderEvent;
import nikosdk3.nugclient.modules.Category;
import nikosdk3.nugclient.modules.Module;
import nikosdk3.nugclient.settings.ColorSetting;
import nikosdk3.nugclient.settings.EnumSetting;
import nikosdk3.nugclient.settings.Setting;
import nikosdk3.nugclient.utils.Color;
import nikosdk3.nugclient.utils.RenderUtils;
import nikosdk3.nugclient.utils.Utils;
import org.joml.Matrix4f;

public class StorageESP extends Module {
    public enum Mode {
        Lines,
        Sides,
        Both
    }

    private final Setting<Mode> mode = addSetting(new EnumSetting.Builder<Mode>()
            .name("mode")
            .description("Rendering mode.")
            .defaultValue(Mode.Both)
            .build()
    );

    private final Setting<Color> standard = addSetting(new ColorSetting.Builder()
            .name("standard")
            .description("Color of chests, barrels, and shulker blocks.")
            .defaultValue(new Color(255, 160, 0, 255))
            .build()
    );

    private final Setting<Color> enderChest = addSetting(new ColorSetting.Builder()
            .name("ender-chest")
            .description("Color of ender chests.")
            .defaultValue(new Color(130, 0, 255, 255))
            .build()
    );

    private final Setting<Color> other = addSetting(new ColorSetting.Builder()
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
            sideColor.a = 25;
        }
    }

    private void renderLines(MatrixStack matrixStack) {
        for (BlockEntity blockEntity : Utils.blockEntities()) {
            getBlockEntityColor(blockEntity);
            if (render) {
                int x = blockEntity.getPos().getX();
                int y = blockEntity.getPos().getY();
                int z = blockEntity.getPos().getZ();

                Matrix4f matrix = matrixStack.peek().getPositionMatrix();
                RenderUtils.blockEdges(matrix, x, y, z, lineColor);
            }
        }
    }

    private void renderSides(MatrixStack matrixStack) {
        for (BlockEntity blockEntity : Utils.blockEntities()) {
            getBlockEntityColor(blockEntity);
            if (render) {
                int x = blockEntity.getPos().getX();
                int y = blockEntity.getPos().getY();
                int z = blockEntity.getPos().getZ();

                Matrix4f matrix = matrixStack.peek().getPositionMatrix();
                RenderUtils.blockSides(matrix, x, y, z, sideColor);
            }
        }
    }

    @Subscribe
    public void onRender(RenderEvent event) {
        if (mode.get() == StorageESP.Mode.Lines) {
            renderLines(event.matrixStack);
        } else if (mode.get() == StorageESP.Mode.Sides) {
            renderSides(event.matrixStack);
        } else {
            renderLines(event.matrixStack);
            renderSides(event.matrixStack);
        }
    }
}
