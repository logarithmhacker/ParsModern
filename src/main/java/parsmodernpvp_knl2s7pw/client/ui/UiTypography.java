package parsmodernpvp_knl2s7pw.client.ui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import parsmodernpvp_knl2s7pw.client.PvpClient;

public final class UiTypography {
   private UiTypography() {
   }

   public static Component component(String value) {
      return FontEngine.component(value, FontEngine.Weight.REGULAR);
   }

   public static void text(GuiGraphicsExtractor graphics, String value, int x, int y, int color) {
      text(graphics, value, x, y, color, 1.0F, 1);
   }

   public static void text(GuiGraphicsExtractor graphics, String value, int x, int y, int color, float scale, int tracking) {
      FontEngine.draw(graphics, value, x, y, color, scale, tracking, FontEngine.Weight.REGULAR, PvpClient.shadow(), false);
   }

   public static void centered(GuiGraphicsExtractor graphics, String value, int center, int y, int color, float scale, int tracking) {
      int width = width(value, scale, tracking);
      text(graphics, value, center - width / 2, y, color, scale, tracking);
   }

   public static int width(String value, float scale, int tracking) {
      return FontEngine.width(value, scale, tracking, FontEngine.Weight.REGULAR);
   }

   public static void title(GuiGraphicsExtractor graphics, String value, int x, int y, int color) {
      PARSFontEngine.draw(graphics, value, x, y, color, PARSFontEngine.Token.DISPLAY, PvpClient.shadow(), PvpClient.glow());
   }

   public static void label(GuiGraphicsExtractor graphics, String value, int x, int y, int color) {
      PARSFontEngine.draw(graphics, value, x, y, color, PARSFontEngine.Token.SMALL, PvpClient.shadow(), false);
   }

   public static void body(GuiGraphicsExtractor graphics, String value, int x, int y, int color) {
      PARSFontEngine.draw(graphics, value, x, y, color, PARSFontEngine.Token.BODY, PvpClient.shadow(), false);
   }

   public static void centeredTitle(GuiGraphicsExtractor graphics, String value, int center, int y, int color) {
      int width = PARSFontEngine.width(value, PARSFontEngine.Token.DISPLAY);
      PARSFontEngine.draw(graphics, value, center - width / 2, y, color, PARSFontEngine.Token.DISPLAY, PvpClient.shadow(), PvpClient.glow());
   }
}
