package parsmodernpvp_knl2s7pw.client.screen;

import java.util.List;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import parsmodernpvp_knl2s7pw.client.PvpClient;
import parsmodernpvp_knl2s7pw.client.cosmetics.Cosmetic;
import parsmodernpvp_knl2s7pw.client.profile.Profile;
import parsmodernpvp_knl2s7pw.client.ui.DesignTokens;
import parsmodernpvp_knl2s7pw.client.ui.PARSFramework;
import parsmodernpvp_knl2s7pw.client.ui.UiScale;
import parsmodernpvp_knl2s7pw.client.ui.UiSoundEngine;
import parsmodernpvp_knl2s7pw.client.ui.UiTypography;

/** Clean, structured PARS profile hub with real player preview. */
public final class ParsProfileScreen extends Screen {
   private static final String[] TABS = {"OVERVIEW", "STATISTICS", "COSMETICS", "ACHIEVEMENTS"};
   private final Profile profile;
   private String tab = "OVERVIEW";
   private long openedAt;

   // Backend-ready values retained from the existing profile model.
   private final int level = 42;
   private final int xpCurrent = 2840;
   private final int xpMax = 5000;
   private final int wins = 127;
   private final int losses = 89;
   private final int kills = 3421;
   private final int deaths = 1876;
   private final int playtimeHours = 156;
   private final String favoriteMode = "Crystal PvP";

   public ParsProfileScreen() { this(PvpClient.profiles().active()); }

   public ParsProfileScreen(Profile profile) {
      super(Component.literal("PARS Profile"));
      this.profile = profile == null ? PvpClient.profiles().active() : profile;
      this.openedAt = System.nanoTime();
   }

   private static int s(int v) { return UiScale.s(v); }
   private int panelW() { return Math.min(s(980), this.width - s(28)); }
   private int panelH() { return Math.min(s(560), this.height - s(28)); }
   private int left() { return (this.width - panelW()) / 2; }
   private int top() { return (this.height - panelH()) / 2; }

   @Override
   protected void init() {
      int l = left(), t = top();
      for (int i = 0; i < TABS.length; i++) {
         final String target = TABS[i];
         Button button = Button.builder(Component.empty(), b -> {
            tab = target;
            openedAt = System.nanoTime();
            UiSoundEngine.click();
         }).bounds(l + s(24 + i * 150), t + s(58), s(138), s(30)).build();
         button.setAlpha(0.0F);
         addRenderableWidget(button);
      }
      Button cosmetics = Button.builder(Component.empty(), b -> {
         UiSoundEngine.open();
         minecraft.setScreen(new ParsCosmeticsScreen());
      }).bounds(l + panelW() - s(208), t + s(18), s(88), s(28)).build();
      cosmetics.setAlpha(0.0F);
      addRenderableWidget(cosmetics);
      Button close = Button.builder(Component.empty(), b -> onClose())
         .bounds(l + panelW() - s(108), t + s(18), s(88), s(28)).build();
      close.setAlpha(0.0F);
      addRenderableWidget(close);
   }

