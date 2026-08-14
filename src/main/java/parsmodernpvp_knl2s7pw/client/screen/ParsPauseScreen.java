package parsmodernpvp_knl2s7pw.client.screen;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import parsmodernpvp_knl2s7pw.client.PvpClient;
import parsmodernpvp_knl2s7pw.client.ui.PARSFramework;
import parsmodernpvp_knl2s7pw.client.ui.UiScale;
import parsmodernpvp_knl2s7pw.client.ui.UiSoundEngine;
import parsmodernpvp_knl2s7pw.client.ui.UiTypography;

public final class ParsPauseScreen extends Screen {
   public ParsPauseScreen() {
      super(Component.literal("PARS Pause"));
   }

   /*
    * Applies the central UI scale to a layout value.
    */
   private static int s(int value) {
      return UiScale.s(value);
   }

   protected void init() {
      int w = s(300);
      int h = s(238);
      int left = this.width / 2 - w / 2;
      int top = this.height / 2 - h / 2;
      this.addButton("RESUME", left + s(24), top + s(64), () -> this.minecraft.setScreen(null));
      this.addButton("OPTIONS", left + s(24), top + s(98), () -> this.minecraft.setScreen(new PvpScreen("settings")));
      this.addButton("PROFILES", left + s(24), top + s(132), () -> this.minecraft.setScreen(new PvpScreen("profiles")));
      this.addButton("COSMETICS", left + s(24), top + s(166), () -> this.minecraft.setScreen(new PvpScreen("style")));
      this.addButton("PARS SETTINGS", left + s(158), top + s(64), () -> this.minecraft.setScreen(new PvpScreen("settings")));
      this.addButton("DISCONNECT", left + s(158), top + s(98), () -> this.minecraft.disconnect(new ParsMainMenuScreen(), true));
   }

   private void addButton(String label, int x, int y, Runnable action) {
      Button button = Button.builder(Component.empty(), ignored -> {
         UiSoundEngine.click();
         action.run();
      }).bounds(x, y, s(118), s(25)).build();
      button.setAlpha(0.0F);
      this.addRenderableWidget(button);
   }

   public boolean keyPressed(KeyEvent event) {
      if (event.key() == 256) {
         this.minecraft.setScreen(null);
         UiSoundEngine.back();
         return true;
      } else {
         return super.keyPressed(event);
      }
   }

   public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float partialTick) {
      PARSFramework.background(g, this.width, this.height);
      int w = s(300);
      int h = s(238);
      int left = this.width / 2 - w / 2;
      int top = this.height / 2 - h / 2;
      PARSFramework.panel(g, left, top, w, h);
      UiTypography.title(g, "PARS", left + s(24), top + s(18), PvpClient.themeEngine().accent());
      UiTypography.label(g, "PAUSE // SAFE SESSION CONTROL", left + s(26), top + s(42), PvpClient.themeEngine().mutedText());
      this.drawButtons(g, left, top, mouseX, mouseY);
      super.extractRenderState(g, mouseX, mouseY, partialTick);
   }

   private void drawButtons(GuiGraphicsExtractor g, int left, int top, int mx, int my) {
      String[] labels = new String[]{"RESUME", "OPTIONS", "PROFILES", "COSMETICS", "PARS SETTINGS", "DISCONNECT"};

      for (int i = 0; i < labels.length; i++) {
         int x = left + (i >= 4 ? s(158) : s(24));
         int y = top + s(64) + (i >= 4 ? (i - 4) * s(34) : i * s(34));
         boolean hover = mx >= x && mx < x + s(118) && my >= y && my < y + s(25);
         PARSFramework.button(g, labels[i], x, y, s(118), s(25), hover, false);
      }

      UiTypography.text(g, "ESC  RESUME", left + s(24), top + s(213), PvpClient.themeEngine().mutedText(), 0.62F, 1);
   }
}
