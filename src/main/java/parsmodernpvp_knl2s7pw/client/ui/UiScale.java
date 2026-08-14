package parsmodernpvp_knl2s7pw.client.ui;

import net.minecraft.client.Minecraft;
import parsmodernpvp_knl2s7pw.client.PvpClient;

/**
 * Central UI scaling system that adapts to different screen resolutions.
 * All layout values flow through this system for consistent sizing across displays.
 */
public final class UiScale {
   private static float cachedScale = -1.0F;
   public static final float DEFAULT = 1.0F;
   
   private UiScale() {
   }

   /**
    * Scales a base value by the global UI scale factor.
    * This is the primary method for all UI dimension calculations.
    */
   public static int s(int value) {
      return Math.round(value * getEffectiveScale());
   }

   /**
    * Scales a float value by the global UI scale factor.
    */
   public static float s(float value) {
      return value * getEffectiveScale();
   }

   /**
    * Returns the effective UI scale considering display resolution and user settings.
    */
   public static float getEffectiveScale() {
      if (cachedScale < 0.0F) {
         cachedScale = calculateEffectiveScale();
      }
      return cachedScale;
   }

   /**
    * Invalidates the cached scale, forcing recalculation on next use.
    * Call this when window size changes or settings are updated.
    */
   public static void invalidateCache() {
      cachedScale = -1.0F;
   }

   /**
    * Calculates the effective scale based on resolution and user preference.
    */
   private static float calculateEffectiveScale() {
      Minecraft mc = Minecraft.getInstance();
      if (mc == null || mc.getWindow() == null) {
         return PvpClient.uiScale();
      }

      int windowHeight = mc.getWindow().getGuiScaledHeight();
      
      // Base scale from user settings
      float userScale = Math.max(0.5F, Math.min(2.0F, PvpClient.uiScale()));
      
      // Resolution-based adjustment for high-DPI displays
      float dpiFactor = 1.0F;
      if (windowHeight > 1440) {
         dpiFactor = 1.25F;
      } else if (windowHeight > 1080) {
         dpiFactor = 1.1F;
      } else if (windowHeight < 720) {
         dpiFactor = 0.9F;
      }

      return userScale * dpiFactor;
   }

   /**
    * Scales font size specifically, accounting for separate font scale setting.
    */
   public static float scaleFont(float baseSize) {
      return baseSize * getEffectiveScale() * PvpClient.fontScale();
   }

   /**
    * Converts screen-space coordinates to scaled UI coordinates.
    */
   public static int toScreenSpace(int uiValue) {
      return Math.round(uiValue / getEffectiveScale());
   }

   /**
    * Returns appropriate icon size based on context.
    */
   public static int iconSize(String context) {
      return switch (context) {
         case "small" -> s(16);
         case "medium", "default" -> s(20);
         case "large" -> s(24);
         case "xl" -> s(32);
         default -> s(20);
      };
   }

   /**
    * Returns appropriate spacing based on context.
    */
   public static int spacing(String context) {
      return switch (context) {
         case "xs" -> s(4);
         case "sm" -> s(8);
         case "md", "default" -> s(12);
         case "lg" -> s(16);
         case "xl" -> s(24);
         case "2xl" -> s(32);
         default -> s(12);
      };
   }
}