   @Override
   public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float partialTick) {
      PARSFramework.background(g, width, height);
      int l = left(), t = top(), w = panelW(), h = panelH();
      PARSFramework.panel(g, l, t, w, h);
      int accent = PvpClient.themeEngine().accent();
      g.fill(l, t, l + w, t + s(4), accent);

      UiTypography.title(g, "PROFILE", l + s(24), t + s(18), accent);
      UiTypography.caption(g, profile.displayName() + "  •  " + profile.id().toUpperCase(Locale.ROOT), l + s(24), t + s(40), PvpClient.themeEngine().muted());
      renderTopButtons(g, l, t);
      renderTabs(g, l, t);

      int bodyY = t + s(104);
      renderIdentity(g, l + s(24), bodyY, s(270), h - s(128), partialTick);
      int contentX = l + s(314);
      int contentW = w - s(338);
      switch (tab) {
         case "OVERVIEW" -> renderOverview(g, contentX, bodyY, contentW);
         case "STATISTICS" -> renderStatistics(g, contentX, bodyY, contentW);
         case "COSMETICS" -> renderCosmetics(g, contentX, bodyY, contentW);
         case "ACHIEVEMENTS" -> renderAchievements(g, contentX, bodyY, contentW);
      }
      super.extractRenderState(g, mouseX, mouseY, partialTick);
   }

   private void renderTopButtons(GuiGraphicsExtractor g, int l, int t) {
      int right = l + panelW() - s(24);
      UiTypography.label(g, "COSMETICS", right - s(178), t + s(27), PvpClient.themeEngine().secondary());
      UiTypography.label(g, "CLOSE", right - s(82), t + s(27), PvpClient.themeEngine().secondary());
   }

   private void renderTabs(GuiGraphicsExtractor g, int l, int t) {
      for (int i = 0; i < TABS.length; i++) {
         int x = l + s(24 + i * 150), y = t + s(58), w = s(138);
         boolean active = TABS[i].equals(tab);
         g.fill(x, y, x + w, y + s(30), active ? PvpClient.themeEngine().color("hover") : PvpClient.themeEngine().color("card"));
         if (active) g.fill(x, y + s(27), x + w, y + s(30), PvpClient.themeEngine().accent());
         UiTypography.centered(g, TABS[i], x + w / 2, y + s(9), active ? PvpClient.themeEngine().text() : PvpClient.themeEngine().secondary(), 0.60F, 1);
      }
   }

   private void renderIdentity(GuiGraphicsExtractor g, int x, int y, int w, int h, float partialTick) {
      PARSFramework.card(g, x, y, w, h);
      UiTypography.heading(g, profile.displayName(), x + s(18), y + s(18), PvpClient.themeEngine().text());
      UiTypography.caption(g, "PARS IDENTITY", x + s(18), y + s(40), PvpClient.themeEngine().muted());

      int previewTop = y + s(58);
      int previewBottom = Math.min(y + s(272), y + h - s(118));
      int pl = x + s(12), pr = x + w - s(12);
      g.fill(pl, previewTop, pr, previewBottom, PvpClient.themeEngine().color("background"));
      if (minecraft != null && minecraft.player != null) {
         EntityRenderDispatcher dispatcher = minecraft.getEntityRenderDispatcher();
         var state = dispatcher.extractEntity(minecraft.player, partialTick);
         float angle = (float)(System.nanoTime() / 1_800_000_000.0);
         g.entity(state, 4.0F, new Vector3f(0.0F, 0.72F, 0.0F), new Quaternionf().rotationY(-angle), new Quaternionf(), pl, previewTop, pr, previewBottom);
      }

      int barY = y + h - s(92);
      g.fill(x + s(18), barY, x + w - s(18), barY + s(8), PvpClient.themeEngine().color("card"));
      int fill = (int)((w - s(36)) * ((double)xpCurrent / xpMax));
      g.fill(x + s(18), barY, x + s(18) + fill, barY + s(8), PvpClient.themeEngine().accent());
      UiTypography.label(g, "LEVEL " + level, x + s(18), barY - s(20), PvpClient.themeEngine().text());
      UiTypography.caption(g, xpCurrent + " / " + xpMax + " XP", x + w - s(90), barY - s(20), PvpClient.themeEngine().muted());
      UiTypography.caption(g, PvpClient.cosmetics().getAllEquipped().size() + " equipped cosmetics", x + s(18), y + h - s(52), PvpClient.themeEngine().secondary());
   }

   private void renderOverview(GuiGraphicsExtractor g, int x, int y, int w) {
      float reveal = PvpClient.reducedMotion() ? 1F : Math.min(1F, (System.nanoTime() - openedAt) / 3.5E8F);
      UiTypography.heading(g, "WELCOME BACK", x, y + (int)(s(12) * reveal), PvpClient.themeEngine().text());
      UiTypography.body(g, "A clean snapshot of your current PvP identity.", x, y + s(32), PvpClient.themeEngine().secondary());
      card(g, x, y + s(62), w, 92, "MATCH PERFORMANCE", new String[][]{
         {"WINS", String.valueOf(wins)}, {"LOSSES", String.valueOf(losses)},
         {"K/D", String.format(Locale.ROOT, "%.2f", (double)kills / Math.max(1, deaths))},
         {"WIN RATE", String.format(Locale.ROOT, "%.1f%%", wins * 100.0 / Math.max(1, wins + losses))}
      });
      card(g, x, y + s(168), w, 92, "ACTIVITY", new String[][]{
         {"KILLS", String.valueOf(kills)}, {"DEATHS", String.valueOf(deaths)},
         {"PLAYTIME", playtimeHours + "h"}, {"FAV MODE", favoriteMode}
      });
      card(g, x, y + s(274), w, 92, "ACTIVE PROFILE", new String[][]{
         {"PROFILE", profile.displayName()}, {"THEME", PvpClient.theme().name()},
         {"HUD", PvpClient.hudRegistered() ? "READY" : "IDLE"}, {"COSMETICS", String.valueOf(PvpClient.cosmetics().getAllEquipped().size())}
      });
   }

   private void renderStatistics(GuiGraphicsExtractor g, int x, int y, int w) {
      UiTypography.heading(g, "STATISTICS", x, y + s(12), PvpClient.themeEngine().text());
      UiTypography.caption(g, "Local profile metrics currently tracked by PARS.", x, y + s(34), PvpClient.themeEngine().muted());
      card(g, x, y + s(58), w, 96, "COMBAT", new String[][]{{"KILLS", String.valueOf(kills)}, {"DEATHS", String.valueOf(deaths)}, {"K/D", String.format(Locale.ROOT, "%.2f", (double)kills / Math.max(1, deaths))}, {"WINS", String.valueOf(wins)}});
      card(g, x, y + s(170), w, 96, "MATCHES", new String[][]{{"LOSSES", String.valueOf(losses)}, {"WIN RATE", String.format(Locale.ROOT, "%.1f%%", wins * 100.0 / Math.max(1, wins + losses))}, {"PLAYTIME", playtimeHours + "h"}, {"MODE", favoriteMode}});
   }

   private void renderCosmetics(GuiGraphicsExtractor g, int x, int y, int w) {
      UiTypography.heading(g, "EQUIPPED COSMETICS", x, y + s(12), PvpClient.themeEngine().text());
      List<Cosmetic> equipped = PvpClient.cosmetics().getAllEquipped();
      if (equipped.isEmpty()) {
         PARSFramework.card(g, x, y + s(54), w, s(86));
         UiTypography.body(g, "No cosmetic is equipped yet.", x + s(18), y + s(76), PvpClient.themeEngine().secondary());
         UiTypography.caption(g, "Open Cosmetics to choose an effect.", x + s(18), y + s(100), PvpClient.themeEngine().muted());
         return;
      }
      int cardW = (w - s(24)) / 3;
      for (int i = 0; i < Math.min(equipped.size(), 6); i++) {
         Cosmetic c = equipped.get(i);
         int cx = x + (i % 3) * (cardW + s(12));
         int cy = y + s(54) + (i / 3) * s(92);
         PARSFramework.card(g, cx, cy, cardW, s(80));
         g.fill(cx, cy, cx + s(3), cy + s(80), c.getRarity().getColor() | 0xFF000000);
         UiTypography.label(g, c.getType().getDisplayName(), cx + s(14), cy + s(14), PvpClient.themeEngine().muted());
         UiTypography.body(g, c.getName(), cx + s(14), cy + s(32), PvpClient.themeEngine().text());
         UiTypography.caption(g, c.isAnimated() ? "ANIMATED • LIVE" : "EQUIPPED • LIVE", cx + s(14), cy + s(54), PvpClient.themeEngine().accent());
      }
   }

   private void renderAchievements(GuiGraphicsExtractor g, int x, int y, int w) {
      UiTypography.heading(g, "ACHIEVEMENTS", x, y + s(12), PvpClient.themeEngine().text());
      String[][] rows = {{"FIRST BLOOD", "First kill", "UNLOCKED"}, {"CENTURY CLUB", "Win 100 matches", "72%"}, {"COMBO MASTER", "20-kill combo", "LOCKED"}, {"LEGENDARY", "Own a Legendary cosmetic", "UNLOCKED"}};
      for (int i = 0; i < rows.length; i++) {
         int ry = y + s(54) + i * s(68);
         PARSFramework.card(g, x, ry, w, s(56));
         UiTypography.body(g, rows[i][0], x + s(16), ry + s(12), PvpClient.themeEngine().text());
         UiTypography.caption(g, rows[i][1], x + s(16), ry + s(31), PvpClient.themeEngine().muted());
         UiTypography.label(g, rows[i][2], x + w - s(92), ry + s(20), rows[i][2].equals("UNLOCKED") ? PvpClient.themeEngine().accent() : PvpClient.themeEngine().secondary());
      }
   }

   private void card(GuiGraphicsExtractor g, int x, int y, int w, int h, String title, String[][] values) {
      PARSFramework.card(g, x, y, w, s(h));
      UiTypography.caption(g, title, x + s(16), y + s(12), PvpClient.themeEngine().muted());
      int cols = Math.min(4, values.length);
      int cellW = (w - s(32)) / cols;
      for (int i = 0; i < values.length; i++) {
         int cx = x + s(16) + i * cellW;
         UiTypography.label(g, values[i][0], cx, y + s(36), PvpClient.themeEngine().secondary());
         UiTypography.body(g, values[i][1], cx, y + s(54), PvpClient.themeEngine().text());
      }
   }

   @Override public boolean shouldCloseOnEsc() { return true; }

   @Override public void onClose() {
      UiSoundEngine.back();
      if (minecraft != null) minecraft.setScreen(new ParsMainMenuScreen());
   }
}
