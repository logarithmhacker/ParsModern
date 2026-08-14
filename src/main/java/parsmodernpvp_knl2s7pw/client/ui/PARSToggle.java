package parsmodernpvp_knl2s7pw.client.ui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import parsmodernpvp_knl2s7pw.client.PvpClient;
import parsmodernpvp_knl2s7pw.client.animation.AnimationEngine;

/**
 * Premium toggle switch component with smooth animations and glassmorphism styling.
 */
public final class PARSToggle {
   private PARSToggle() {
   }

   public static void draw(GuiGraphicsExtractor g, int x, int y, boolean enabled) {
      draw(g, x, y, enabled, true);
   }

   public static void draw(GuiGraphicsExtractor g, int x, int y, boolean enabled, boolean animated) {
      int width = 36;
      int height = 20;
      float radius = height / 2.0F;
      
      int trackBg = enabled 
         ? withAlpha(PvpClient.theme().accent(), 0.85F) 
         : 0x4D586070;
      
      if (PvpClient.shadow()) {
         g.fill(x + 1, y + 2, x + width + 1, y + height + 2, DesignTokens.SHADOW_COLOR_SOFT);
      }
      
      fillRoundedRect(g, x, y, width, height, radius, trackBg);
      
      if (enabled && PvpClient.glow()) {
         int glowColor = withAlpha(PvpClient.theme().accent(), 0.3F);
         g.fill(x - 2, y - 2, x + width + 2, y + height + 2, glowColor);
      }
      
      float thumbX;
      if (animated && !PvpClient.reducedMotion()) {
         long animStart = System.nanoTime() - (enabled ? 0L : DesignTokens.DURATION_NORMAL * 1000000L);
         float progress = AnimationEngine.progress(animStart, DesignTokens.DURATION_NORMAL * 1000000L, AnimationEngine.Curve.SPRING, false);
         thumbX = x + 2 + (width - height - 2) * progress;
      } else {
         thumbX = enabled ? x + width - height + 1 : x + 1;
      }
      
      int thumbY = y + 1;
      int thumbSize = height - 2;
      
      g.fill((int)thumbX + 1, thumbY + 2, (int)thumbX + thumbSize + 1, thumbY + thumbSize + 2, DesignTokens.SHADOW_COLOR);
      
      int thumbColor = enabled ? -1 : 0xFF8890A0;
      fillRoundedRect(g, (int)thumbX, thumbY, thumbSize, thumbSize, thumbSize / 2.0F, thumbColor);
      
      if (enabled) {
         int highlight = 0x40FFFFFF;
         g.fill((int)thumbX + 3, thumbY + 3, (int)thumbX + thumbSize - 3, thumbY + 5, highlight);
      }
   }
   
   private static void fillRoundedRect(GuiGraphicsExtractor g, int x, int y, int width, int height, float radius, int color) {
      int r = Math.max(0, Math.min((int)radius, Math.min(width, height) / 2));
      g.fill(x + r, y, x + width - r, y + height, color);
      g.fill(x, y + r, x + width, y + height - r, color);
      if (r > 0) {
         g.fill(x + r, y, x + width - r, y + r, color);
         g.fill(x + r, y + height - r, x + width - r, y + height, color);
      }
   }
   
   private static int withAlpha(int color, float alpha) {
      int a = Math.max(0, Math.min(255, (int)((color >>> 24 & 0xFF) * alpha)));
      return a << 24 | color & 0xFFFFFF;
   }
}
