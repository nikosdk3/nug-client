package nikosdk3.nugclient.modules.render;

import com.google.common.eventbus.Subscribe;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import nikosdk3.nugclient.events.event.RenderEvent;
import nikosdk3.nugclient.modules.Category;
import nikosdk3.nugclient.modules.Module;
import nikosdk3.nugclient.settings.BoolSetting;
import nikosdk3.nugclient.settings.ColorSetting;
import nikosdk3.nugclient.settings.EnumSetting;
import nikosdk3.nugclient.settings.Setting;
import nikosdk3.nugclient.utils.Color;
import nikosdk3.nugclient.utils.EntityUtils;
import nikosdk3.nugclient.utils.RenderUtils;
import org.joml.Matrix4f;

public class ESP extends Module {
    public enum Mode {
        Lines,
        Sides,
        Both,
        Glowing
    }

    private final Setting<Mode> mode = addSetting(new EnumSetting.Builder<Mode>()
            .name("mode")
            .description("Rendering mode.")
            .defaultValue(Mode.Both)
            .build()
    );

    private final Setting<Color> color = addSetting(new ColorSetting.Builder()
            .name("color")
            .description("Rendering color.")
            .defaultValue(new Color(175, 175, 175, 255))
            .build()
    );

    private final Setting<Boolean> players = addSetting(new BoolSetting.Builder()
            .name("players")
            .description("See players.")
            .defaultValue(true)
            .build()
    );

    private final Setting<Boolean> animals = addSetting(new BoolSetting.Builder()
            .name("animals")
            .description("See animals.")
            .defaultValue(true)
            .build()
    );

    private final Setting<Boolean> mobs = addSetting(new BoolSetting.Builder()
            .name("mobs")
            .description("See mobs.")
            .defaultValue(true)
            .build()
    );

    private final Setting<Boolean> items = addSetting(new BoolSetting.Builder()
            .name("items")
            .description("See items.")
            .defaultValue(true)
            .build()
    );

    private final Setting<Boolean> crystals = addSetting(new BoolSetting.Builder()
            .name("crystals")
            .description("See crystals.")
            .defaultValue(true)
            .build()
    );

    private final Setting<Boolean> vehicles = addSetting(new BoolSetting.Builder()
            .name("vehicles")
            .description("See vehicles.")
            .defaultValue(true)
            .build()
    );

    private final Color lineColor = new Color(0, 0, 0, 0);
    private final Color sideColor = new Color(0, 0, 0, 0);

    public ESP() {
        super(Category.Render, "esp", "Render entities through walls.");
    }

    @Override
    public void onDeactivate() {
        if (mode.get() == Mode.Glowing) {
            for (Entity entity : mc.world.getEntities()) {
                if (entity != mc.player)
                    entity.setGlowing(false);
            }
        }
    }

    private void recalculateColor() {
        lineColor.set(color.get());

        sideColor.set(lineColor);
        sideColor.a = 25;
    }

    private void render(Entity entity, Matrix4f matrix) {
        switch (mode.get()) {
            case Lines -> {
                Box box = entity.getBoundingBox();
                RenderUtils.boxEdges(matrix, box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, lineColor);
            }
            case Sides -> {
                Box box = entity.getBoundingBox();
                RenderUtils.boxSides(matrix, box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, sideColor);
            }
            case Both -> {
                Box box = entity.getBoundingBox();
                RenderUtils.boxEdges(matrix, box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, lineColor);
                RenderUtils.boxSides(matrix, box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, sideColor);
            }
            case Glowing -> entity.setGlowing(true);
        }
    }

    @Subscribe
    private void onRender(RenderEvent event) {
        Matrix4f matrix = event.matrixStack.peek().getPositionMatrix();
        recalculateColor();
        for (Entity entity : mc.world.getEntities()) {
            if (players.get() && EntityUtils.isPlayer(entity) && entity != mc.player) render(entity, matrix);
            else if (mobs.get() && EntityUtils.isMob(entity)) render(entity, matrix);
            else if (animals.get() && EntityUtils.isAnimal(entity)) render(entity, matrix);
            else if (items.get() && EntityUtils.isItem(entity)) render(entity, matrix);
            else if (crystals.get() && EntityUtils.isCrystal(entity)) render(entity, matrix);
            else if (vehicles.get() && EntityUtils.isVehicle(entity)) render(entity, matrix);
        }
    }
}