package parsmodernpvp_knl2s7pw.client.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import parsmodernpvp_knl2s7pw.client.PvpClient;
import parsmodernpvp_knl2s7pw.client.animation.PARSAnimationEngine;
import parsmodernpvp_knl2s7pw.client.ui.PARSFramework;
import parsmodernpvp_knl2s7pw.client.ui.UiScale;
import parsmodernpvp_knl2s7pw.client.ui.UiSoundEngine;
import parsmodernpvp_knl2s7pw.client.ui.UiTypography;

public final class ParsLoadingScreen extends Screen {
   private static final long DURATION_NANOS = 3200000000L;
   private static final String[] STAGES = new String[]{"INITIALIZING", "FONTS", "THEMES", "MODULES", "HUD", "CONFIG", "STARTING PARS"};
   private final Screen next;
   private final long started = System.nanoTime();
   private boolean played;

   public ParsLoadingScreen(Screen next) {
      super(Component.literal("PARS Modern PvP Loading"));
      this.next = next;
   }

   /*
    * Applies the central UI scale to a layout value.
    */
   private static int s(int value) {
      return UiScale.s(value);
   }

   public void tick() {
      if (!this.played) {
         this.played = true;
         UiSoundEngine.startup();
      }

      if (PvpClient.reducedMotion() || System.nanoTime() - this.started >= 3200000000L) {
         Minecraft.getInstance().setScreen(this.next);
      }
   }

   public boolean keyPressed(KeyEvent event) {
      if (event.key() != 256 && event.key() != 32) {
         return super.keyPressed(event);
      }

      Minecraft.getInstance().setScreen(this.next);
      return true;
   }

   public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
      float progress = PARSAnimationEngine.progress(this.started, 3200000000L, PARSAnimationEngine.Easing.EASE_IN_OUT, PvpClient.reducedMotion());
      int w = graphics.guiWidth();
      int h = graphics.guiHeight();
      int cx = w / 2;
      PARSFramework.background(graphics, w, h);
      float blackFade = Math.max(0.0F, 1.0F - progress * 3.5F);
      if (blackFade > 0.0F) {
         graphics.fill(0, 0, w, h, (int)(blackFade * 235.0F) << 24);
      }

      int panelWidth = Math.min(s(520), Math.max(s(320), w - s(80)));
      int panelLeft = cx - panelWidth / 2;
      int panelTop = h / 2 - s(92);
      PARSFramework.panel(graphics, panelLeft, panelTop, panelWidth, s(187));
      int sweepX = panelLeft - s(60) + (int)((panelWidth + s(120)) * progress);
      graphics.fill(sweepX, panelTop + s(8), sweepX + s(18), panelTop + s(77), 857459455);
      UiTypography.centeredTitle(graphics, "PARS", cx, panelTop + s(24), PvpClient.theme().accent());
      UiTypography.centered(
         graphics, "PARS MODERN PVP  //  " + this.minecraftVersion(), cx, panelTop + s(55), PvpClient.theme().text(), 0.82F * PvpClient.fontScale()
      );
      PARSFramework.progress(graphics, panelLeft + s(42), panelTop + s(91), panelWidth - s(84), 8, progress);
      int stage = Math.min(STAGES.length - 1, (int)(progress * STAGES.length));
      UiTypography.centered(
         graphics, STAGES[stage] + "   " + Math.round(progress * 100.0F) + "%", cx, panelTop + s(112), PvpClient.theme().text(), 0.9F * PvpClient.fontScale()
      );

      for (int i = 0; i < STAGES.length; i++) {
         int x = panelLeft + s(42) + i * ((panelWidth - s(84)) / STAGES.length);
         graphics.fill(x, panelTop + s(132), x + s(20), panelTop + s(135), i <= stage ? PvpClient.theme().secondary() : 1429423189);
      }

      UiTypography.centered(graphics, "SPACE / ESC  SKIP", cx, h - s(26), -7562577, 0.8F * PvpClient.fontScale());
      super.extractRenderState(graphics, mouseX, mouseY, partialTick);
   }

   private String minecraftVersion() {
      return Minecraft.getInstance().getLaunchedVersion();
   }
}
