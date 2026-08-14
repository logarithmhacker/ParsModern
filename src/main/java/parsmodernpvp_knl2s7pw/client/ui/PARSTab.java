package parsmodernpvp_knl2s7pw.client.ui;

import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class PARSTab {
   private PARSTab() {
   }

   public static void draw(GuiGraphicsExtractor g, String text, int x, int y, int w, boolean active) {
      PARSFramework.button(g, text, x, y, w, 24, false, active);
   }
}
