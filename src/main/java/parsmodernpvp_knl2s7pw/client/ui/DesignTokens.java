package parsmodernpvp_knl2s7pw.client.ui;

import parsmodernpvp_knl2s7pw.client.PvpClient;

/**
 * PARS Design System - Central design tokens for consistent visual language.
 * All spacing, colors, radii, shadows, and animations flow through this system.
 * 
 * Visual Identity: FAST | PREMIUM | COMPETITIVE | CLEAN | MODERN | TECHNICAL | POWERFUL
 */
public final class DesignTokens {
   private DesignTokens() {
   }

   /* =====================================================
    * SPACING SYSTEM - 4px base grid, responsive scaling
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
   public static final int SPACE_40 = 80;
   public static final int SPACE_48 = 96;
   public static final int SPACE_64 = 128;

   /* =====================================================
    * BORDER RADIUS SYSTEM - Subtle, professional curves
    * ===================================================== */
   public static final float RADIUS_NONE = 0.0F;
   public static final float RADIUS_SM = 2.0F;
   public static final float RADIUS_MD = 4.0F;
   public static final float RADIUS_LG = 6.0F;
   public static final float RADIUS_XL = 10.0F;
   public static final float RADIUS_2XL = 16.0F;
   public static final float RADIUS_FULL = 9999.0F;

   /* =====================================================
    * SHADOW SYSTEM - Layered depth
    * ===================================================== */
   public static final int SHADOW_COLOR = 0x2A000000;
   public static final int SHADOW_COLOR_SOFT = 0x1A000000;
   public static final int SHADOW_COLOR_STRONG = 0x40000000;
   public static final int GLOW_COLOR_BASE = 0x33FFFFFF;
   public static final int GLOW_COLOR_ACCENT = 0x22FF6B00;

   /* =====================================================
    * ELEVATION LAYERS - Z-index hierarchy
    * ===================================================== */
   public static final int ELEVATION_BACKGROUND = 0;
   public static final int ELEVATION_PANEL = 1;
   public static final int ELEVATION_CARD = 2;
   public static final int ELEVATION_FLOATING = 3;
   public static final int ELEVATION_MODAL = 4;
   public static final int ELEVATION_TOAST = 5;
   public static final int ELEVATION_TOOLTIP = 6;

   /* =====================================================
    * ANIMATION DURATIONS (ms) - Smooth, responsive timing
    * ===================================================== */
   public static final long DURATION_INSTANT = 0L;
   public static final long DURATION_FASTEST = 80L;
   public static final long DURATION_FAST = 120L;
   public static final long DURATION_NORMAL = 180L;
   public static final long DURATION_SLOW = 280L;
   public static final long DURATION_SLOWER = 400L;
   public static final long DURATION_ENTERPRISE = 600L;

   /* =====================================================
    * OPACITY LEVELS - Consistent transparency
    * ===================================================== */
   public static final float OPACITY_DISABLED = 0.4F;
   public static final float OPACITY_MUTED = 0.6F;
   public static final float OPACITY_SECONDARY = 0.75F;
   public static final float OPACITY_PRIMARY = 0.92F;
   public static final float OPACITY_FULL = 1.0F;
   public static final float OPACITY_OVERLAY = 0.5F;
   public static final float OPACITY_GLASS = 0.08F;

   /* =====================================================
    * PANEL DIMENSIONS - Responsive layout constraints
    * ===================================================== */
   public static final int PANEL_MAIN_WIDTH = 900;
   public static final int PANEL_MAIN_HEIGHT = 540;
   public static final int PANEL_NARROW_WIDTH = 600;
   public static final int PANEL_SIDEBAR_WIDTH = 220;
   public static final int PANEL_CONTENT_PADDING = 20;
   public static final int CARD_MIN_HEIGHT = 56;
   public static final int CARD_ELEVATED_HEIGHT = 72;
   
   /* =====================================================
    * BUTTON SIZES - Hierarchical action buttons
    * ===================================================== */
   public static final int BUTTON_HEIGHT_SM = 28;
   public static final int BUTTON_HEIGHT_MD = 36;
   public static final int BUTTON_HEIGHT_LG = 44;
   public static final int BUTTON_HEIGHT_XL = 52;
   public static final int BUTTON_ICON_SIZE = 20;
   public static final int BUTTON_PADDING_H = 16;
   public static final int BUTTON_PADDING_V = 8;

