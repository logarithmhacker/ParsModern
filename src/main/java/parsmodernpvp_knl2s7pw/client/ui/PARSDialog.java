package parsmodernpvp_knl2s7pw.client.ui;

import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class PARSDialog {
   private PARSDialog() {
   }

   public static void draw(GuiGraphicsExtractor g, int x, int y, int w, int h) {
      PARSFramework.panel(g, x, y, w, h);
   }
}
