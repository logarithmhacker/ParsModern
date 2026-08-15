package parsmodernpvp_knl2s7pw.client.ui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import parsmodernpvp_knl2s7pw.client.PvpClient;

/**
 * Quiet cinematic background for the PARS interface.
 *
 * The old background used a very visible full-screen grid and heavy blocks.
 * This version keeps the tech identity but pushes it behind the content.
 */
public final class UiBackground {
    private static long startTime = System.nanoTime();

    private UiBackground() {
    }

    public static void render(GuiGraphicsExtractor g, int width, int height) {
        render(g, width, height, PvpClient.backgroundOpacity());
    }

    public static void render(GuiGraphicsExtractor g, int width, int height, float opacity) {
        if (!PvpClient.initialized()) {
            return;
        }

        int background = PvpClient.themeEngine().color("background");
        int accent = PvpClient.themeEngine().accent() & 0xFFFFFF;
        int secondary = PvpClient.themeEngine().secondary() & 0xFFFFFF;
        float strength = Math.max(0.0F, Math.min(1.0F, opacity));

        g.fill(0, 0, width, height, background);

        // Very soft vertical tonal shift.
        int strips = Math.max(24, Math.min(64, height / 10));
        for (int i = 0; i < strips; i++) {
            float t = i / (float) Math.max(1, strips - 1);
            int color = mix(background, 0x0BFFFFFF, t);
            g.fill(0, i * height / strips, width, (i + 1) * height / strips + 1,
                    withAlpha(color, 0.32F * strength));
        }

        if (PvpClient.animatedGradient() && !PvpClient.lowPerformance()) {
            renderGlow(g, width, height, accent, secondary, strength);
        }

        // The grid is intentionally tiny and faint. It should be texture,
        // not the first thing the eye sees.
        if (PvpClient.grid() && !PvpClient.lowPerformance()) {
            renderGrid(g, width, height);
        }

        if (PvpClient.vignette()) {
            renderVignette(g, width, height);
        }

        // A clean one-pixel frame finishes the screen without visual noise.
        g.fill(0, 0, width, 1, withAlpha(0xFFFFFFFF, 0.05F));
        g.fill(0, height - 1, width, height, withAlpha(0x000000, 0.22F));
    }

    private static void renderGlow(GuiGraphicsExtractor g, int width, int height,
                                   int accent, int secondary, float strength) {
        long elapsed = System.nanoTime() - startTime;
        float speed = Math.max(0.1F, PvpClient.animationSpeed());
        float time = elapsed / 1.0E9F * speed;

        int centerX = (int) (width * (0.50F + 0.16F * (float) Math.sin(time * 0.11F)));
        int centerY = (int) (height * (0.34F + 0.10F * (float) Math.cos(time * 0.09F)));

        // Rectangle-based bloom: several soft, transparent layers.
        int[] sizes = {260, 190, 125, 70};
        float[] alpha = {0.018F, 0.024F, 0.032F, 0.045F};
        for (int i = 0; i < sizes.length; i++) {
            int size = sizes[i];
            int color = i % 2 == 0 ? accent : secondary;
            g.fill(centerX - size, centerY - size / 2,
                    centerX + size, centerY + size / 2,
                    withAlpha(color, alpha[i] * strength));
        }
    }

    private static void renderGrid(GuiGraphicsExtractor g, int width, int height) {
        int size = 64;
        int line = withAlpha(PvpClient.themeEngine().accent(), 0.045F);

        for (int x = size; x < width; x += size) {
            g.fill(x, 0, x + 1, height, line);
        }
        for (int y = size; y < height; y += size) {
            g.fill(0, y, width, y + 1, line);
        }
    }

    private static void renderVignette(GuiGraphicsExtractor g, int width, int height) {
        int edgeX = Math.max(18, width / 12);
        int edgeY = Math.max(18, height / 12);
        int color = 0x22000000;

        g.fill(0, 0, width, edgeY, color);
        g.fill(0, height - edgeY, width, height, color);
        g.fill(0, 0, edgeX, height, color);
        g.fill(width - edgeX, 0, width, height, color);
    }

    private static int mix(int first, int second, float t) {
        t = Math.max(0.0F, Math.min(1.0F, t));
        int a = Math.round(((first >>> 24) & 0xFF) * (1.0F - t) + ((second >>> 24) & 0xFF) * t);
        int r = Math.round(((first >>> 16) & 0xFF) * (1.0F - t) + ((second >>> 16) & 0xFF) * t);
        int g = Math.round(((first >>> 8) & 0xFF) * (1.0F - t) + ((second >>> 8) & 0xFF) * t);
        int b = Math.round((first & 0xFF) * (1.0F - t) + (second & 0xFF) * t);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    private static int withAlpha(int color, float alpha) {
        return ((Math.max(0, Math.min(255, Math.round(alpha * 255.0F)))) << 24) | (color & 0xFFFFFF);
    }
}
