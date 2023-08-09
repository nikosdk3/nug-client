package nikosdk3.nugclient.modules.render;

import net.minecraft.entity.LivingEntity;
import nikosdk3.nugclient.modules.Category;
import nikosdk3.nugclient.modules.Module;
import nikosdk3.nugclient.settings.BoolSetting;
import nikosdk3.nugclient.settings.Setting;
import nikosdk3.nugclient.utils.EntityUtils;

public class Chams extends Module {
    private final Setting<Boolean> players = addSetting(new BoolSetting.Builder()
            .name("players")
            .description("Renders players.")
            .defaultValue(true)
            .build()
    );

    private final Setting<Boolean> mobs = addSetting(new BoolSetting.Builder()
            .name("mobs")
            .description("Render mobs.")
            .defaultValue(true)
            .build()
    );

    private final Setting<Boolean> animals = addSetting(new BoolSetting.Builder()
            .name("animals")
            .description("Render animals.")
            .defaultValue(true)
            .build()
    );

    public Chams() {
        super(Category.Render, "chams", "Renders entities through blocks.");
    }

    public boolean shouldRender(LivingEntity entity) {
        if (!isActive()) return false;
        if (EntityUtils.isPlayer(entity)) return players.get();
        if (EntityUtils.isAnimal(entity)) return animals.get();
        if (EntityUtils.isMob(entity)) return mobs.get();
        return false;
    }
}
