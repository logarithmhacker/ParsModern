package parsmodernpvp_knl2s7pw.client.ui;

import net.minecraft.client.gui.GuiGraphicsExtractor;

/** Compact panel facade. */
public final class PARSPanel {
    private PARSPanel() {
    }

    public static void draw(GuiGraphicsExtractor g, int x, int y, int w, int h) {
        PARSFramework.panel(g, x, y, UiScale.s(w), UiScale.s(h));
    }
}
