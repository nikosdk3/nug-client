package nikosdk3.nugclient.modules.combat;

import com.google.common.eventbus.Subscribe;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import nikosdk3.nugclient.events.event.TickEvent;
import nikosdk3.nugclient.modules.Category;
import nikosdk3.nugclient.modules.Module;
import nikosdk3.nugclient.settings.BoolSetting;
import nikosdk3.nugclient.settings.DoubleSetting;
import nikosdk3.nugclient.settings.EnumSetting;
import nikosdk3.nugclient.settings.Setting;
import nikosdk3.nugclient.utils.EntityUtils;

import java.util.stream.StreamSupport;

public class KillAura extends Module {
    public enum Priority {
        LowestDistance,
        HighestDistance,
        LowestHealth,
        HighestHealth
    }

    public Setting<Boolean> players = addSetting(new BoolSetting.Builder()
            .name("players")
            .description("Attack players.")
            .defaultValue(true)
            .build()
    );

    public Setting<Boolean> animals = addSetting(new BoolSetting.Builder()
            .name("animals")
            .description("Attack animals.")
            .defaultValue(true)
            .build()
    );

    public Setting<Boolean> mobs = addSetting(new BoolSetting.Builder()
            .name("mobs")
            .description("Attack mobs.")
            .defaultValue(true)
            .build()
    );

    public Setting<Double> range = addSetting(new DoubleSetting.Builder()
            .name("range")
            .description("Attack range.")
            .defaultValue(5.5)
            .min(0.0)
            .build()
    );

    public Setting<Boolean> ignoreWalls = addSetting(new BoolSetting.Builder()
            .name("ignore-walls")
            .description("Attack through walls.")
            .defaultValue(true)
            .build()
    );

    public Setting<Priority> priority = addSetting(new EnumSetting.Builder<Priority>()
            .name("priority")
            .description("What entities to target.")
            .defaultValue(Priority.LowestHealth)
            .build()
    );

    public KillAura() {
        super(Category.Combat, "kill-aura", "Automatically attacks entities.");
    }

    private boolean isInRange(Entity entity) {
        return entity.distanceTo(mc.player) <= range.get();
    }

    private boolean canAttackEntity(Entity entity) {
        if (entity.getUuid().equals(mc.player.getUuid())) return false;
        if (EntityUtils.isPlayer(entity) && players.get()) return true;
        if (EntityUtils.isAnimal(entity) && animals.get()) return true;
        return EntityUtils.isMob(entity) && mobs.get();
    }

    private boolean canSeeEntity(Entity entity) {
        return ignoreWalls.get() || mc.player.canSee(entity);
    }

    private int invertSort(int sort) {
        if (sort == 0) return 0;
        return sort > 0 ? -1 : 1;
    }

    private int sort(LivingEntity e1, LivingEntity e2) {
        switch (priority.get()) {
            case LowestDistance:  return Double.compare(e1.distanceTo(mc.player), e2.distanceTo(mc.player));
            case HighestDistance: return invertSort(Double.compare(e1.distanceTo(mc.player), e2.distanceTo(mc.player)));
            case LowestHealth:    return Float.compare(e1.getHealth(), e2.getHealth());
            case HighestHealth:   return invertSort(Float.compare(e1.getHealth(), e2.getHealth()));
            default:              return 0;
        }
    }

    @Subscribe
    private void onTick(TickEvent event) {
        StreamSupport.stream(mc.world.getEntities().spliterator(), false)
                .filter(entity -> entity instanceof LivingEntity)
                .map(entity -> (LivingEntity) entity)
                .filter(this::isInRange)
                .filter(this::canAttackEntity)
                .filter(this::canSeeEntity)
                .filter(livingEntity -> !livingEntity.isDead())
                .min(this::sort)
                .ifPresent(entity -> {
                    mc.interactionManager.attackEntity(mc.player, entity);
                    mc.player.swingHand(Hand.MAIN_HAND);
                });
    }
}
