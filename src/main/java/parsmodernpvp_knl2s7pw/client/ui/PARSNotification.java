package parsmodernpvp_knl2s7pw.client.ui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import parsmodernpvp_knl2s7pw.client.PvpClient;

/** Compact inline notification card. */
public final class PARSNotification {
    private PARSNotification() {
    }

    public static void draw(GuiGraphicsExtractor g, String title, String body,
                            int x, int y, int w) {
        int width = UiScale.s(w);
        int height = UiScale.s(52);
        PARSFramework.card(g, x, y, width, height, false, true);
        int pad = UiScale.s(10);
        UiTypography.label(g, title, x + pad, y + UiScale.s(9),
                PvpClient.themeEngine().accent());
        UiTypography.text(g, body, x + pad, y + UiScale.s(27),
                PvpClient.themeEngine().mutedText(), 0.70F, 0);
    }
}
