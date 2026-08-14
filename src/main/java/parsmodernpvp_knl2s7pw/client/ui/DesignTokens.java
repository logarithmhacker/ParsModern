package parsmodernpvp_knl2s7pw.client.ui;

import parsmodernpvp_knl2s7pw.client.PvpClient;

/**
 * PARS Design System - Central design tokens for consistent visual language.
 * All spacing, colors, radii, shadows, and animations flow through this system.
 */
public final class DesignTokens {
   private DesignTokens() {
   }

   /* =====================================================
    * SPACING SYSTEM - 4px base grid
    * ===================================================== */
   public static final int SPACE_0 = 0;
   public static final int SPACE_1 = 2;
   public static final int SPACE_2 = 4;
   public static final int SPACE_3 = 6;
   public static final int SPACE_4 = 8;
   public static final int SPACE_5 = 10;
   public static final int SPACE_6 = 12;
   public static final int SPACE_8 = 16;
   public static final int SPACE_10 = 20;
   public static final int SPACE_12 = 24;
   public static final int SPACE_16 = 32;
   public static final int SPACE_20 = 40;
   public static final int SPACE_24 = 48;
   public static final int SPACE_32 = 64;

   /* =====================================================
    * BORDER RADIUS SYSTEM
    * ===================================================== */
   public static final float RADIUS_NONE = 0.0F;
   public static final float RADIUS_SM = 2.0F;
   public static final float RADIUS_MD = 4.0F;
   public static final float RADIUS_LG = 8.0F;
   public static final float RADIUS_XL = 12.0F;
   public static final float RADIUS_FULL = 9999.0F;

   /* =====================================================
    * SHADOW SYSTEM
    * ===================================================== */
   public static final int SHADOW_COLOR = 0x2A000000;
   public static final int SHADOW_COLOR_SOFT = 0x1A000000;
   public static final int GLOW_COLOR_BASE = 0x33FFFFFF;

   /* =====================================================
    * ELEVATION LAYERS
    * ===================================================== */
   public static final int ELEVATION_BACKGROUND = 0;
   public static final int ELEVATION_PANEL = 1;
   public static final int ELEVATION_CARD = 2;
   public static final int ELEVATION_FLOATING = 3;
   public static final int ELEVATION_MODAL = 4;
   public static final int ELEVATION_TOAST = 5;

   /* =====================================================
    * ANIMATION DURATIONS (ms)
    * ===================================================== */
   public static final long DURATION_INSTANT = 0L;
   public static final long DURATION_FAST = 120L;
   public static final long DURATION_NORMAL = 200L;
   public static final long DURATION_SLOW = 320L;
   public static final long DURATION_SLOWER = 480L;

   /* =====================================================
    * OPACITY LEVELS
    * ===================================================== */
   public static final float OPACITY_DISABLED = 0.4F;
   public static final float OPACITY_MUTED = 0.6F;
   public static final float OPACITY_SECONDARY = 0.75F;
   public static final float OPACITY_PRIMARY = 0.92F;
   public static final float OPACITY_FULL = 1.0F;

   /* =====================================================
    * PANEL DIMENSIONS
    * ===================================================== */
   public static final int PANEL_MAIN_WIDTH = 840;
   public static final int PANEL_MAIN_HEIGHT = 480;
   public static final int PANEL_SIDEBAR_WIDTH = 180;
   public static final int PANEL_CONTENT_PADDING = 16;
   public static final int CARD_MIN_HEIGHT = 48;
   public static final int BUTTON_HEIGHT_SM = 24;
   public static final int BUTTON_HEIGHT_MD = 32;
   public static final int BUTTON_HEIGHT_LG = 40;

   /* =====================================================
    * TYPOGRAPHY BASE SIZES
    * ===================================================== */
   public static final float FONT_SIZE_XS = 9.0F;
   public static final float FONT_SIZE_SM = 11.0F;
   public static final float FONT_SIZE_BASE = 13.0F;
   public static final float FONT_SIZE_LG = 16.0F;
   public static final float FONT_SIZE_XL = 20.0F;
   public static final float FONT_SIZE_2XL = 26.0F;
   public static final float FONT_SIZE_3XL = 34.0F;

   /* =====================================================
    * EFFECT INTENSITIES
    * ===================================================== */
   public static final float BLUR_NONE = 0.0F;
   public static final float BLUR_SM = 2.0F;
   public static final float BLUR_MD = 4.0F;
   public static final float BLUR_LG = 8.0F;

   public static final float GLOW_NONE = 0.0F;
   public static final float GLOW_SM = 0.15F;
   public static final float GLOW_MD = 0.25F;
   public static final float GLOW_LG = 0.4F;

   /**
    * Returns effective radius based on user settings.
    */
   public static float effectiveRadius() {
      return clamp(PvpClient.cornerRadius(), RADIUS_NONE, RADIUS_XL);
   }

   /**
    * Returns spacing scaled by current UI scale.
    */
   public static int scaledSpacing(int baseSpace) {
      return UiScale.s(baseSpace);
   }

   private static float clamp(float value, float min, float max) {
      return Math.max(min, Math.min(max, value));
   }
}
