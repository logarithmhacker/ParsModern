package parsmodernpvp_knl2s7pw.client.ui;

import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class PARSSlider {
   private PARSSlider() {
   }

   public static void draw(GuiGraphicsExtractor g, int x, int y, int w, float value) {
      PARSFramework.progress(g, x, y, w, value);
   }
}
