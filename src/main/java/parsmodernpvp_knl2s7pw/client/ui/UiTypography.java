package parsmodernpvp_knl2s7pw.client.ui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import parsmodernpvp_knl2s7pw.client.PvpClient;

/** Consistent PARS text hierarchy and alignment helpers. */
public final class UiTypography {
    private UiTypography() {
    }

    public static void display(GuiGraphicsExtractor g, String text, int x, int y, int color) {
        render(g, text, x, y, color, DesignTokens.FONT_SIZE_3XL, 1.0F, true);
    }

    public static void title(GuiGraphicsExtractor g, String text, int x, int y, int color) {
        render(g, text, x, y, color, DesignTokens.FONT_SIZE_2XL, 0.95F, true);
    }

    public static void centeredTitle(GuiGraphicsExtractor g, String text, int centerX, int y, int color) {
        centered(g, text, centerX, y, color, DesignTokens.FONT_SIZE_2XL, 0.95F);
    }

    public static void centered(GuiGraphicsExtractor g, String text, int centerX, int centerY,
                                int color, float size, float scale) {
        PARSFontEngine.Token token = getFontSizeToken(size * scale);
        PARSFontEngine.centered(
                g,
                text,
                centerX,
                centerY,
                color,
                token,
                PvpClient.shadow() && !PvpClient.lowPerformance(),
                false
        );
    }

    public static void centered(GuiGraphicsExtractor g, String text, int centerX, int centerY,
                                int color, float scale, int alignment) {
        centered(g, text, centerX, centerY, color, DesignTokens.FONT_SIZE_BASE, scale);
    }

    public static void centered(GuiGraphicsExtractor g, String text, int centerX, int centerY,
                                int color, float size) {
        centered(g, text, centerX, centerY, color, size, 1.0F);
    }

    public static void heading(GuiGraphicsExtractor g, String text, int x, int y, int color) {
        render(g, text, x, y, color, DesignTokens.FONT_SIZE_XL, 0.85F, false);
    }

    public static void body(GuiGraphicsExtractor g, String text, int x, int y, int color) {
        render(g, text, x, y, color, DesignTokens.FONT_SIZE_BASE, 1.0F, false);
    }

    public static void caption(GuiGraphicsExtractor g, String text, int x, int y, int color) {
        render(g, text, x, y, color, DesignTokens.FONT_SIZE_SM, 0.72F, false);
    }

    public static void numeric(GuiGraphicsExtractor g, String text, int x, int y, int color) {
        render(g, text, x, y, color, DesignTokens.FONT_SIZE_BASE, 0.82F, false);
    }

    public static void button(GuiGraphicsExtractor g, String text, int x, int y,
                              int width, int height, int color) {
        PARSFontEngine.centered(
                g,
                text,
                x + width / 2,
                y + height / 2,
                color,
                PARSFontEngine.Token.SMALL,
                PvpClient.shadow(),
                false
        );
    }

    public static void label(GuiGraphicsExtractor g, String text, int x, int y, int color) {
        render(g, text, x, y, color, DesignTokens.FONT_SIZE_SM, 0.78F, false);
    }

    public static void text(GuiGraphicsExtractor g, String text, int x, int y, int color,
                            float scale, int alignment) {
        render(g, text, x, y, color, DesignTokens.FONT_SIZE_BASE, scale, false);
    }

    private static void render(GuiGraphicsExtractor g, String text, int x, int y, int color,
                               float baseSize, float scale, boolean bold) {
        if (text == null || text.isEmpty()) {
            return;
        }

        PARSFontEngine.Token token = getFontSizeToken(baseSize * scale);
        PARSFontEngine.draw(
                g,
                text,
                x,
                y,
                color,
                token,
                PvpClient.shadow() && !PvpClient.lowPerformance(),
                false
        );
    }

    private static PARSFontEngine.Token getFontSizeToken(float size) {
        if (size >= 24.0F) return PARSFontEngine.Token.DISPLAY;
        if (size >= 18.0F) return PARSFontEngine.Token.TITLE;
        if (size >= 14.0F) return PARSFontEngine.Token.HEADING;
        if (size >= 11.0F) return PARSFontEngine.Token.BODY;
        if (size >= 9.0F) return PARSFontEngine.Token.SMALL;
        return PARSFontEngine.Token.CAPTION;
    }

    public static int mutedColor() {
        return 0xFF7F8AA0;
    }

    public static int secondaryColor() {
        return PvpClient.themeEngine().secondary();
    }

    public static int primaryColor() {
        return PvpClient.themeEngine().text();
    }
}
