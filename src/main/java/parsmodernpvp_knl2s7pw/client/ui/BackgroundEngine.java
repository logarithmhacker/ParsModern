package parsmodernpvp_knl2s7pw.client.ui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import parsmodernpvp_knl2s7pw.client.PvpClient;

public final class BackgroundEngine {
   private static BackgroundEngine.Preset preset = BackgroundEngine.Preset.BALANCED;
   private static BackgroundEngine.Type type = BackgroundEngine.Type.NEBULA;
   private static float speed = 1.0F;
   private static float opacity = 0.82F;
   private static float parallax = 0.12F;
   private static boolean fpsSafe;

   private BackgroundEngine() {
   }

   public static void render(GuiGraphicsExtractor g, int width, int height, float intensity) {
      g.fill(0, 0, width, height, PvpClient.themeEngine().background());
      float time = PvpClient.animatedGradient() ? (float)System.nanoTime() / 1.0E9F * speed : 0.0F;
      int accent = PvpClient.themeEngine().accent();
      int secondary = PvpClient.themeEngine().secondary();
      if (type == BackgroundEngine.Type.MINIMAL) {
         g.fill(0, 0, width, 1, PvpClient.themeEngine().border());
      } else {
         float parallaxAmount = PvpClient.parallax() ? parallax : 0.0F;
         int glowX = (int)(width * (0.58F + parallaxAmount * Math.sin(time * 0.1F)));
         int glowY = (int)(height * (0.38F + parallaxAmount * 0.8F * Math.cos(time * 0.08F)));
         int layers = !fpsSafe && preset != BackgroundEngine.Preset.LOW
            ? (preset == BackgroundEngine.Preset.HIGH ? 5 : (preset == BackgroundEngine.Preset.ULTRA ? 8 : 4))
            : 2;
         if (PvpClient.animatedGradient()) {
            int bands = preset == BackgroundEngine.Preset.ULTRA ? 18 : (preset == BackgroundEngine.Preset.HIGH ? 12 : 7);

            for (int band = 0; band < bands; band++) {
               float amount = (float)band / Math.max(1, bands - 1);
               int color = blend(accent, secondary, (amount + (float)Math.sin(time * 0.08) * 0.08F) % 1.0F);
               int alpha = (int)(intensity * 12.0F);
               g.fill(0, band * height / bands, width, (band + 1) * height / bands + 1, alpha << 24 | color & 16777215);
            }
         }

         if (PvpClient.softLighting() && PvpClient.glow() && (type == BackgroundEngine.Type.NEBULA || type == BackgroundEngine.Type.GRADIENT)) {
            for (int layer = layers; layer > 0; layer--) {
               int radius = layer * 70;
               int alpha = Math.max(2, (int)(intensity * opacity * (8 + layer * 2)));
               g.fill(glowX - radius, glowY - radius / 2, glowX + radius, glowY + radius / 2, alpha << 24 | accent & 16777215);
            }
         }

         if (PvpClient.grid() && (type == BackgroundEngine.Type.GRID || type == BackgroundEngine.Type.NEBULA || type == BackgroundEngine.Type.STARS)) {
            int grid = accent & 16777215 | 167772160;
            int offset = (int)(time * 4.0F % 48.0F);
            if (!fpsSafe && type != BackgroundEngine.Type.STARS) {
               for (int x = -48 + offset; x < width + 48; x += 48) {
                  g.fill(x, 0, x + 1, height, grid);
               }

               for (int y = -48 + offset; y < height + 48; y += 48) {
                  g.fill(0, y, width, y + 1, grid);
               }
            }

            int count = !PvpClient.particles()
               ? 0
               : (!fpsSafe && preset != BackgroundEngine.Preset.LOW ? (preset == BackgroundEngine.Preset.ULTRA ? 28 : 16) : 7);

            for (int i = 0; i < count; i++) {
               float seed = i * 17.371F;
               int x = Math.floorMod((int)(seed * 41.0F + time * (1 + i % 3)), Math.max(1, width));
               int y = Math.floorMod((int)(seed * 23.0F - time * (1 + i % 2)), Math.max(1, height));
               int size = i % 6 == 0 ? 2 : 1;
               g.fill(x, y, x + size, y + size, 32 + i % 3 * 8 << 24 | (i % 2 == 0 ? accent : secondary) & 16777215);
            }
         }

         g.fill(0, 0, width, 1, 1429423189);
         g.fill(0, height - 1, width, height, 1429423189);
         if (PvpClient.blur()) {
            g.fill(0, 0, width, height, 336072728);
            g.fill(0, 0, width, height, 169354298);
            g.fill(0, 0, width, 2, 403181592);
            g.fill(0, height - 2, width, height, 403181592);
         }

         if (PvpClient.motionBlur()) {
            int trail = secondary & 16777215 | 301989888;
            g.fill(0, height / 3, width, height / 3 + 1, trail);
            g.fill(0, height * 2 / 3, width, height * 2 / 3 + 1, trail);
         }

         if (PvpClient.colorGrading()) {
            g.fill(0, 0, width, height, accent & 16777215 | 134217728);
         }

         if (PvpClient.screenFade()) {
            int fade = Math.max(2, (int)(7.0 + 5.0 * Math.sin(time * 0.9F)));
            g.fill(0, 0, width, height, fade << 24 | 328458);
         }

         if (PvpClient.vignette()) {
            int edge = 0 | (preset == BackgroundEngine.Preset.ULTRA ? 637534208 : 436207616);
            g.fill(0, 0, width, 3, edge);
            g.fill(0, height - 3, width, height, edge);
            g.fill(0, 0, 3, height, edge);
            g.fill(width - 3, 0, width, height, edge);
         }
      }
   }

   private static int blend(int first, int second, float amount) {
      float t = amount < 0.0F ? amount + 1.0F : amount;
      int r = Math.round((first >>> 16 & 0xFF) * (1.0F - t) + (second >>> 16 & 0xFF) * t);
      int gr = Math.round((first >>> 8 & 0xFF) * (1.0F - t) + (second >>> 8 & 0xFF) * t);
      int b = Math.round((first & 0xFF) * (1.0F - t) + (second & 0xFF) * t);
      return r << 16 | gr << 8 | b;
   }

   public static void setPreset(BackgroundEngine.Preset value) {
      if (value != null) {
         preset = value;
      }
   }

   public static void setType(BackgroundEngine.Type value) {
      if (value != null) {
         type = value;
      }
   }

   public static void setSpeed(float value) {
      speed = Math.max(0.1F, Math.min(3.0F, value));
   }

   public static void setOpacity(float value) {
      opacity = Math.max(0.0F, Math.min(1.0F, value));
   }

   public static void setParallax(float value) {
      parallax = Math.max(0.0F, Math.min(0.5F, value));
   }

   public static void setFpsSafe(boolean value) {
      fpsSafe = value;
   }

   public static BackgroundEngine.Preset preset() {
      return preset;
   }

   public static BackgroundEngine.Type type() {
      return type;
   }

   public enum Preset {
      LOW,
      BALANCED,
      HIGH,
      ULTRA;
   }

   public enum Type {
      GRADIENT,
      PARTICLES,
      GRID,
      STARS,
      NEBULA,
      MINIMAL;
   }
}
