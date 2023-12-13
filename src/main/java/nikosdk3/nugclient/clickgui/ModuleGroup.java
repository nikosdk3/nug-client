package nikosdk3.nugclient.clickgui;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.LightmapTextureManager;
import nikosdk3.nugclient.modules.Category;
import nikosdk3.nugclient.modules.Module;
import nikosdk3.nugclient.modules.ModuleManager;
import nikosdk3.nugclient.utils.Color;
import nikosdk3.nugclient.utils.RenderUtils;
import nikosdk3.nugclient.utils.Utils;

import java.util.List;

public class ModuleGroup {
    public Category category;
    public double x, y;
    public boolean highlighted;

    private double width, height, titleOffset;
    private String title;
    private List<Module> modules;

    private Color outlineColor = new Color(40, 40, 40, 255);
    private Color outlineHighlightedColor = new Color(125, 125, 125, 255);
    private Color backgroundColor = new Color(120, 120, 120, 255);
    private Color backgroundHighlightedColor = new Color(145, 145, 145, 255);
    private Color backgroundActiveColor = new Color(175, 175, 175, 255);
    private int titleColor = Color.fromRGBA(255, 255, 255, 255);

    public ModuleGroup(Category category, double x, double y, double width, double height, double titleOffset) {
        this.category = category;
        this.title = category.toString();
        this.modules = ModuleManager.getGroup(category);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.titleOffset = titleOffset;
    }

    public boolean isMouseOver(double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    private boolean isMouseOverModule(double mouseX, double mouseY, int i) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y + i * height && mouseY <= y + i * height + height;
    }

    public void mouseDragged(double deltaX, double deltaY) {
        x += deltaX;
        y += deltaY;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (int i = 0; i < modules.size(); i++) {
            if (isMouseOverModule(mouseX, mouseY, i + 1) && button == 0) {
                modules.get(i).toggle();
                return true;
            }
        }
        return false;
    }

    public void render(int mouseX, int mouseY) {
        // Determine if highlighted
        Color outlineColor = this.outlineColor;
        Color backgroundColor = this.backgroundColor;
        if (highlighted) {
            outlineColor = outlineHighlightedColor;
            backgroundColor = backgroundHighlightedColor;
        }

        quad(x, y, x + width, y + height, backgroundColor); // Background

        quad(x, y, x + width, y + 2, outlineColor); // Top
        quad(x, y + height, x + width, y + height - 2, outlineColor); // Bottom
        quad(x, y, x + 2, y + height, outlineColor); // Left
        quad(x + width, y, x + width - 2, y + height, outlineColor); // Right

        // Modules
        for (int i = 0; i < modules.size(); i++) renderModule(modules.get(i), i + 1, mouseX, mouseY);
    }

    private void renderModule(Module module, int i, double mouseX, double mouseY) {
        double y = this.y + i * height;

        Color backgroundColor = this.backgroundColor;
        if (module.isActive() || isMouseOverModule(mouseX, mouseY, i)) backgroundColor = backgroundActiveColor;

        quad(x, y, x + width, y + height, backgroundColor); // Background

        quad(x, y, x + width, y + 1, outlineColor); // Top
        quad(x, y + height, x + width, y + height - 1, outlineColor); // Bottom
        quad(x, y, x + 1, y + height, outlineColor); // Left
        quad(x + width, y, x + width - 1, y + height, outlineColor); // Right
    }

    public void renderText(DrawContext context, TextRenderer font) {
        double xOffset = (width - Utils.getTextWidth(title)) / 2;
        font.draw(title, (float) (x + xOffset + 1), (float) (y + titleOffset + 1), titleColor, false, context.getMatrices().peek().getPositionMatrix(), context.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL, 0, LightmapTextureManager.MAX_LIGHT_COORDINATE); // Title

        // Modules
        for (int i = 0; i < modules.size(); i++) renderModuleText(context, font, modules.get(i), i + 1);
    }

    private void renderModuleText(DrawContext context, TextRenderer font, Module module, int i) {
        double y = this.y + i * height;

        font.draw(module.title, (int) (x + 4 + 1), (int) (y + titleOffset + 1), titleColor, false, context.getMatrices().peek().getPositionMatrix(), context.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL, 0, LightmapTextureManager.MAX_LIGHT_COORDINATE); // Title
    }

    private void quad(double x1, double y1, double x2, double y2, Color color) {
        RenderUtils.quad(x1, y1, 0, x2, y1, 0, x2, y2, 0, x1, y2, 0, color);
    }
}
