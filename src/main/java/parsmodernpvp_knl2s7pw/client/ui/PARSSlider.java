package parsmodernpvp_knl2s7pw.client.ui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import parsmodernpvp_knl2s7pw.client.PvpClient;
import parsmodernpvp_knl2s7pw.client.animation.AnimationEngine;

/**
 * Premium slider component with smooth animations and gradient fill.
 */
public final class PARSSlider {
   private PARSSlider() {
   }

   public static void draw(GuiGraphicsExtractor g, int x, int y, int width, float value) {
      draw(g, x, y, width, DesignTokens.BUTTON_HEIGHT_MD, value, false);
   }

   public static void draw(GuiGraphicsExtractor g, int x, int y, int width, int height, float value) {
      draw(g, x, y, width, height, value, false);
   }

   public static void draw(GuiGraphicsExtractor g, int x, int y, int width, int height, float value, boolean interactive) {
      int r = height / 2;
      float clamped = Math.max(0.0F, Math.min(1.0F, value));
      int filledWidth = Math.round(width * clamped);
      
      // Track background
      int trackColor = 0x4D586070;
      fillRoundedRect(g, x, y, width, height, r, trackColor);
      
      // Fill with gradient effect
      int fillColor = PvpClient.themeEngine().accent();
      
      // Glow effect
      if (PvpClient.glow() && interactive) {
         int glowColor = withAlpha(fillColor, 0.25F);
         g.fill(x - 2, y - 2, x + filledWidth + 2, y + height + 2, glowColor);
      }
      
      // Main fill
      fillRoundedRect(g, x, y, filledWidth, height, r, fillColor);
      
      // Highlight overlay
      int highlight = 0x50FFFFFF;
      g.fill(x + 3, y + 2, x + filledWidth - 3, y + 4, highlight);
      
      // Thumb indicator
      if (interactive || filledWidth > 0) {
         int thumbX = x + filledWidth - r;
         int thumbY = y + 1;
         int thumbSize = height - 2;
         
         // Thumb shadow
         g.fill(thumbX + 1, thumbY + 2, thumbX + thumbSize + 1, thumbY + thumbSize + 2, DesignTokens.SHADOW_COLOR);
         
         // Thumb body
         int thumbColor = -1;
         fillRoundedRect(g, thumbX, thumbY, thumbSize, thumbSize, thumbSize / 2.0F, thumbColor);
      }
   }
   
   private static void fillRoundedRect(GuiGraphicsExtractor g, int x, int y, int width, int height, int radius, int color) {
      int r = Math.max(0, Math.min(radius, Math.min(width, height) / 2));
      if (width <= 0 || height <= 0) return;
      
      g.fill(x + r, y, Math.max(x + r, x + width - r), y + height, color);
      g.fill(x, y + r, x + width, y + height - r, color);
      if (r > 0) {
         g.fill(x + r, y, Math.max(x + r, x + width - r), y + r, color);
         g.fill(x + r, y + height - r, Math.max(x + r, x + width - r), y + height, color);
      }
   }
   
   private static int withAlpha(int color, float alpha) {
      int a = Math.max(0, Math.min(255, (int)((color >>> 24 & 0xFF) * alpha)));
      return a << 24 | color & 0xFFFFFF;
   }
}
