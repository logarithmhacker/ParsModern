package parsmodernpvp_knl2s7pw.client.ui;

import net.minecraft.client.gui.GuiGraphicsExtractor;

/** Clean compact dropdown field. */
public final class PARSDropdown {
    private PARSDropdown() {
    }

    public static void draw(GuiGraphicsExtractor g, String value, int x, int y, int w) {
        int width = UiScale.s(w);
        int height = UiScale.s(30);
        PARSFramework.button(g, value + "  >", x, y, width, height, false, false);
    }
}
