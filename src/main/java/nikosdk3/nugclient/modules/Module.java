package nikosdk3.nugclient.modules;

import net.minecraft.client.MinecraftClient;
import nikosdk3.nugclient.NugClient;
import nikosdk3.nugclient.events.EventStore;
import nikosdk3.nugclient.settings.Setting;
import nikosdk3.nugclient.utils.Color;
import nikosdk3.nugclient.utils.Utils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Module {
    protected static MinecraftClient mc;

    public final Category category;
    public final String name;
    public final String title;
    public final String description;
    public final int color;
    public final List<Setting> settings = new ArrayList<>();
    public final boolean setting;
    public final boolean serialize;
    private int key = -1;

    private boolean active;
    private boolean visible;

    public Module(Category category, String name, String description, boolean setting, boolean visible, boolean serialize) {
        this.category = category;
        this.name = name.toLowerCase();
        title = Arrays.stream(name.split("-")).map(StringUtils::capitalize).collect(Collectors.joining(" "));
        this.description = description;
        this.setting = setting;
        this.visible = visible;
        this.serialize = serialize;
        color = Color.fromRGBA(Utils.random(180, 255), Utils.random(180, 255), Utils.random(180, 255), 255);
        mc = MinecraftClient.getInstance();
    }

    public Module(Category category, String name, String description, boolean setting, boolean visible) {
        this(category, name, description, setting, visible, true);
    }

    public Module(Category category, String name, String description, boolean setting) {
        this(category, name, description, setting, true);
    }

    public Module(Category category, String name, String description) {
        this(category, name, description, false);
    }

    public void onActivate() {
    }

    public void onDeactivate() {
    }

    public void toggle() {
       if(setting)
           return;

        if (!active) {
            active = true;
            ModuleManager.addActive(this);
            NugClient.eventBus.register(this);
            Utils.sendMessage("#blue%s #yellowenabled.", this.title);
            onActivate();
        } else {
            active = false;
            ModuleManager.removeActive(this);
            NugClient.eventBus.unregister(this);
            Utils.sendMessage("#blue%s #yellowdisabled.", this.title);
            onDeactivate();
        }
    }

    public Setting getSetting(String name) {
        for (Setting setting : settings) {
            if (name.equalsIgnoreCase(setting.name))
                return setting;
        }
        return null;
    }

    public <T> Setting<T> addSetting(Setting<T> setting) {
        settings.add(setting);
        return setting;
    }

    public boolean isActive() {
        return active;
    }

    public String getInfoString() {
        return null;
    }

    public void setKey(int key) {
        this.key = key;
        NugClient.eventBus.post(EventStore.moduleBindChangedEvent());
    }

    public int getKey() {
        return key;
    }

    public void setVisible() {
        this.visible = visible;
        NugClient.eventBus.post(EventStore.moduleVisibilityChangedEvent());
    }

    public boolean isVisible() {
        return visible;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Module && name.equals(((Module) obj).name);
    }
}
