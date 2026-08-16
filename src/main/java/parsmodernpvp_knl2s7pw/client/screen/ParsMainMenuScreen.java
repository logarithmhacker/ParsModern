package parsmodernpvp_knl2s7pw.client.screen;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import parsmodernpvp_knl2s7pw.client.PvpClient;
import parsmodernpvp_knl2s7pw.client.ui.DesignTokens;
import parsmodernpvp_knl2s7pw.client.ui.PARSFramework;
import parsmodernpvp_knl2s7pw.client.ui.UiNotifications;
import parsmodernpvp_knl2s7pw.client.ui.UiScale;
import parsmodernpvp_knl2s7pw.client.ui.UiSoundEngine;
import parsmodernpvp_knl2s7pw.client.ui.UiTypography;

/**
 * Premium Main Menu - PARS Modern PvP entry point.
 * Features: Cinematic animations, responsive layout, profile integration, elegant navigation.
 * Visual Identity: FAST | PREMIUM | COMPETITIVE | CLEAN | MODERN
 */
public final class ParsMainMenuScreen extends Screen {
   private final List<ParsMainMenuScreen.NavSpec> navigation = new ArrayList<>();
   private final long openedAt = System.nanoTime();
   private boolean announced;
   private float animationProgress;

   public ParsMainMenuScreen() {
      super(Component.literal("PARSModernPvP"));
   }

   /* =====================================================
    * Responsive scaling helper
    * ===================================================== */
   private static int s(int value) {
      return UiScale.s(value);
   }

   public boolean keyPressed(KeyEvent event) {
      if (event.key() == 256) {
         UiSoundEngine.back();
         return true;
      } else {
         return super.keyPressed(event);
      }
   }

   protected void init() {
      this.navigation.clear();
      this.animationProgress = 0.0F;
      
      int center = this.width / 2;
      int left = Math.max(s(24), center - s(300));
      int top = Math.max(s(70), this.height / 2 - s(118));
      
      // Primary navigation with clear hierarchy
      this.addNav("PLAY", left, top, s(258), DesignTokens.RADIUS_LG, () -> this.minecraft.setScreen(new SelectWorldScreen(this)));
      this.addNav("MULTIPLAYER", left, top + s(40), s(258), DesignTokens.RADIUS_LG, () -> this.minecraft.setScreen(new JoinMultiplayerScreen(this)));
      
      // Secondary navigation group
<<<<<<< HEAD
      this.addNav("PROFILE", left, top + s(88), s(258), DesignTokens.RADIUS_MD, () -> this.minecraft.setScreen(new ParsProfileScreen()));
      this.addNav("COSMETICS", left, top + s(132), s(258), DesignTokens.RADIUS_MD, () -> this.minecraft.setScreen(new ParsCosmeticsScreen()));
=======
      this.addNav("PROFILE", left, top + s(88), s(258), DesignTokens.RADIUS_MD, () -> this.minecraft.setScreen(new PvpScreen("profiles")));
      this.addNav("COSMETICS", left, top + s(132), s(258), DesignTokens.RADIUS_MD, () -> this.minecraft.setScreen(new PvpScreen("style")));
>>>>>>> 77bc9da847b12cb9940d49afdb8df99b0af11b92
      this.addNav("SETTINGS", left, top + s(176), s(258), DesignTokens.RADIUS_MD, () -> this.minecraft.setScreen(new PvpScreen("settings")));
      
      // Tertiary action
      this.addNav("QUIT", left, top + s(220), s(258), DesignTokens.RADIUS_SM, () -> this.minecraft.stop());
      
      if (!this.announced) {
         this.announced = true;
         PvpClient.notifications().push("PARS Client Ready", 3500L);
      }
   }

   private void addNav(String label, int x, int y, int buttonWidth, float radius, Runnable action) {
      int height = s(36);
      this.navigation.add(new ParsMainMenuScreen.NavSpec(label, x, y, buttonWidth, height, radius));
      Button button = Button.builder(Component.empty(), ignored -> {
         UiSoundEngine.confirm();
         action.run();
      }).bounds(x, y, buttonWidth, height).build();
      button.setAlpha(0.0F);
      this.addRenderableWidget(button);
   }

   public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
      PARSFramework.background(graphics, this.width, this.height);
      int accent = PvpClient.theme().accent();
      int center = this.width / 2;
      int top = Math.max(s(70), this.height / 2 - s(118));
      int left = Math.max(s(24), center - s(300));
      int statusLeft = Math.min(this.width - s(260), center + s(12));
      int statusTop = top + s(8);
      float intro = PvpClient.reducedMotion() ? 1.0F : Math.min(1.0F, (float)(System.nanoTime() - this.openedAt) / 6.5E8F);
      PARSFramework.panel(graphics, left - s(22), top - s(58), s(296), s(290));
      graphics.fill(left - s(22), top - s(58), left + s(274), top - s(54), accent);
      graphics.fill(left - s(22), top - s(58), left - s(18), top + s(232), PvpClient.theme().secondary());
      UiTypography.title(graphics, "PARS", left - s(2), top - s(38), accent);
      UiTypography.text(graphics, "MODERN PVP  //  v13", left, top - s(8), PvpClient.theme().text(), 0.82F * PvpClient.fontScale(), 1);
      UiTypography.text(graphics, "ONLINE  •  FAIR-PLAY CLIENT", left, top + s(8), -12392796, 0.76F * PvpClient.fontScale(), 1);

