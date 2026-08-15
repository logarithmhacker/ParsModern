package parsmodernpvp_knl2s7pw.client.ui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import parsmodernpvp_knl2s7pw.client.PvpClient;

/**
 * Background compatibility facade.
 *
 * Keeps the existing preset/type API while using the same restrained visual
 * treatment as UiBackground.  Older screens can continue calling this class.
 */
public final class BackgroundEngine {
    private static Preset preset = Preset.BALANCED;
    private static Type type = Type.NEBULA;
    private static float speed = 1.0F;
    private static float opacity = 0.82F;
    private static float parallax = 0.12F;
    private static boolean fpsSafe;

    private BackgroundEngine() {
    }

    public static void render(GuiGraphicsExtractor g, int width, int height, float intensity) {
        if (type == Type.MINIMAL) {
            g.fill(0, 0, width, height, PvpClient.themeEngine().color("background"));
            g.fill(0, 0, width, 1, withAlpha(PvpClient.themeEngine().accent(), 0.45F));
            return;
        }

        UiBackground.render(g, width, height, Math.max(0.0F, Math.min(1.0F, intensity * opacity)));

        if (!fpsSafe && PvpClient.particles() && type != Type.GRID) {
            drawParticles(g, width, height);
        }
    }

    private static void drawParticles(GuiGraphicsExtractor g, int width, int height) {
        int count = switch (preset) {
            case LOW -> 5;
            case BALANCED -> 9;
            case HIGH -> 14;
            case ULTRA -> 20;
        };

        float time = PvpClient.animatedGradient()
                ? (float) System.nanoTime() / 1.0E9F * speed
                : 0.0F;
        int accent = PvpClient.themeEngine().accent();
        int secondary = PvpClient.themeEngine().secondary();

        for (int i = 0; i < count; i++) {
            float seed = i * 17.371F;
            int x = Math.floorMod((int) (seed * 41.0F + time * (1 + i % 3)), Math.max(1, width));
            int y = Math.floorMod((int) (seed * 23.0F - time * (1 + i % 2)), Math.max(1, height));
            int size = i % 7 == 0 ? 2 : 1;
            int color = i % 2 == 0 ? accent : secondary;
            g.fill(x, y, x + size, y + size, withAlpha(color, 0.10F));
        }
    }

    public static void setPreset(Preset value) {
        if (value != null) preset = value;
    }

    public static void setType(Type value) {
        if (value != null) type = value;
    }

    public static void setSpeed(float value) {
        speed = Math.max(0.1F, Math.min(3.0F, value));
    }

    public static void setOpacity(float value) {
        opacity = Math.max(0.0F, Math.min(1.0F, value));
    }

    public static void setParallax(float value) {
        parallax = Math.max(0.0F, Math.min(0.5F, value));
    }

    public static void setFpsSafe(boolean value) {
        fpsSafe = value;
    }

    public static Preset preset() { return preset; }
    public static Type type() { return type; }

    public enum Preset {
        LOW, BALANCED, HIGH, ULTRA
    }

    public enum Type {
        GRADIENT, PARTICLES, GRID, STARS, NEBULA, MINIMAL
    }

    private static int withAlpha(int color, float alpha) {
        return (Math.max(0, Math.min(255, Math.round(alpha * 255.0F))) << 24) | (color & 0xFFFFFF);
    }
}
