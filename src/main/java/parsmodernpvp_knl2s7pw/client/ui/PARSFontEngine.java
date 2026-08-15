package parsmodernpvp_knl2s7pw.client.ui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import parsmodernpvp_knl2s7pw.client.PvpClient;

/**
 * PARS typography facade.
 *
 * Measurements and drawing always use the exact same effective scale so text
 * never drifts away from the component that contains it.
 */
public final class PARSFontEngine {
    private PARSFontEngine() {
    }

    public static void draw(GuiGraphicsExtractor graphics, String value, int x, int y,
                            int color, Token token, boolean shadow, boolean outline) {
        if (value == null || value.isEmpty()) {
            return;
        }
        Token safe = token == null ? Token.BODY : token;
        FontEngine.draw(
                graphics,
                value,
                x,
                y,
                color,
                effective(safe),
                safe.tracking,
                safe.weight,
                shadow,
                outline
        );
    }

    public static int width(String value, Token token) {
        if (value == null || value.isEmpty()) {
            return 0;
        }
        Token safe = token == null ? Token.BODY : token;
        return FontEngine.width(value, effective(safe), safe.tracking, safe.weight);
    }

    /** Returns the exact scale used by both width() and draw(). */
    public static float effective(Token token) {
        Token safe = token == null ? Token.BODY : token;
        return safe.scale * PvpClient.fontScale() * UiScale.getEffectiveScale();
    }

    public static int lineHeight(Token token) {
        return Math.max(5, Math.round(11.0F * effective(token)));
    }

    public static void centered(GuiGraphicsExtractor graphics, String value, int centerX,
                                int centerY, int color, Token token, boolean shadow,
                                boolean outline) {
        if (value == null || value.isEmpty()) {
            return;
        }

        Token safe = token == null ? Token.BODY : token;
        int width = width(value, safe);
        int height = lineHeight(safe);
        draw(graphics, value, centerX - width / 2, centerY - height / 2,
                color, safe, shadow, outline);
    }

    public static void clearCaches() {
        FontEngine.clearCache();
    }

    public static String activeFamily() {
        return "Vazirmatn / Minecraft Unicode fallback";
    }

    public enum Token {
        DISPLAY(1.12F, 0, FontEngine.Weight.PREMIUM),
        TITLE(0.94F, 0, FontEngine.Weight.BOLD),
        HEADING(0.82F, 0, FontEngine.Weight.SEMIBOLD),
        BODY(0.72F, 0, FontEngine.Weight.REGULAR),
        SMALL(0.64F, 0, FontEngine.Weight.MEDIUM),
        CAPTION(0.56F, 0, FontEngine.Weight.REGULAR),
        NUMERIC(0.70F, 0, FontEngine.Weight.SEMIBOLD);

        private final float scale;
        private final int tracking;
        private final FontEngine.Weight weight;

        Token(float scale, int tracking, FontEngine.Weight weight) {
            this.scale = scale;
            this.tracking = tracking;
            this.weight = weight;
        }
    }
}
