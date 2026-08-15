package parsmodernpvp_knl2s7pw.client.ui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import parsmodernpvp_knl2s7pw.client.PvpClient;

/** Minimal tooltip text; the screen owns the placement. */
public final class PARSTooltip {
    private PARSTooltip() {
    }

    public static void draw(GuiGraphicsExtractor g, String text, int x, int y) {
        UiTypography.text(g, text, x, y, PvpClient.themeEngine().text(), 0.72F, 0);
    }
}
