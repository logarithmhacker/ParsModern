package parsmodernpvp_knl2s7pw.client.ui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import parsmodernpvp_knl2s7pw.client.PvpClient;
import parsmodernpvp_knl2s7pw.client.animation.PARSAnimationEngine;

public final class PARSFramework {
   private PARSFramework() {
   }

   public static void background(GuiGraphicsExtractor g, int width, int height) {
      UiBackground.render(g, width, height, 0.82F);
   }

   public static void panel(GuiGraphicsExtractor g, int x, int y, int width, int height) {
      int shadow = PvpClient.themeEngine().color("shadow");
      int border = PvpClient.themeEngine().color("border");
      int radius = Math.max(0, Math.min(3, (int)PvpClient.cornerRadius()));
      if (PvpClient.shadow()) {
         g.fill(x + 2, y + 3, x + width + 2, y + height + 3, shadow);
      }

      g.fill(x, y, x + width, y + height, PvpClient.themeEngine().color("panel"));
      if (radius > 0) {
         int corner = PvpClient.themeEngine().color("background");
         g.fill(x, y, x + radius, y + radius, corner);
         g.fill(x + width - radius, y, x + width, y + radius, corner);
         g.fill(x, y + height - radius, x + radius, y + height, corner);
         g.fill(x + width - radius, y + height - radius, x + width, y + height, corner);
      }

      g.fill(x + radius, y, x + width - radius, y + 1, border);
      g.fill(x + radius, y + height - 1, x + width - radius, y + height, border);
      g.fill(x, y + radius, x + 2, y + height - radius, PvpClient.themeEngine().accent());
   }

   public static void card(GuiGraphicsExtractor g, int x, int y, int width, int height, boolean selected) {
      g.fill(x + 1, y + 2, x + width + 1, y + height + 2, PvpClient.themeEngine().color("shadow"));
      g.fill(x, y, x + width, y + height, selected ? PvpClient.themeEngine().color("hover") : PvpClient.themeEngine().color("card"));
      g.fill(x, y, x + (selected ? 3 : 1), y + height, selected ? PvpClient.themeEngine().accent() : PvpClient.themeEngine().border());
   }

   public static void button(GuiGraphicsExtractor g, String label, int x, int y, int width, int height, boolean hovered, boolean active) {
      card(g, x, y, width, height, hovered || active);
      float pulse = PvpClient.reducedMotion()
         ? 1.0F
         : PARSAnimationEngine.ease((float)((Math.sin(System.nanoTime() / 1.8E8) + 1.0) * 0.5), PARSAnimationEngine.Easing.EASE_IN_OUT);
      if (hovered) {
         g.fill(x + 3, y + height - 2, x + width - 3, y + height - 1, withAlpha(PvpClient.themeEngine().secondary(), 0.35F + pulse * 0.35F));
      }

      PARSFontEngine.centered(
         g,
         label,
         x + width / 2,
         y + height / 2,
         !hovered && !active ? PvpClient.themeEngine().mutedText() : PvpClient.themeEngine().text(),
         PARSFontEngine.Token.SMALL,
         PvpClient.shadow(),
         false
      );
   }

   public static void progress(GuiGraphicsExtractor g, int x, int y, int width, float value) {
      g.fill(x, y, x + width, y + 3, 1429423189);
      g.fill(x, y, x + Math.max(0, Math.min(width, Math.round(width * value))), y + 3, PvpClient.themeEngine().accent());
   }

   private static int withAlpha(int color, float alpha) {
      return Math.max(0, Math.min(255, Math.round((color >>> 24 & 0xFF) * alpha))) << 24 | color & 16777215;
   }
}
