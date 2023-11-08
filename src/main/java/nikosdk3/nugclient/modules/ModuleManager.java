package nikosdk3.nugclient.modules;

import com.google.common.eventbus.Subscribe;
import nikosdk3.nugclient.NugClient;
import nikosdk3.nugclient.events.EventStore;
import nikosdk3.nugclient.events.event.KeyEvent;
import nikosdk3.nugclient.modules.combat.Criticals;
import nikosdk3.nugclient.modules.misc.AutoSign;
import nikosdk3.nugclient.modules.misc.LongerChat;
import nikosdk3.nugclient.modules.movement.*;
import nikosdk3.nugclient.modules.player.AutoFish;
import nikosdk3.nugclient.modules.player.DeathPos;
import nikosdk3.nugclient.modules.player.FastUse;
import nikosdk3.nugclient.modules.render.*;
import nikosdk3.nugclient.utils.Utils;
import org.lwjgl.glfw.GLFW;

import java.util.*;

public class ModuleManager {
    private static final List<Module> modules = new ArrayList<>();
    private static final Map<Category, List<Module>> groups = new HashMap<>();
    private static final List<Module> active = new ArrayList<>();
    public static Module moduleToBind;

    public static void init() {
        initCombat();
        initPlayer();
        initMovement();
        initRender();
        initMisc();
    }

    public static List<Module> getGroup(Category category) {
        return groups.computeIfAbsent(category, k -> new ArrayList<>());
    }

    public static Set<Category> getCategories() {
        return groups.keySet();
    }

    public static <T extends Module> T get(Class<T> klass) {
        for (Module module : modules) {
            if(module.getClass() == klass) return (T) module;
        }
        return null;
    }

    public static Module get(String name) {
        name = name.toLowerCase();
        for (Module module : modules) {
            if (module.name.equals(name)) return module;
        }
        return null;
    }

    public static List<Module> getActive() {
        return active;
    }

    public static List<Module> getAll() {
        return modules;
    }

    public static void deactivateAll() {
        List<Module> toDeactivate = new ArrayList<>(active);
        for (Module module : toDeactivate) {
            module.toggle();
        }
    }

    @Subscribe
    private static void onKey(KeyEvent event) {
        if (!event.push) return;

        //Check if binding a module
        if (moduleToBind != null) {
            moduleToBind.setKey(event.key);
            Utils.sendMessage("#yellowModule #blue'%s' #yellowbound to #blue%s#yellow.", moduleToBind.title, GLFW.glfwGetKeyName(event.key, 0).toUpperCase());
            moduleToBind = null;
            event.cancel();
            return;
        }

        //Find module bound to key
        for (Module module : modules) {
            if (module.getKey() == event.key) {
                module.toggle();
                event.cancel();
            }
        }
    }

    static void addActive(Module module) {
        active.add(module);
        NugClient.eventBus.post(EventStore.activeModulesChangedEvent());
    }

    static void removeActive(Module module) {
        active.remove(module);
        NugClient.eventBus.post(EventStore.activeModulesChangedEvent());
    }

    private static void addModule(Module module) {
        modules.add(module);
        getGroup(module.category).add(module);
    }

    private static void initCombat() {
        addModule(new Criticals());
    }

    private static void initPlayer() {
        addModule(new AutoFish());
        addModule(new DeathPos());
        addModule(new FastUse());
    }

    private static void initMovement() {
        addModule(new AutoJump());
        addModule(new AutoWalk());
        addModule(new Blink());
        addModule(new FastLadder());
        addModule(new Flight());
        addModule(new NoFall());
        addModule(new Spider());
    }

    private static void initRender() {
        addModule(new ActiveModules());
        addModule(new AntiFog());
        addModule(new Chams());
        addModule(new ESP());
        addModule(new Freecam());
        addModule(new FullBright());
        addModule(new Info());
        addModule(new NoHurtCam());
        addModule(new Position());
        addModule(new StorageESP());
        addModule(new Tracers());
        addModule(new XRay());
    }

    private static void initMisc() {
        addModule(new AutoSign());
        addModule(new LongerChat());
    }
}