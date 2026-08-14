package parsmodernpvp_knl2s7pw.client.ui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import parsmodernpvp_knl2s7pw.client.PvpClient;

public final class PARSTooltip {
   private PARSTooltip() {
   }

   public static void draw(GuiGraphicsExtractor g, String text, int x, int y) {
      UiTypography.text(g, text, x, y, PvpClient.themeEngine().text(), 0.65F, 0);
   }
}
