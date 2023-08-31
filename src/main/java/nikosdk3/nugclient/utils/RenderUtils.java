package nikosdk3.nugclient.utils;

import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import static nikosdk3.nugclient.utils.Utils.mc;

public class RenderUtils {
    private static final Tessellator lineTessellator = new Tessellator(1000);
    private static final BufferBuilder lineBufferBuilder = lineTessellator.getBuffer();

    private static final Tessellator quadTessellator = new Tessellator(1000);
    private static final BufferBuilder quadBufferBuilder = quadTessellator.getBuffer();

    public static void applyOffset(MatrixStack matrixStack) {
        Vec3d camPos = mc.gameRenderer.getCamera().getPos();
        matrixStack.translate(-camPos.x, -camPos.y, -camPos.z);
    }

    public static void beginLines() {
        lineBufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
    }

    public static void endLines() {
        lineTessellator.draw();
    }

    public static void line(Matrix4f matrix, double x1, double y1, double z1, double x2, double y2, double z2, Color color) {
        lineBufferBuilder.vertex(matrix, (float) x1, (float) y1, (float) z1).color(color.r, color.g, color.b, color.a).next();
        lineBufferBuilder.vertex(matrix, (float) x2, (float) y2, (float) z2).color(color.r, color.g, color.b, color.a).next();
    }

    public static void boxEdges(Matrix4f matrix, double x1, double y1, double z1, double x2, double y2, double z2, Color color) {
        line(matrix, x1, y1, z1, x2, y1, z1, color);
        line(matrix, x1, y1, z1, x1, y2, z1, color);
        line(matrix, x1, y1, z1, x1, y1, z2, color);

        line(matrix, x2, y2, z2, x1, y2, z2, color);
        line(matrix, x2, y2, z2, x2, y1, z2, color);
        line(matrix, x2, y2, z2, x2, y2, z1, color);

        line(matrix, x2, y1, z1, x2, y2, z1, color);
        line(matrix, x1, y1, z2, x1, y2, z2, color);

        line(matrix, x2, y1, z1, x2, y1, z2, color);
        line(matrix, x1, y1, z2, x2, y1, z2, color);

        line(matrix, x1, y2, z1, x2, y2, z1, color);
        line(matrix, x1, y2, z1, x1, y2, z2, color);
    }

    public static void blockEdges(Matrix4f matrix, int x, int y, int z, Color color) {
        boxEdges(matrix, x, y, z, x + 1, y + 1, z + 1, color);
    }

    public static void beginQuads() {
        quadBufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
    }

    public static void endQuads() {
        GL11.glDisable(GL11.GL_CULL_FACE);
        quadTessellator.draw();
    }

    public static void quad(Matrix4f matrix, double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4, Color color) {
        quadBufferBuilder.vertex(matrix, (float) x1, (float) y1, (float) z1).color(color.r, color.g, color.b, color.a).next();
        quadBufferBuilder.vertex(matrix, (float) x2, (float) y2, (float) z2).color(color.r, color.g, color.b, color.a).next();
        quadBufferBuilder.vertex(matrix, (float) x3, (float) y3, (float) z3).color(color.r, color.g, color.b, color.a).next();
        quadBufferBuilder.vertex(matrix, (float) x4, (float) y4, (float) z4).color(color.r, color.g, color.b, color.a).next();
    }

    public static void boxSides(Matrix4f matrix, double x1, double y1, double z1, double x2, double y2, double z2, Color color) {
        quad(matrix, x1, y1, z1, x1, y1, z2, x2, y1, z2, x2, y1, z1, color); // Bottom
        quad(matrix, x1, y2, z1, x1, y2, z2, x2, y2, z2, x2, y2, z1, color); // Top

        quad(matrix, x1, y1, z1, x1, y2, z1, x2, y2, z1, x2, y1, z1, color); // Front
        quad(matrix, x1, y1, z2, x1, y2, z2, x2, y2, z2, x2, y1, z2, color); // Back

        quad(matrix, x1, y1, z1, x1, y2, z1, x1, y2, z2, x1, y1, z2, color); // Left
        quad(matrix, x2, y1, z1, x2, y2, z1, x2, y2, z2, x2, y1, z2, color); // Right
    }

    public static void blockSides(Matrix4f matrix, int x, int y, int z, Color color) {
        boxSides(matrix, x, y, z, x + 1, y + 1, z + 1, color);
    }
}
