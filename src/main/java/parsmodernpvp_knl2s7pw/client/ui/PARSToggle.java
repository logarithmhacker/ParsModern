package parsmodernpvp_knl2s7pw.client.ui;

import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class PARSToggle {
   private PARSToggle() {
   }

   public static void draw(GuiGraphicsExtractor g, int x, int y, boolean enabled) {
      PARSFramework.card(g, x, y, 34, 16, enabled);
   }
}
