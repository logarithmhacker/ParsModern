package parsmodernpvp_knl2s7pw.client.ui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import parsmodernpvp_knl2s7pw.client.PvpClient;

/** Compact text field surface. */
public final class PARSTextField {
    private PARSTextField() {
    }

    public static void draw(GuiGraphicsExtractor g, String value, int x, int y, int w) {
        int width = UiScale.s(w);
        int height = UiScale.s(30);
        PARSFramework.card(g, x, y, width, height, false);
        UiTypography.text(
                g,
                value,
                x + UiScale.s(10),
                y + UiScale.s(9),
                PvpClient.themeEngine().text(),
                0.80F,
                0
        );
    }
}
