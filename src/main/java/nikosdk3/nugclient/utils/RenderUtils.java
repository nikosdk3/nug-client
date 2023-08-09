package nikosdk3.nugclient.utils;

import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

import static nikosdk3.nugclient.utils.Utils.mc;

public class RenderUtils {
    public static Vec3d center;

    private final static Tessellator lineTessellator = new Tessellator();
    private final static BufferBuilder lineBufferBuilder = lineTessellator.getBuffer();

    private final static Tessellator quadTessellator = new Tessellator();
    private final static BufferBuilder quadBufferBuilder = quadTessellator.getBuffer();

    public static void beginLines() {
        lineBufferBuilder.begin(VertexFormat.DrawMode.LINES, VertexFormats.POSITION_COLOR);
    }

    public static void endLines() {
        lineTessellator.draw();
    }

    public static void line(double x1, double y1, double z1, double x2, double y2, double z2, Color color) {
        lineBufferBuilder.vertex(x1, y1, z1).color(color.r, color.g, color.b, color.a).next();
        lineBufferBuilder.vertex(x2, y2, z2).color(color.r, color.g, color.b, color.a).next();
    }

    public static void blockEdges(int x, int y, int z, Color color) {
        int x2 = x + 1;
        int y2 = y + 1;
        int z2 = z + 1;

        line(x, y, z, x2, y, z, color);
        line(x, y, z, x, y2, z, color);
        line(x, y, z, x, y, z2, color);

        line(x2, y2, z2, x, y2, z2, color);
        line(x2, y2, z2, x2, y, z2, color);
        line(x2, y2, z2, x2, y2, z, color);

        line(x2, y, z, x2, y2, z, color);
        line(x, y, z2, x, y2, z2, color);

        line(x2, y, z, x2, y, z2, color);
        line(x, y, z2, x2, y, z2, color);

        line(x, y2, z, x2, y2, z, color);
        line(x, y2, z, x, y2, z2, color);
    }

    public static void beginQuads() {
        quadBufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
    }

    public static void endQuads() {
        quadTessellator.draw();
    }

    public static void quad(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4, Color color) {
        quadBufferBuilder.vertex(x1, y1, z1).color(color.r, color.g, color.b, color.a).next();
        quadBufferBuilder.vertex(x2, y2, z2).color(color.r, color.g, color.b, color.a).next();
        quadBufferBuilder.vertex(x3, y3, z3).color(color.r, color.g, color.b, color.a).next();
        quadBufferBuilder.vertex(x4, y4, z4).color(color.r, color.g, color.b, color.a).next();
    }

    public static void blockSides(int x, int y, int z, Color color) {
        quad(x, y, z, x + 1, y, z, x + 1, y, z + 1, x, y, z + 1, color); //Bottom
        quad(x, y + 1, z, x + 1, y + 1, z, x + 1, y + 1, z + 1, x, y + 1, z + 1, color); //Top

        quad(x, y, z, x + 1, y, z, x + 1, y + 1, z, x, y + 1, z, color); //Front
        quad(x, y, z + 1, x + 1, y, z + 1, x + 1, y + 1, z + 1, x, y + 1, z + 1, color); //Back

        quad(x, y, z, x, y, z + 1, x, y + 1, z + 1, x, y + 1, z, color);//Left
        quad(x + 1, y, z, x + 1, y, z + 1, x + 1, y + 1, z + 1, x + 1, y + 1, z, color);//Right
    }

    public static void updateScreenCenter() {
        Vector3f pos = new Vector3f(0, 0, 1);
        center = new Vec3d(pos.x, -pos.y, pos.z)
                .rotateX(-(float) Math.toRadians(mc.gameRenderer.getCamera().getPitch()))
                .rotateY(-(float) Math.toRadians(mc.gameRenderer.getCamera().getYaw()))
                .add(mc.gameRenderer.getCamera().getPos());
    }
}
