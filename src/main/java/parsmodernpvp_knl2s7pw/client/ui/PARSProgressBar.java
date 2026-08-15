package parsmodernpvp_knl2s7pw.client.ui;

import net.minecraft.client.gui.GuiGraphicsExtractor;

/** Compact progress indicator. */
public final class PARSProgressBar {
    private PARSProgressBar() {
    }

    public static void draw(GuiGraphicsExtractor g, int x, int y, int w, float value) {
        PARSFramework.progress(g, x, y, UiScale.s(w), UiScale.s(6), value);
    }
}
