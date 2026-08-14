package parsmodernpvp_knl2s7pw.client.ui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import parsmodernpvp_knl2s7pw.client.PvpClient;

public final class PARSTextField {
   private PARSTextField() {
   }

   public static void draw(GuiGraphicsExtractor g, String value, int x, int y, int w) {
      PARSFramework.card(g, x, y, w, 24, true);
      UiTypography.text(g, value, x + 9, y + 8, PARSTextField.PvpmodernColor.text(), 0.72F, 0);
   }

   private static final class PvpmodernColor {
      static int text() {
         return PvpClient.themeEngine().text();
      }
   }
}
