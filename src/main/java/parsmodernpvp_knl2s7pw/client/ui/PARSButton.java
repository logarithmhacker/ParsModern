package parsmodernpvp_knl2s7pw.client.ui;

import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class PARSButton {
   private PARSButton() {
   }

   public static void draw(GuiGraphicsExtractor g, String text, int x, int y, int w, int h, boolean hover, boolean active) {
      PARSFramework.button(g, text, x, y, w, h, hover, active);
   }
}
