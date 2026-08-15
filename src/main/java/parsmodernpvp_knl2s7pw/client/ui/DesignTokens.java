package parsmodernpvp_knl2s7pw.client.ui;

import parsmodernpvp_knl2s7pw.client.PvpClient;

/**
 * PARS Design System.
 *
 * A compact, restrained visual language: dark glass surfaces, one strong
 * accent, soft depth, and consistent 4px rhythm.  Rendering primitives live
 * in {@link PARSFramework}; these values are the shared design contract.
 */
public final class DesignTokens {
    private DesignTokens() {
    }

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

    public static final float RADIUS_NONE = 0.0F;
    public static final float RADIUS_SM = 4.0F;
    public static final float RADIUS_MD = 7.0F;
    public static final float RADIUS_LG = 10.0F;
    public static final float RADIUS_XL = 14.0F;
    public static final float RADIUS_2XL = 18.0F;
    public static final float RADIUS_FULL = 9999.0F;

    public static final int SHADOW_COLOR = 0x65000000;
    public static final int SHADOW_COLOR_SOFT = 0x35000000;
    public static final int SHADOW_COLOR_STRONG = 0x90000000;
    public static final int GLOW_COLOR_BASE = 0x18FFFFFF;
    public static final int GLOW_COLOR_ACCENT = 0x22FFFFFF;

    public static final int ELEVATION_BACKGROUND = 0;
    public static final int ELEVATION_PANEL = 1;
    public static final int ELEVATION_CARD = 2;
    public static final int ELEVATION_FLOATING = 3;
    public static final int ELEVATION_MODAL = 4;
    public static final int ELEVATION_TOAST = 5;
    public static final int ELEVATION_TOOLTIP = 6;

    public static final long DURATION_INSTANT = 0L;
    public static final long DURATION_FASTEST = 80L;
    public static final long DURATION_FAST = 120L;
    public static final long DURATION_NORMAL = 180L;
    public static final long DURATION_SLOW = 280L;
    public static final long DURATION_SLOWER = 400L;
    public static final long DURATION_ENTERPRISE = 600L;

    public static final float OPACITY_DISABLED = 0.40F;
    public static final float OPACITY_MUTED = 0.60F;
    public static final float OPACITY_SECONDARY = 0.75F;
    public static final float OPACITY_PRIMARY = 0.94F;
    public static final float OPACITY_FULL = 1.0F;
    public static final float OPACITY_OVERLAY = 0.50F;
    public static final float OPACITY_GLASS = 0.08F;

    public static final int PANEL_MAIN_WIDTH = 900;
    public static final int PANEL_MAIN_HEIGHT = 540;
    public static final int PANEL_NARROW_WIDTH = 600;
    public static final int PANEL_SIDEBAR_WIDTH = 220;
    public static final int PANEL_CONTENT_PADDING = 20;
    public static final int CARD_MIN_HEIGHT = 56;
    public static final int CARD_ELEVATED_HEIGHT = 72;

    public static final int BUTTON_HEIGHT_SM = 28;
    public static final int BUTTON_HEIGHT_MD = 36;
    public static final int BUTTON_HEIGHT_LG = 44;
    public static final int BUTTON_HEIGHT_XL = 52;
    public static final int BUTTON_ICON_SIZE = 20;
    public static final int BUTTON_PADDING_H = 16;
    public static final int BUTTON_PADDING_V = 8;

    public static final float FONT_SIZE_XS = 8.0F;
    public static final float FONT_SIZE_SM = 10.0F;
    public static final float FONT_SIZE_BASE = 12.0F;
    public static final float FONT_SIZE_LG = 14.0F;
    public static final float FONT_SIZE_XL = 18.0F;
    public static final float FONT_SIZE_2XL = 24.0F;
    public static final float FONT_SIZE_3XL = 32.0F;
    public static final float FONT_SIZE_4XL = 42.0F;

    public static final float LINE_HEIGHT_TIGHT = 1.2F;
    public static final float LINE_HEIGHT_NORMAL = 1.5F;
    public static final float LINE_HEIGHT_RELAXED = 1.75F;

    public static final float BLUR_NONE = 0.0F;
    public static final float BLUR_SM = 2.0F;
    public static final float BLUR_MD = 4.0F;
    public static final float BLUR_LG = 8.0F;
    public static final float BLUR_XL = 16.0F;

    public static final float GLOW_NONE = 0.0F;
    public static final float GLOW_SM = 0.10F;
    public static final float GLOW_MD = 0.18F;
    public static final float GLOW_LG = 0.28F;

    public static final int ICON_SIZE_XS = 12;
    public static final int ICON_SIZE_SM = 16;
    public static final int ICON_SIZE_MD = 20;
    public static final int ICON_SIZE_LG = 24;
    public static final int ICON_SIZE_XL = 32;
    public static final int ICON_SIZE_2XL = 48;

    public static final int GRID_COLUMNS = 12;
    public static final int GRID_GUTTER = 16;
    public static final int GRID_MARGIN = 24;

    public static float effectiveRadius() {
        return clamp(PvpClient.cornerRadius(), RADIUS_NONE, RADIUS_XL);
    }

    public static int scaledSpacing(int baseSpace) {
        return UiScale.s(baseSpace);
    }

    public static float scaledFontSize(float baseSize) {
        return baseSize * UiScale.getEffectiveScale() * PvpClient.fontScale();
    }

    public static int buttonHeight(String size) {
        return switch (size) {
            case "sm" -> BUTTON_HEIGHT_SM;
            case "lg" -> BUTTON_HEIGHT_LG;
            case "xl" -> BUTTON_HEIGHT_XL;
            default -> BUTTON_HEIGHT_MD;
        };
    }

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

    public static int responsiveWidth(int baseWidth, int screenWidth) {
        return Math.min(UiScale.s(baseWidth), Math.max(1, screenWidth - GRID_MARGIN * 2));
    }

    public static int responsiveHeight(int baseHeight, int screenHeight) {
        return Math.min(UiScale.s(baseHeight), Math.max(1, screenHeight - GRID_MARGIN * 2));
    }

    public static float fontScale() {
        return PvpClient.fontScale() * UiScale.getEffectiveScale();
    }

    private static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }
}
