package parsmodernpvp_knl2s7pw.client.ui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import parsmodernpvp_knl2s7pw.client.PvpClient;

/** Minimal pill toggle with a crisp thumb and subtle accent glow. */
public final class PARSToggle {
    private PARSToggle() {
    }

    public static void draw(GuiGraphicsExtractor g, int x, int y, boolean enabled) {
        draw(g, x, y, enabled, true);
    }

    public static void draw(GuiGraphicsExtractor g, int x, int y, boolean enabled, boolean animated) {
        int width = UiScale.s(38);
        int height = UiScale.s(20);
        int radius = height / 2;

        int accent = PvpClient.themeEngine().accent();
        int track = enabled
                ? PARSFramework.withAlpha(accent, 0.84F)
                : PARSFramework.withAlpha(PvpClient.themeEngine().color("border"), 0.72F);

        if (PvpClient.shadow()) {
            fillRounded(g, x + UiScale.s(1), y + UiScale.s(2), width, height, radius, 0x30000000);
        }

        fillRounded(g, x, y, width, height, radius, track);
        fillRounded(g, x + UiScale.s(1), y + UiScale.s(1),
                Math.max(1, width - UiScale.s(2)), Math.max(1, height - UiScale.s(2)),
                Math.max(1, radius - UiScale.s(1)),
                enabled ? PARSFramework.withAlpha(accent, 0.34F) : 0x20000000);

        int thumb = UiScale.s(16);
        int thumbX = enabled ? x + width - thumb - UiScale.s(2) : x + UiScale.s(2);
        int thumbY = y + (height - thumb) / 2;

        if (enabled && PvpClient.glow()) {
            fillRounded(g, thumbX - UiScale.s(2), thumbY - UiScale.s(2),
                    thumb + UiScale.s(4), thumb + UiScale.s(4), thumb / 2,
                    PARSFramework.withAlpha(accent, 0.16F));
        }

        fillRounded(g, thumbX + UiScale.s(1), thumbY + UiScale.s(2), thumb, thumb,
                thumb / 2, 0x35000000);
        fillRounded(g, thumbX, thumbY, thumb, thumb, thumb / 2,
                enabled ? 0xFFFFFFFF : 0xFFD2D7E0);
    }

    private static void fillRounded(GuiGraphicsExtractor g, int x, int y,
                                    int width, int height, int radius, int color) {
        if (width <= 0 || height <= 0) {
            return;
        }
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
