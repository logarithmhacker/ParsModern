package parsmodernpvp_knl2s7pw.client.ui;

import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class PARSDropdown {
   private PARSDropdown() {
   }

   public static void draw(GuiGraphicsExtractor g, String value, int x, int y, int w) {
      PARSFramework.button(g, value, x, y, w, 22, false, false);
   }
}
