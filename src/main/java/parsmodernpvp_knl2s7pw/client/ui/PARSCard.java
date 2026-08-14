package parsmodernpvp_knl2s7pw.client.ui;

import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class PARSCard {
   private PARSCard() {
   }

   public static void draw(GuiGraphicsExtractor g, int x, int y, int w, int h, boolean selected) {
      PARSFramework.card(g, x, y, w, h, selected);
   }
}
