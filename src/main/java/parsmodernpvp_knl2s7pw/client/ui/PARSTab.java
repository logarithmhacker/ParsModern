package parsmodernpvp_knl2s7pw.client.ui;

import net.minecraft.client.gui.GuiGraphicsExtractor;

/** Compact tab with a strong selected state. */
public final class PARSTab {
    private PARSTab() {
    }

    public static void draw(GuiGraphicsExtractor g, String text, int x, int y, int w, boolean active) {
        PARSFramework.button(g, text, x, y, UiScale.s(w), UiScale.s(30), false, active);
    }
}
