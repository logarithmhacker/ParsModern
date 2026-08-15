package parsmodernpvp_knl2s7pw.client.ui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import parsmodernpvp_knl2s7pw.client.PvpClient;

/** Clean slider with a single accent rail and precise thumb. */
public final class PARSSlider {
    private PARSSlider() {
    }

    public static void draw(GuiGraphicsExtractor g, int x, int y, int width, float value) {
        draw(g, x, y, width, DesignTokens.BUTTON_HEIGHT_MD, value, false);
    }

    public static void draw(GuiGraphicsExtractor g, int x, int y, int width,
                            int height, float value) {
        draw(g, x, y, width, height, value, false);
    }

    public static void draw(GuiGraphicsExtractor g, int x, int y, int width,
                            int height, float value, boolean interactive) {
        int w = UiScale.s(width);
        int h = Math.max(UiScale.s(6), UiScale.s(height) / 3);
        float clamped = Math.max(0.0F, Math.min(1.0F, value));
        int filled = Math.round(w * clamped);
        int radius = h / 2;
        int accent = PvpClient.themeEngine().accent();
        int track = PARSFramework.withAlpha(PvpClient.themeEngine().color("border"), 0.70F);

        fillRounded(g, x, y, w, h, radius, track);

        if (filled > 0) {
            if (interactive && PvpClient.glow()) {
                fillRounded(g, x - 1, y - 1, filled + 2, h + 2, radius + 1,
                        PARSFramework.withAlpha(accent, 0.14F));
            }
            fillRounded(g, x, y, filled, h, Math.min(radius, filled / 2), accent);
        }

        int thumb = Math.max(UiScale.s(10), h + UiScale.s(4));
        int thumbX = x + Math.round((w - thumb) * clamped);
        int thumbY = y + h / 2 - thumb / 2;
        fillRounded(g, thumbX + 1, thumbY + 2, thumb, thumb, thumb / 2, 0x35000000);
        fillRounded(g, thumbX, thumbY, thumb, thumb, thumb / 2, 0xFFFFFFFF);

        if (interactive) {
            g.fill(thumbX + thumb / 2 - 1, thumbY + thumb / 2 - 1,
                    thumbX + thumb / 2 + 1, thumbY + thumb / 2 + 1,
                    PARSFramework.withAlpha(accent, 0.75F));
        }
    }

    private static void fillRounded(GuiGraphicsExtractor g, int x, int y,
                                    int width, int height, int radius, int color) {
        if (width <= 0 || height <= 0) return;
        if (radius <= 0) {
            g.fill(x, y, x + width, y + height, color);
            return;
        }
        for (int row = 0; row < height; row++) {
            int fromEdge = Math.min(row, height - row - 1);
            int inset = fromEdge < radius
                    ? radius - (int) Math.floor(Math.sqrt(radius * radius -
                    (radius - fromEdge - 0.5) * (radius - fromEdge - 0.5)))
                    : 0;
            g.fill(x + inset, y + row, x + width - inset, y + row + 1, color);
        }
    }
}
