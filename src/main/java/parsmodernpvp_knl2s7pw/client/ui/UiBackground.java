package parsmodernpvp_knl2s7pw.client.ui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import parsmodernpvp_knl2s7pw.client.PvpClient;
import parsmodernpvp_knl2s7pw.client.animation.AnimationEngine;

/**
 * Premium animated background renderer with glassmorphism effects.
 */
public final class UiBackground {
   private static long startTime = System.nanoTime();

   private UiBackground() {
   }

   public static void render(GuiGraphicsExtractor g, int width, int height) {
      render(g, width, height, PvpClient.backgroundOpacity());
   }

   public static void render(GuiGraphicsExtractor g, int width, int height, float opacity) {
      if (!PvpClient.initialized()) return;

      // Base gradient background
      renderBaseGradient(g, width, height, opacity);

      // Animated overlay effects
      if (PvpClient.animatedGradient() && !PvpClient.lowPerformance()) {
         renderAnimatedOverlay(g, width, height);
      }

      // Grid pattern for tech aesthetic
      if (PvpClient.grid() && !PvpClient.lowPerformance()) {
         renderGrid(g, width, height);
      }

      // Vignette effect
      if (PvpClient.vignette()) {
         renderVignette(g, width, height);
      }
   }

   private static void renderBaseGradient(GuiGraphicsExtractor g, int width, int height, float opacity) {
      int baseColor = PvpClient.themeEngine().color("background");
      
      // Subtle vertical gradient
      int topColor = withAlpha(baseColor, opacity);
      int bottomColor = withAlpha(mixColors(baseColor, 0x1A000000, 1.0F), opacity);
      
      // Draw gradient using horizontal strips
      int stripHeight = Math.max(1, height / 64);
      for (int i = 0; i < height; i += stripHeight) {
         float t = (float)i / height;
         int color = mixColors(topColor, bottomColor, t);
         g.fill(0, i, width, Math.min(i + stripHeight, height), color);
      }
   }

   private static void renderAnimatedOverlay(GuiGraphicsExtractor g, int width, int height) {
      long elapsed = System.nanoTime() - startTime;
      float time = (float)(elapsed / 1.0E9) * PvpClient.animationSpeed();
      
      int accent = PvpClient.themeEngine().accent() & 0xFFFFFF;
      int secondary = PvpClient.themeEngine().secondary() & 0xFFFFFF;
      
      // Slow moving gradient bands
      int bandHeight = 80;
      int offset = (int)(time * 20.0F) % (height + bandHeight) - bandHeight;
      
      for (int i = 0; i < 3; i++) {
         int y = offset + i * (height / 3);
         int alpha = 8 + (int)(4.0F * Math.sin(time * 0.5F + i));
         int color = (i % 2 == 0 ? accent : secondary) | alpha << 24;
         g.fill(0, y, width, y + bandHeight / 4, color);
      }
   }

   private static void renderGrid(GuiGraphicsExtractor g, int width, int height) {
      int gridSize = 48;
      int gridColor = 0x0DFFFFFF;
      
      // Vertical lines
      for (int x = 0; x < width; x += gridSize) {
         g.fill(x, 0, x + 1, height, gridColor);
      }
      
      // Horizontal lines
      for (int y = 0; y < height; y += gridSize) {
         g.fill(0, y, width, y + 1, gridColor);
      }
   }

   private static void renderVignette(GuiGraphicsExtractor g, int width, int height) {
      int vignetteSize = Math.min(width, height) / 3;
      int vignetteColor = 0x40000000;
      
      // Top edge
      g.fill(0, 0, width, vignetteSize / 2, vignetteColor);
      // Bottom edge
      g.fill(0, height - vignetteSize / 2, width, height, vignetteColor);
      // Left edge
      g.fill(0, 0, vignetteSize / 2, height, vignetteColor);
      // Right edge
      g.fill(width - vignetteSize / 2, 0, width, height, vignetteColor);
   }

   private static int withAlpha(int color, float alpha) {
      int a = Math.max(0, Math.min(255, Math.round((color >>> 24 & 0xFF) * alpha)));
      return a << 24 | color & 0xFFFFFF;
   }

   private static int mixColors(int c1, int c2, float t) {
      t = Math.max(0.0F, Math.min(1.0F, t));
      int a = Math.round((c1 >>> 24 & 0xFF) * (1.0F - t) + (c2 >>> 24 & 0xFF) * t);
      int r = Math.round((c1 >>> 16 & 0xFF) * (1.0F - t) + (c2 >>> 16 & 0xFF) * t);
      int g = Math.round((c1 >>> 8 & 0xFF) * (1.0F - t) + (c2 >>> 8 & 0xFF) * t);
      int b = Math.round((c1 & 0xFF) * (1.0F - t) + (c2 & 0xFF) * t);
      return a << 24 | r << 16 | g << 8 | b;
   }
}
