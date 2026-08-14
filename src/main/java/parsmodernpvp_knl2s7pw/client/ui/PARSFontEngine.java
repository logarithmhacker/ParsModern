package parsmodernpvp_knl2s7pw.client.ui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import parsmodernpvp_knl2s7pw.client.PvpClient;

public final class PARSFontEngine {
   private PARSFontEngine() {
   }

   public static void draw(GuiGraphicsExtractor graphics, String value, int x, int y, int color, PARSFontEngine.Token token, boolean shadow, boolean outline) {
      PARSFontEngine.Token safe = token == null ? PARSFontEngine.Token.BODY : token;
      FontEngine.draw(graphics, value, x, y, color, effective(safe), safe.tracking, safe.weight, shadow, outline);
   }

   public static int width(String value, PARSFontEngine.Token token) {
      PARSFontEngine.Token safe = token == null ? PARSFontEngine.Token.BODY : token;
      return FontEngine.width(value, effective(safe), safe.tracking, safe.weight);
   }

   /*
    * Effective pixel scale for a token: token multiplier combined
    * with the user font scale. Rendering and width() both go
    * through this method so measurement always matches drawing.
    */
   public static float effective(PARSFontEngine.Token token) {
      PARSFontEngine.Token safe = token == null ? PARSFontEngine.Token.BODY : token;
      return safe.scale * PvpClient.fontScale();
   }

   /*
    * Approximate rendered line height for a token, used to center
    * text vertically inside buttons and panels. The TTF is rendered
    * at size 11, so the visual extent is roughly 11 * effective.
    */
   public static int lineHeight(PARSFontEngine.Token token) {
      return Math.max(6, Math.round(11.0F * effective(token)));
   }

   /*
    * Draws text centered on both axes around (centerX, centerY).
    * Horizontally it is centered on the measured width and
    * vertically on the token line height, so alignment is exact.
    */
   public static void centered(GuiGraphicsExtractor graphics, String value, int centerX, int centerY, int color, PARSFontEngine.Token token, boolean shadow, boolean outline) {
      if (value == null || value.isEmpty()) {
         return;
      }

      PARSFontEngine.Token safe = token == null ? PARSFontEngine.Token.BODY : token;
      int w = width(value, safe);
      int h = lineHeight(safe);
      draw(graphics, value, centerX - w / 2, centerY - h / 2, color, safe, shadow, outline);
   }

   public static void clearCaches() {
      FontEngine.clearCache();
   }

   public static String activeFamily() {
      return "Vazirmatn / Minecraft Unicode fallback";
   }

   /*
    * Typography hierarchy for the compact PARS UI.
    *
    * Vazirmatn is registered as an 11px TTF, so a 1.0 multiplier is
    * noticeably larger than vanilla text. These multipliers keep a
    * clear hierarchy while fitting compact panels.
    */
   public enum Token {
      DISPLAY(1.15F, 0, FontEngine.Weight.PREMIUM),
      TITLE(0.92F, 0, FontEngine.Weight.BOLD),
      HEADING(0.8F, 0, FontEngine.Weight.SEMIBOLD),
      BODY(0.72F, 0, FontEngine.Weight.REGULAR),
      SMALL(0.65F, 0, FontEngine.Weight.MEDIUM),
      CAPTION(0.58F, 0, FontEngine.Weight.REGULAR),
      NUMERIC(0.7F, 0, FontEngine.Weight.SEMIBOLD);

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
