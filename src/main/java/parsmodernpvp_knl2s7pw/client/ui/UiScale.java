package parsmodernpvp_knl2s7pw.client.ui;

import net.minecraft.client.Minecraft;
import parsmodernpvp_knl2s7pw.client.PvpClient;

public final class UiScale {

    /**
     * Global UI scale.
     *
     * 1.00 = normal
     * 0.90 = 10% smaller
     * 0.80 = 20% smaller
     * 0.75 = 25% smaller
     * 0.50 = 50% smaller
     */
    private static final float GLOBAL_SCALE = 0.65F;

    private static float cachedScale = -1.0F;

    public static final float DEFAULT = 1.0F;

    private UiScale() {
    }

    public static int s(int value) {
        return Math.round(value * getEffectiveScale());
    }

    public static float s(float value) {
        return value * getEffectiveScale();
    }

    public static float getEffectiveScale() {
        if (cachedScale < 0.0F) {
            cachedScale = calculateEffectiveScale();
        }

        return cachedScale;
    }

    public static void invalidateCache() {
        cachedScale = -1.0F;
    }

    private static float calculateEffectiveScale() {
        float userScale = Math.max(
                0.5F,
                Math.min(
                        2.0F,
                        PvpClient.uiScale()
                )
        );

        return userScale * GLOBAL_SCALE;
    }

    public static float scaleFont(float baseSize) {
        return baseSize
                * getEffectiveScale()
                * PvpClient.fontScale();
    }

    public static int toScreenSpace(int uiValue) {
        float scale = getEffectiveScale();

        if (scale <= 0.0F) {
            return uiValue;
        }

        return Math.round(
                uiValue / scale
        );
    }

    public static int iconSize(String context) {
        return switch (context) {
            case "small" -> s(16);
            case "medium", "default" -> s(20);
            case "large" -> s(24);
            case "xl" -> s(32);
            default -> s(20);
        };
    }

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