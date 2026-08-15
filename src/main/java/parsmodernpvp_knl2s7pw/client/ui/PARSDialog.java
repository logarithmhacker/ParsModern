package parsmodernpvp_knl2s7pw.client.ui;

import net.minecraft.client.gui.GuiGraphicsExtractor;

/** Dialog surface using the same visual language as panels. */
public final class PARSDialog {
    private PARSDialog() {
    }

    public static void draw(GuiGraphicsExtractor g, int x, int y, int w, int h) {
        PARSFramework.panel(g, x, y, UiScale.s(w), UiScale.s(h), DesignTokens.RADIUS_XL);
    }
}