   /* =====================================================
    * TYPOGRAPHY BASE SIZES - Vazirmatn optimized
    * ===================================================== */
   public static final float FONT_SIZE_XS = 8.0F;
   public static final float FONT_SIZE_SM = 10.0F;
   public static final float FONT_SIZE_BASE = 12.0F;
   public static final float FONT_SIZE_LG = 14.0F;
   public static final float FONT_SIZE_XL = 18.0F;
   public static final float FONT_SIZE_2XL = 24.0F;
   public static final float FONT_SIZE_3XL = 32.0F;
   public static final float FONT_SIZE_4XL = 42.0F;
   
   /* =====================================================
    * LINE HEIGHTS - Readable text blocks
    * ===================================================== */
   public static final float LINE_HEIGHT_TIGHT = 1.2F;
   public static final float LINE_HEIGHT_NORMAL = 1.5F;
   public static final float LINE_HEIGHT_RELAXED = 1.75F;

   /* =====================================================
    * EFFECT INTENSITIES - Subtle visual enhancements
    * ===================================================== */
   public static final float BLUR_NONE = 0.0F;
   public static final float BLUR_SM = 2.0F;
   public static final float BLUR_MD = 4.0F;
   public static final float BLUR_LG = 8.0F;
   public static final float BLUR_XL = 16.0F;

   public static final float GLOW_NONE = 0.0F;
   public static final float GLOW_SM = 0.12F;
   public static final float GLOW_MD = 0.22F;
   public static final float GLOW_LG = 0.35F;

   /* =====================================================
    * ICON SIZES - Consistent visual weight
    * ===================================================== */
   public static final int ICON_SIZE_XS = 12;
   public static final int ICON_SIZE_SM = 16;
   public static final int ICON_SIZE_MD = 20;
   public static final int ICON_SIZE_LG = 24;
   public static final int ICON_SIZE_XL = 32;
   public static final int ICON_SIZE_2XL = 48;

   /* =====================================================
    * GRID SYSTEM - Layout structure
    * ===================================================== */
   public static final int GRID_COLUMNS = 12;
   public static final int GRID_GUTTER = 16;
   public static final int GRID_MARGIN = 24;

   /* =====================================================
    * RESPONSIVE HELPERS - GUI Scale adaptive functions
    * ===================================================== */
   
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
   
   /**
    * Returns responsive font size scaled by UI and font settings.
    */
   public static float scaledFontSize(float baseSize) {
      return baseSize * UiScale.getEffectiveScale() * PvpClient.fontScale();
   }
   
   /**
    * Returns appropriate button height for context.
    */
   public static int buttonHeight(String size) {
      return switch (size) {
         case "sm" -> BUTTON_HEIGHT_SM;
         case "lg" -> BUTTON_HEIGHT_LG;
         case "xl" -> BUTTON_HEIGHT_XL;
         default -> BUTTON_HEIGHT_MD;
      };
   }
   
   /**
    * Returns appropriate icon size for context.
    */
   public static int iconSize(String size) {
      return switch (size) {
         case "xs" -> ICON_SIZE_XS;
         case "sm" -> ICON_SIZE_SM;
         case "lg" -> ICON_SIZE_LG;
         case "xl" -> ICON_SIZE_XL;
         case "2xl" -> ICON_SIZE_2XL;
         default -> ICON_SIZE_MD;
      };
   }

   /**
    * Returns responsive width based on screen size and scale.
    */
   public static int responsiveWidth(int baseWidth, int screenWidth) {
      int scaled = UiScale.s(baseWidth);
      return Math.min(scaled, screenWidth - GRID_MARGIN * 2);
   }

   /**
    * Returns responsive height based on screen size and scale.
    */
   public static int responsiveHeight(int baseHeight, int screenHeight) {
      int scaled = UiScale.s(baseHeight);
      return Math.min(scaled, screenHeight - GRID_MARGIN * 2);
   }

   /**
    * Calculates font scale based on GUI scale setting.
    */
   public static float fontScale() {
      return PvpClient.fontScale() * UiScale.getEffectiveScale();
   }

   private static float clamp(float value, float min, float max) {
      return Math.max(min, Math.min(max, value));
   }
   
   private static int clamp(int value, int min, int max) {
      return Math.max(min, Math.min(max, value));
   }
}
