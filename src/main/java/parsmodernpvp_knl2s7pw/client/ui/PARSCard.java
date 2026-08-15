package parsmodernpvp_knl2s7pw.client.ui;

import net.minecraft.client.gui.GuiGraphicsExtractor;

/** Compact card facade. */
public final class PARSCard {
    private PARSCard() {
    }

    public static void draw(GuiGraphicsExtractor g, int x, int y, int w, int h, boolean selected) {
        PARSFramework.card(g, x, y, UiScale.s(w), UiScale.s(h), selected);
    }
}
