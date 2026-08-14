package parsmodernpvp_knl2s7pw.client.ui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import parsmodernpvp_knl2s7pw.client.PvpClient;

/**
 * Premium typography system with consistent hierarchy and Vazirmatn font integration.
 */
public final class UiTypography {
   private UiTypography() {
   }

   /**
    * Renders display text (largest, for main titles).
    */
   public static void display(GuiGraphicsExtractor g, String text, int x, int y, int color) {
      render(g, text, x, y, color, DesignTokens.FONT_SIZE_3XL, 1.0F, true);
   }

   /**
    * Renders title text (section headers).
    */
   public static void title(GuiGraphicsExtractor g, String text, int x, int y, int color) {
      render(g, text, x, y, color, DesignTokens.FONT_SIZE_2XL, 0.95F, true);
   }

   /**
    * Renders heading text (sub-section headers).
    */
   public static void heading(GuiGraphicsExtractor g, String text, int x, int y, int color) {
      render(g, text, x, y, color, DesignTokens.FONT_SIZE_XL, 0.85F, false);
   }

   /**
    * Renders body text (standard content).
    */
   public static void body(GuiGraphicsExtractor g, String text, int x, int y, int color) {
      render(g, text, x, y, color, DesignTokens.FONT_SIZE_BASE, PvpClient.fontScale(), false);
   }

   /**
    * Renders caption text (small descriptive text).
    */
   public static void caption(GuiGraphicsExtractor g, String text, int x, int y, int color) {
      render(g, text, x, y, color, DesignTokens.FONT_SIZE_SM, 0.6F, false);
   }

   /**
    * Renders numeric/monospace text (values, stats).
    */
   public static void numeric(GuiGraphicsExtractor g, String text, int x, int y, int color) {
      render(g, text, x, y, color, DesignTokens.FONT_SIZE_BASE, 0.75F, false);
   }

   /**
    * Renders button text.
    */
   public static void button(GuiGraphicsExtractor g, String text, int x, int y, int width, int height, int color) {
      PARSFontEngine.centered(g, text, x + width / 2, y + height / 2, color, PARSFontEngine.Token.SMALL, PvpClient.shadow(), false);
   }

   /**
    * General purpose text rendering with scale control.
    */
   public static void text(GuiGraphicsExtractor g, String text, int x, int y, int color, float scale, int alignment) {
      float adjustedScale = scale * PvpClient.fontScale();
      render(g, text, x, y, color, DesignTokens.FONT_SIZE_BASE, adjustedScale, false);
   }

   /**
    * Core render method.
    */
   private static void render(GuiGraphicsExtractor g, String text, int x, int y, int color, float baseSize, float scale, boolean bold) {
      if (text == null || text.isEmpty()) return;
      
      PARSFontEngine.Token token = getFontSizeToken(baseSize * scale);
      PARSFontEngine.draw(g, text, x, y, color, token, PvpClient.shadow() && !PvpClient.lowPerformance(), false);
   }

   /**
    * Gets appropriate font token based on size.
    */
   private static PARSFontEngine.Token getFontSizeToken(float size) {
      if (size >= 24.0F) return PARSFontEngine.Token.DISPLAY;
      if (size >= 18.0F) return PARSFontEngine.Token.TITLE;
      if (size >= 14.0F) return PARSFontEngine.Token.HEADING;
      if (size >= 11.0F) return PARSFontEngine.Token.BODY;
      if (size >= 9.0F) return PARSFontEngine.Token.SMALL;
      return PARSFontEngine.Token.CAPTION;
   }

   /**
    * Returns muted text color.
    */
   public static int mutedColor() {
      return -8220248;
   }

   /**
    * Returns secondary text color.
    */
   public static int secondaryColor() {
      return PvpClient.themeEngine().secondary();
   }

   /**
    * Returns primary text color.
    */
   public static int primaryColor() {
      return PvpClient.themeEngine().text();
   }
}
