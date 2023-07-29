package nikosdk3.nugclient.utils;

public class Color {
    public int r, g, b, a;

    public Color() {
        this(255, 255, 255, 255);
    }

    public Color(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
        a = 255;
    }

    public Color(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public Color(Color color) {
        r = color.r;
        g = color.g;
        b = color.b;
        a = color.a;
    }

    public void set(Color value) {
        r = value.r;
        g = value.g;
        b = value.b;
        a = value.a;
    }

    public void validate() {
        if (r < 0) r = 0;
        else if (r > 255) r = 255;

        if (g < 0) g = 0;
        else if (g > 255) g = 255;

        if (b < 0) b = 0;
        else if (b > 255) b = 255;

        if (a < 0) a = 0;
        else if (a > 255) a = 255;
    }

    public int getPacked() {
        return fromRGBA(r, g, b, a);
    }

    @Override
    public String toString() {
        return r + " " + b + " " + g + " " + a;
    }

    public static int fromRGBA(int r, int g, int b, int a) {
        return (r << 16) + (g << 8) + b + (a << 24);
    }

    public static int toRGBAR(int color) {
        return (color >> 16) & 0xFF;
    }

    public static int toRGBAG(int color) {
        return (color >> 8) & 0xFF;
    }

    public static int toRGBAB(int color) {
        return color & 0xFF;
    }

    public static int toRGBAA(int color) {
        return (color >> 24) & 0xFF;
    }
}
