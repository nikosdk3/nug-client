package nikosdk3.nugclient.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import nikosdk3.nugclient.mixininterface.IMinecraftClient;
import nikosdk3.nugclient.modules.Module;
import nikosdk3.nugclient.modules.ModuleManager;

import java.util.Random;

public class Utils {
    private static final Random random = new Random();
    public static MinecraftClient mc;

    public static boolean canUpdate() {
        return mc.world != null && mc.player != null;
    }

    public static int random(int min, int max) {
        return random.nextInt(max - min) + min;
    }

    public static int getTextWidth(String text) {
        return mc.textRenderer.getWidth(text);
    }

    public static int getTextHeight() {
        return mc.textRenderer.fontHeight;
    }

    public static void drawText(DrawContext context, String text, int x, int y, int color, boolean shadow) {
        context.drawText(mc.textRenderer, text, x, y, color, shadow);
    }

    public static void sendMessage(String msg, Object... args) {
        msg = String.format(msg, args);
        msg = msg.replaceAll("#yellow", Formatting.YELLOW.toString());
        msg = msg.replaceAll("#white", Formatting.WHITE.toString());
        msg = msg.replaceAll("#red", Formatting.RED.toString());
        msg = msg.replaceAll("#blue", Formatting.BLUE.toString());
        msg = msg.replaceAll("#pink", Formatting.LIGHT_PURPLE.toString());
        msg = msg.replaceAll("#gray", Formatting.GRAY.toString());

        mc.player.sendMessage(Text.literal(msg));
    }

    public static void leftClick() {
        ((IMinecraftClient) mc).leftClick();
    }

    public static void rightClick() {
        ((IMinecraftClient) mc).rightClick();
    }

    public static Module tryToGetModule(String[] args) {
        if (args.length < 1) {
            Utils.sendMessage("#redYou must specify module name.");
            return null;
        }
        Module module = ModuleManager.get(args[0]);
        if (module == null) {
            Utils.sendMessage("#redModule with name #blue'%s' #reddoesn't exist.", args[0]);
            return null;
        }
        return module;
    }

}