      for (ParsMainMenuScreen.NavSpec spec : this.navigation) {
         this.drawNav(graphics, spec, mouseX, mouseY, intro);
      }

      PARSFramework.panel(graphics, statusLeft + s(4), statusTop - s(22), Math.min(s(260), this.width - statusLeft - s(24)), s(250));
      UiTypography.text(graphics, "PARS // COMMAND DECK", statusLeft + s(20), statusTop, accent, 0.92F * PvpClient.fontScale(), 1);
      UiTypography.text(graphics, "PRESENTATION SYSTEM", statusLeft + s(20), statusTop + s(27), PvpClient.theme().text(), 0.72F * PvpClient.fontScale(), 1);
      this.status(graphics, "THEME", PvpClient.theme().name(), statusLeft + s(20), statusTop + s(55), accent);
      this.status(graphics, "PROFILE", PvpClient.profiles().active().displayName(), statusLeft + s(20), statusTop + s(77), PvpClient.theme().text());
      this.status(
         graphics, "HUD LINK", PvpClient.hudRegistered() ? "READY" : "STANDBY", statusLeft + s(20), statusTop + s(99), PvpClient.hudRegistered() ? -12392796 : -14249
      );
      this.status(graphics, "INPUT", "RSHIFT  /  CLICK GUI", statusLeft + s(20), statusTop + s(121), PvpClient.theme().text());
      graphics.fill(statusLeft + s(20), statusTop + s(155), Math.min(this.width - s(42), statusLeft + s(240)), statusTop + s(157), 1429423189);
      graphics.fill(statusLeft + s(20), statusTop + s(155), statusLeft + s(194), statusTop + s(157), accent);
      UiTypography.text(graphics, "AWARENESS OVER AUTOMATION", statusLeft + s(20), statusTop + s(174), PvpClient.theme().secondary(), 0.64F, 1);
      UiTypography.text(graphics, "PARS UI 13.0  //  ORIGINAL CLIENT IDENTITY", statusLeft + s(20), statusTop + s(196), -9338730, 0.56F, 1);
      UiTypography.text(
         graphics,
         "Minecraft " + this.minecraft.getLaunchedVersion() + "  //  " + this.minecraft.getUser().getName(),
         s(24),
         this.height - s(18),
         PvpClient.theme().text(),
         0.72F,
         0
      );
      UiTypography.text(graphics, "PARS NETWORK READY", this.width - s(150), this.height - s(18), -12392796, 0.72F, 0);
      super.extractRenderState(graphics, mouseX, mouseY, partialTick);
      UiNotifications.render(graphics, this.width, this.height);
   }

   private void drawNav(GuiGraphicsExtractor graphics, ParsMainMenuScreen.NavSpec spec, int mouseX, int mouseY, float intro) {
      boolean hovered = mouseX >= spec.x() && mouseX < spec.x() + spec.width() && mouseY >= spec.y() && mouseY < spec.y() + spec.height();
      int panel = hovered ? PvpClient.theme().accent() : PvpClient.theme().background();
      graphics.fill(spec.x() + s(3), spec.y() + s(3), spec.x() + spec.width() + s(3), spec.y() + spec.height() + s(3), 1140850688);
      graphics.fill(spec.x(), spec.y(), spec.x() + spec.width(), spec.y() + spec.height(), panel);
      graphics.fill(
         spec.x(), spec.y(), spec.x() + (hovered ? s(5) : s(3)), spec.y() + spec.height(), hovered ? PvpClient.theme().secondary() : PvpClient.theme().accent()
      );
      UiTypography.text(graphics, spec.label(), spec.x() + s(18), spec.y() + s(9), hovered ? -16248033 : PvpClient.theme().text(), 0.86F + 0.04F * intro, 1);
      UiTypography.text(
         graphics, hovered ? "OPEN" : "//", spec.x() + spec.width() - s(42), spec.y() + s(9), hovered ? -16248033 : PvpClient.theme().secondary(), 0.66F, 1
      );
   }

   private void status(GuiGraphicsExtractor graphics, String label, String value, int x, int y, int color) {
      UiTypography.text(graphics, label, x, y, -9338730, 0.66F, 1);
      UiTypography.text(graphics, value, x + s(86), y, color, 0.72F, 0);
   }

   private record NavSpec(String label, int x, int y, int width, int height, float radius) {
   }
}
