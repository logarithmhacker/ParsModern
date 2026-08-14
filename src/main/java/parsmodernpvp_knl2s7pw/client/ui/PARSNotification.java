package parsmodernpvp_knl2s7pw.client.ui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import parsmodernpvp_knl2s7pw.client.PvpClient;

public final class PARSNotification {
   private PARSNotification() {
   }

   public static void draw(GuiGraphicsExtractor g, String title, String body, int x, int y, int w) {
      PARSFramework.card(g, x, y, w, 44, true);
      UiTypography.label(g, title, x + 10, y + 7, PvpClient.themeEngine().accent());
      UiTypography.text(g, body, x + 10, y + 23, PvpClient.themeEngine().mutedText(), 0.65F, 0);
   }
}
