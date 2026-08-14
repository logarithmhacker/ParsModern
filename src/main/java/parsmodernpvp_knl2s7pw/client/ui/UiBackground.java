package parsmodernpvp_knl2s7pw.client.ui;

import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class UiBackground {
   private UiBackground() {
   }

   public static void render(GuiGraphicsExtractor graphics, int width, int height, float intensity) {
      BackgroundEngine.render(graphics, width, height, intensity);
   }
}
