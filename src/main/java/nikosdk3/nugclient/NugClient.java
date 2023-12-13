package nikosdk3.nugclient;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import nikosdk3.nugclient.clickgui.ClickGUI;
import nikosdk3.nugclient.commands.CommandManager;
import nikosdk3.nugclient.events.event.TickEvent;
import nikosdk3.nugclient.json.ConfigSerializerDeserializer;
import nikosdk3.nugclient.modules.ModuleManager;
import nikosdk3.nugclient.utils.EntityUtils;
import nikosdk3.nugclient.utils.Utils;
import org.lwjgl.glfw.GLFW;

import java.io.*;

public class NugClient implements ClientModInitializer {
    public static final String MOD_ID = "nug-client";

    public static NugClient instance;
    public static EventBus eventBus = new EventBus();
    public static Gson gson;

    private static final KeyBinding openClickGui = new KeyBinding("key.nug-client.open-click-gui", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, "Nug Client");
    private static File configFile;
    private static MinecraftClient mc;

    boolean init;

    @Override
    public void onInitializeClient() {
        if (!init) {
            init = true;
            KeyBindingHelper.registerKeyBinding(openClickGui);
            instance = this;
            return;
        }

        System.out.println("Initializing Nug Client.");

        mc = MinecraftClient.getInstance();
        Utils.mc = mc;
        EntityUtils.mc = mc;

        gson = new GsonBuilder().registerTypeAdapter(Config.class, new ConfigSerializerDeserializer()).setPrettyPrinting().create();

        configFile = new File(FabricLoader.getInstance().getGameDir().toFile(), "nug-client.json");

        CommandManager.init();
        ModuleManager.init();

        eventBus.register(this);
    }

    @Subscribe
    private void onTick(TickEvent event) {
        if (openClickGui.isPressed() && !mc.isPaused()) mc.setScreen(new ClickGUI());
    }

    public static void saveConfig() {
        try {
            Writer writer = new FileWriter(configFile);
            gson.toJson(Config.instance, writer);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loadConfig() {
        if (configFile.exists()) {
            try {
                FileReader reader = new FileReader(configFile);
                Config.instance = gson.fromJson(reader, Config.class);
                reader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}