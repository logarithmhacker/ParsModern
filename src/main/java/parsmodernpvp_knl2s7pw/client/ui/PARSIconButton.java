package parsmodernpvp_knl2s7pw.client.ui;

import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class PARSIconButton {
   private PARSIconButton() {
   }

   public static void draw(GuiGraphicsExtractor g, String icon, int x, int y, boolean hover) {
      PARSFramework.button(g, icon, x, y, 26, 26, hover, false);
   }
}
