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

    private final Setting<Boolean> players = addSetting(new BoolSetting.Builder()
            .name("players")
            .description("See players.")
            .defaultValue(true)
            .build()
    );

    private final Setting<Color> playersColor = addSetting(new ColorSetting.Builder()
            .name("players-color")
            .description("Color for players.")
            .defaultValue(new Color(255, 255, 255, 255))
            .onChanged(color1 -> recalculateColor())
            .build()
    );

    private final Setting<Boolean> mobs = addSetting(new BoolSetting.Builder()
            .name("mobs")
            .description("See mobs.")
            .defaultValue(false)
            .build()
    );

    private final Setting<Color> mobsColor = addSetting(new ColorSetting.Builder()
            .name("mobs-color")
            .description("Color for mobs.")
            .defaultValue(new Color(255, 0, 0, 255))
            .onChanged(color1 -> recalculateColor())
            .build()
    );

    private final Setting<Boolean> animals = addSetting(new BoolSetting.Builder()
            .name("animals")
            .description("See animals.")
            .defaultValue(false)
            .build()
    );

    private final Setting<Color> animalsColor = addSetting(new ColorSetting.Builder()
            .name("animals-color")
            .description("Color for animals.")
            .defaultValue(new Color(0, 255, 0, 255))
            .onChanged(color1 -> recalculateColor())
            .build()
    );

    private final Setting<Boolean> items = addSetting(new BoolSetting.Builder()
            .name("items")
            .description("See items.")
            .defaultValue(false)
            .build()
    );

    private final Setting<Color> itemsColor = addSetting(new ColorSetting.Builder()
            .name("items-color")
            .description("Color for items.")
            .defaultValue(new Color(185, 185, 185, 255))
            .onChanged(color1 -> recalculateColor())
            .build()
    );

    private final Setting<Boolean> crystals = addSetting(new BoolSetting.Builder()
            .name("crystals")
            .description("See crystals.")
            .defaultValue(false)
            .build()
    );

    private final Setting<Color> crystalsColor = addSetting(new ColorSetting.Builder()
            .name("crystals-color")
            .description("Color for crystals.")
            .defaultValue(new Color(145, 25, 255, 255))
            .onChanged(color1 -> recalculateColor())
            .build()
    );

    private final Setting<Boolean> vehicles = addSetting(new BoolSetting.Builder()
            .name("vehicles")
            .description("See vehicles.")
            .defaultValue(false)
            .build()
    );

    private final Setting<Color> vehiclesColor = addSetting(new ColorSetting.Builder()
            .name("vehicles-color")
            .description("Color for vehicles.")
            .defaultValue(new Color(100, 100, 100, 255))
            .onChanged(color1 -> recalculateColor())
            .build()
    );

    private final Color playersLineColor = new Color();
    private final Color playersSideColor = new Color();
    private final Color animalsLineColor = new Color();
    private final Color animalsSideColor = new Color();
    private final Color mobsLineColor = new Color();
    private final Color mobsSideColor = new Color();
    private final Color itemsLineColor = new Color();
    private final Color itemsSideColor = new Color();
    private final Color crystalsLineColor = new Color();
    private final Color crystalsSideColor = new Color();
    private final Color vehiclesLineColor = new Color();
    private final Color vehiclesSideColor = new Color();

    public ESP() {
        super(Category.Render, "esp", "Render entities through walls.");
        recalculateColor();
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
        // Players
        playersLineColor.set(playersColor.get());
        playersSideColor.set(playersColor.get());
        playersSideColor.a = 25;

        // Mobs
        mobsLineColor.set(mobsColor.get());
        mobsSideColor.set(mobsColor.get());
        mobsSideColor.a = 25;

        // Animals
        animalsLineColor.set(animalsColor.get());
        animalsSideColor.set(animalsColor.get());
        animalsSideColor.a = 25;

        // Items
        itemsLineColor.set(itemsColor.get());
        itemsSideColor.set(itemsColor.get());
        itemsSideColor.a = 25;

        // Crystals
        crystalsLineColor.set(crystalsColor.get());
        crystalsSideColor.set(crystalsColor.get());
        crystalsSideColor.a = 25;

        // Vehicles
        vehiclesLineColor.set(vehiclesColor.get());
        vehiclesSideColor.set(vehiclesColor.get());
        vehiclesSideColor.a = 25;
    }

    private void render(Entity entity, Color lineColor, Color sideColor) {
        switch (mode.get()) {
            case Lines -> {
                Box box = entity.getBoundingBox();
                RenderUtils.boxEdges(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, lineColor);
            }
            case Sides -> {
                Box box = entity.getBoundingBox();
                RenderUtils.boxSides(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, sideColor);
            }
            case Both -> {
                Box box = entity.getBoundingBox();
                RenderUtils.boxEdges(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, lineColor);
                RenderUtils.boxSides(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, sideColor);
            }
            case Glowing -> entity.setGlowing(true);
        }
    }

    @Subscribe
    private void onRender(RenderEvent event) {
        for (Entity entity : mc.world.getEntities()) {
            if (players.get() && EntityUtils.isPlayer(entity) && entity != mc.player) render(entity, playersLineColor, playersSideColor);
            else if (mobs.get() && EntityUtils.isMob(entity)) render(entity, mobsLineColor, mobsSideColor);
            else if (animals.get() && EntityUtils.isAnimal(entity)) render(entity, animalsLineColor, animalsSideColor);
            else if (items.get() && EntityUtils.isItem(entity)) render(entity, itemsLineColor, itemsSideColor);
            else if (crystals.get() && EntityUtils.isCrystal(entity)) render(entity, crystalsLineColor, crystalsSideColor);
            else if (vehicles.get() && EntityUtils.isVehicle(entity)) render(entity, vehiclesLineColor, vehiclesSideColor);
        }
    }
}