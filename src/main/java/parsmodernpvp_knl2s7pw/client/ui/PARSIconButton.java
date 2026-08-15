package parsmodernpvp_knl2s7pw.client.ui;

import net.minecraft.client.gui.GuiGraphicsExtractor;

/** Small icon action button. */
public final class PARSIconButton {
    private PARSIconButton() {
    }

    public static void draw(GuiGraphicsExtractor g, String icon, int x, int y, boolean hover) {
        int size = UiScale.s(30);
        PARSFramework.button(g, icon, x, y, size, size, hover, false);
    }
}
