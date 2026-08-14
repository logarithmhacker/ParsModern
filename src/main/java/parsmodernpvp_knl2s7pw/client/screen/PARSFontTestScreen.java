package parsmodernpvp_knl2s7pw.client.screen;

import java.util.Locale;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import parsmodernpvp_knl2s7pw.client.PvpClient;
import parsmodernpvp_knl2s7pw.client.ui.PARSFontEngine;
import parsmodernpvp_knl2s7pw.client.ui.PARSFramework;
import parsmodernpvp_knl2s7pw.client.ui.UiScale;
import parsmodernpvp_knl2s7pw.client.ui.UiTypography;

public final class PARSFontTestScreen extends Screen {
   public PARSFontTestScreen() {
      super(Component.literal("PARS Font Test"));
   }

   /*
    * Applies the central UI scale to a layout value.
    */
   private static int s(int value) {
      return UiScale.s(value);
   }

   public boolean keyPressed(KeyEvent event) {
      if (event.key() == 256) {
         this.minecraft.setScreen(new PvpScreen("style"));
         return true;
      } else {
         return super.keyPressed(event);
      }
   }

   public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float partialTick) {
      PARSFramework.background(g, this.width, this.height);
      int w = Math.min(s(640), this.width - s(32));
      int h = s(330);
      int left = this.width / 2 - w / 2;
      int top = this.height / 2 - h / 2;
      PARSFramework.panel(g, left, top, w, h);
      UiTypography.title(g, "PARS FONT LAB", left + s(24), top + s(20), PvpClient.theme().accent());
      UiTypography.text(g, "Vazirmatn-compatible Unicode run // live weight + scale", left + s(26), top + s(48), PvpClient.themeEngine().mutedText(), 0.68F, 0);
      int x = left + s(28);
      PARSFontEngine.draw(g, "English typography preview", x, top + s(82), PvpClient.theme().text(), PARSFontEngine.Token.H1, true, false);
      PARSFontEngine.draw(g, "فارسی زیبا و خوانا", x, top + s(122), PvpClient.theme().secondary(), PARSFontEngine.Token.H1, true, false);
      PARSFontEngine.draw(g, "عربي: اختبار تشكيل الحروف", x, top + s(162), PvpClient.theme().text(), PARSFontEngine.Token.H2, false, false);
      PARSFontEngine.draw(g, "123456789  0.42  +100  → ← ↑ ↓ ★ ✓", x, top + s(198), PvpClient.theme().accent(), PARSFontEngine.Token.NUMERIC, true, false);
      PARSFontEngine.draw(g, "PARS سلام 123  //  Mixed: فارسی + English + عربي", x, top + s(234), PvpClient.theme().text(), PARSFontEngine.Token.BODY, true, true);
      UiTypography.text(
         g,
         "ACTIVE WEIGHT  " + PvpClient.fontWeight() + "   SCALE  " + String.format(Locale.ROOT, "%.2f", PvpClient.fontScale()),
         x,
         top + s(274),
         PvpClient.themeEngine().mutedText(),
         0.64F,
         1
      );
      UiTypography.text(g, "ESC  BACK TO STYLE LAB", x, top + h - s(22), PvpClient.themeEngine().mutedText(), 0.58F, 1);
      super.extractRenderState(g, mouseX, mouseY, partialTick);
   }
}
