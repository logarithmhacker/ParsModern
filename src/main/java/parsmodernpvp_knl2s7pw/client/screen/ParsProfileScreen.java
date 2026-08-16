package parsmodernpvp_knl2s7pw.client.screen;

<<<<<<< HEAD
import java.util.List;
import java.util.Locale;
import net.minecraft.client.Minecraft;
=======
import java.util.ArrayList;
import java.util.List;
>>>>>>> 77bc9da847b12cb9940d49afdb8df99b0af11b92
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
<<<<<<< HEAD
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import org.joml.Quaternionf;
import org.joml.Vector3f;
=======
>>>>>>> 77bc9da847b12cb9940d49afdb8df99b0af11b92
import parsmodernpvp_knl2s7pw.client.PvpClient;
import parsmodernpvp_knl2s7pw.client.cosmetics.Cosmetic;
import parsmodernpvp_knl2s7pw.client.profile.Profile;
import parsmodernpvp_knl2s7pw.client.ui.DesignTokens;
import parsmodernpvp_knl2s7pw.client.ui.PARSFramework;
import parsmodernpvp_knl2s7pw.client.ui.UiScale;
import parsmodernpvp_knl2s7pw.client.ui.UiSoundEngine;
import parsmodernpvp_knl2s7pw.client.ui.UiTypography;

<<<<<<< HEAD
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
=======
/**
 * Premium Profile Screen - Complete player identity experience.
 * Features: Avatar display, level/XP progress, statistics, equipped cosmetics, quick actions.
 * Visual Identity: Clean hierarchy, professional cards, smooth interactions.
 */
public class ParsProfileScreen extends Screen {
   private static final String[] TABS = {"OVERVIEW", "STATISTICS", "COSMETICS", "ACHIEVEMENTS"};
   
   private final Profile profile;
   private String selectedTab = "OVERVIEW";
   private int scrollOffset;
   private int maxScroll;
   private long openedAt;
   
   // Mock statistics (client-side only, backend-ready architecture)
   private final int level;
   private final int xpCurrent;
   private final int xpMax;
   private final int wins;
   private final int losses;
   private final int kills;
   private final int deaths;
   private final int playtimeHours;
   private final String favoriteMode;
   
   public ParsProfileScreen() {
      this(PvpClient.profiles().active());
   }
   
   public ParsProfileScreen(Profile profile) {
      super(Component.literal("PARS Profile"));
      this.profile = profile;
      this.openedAt = System.nanoTime();
      
      // Client-side mock data (replaceable with backend data later)
      this.level = 42;
      this.xpCurrent = 2840;
      this.xpMax = 5000;
      this.wins = 127;
      this.losses = 89;
      this.kills = 3421;
      this.deaths = 1876;
      this.playtimeHours = 156;
      this.favoriteMode = "Crystal PvP";
   }
   
   private static int s(int value) {
      return UiScale.s(value);
   }
   
   private int left() {
      return Math.max(s(16), this.width / 2 - s(450));
   }
   
   private int top() {
      return Math.max(s(16), this.height / 2 - s(260));
   }
   
   private int contentLeft() {
      return this.left() + s(280);
   }
   
   @Override
   protected void init() {
      int l = this.left();
      int t = this.top();
      
      // Tab navigation
      for (int i = 0; i < TABS.length; i++) {
         final int idx = i;
         this.addRenderableWidget(Button.builder(Component.empty(), btn -> {
            this.selectedTab = TABS[idx];
            this.scrollOffset = 0;
            UiSoundEngine.click();
            this.rebuildWidgets();
         }).bounds(l + s(12 + i * 138), t + s(14), s(132), s(28)).build());
      }
      
      // Quick action buttons
      this.addRenderableWidget(Button.builder(Component.literal("⚙ SETTINGS"), btn -> {
         UiSoundEngine.confirm();
         this.minecraft.setScreen(new PvpScreen("settings"));
      }).bounds(this.width - s(180), s(12), s(168), s(28)).build());
      
      this.addRenderableWidget(Button.builder(Component.literal("✕ CLOSE"), btn -> this.onClose())
         .bounds(this.width - s(40), s(12), s(28), s(28)).build());
      
      // Reset alpha for smooth appearance
      this.children().forEach(child -> {
         if (child instanceof Button btn) {
            btn.setAlpha(0.0F);
         }
      });
   }
   
   @Override
   public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      if (verticalAmount != 0 && this.maxScroll > 0) {
         this.scrollOffset = (int)Math.clamp(
            this.scrollOffset - verticalAmount * s(30),
            0,
            this.maxScroll
         );
         return true;
      }
      return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
   }
   
   public boolean mouseClicked(MouseButtonEvent e, boolean doubleClick) {
      int button = e.button();
      double mouseX = e.x();
      double mouseY = e.y();
      if (button == 1 && mouseX >= this.left() && mouseX <= this.left() + s(884) 
          && mouseY >= this.top() && mouseY <= this.top() + s(520)) {
         return true;
      }
      return super.mouseClicked(e, doubleClick);
   }
   
   @Override
   public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float partialTick) {
      // Background
      PARSFramework.background(g, this.width, this.height);
      
      int l = this.left();
      int t = this.top();
      int cl = this.contentLeft();
      
      // Main panel
      PARSFramework.panel(g, l, t, s(884), s(520));
      
      // Header bar with accent
      int accent = PvpClient.themeEngine().accent();
      g.fill(l, t, l + s(884), t + s(6), accent);
      
      // Render tabs with active state
      for (int i = 0; i < TABS.length; i++) {
         int tabX = l + s(12 + i * 138);
         int tabY = t + s(14);
         boolean active = TABS[i].equals(this.selectedTab);
         this.renderTab(g, TABS[i], tabX, tabY, active);
      }
      
      // Profile header (left sidebar)
      this.renderProfileHeader(g, l + s(20), t + s(56));
      
      // Content area based on selected tab
      switch (this.selectedTab) {
         case "OVERVIEW" -> this.renderOverview(g, cl, t + s(56), mouseX, mouseY);
         case "STATISTICS" -> this.renderStatistics(g, cl, t + s(56));
         case "COSMETICS" -> this.renderProfileCosmetics(g, cl, t + s(56));
         case "ACHIEVEMENTS" -> this.renderAchievements(g, cl, t + s(56));
      }
      
      super.extractRenderState(g, mouseX, mouseY, partialTick);
   }
   
   private void renderTab(GuiGraphicsExtractor g, String label, int x, int y, boolean active) {
      int width = s(132);
      int height = s(28);
      
      // Background
      int bgColor = active ? PvpClient.themeEngine().color("hover") : 0;
      if (bgColor != 0) {
         g.fill(x, y, x + width, y + height, bgColor);
      }
      
      // Selection indicator
      if (active) {
         g.fill(x + s(4), y + height - s(3), x + width - s(4), y + height, PvpClient.themeEngine().accent());
      }
      
      // Text
      int textColor = active ? PvpClient.themeEngine().text() : PvpClient.themeEngine().secondary();
      UiTypography.text(g, label, x + width / 2, y + s(8), textColor, 0.68F, 1);
   }
   
   private void renderProfileHeader(GuiGraphicsExtractor g, int x, int y) {
      int width = s(240);
      int height = s(280);
      
      // Card background
      PARSFramework.card(g, x, y, width, height);
      
      // Avatar placeholder (colored square representing skin)
      int avatarSize = s(80);
      int avatarX = x + (width - avatarSize) / 2;
      int avatarY = y + s(20);
      g.fill(avatarX, avatarY, avatarX + avatarSize, avatarY + avatarSize, PvpClient.themeEngine().color("card"));
      g.fill(avatarX + s(4), avatarY + s(4), avatarX + avatarSize - s(4), avatarY + avatarSize - s(4), 
             PvpClient.themeEngine().background());
      
      // Username
      String username = this.profile.displayName();
      UiTypography.heading(g, username, x + s(16), y + s(116), PvpClient.themeEngine().text());
      
      // Level badge
      int levelBadgeY = y + s(140);
      g.fill(x + s(16), levelBadgeY, x + s(60), levelBadgeY + s(24), PvpClient.themeEngine().accent());
      UiTypography.numeric(g, "LVL " + this.level, x + s(38), levelBadgeY + s(6), -16248033);
      
      // XP Progress bar
      int xpBarY = y + s(176);
      int xpBarWidth = width - s(32);
      int xpBarHeight = s(8);
      g.fill(x + s(16), xpBarY, x + s(16) + xpBarWidth, xpBarY + xpBarHeight, PvpClient.themeEngine().color("card"));
      
      float xpProgress = (float)this.xpCurrent / this.xpMax;
      int xpFilled = (int)(xpBarWidth * xpProgress);
      g.fill(x + s(16), xpBarY, x + s(16) + xpFilled, xpBarY + xpBarHeight, PvpClient.themeEngine().accent());
      
      // XP text
      UiTypography.label(g, this.xpCurrent + " / " + this.xpMax + " XP", x + s(16), xpBarY + s(20), 
                         PvpClient.themeEngine().secondary());
      
      // Stats summary
      int statsY = y + s(210);
      this.renderStatMini(g, x + s(16), statsY, "WINS", String.valueOf(this.wins));
      this.renderStatMini(g, x + s(86), statsY, "K/D", String.format("%.2f", (double)this.kills / Math.max(1, this.deaths)));
      this.renderStatMini(g, x + s(16), statsY + s(28), "PLAYTIME", this.playtimeHours + "h");
      this.renderStatMini(g, x + s(86), statsY + s(28), "MODE", this.favoriteMode);
      
      // Equipped cosmetics count
      int equippedCount = (int)PvpClient.cosmetics().getAllEquipped().stream()
         .filter(c -> c != null).count();
      UiTypography.label(g, equippedCount + " COSMETICS EQUIPPED", x + s(16), y + height - s(24), PvpClient.themeEngine().muted());
   }
   
   private void renderStatMini(GuiGraphicsExtractor g, int x, int y, String label, String value) {
      UiTypography.label(g, label, x, y, PvpClient.themeEngine().secondary());
      UiTypography.body(g, value, x, y + s(12), PvpClient.themeEngine().text());
   }
   
   private void renderOverview(GuiGraphicsExtractor g, int x, int y, int mouseX, int mouseY) {
      // Welcome message
      float intro = PvpClient.reducedMotion() ? 1.0F : Math.min(1.0F, (float)(System.nanoTime() - this.openedAt) / 5.0E8F);
      UiTypography.title(g, "Welcome back, " + this.profile.displayName(), x, y + (int)(s(20) * intro), 
                        PvpClient.themeEngine().text());
      UiTypography.body(g, "Here's your PvP journey at a glance.", x, y + s(52), PvpClient.themeEngine().secondary());
      
      // Stats cards grid
      int cardWidth = s(180);
      int cardHeight = s(90);
      int gap = s(16);
      
      String[] labels = {"TOTAL WINS", "TOTAL KILLS", "K/D RATIO", "PLAYTIME", "FAVORITE MODE", "SESSION BEST"};
      String[] values = {String.valueOf(this.wins), String.valueOf(this.kills), 
                        String.format("%.2f", (double)this.kills / Math.max(1, this.deaths)),
                        this.playtimeHours + " hours", this.favoriteMode, "12 kill combo"};
      
      for (int i = 0; i < 6; i++) {
         int row = i / 3;
         int col = i % 3;
         int cardX = x + col * (cardWidth + gap);
         int cardY = y + s(88) + row * (cardHeight + gap);
         
         PARSFramework.card(g, cardX, cardY, cardWidth, cardHeight);
         
         // Accent top border
         g.fill(cardX, cardY, cardX + s(3), cardY + cardHeight, PvpClient.themeEngine().accent());
         
         UiTypography.label(g, labels[i], cardX + s(14), cardY + s(12), PvpClient.themeEngine().secondary());
         UiTypography.heading(g, values[i], cardX + s(14), cardY + s(32), PvpClient.themeEngine().text());
      }
      
      // Recent activity section
      int activityY = y + s(280);
      UiTypography.heading(g, "RECENT ACTIVITY", x, activityY, PvpClient.themeEngine().text());
      g.fill(x, activityY + s(24), x + s(580), activityY + s(26), PvpClient.themeEngine().border());
      
      // Activity items (mock data)
      String[] activities = {
         "Won Crystal PvP match • 2 minutes ago",
         "Unlocked Epic cape • 1 hour ago",
         "New personal best: 18 kills • Today",
         "Equipped Legendary wings • Yesterday"
      };
      
      for (int i = 0; i < activities.length; i++) {
         int itemY = activityY + s(40) + i * s(32);
         UiTypography.body(g, activities[i], x, itemY, PvpClient.themeEngine().secondary());
      }
   }
   
   private void renderStatistics(GuiGraphicsExtractor g, int x, int y) {
      UiTypography.heading(g, "COMBAT STATISTICS", x, y, PvpClient.themeEngine().text());
      
      int startY = y + s(40);
      int cardWidth = s(260);
      int gap = s(16);
      
      // Combat stats
      String[][] combatStats = {
         {"KILLS", String.valueOf(this.kills)},
         {"DEATHS", String.valueOf(this.deaths)},
         {"K/D RATIO", String.format("%.2f", (double)this.kills / Math.max(1, this.deaths))},
         {"WINS", String.valueOf(this.wins)},
         {"LOSSES", String.valueOf(this.losses)},
         {"WIN RATE", String.format("%.1f%%", (double)this.wins / (this.wins + this.losses) * 100)}
      };
      
      for (int i = 0; i < combatStats.length; i++) {
         int row = i / 2;
         int col = i % 2;
         int cardX = x + col * (cardWidth + gap);
         int cardY = startY + row * (s(70) + gap);
         
         PARSFramework.card(g, cardX, cardY, cardWidth, s(70));
         UiTypography.label(g, combatStats[i][0], cardX + s(16), cardY + s(14), PvpClient.themeEngine().secondary());
         UiTypography.heading(g, combatStats[i][1], cardX + s(16), cardY + s(34), PvpClient.themeEngine().text());
      }
      
      // Session stats
      int sessionY = startY + s(240);
      UiTypography.heading(g, "SESSION STATISTICS", x, sessionY, PvpClient.themeEngine().text());
      
      String[][] sessionStats = {
         {"MATCHES PLAYED", "8"},
         {"SESSION KILLS", "67"},
         {"BEST COMBO", "12"},
         {"DAMAGE DEALT", "24.5k"}
      };
      
      for (int i = 0; i < sessionStats.length; i++) {
         int cardX = x + (i % 4) * (cardWidth + gap);
         int cardY = sessionY + s(40);
         
         PARSFramework.card(g, cardX, cardY, cardWidth, s(70));
         UiTypography.label(g, sessionStats[i][0], cardX + s(16), cardY + s(14), PvpClient.themeEngine().secondary());
         UiTypography.heading(g, sessionStats[i][1], cardX + s(16), cardY + s(34), PvpClient.themeEngine().text());
      }
   }
   
   private void renderProfileCosmetics(GuiGraphicsExtractor g, int x, int y) {
      UiTypography.heading(g, "EQUIPPED COSMETICS", x, y, PvpClient.themeEngine().text());
      
      List<Cosmetic> equipped = PvpClient.cosmetics().getAllEquipped();
      int cardWidth = s(120);
      int gap = s(16);
      
      if (equipped.isEmpty()) {
         int emptyY = y + s(60);
         UiTypography.body(g, "No cosmetics currently equipped.", x, emptyY, PvpClient.themeEngine().secondary());
         UiTypography.label(g, "Visit the Cosmetics screen to customize your look.", x, emptyY + s(24), PvpClient.themeEngine().muted());
         return;
      }
      
      int col = 0;
      int row = 0;
      for (Cosmetic c : equipped) {
         if (c == null) continue;
         
         int cardX = x + col * (cardWidth + gap);
         int cardY = y + s(40) + row * (cardWidth + gap);
         
         PARSFramework.card(g, cardX, cardY, cardWidth, cardWidth);
         
         // Rarity indicator
         int rarityColor = c.getRarity().getColor();
         g.fill(cardX, cardY, cardX + s(3), cardY + cardWidth, rarityColor | 0xFF000000);
         
         // Icon placeholder
         int iconSize = s(48);
         int iconX = cardX + (cardWidth - iconSize) / 2;
         int iconY = cardY + s(20);
         g.fill(iconX, iconY, iconX + iconSize, iconY + iconSize, rarityColor | 0x66FFFFFF);
         
         // Name
         UiTypography.label(g, c.getName(), cardX + s(8), cardY + cardWidth - s(20), PvpClient.themeEngine().text());
         
         col++;
         if (col >= 4) {
            col = 0;
            row++;
         }
      }
   }
   
   private void renderAchievements(GuiGraphicsExtractor g, int x, int y) {
      UiTypography.heading(g, "ACHIEVEMENTS", x, y, PvpClient.themeEngine().text());
      g.fill(x, y + s(24), x + s(580), y + s(26), PvpClient.themeEngine().border());
      
      // Placeholder achievements (backend-ready)
      String[][] achievements = {
         {"FIRST BLOOD", "Get your first kill", "✓", "UNLOCKED"},
         {"CENTURY CLUB", "Win 100 matches", "◐", "72/100"},
         {"COMBO MASTER", "Achieve a 20-kill combo", "○", "LOCKED"},
         {"LEGENDARY", "Unlock a Legendary cosmetic", "✓", "UNLOCKED"}
      };
      
      for (int i = 0; i < achievements.length; i++) {
         int cardY = y + s(40) + i * s(60);
         
         PARSFramework.card(g, x, cardY, s(580), s(52));
         
         // Icon
         g.fill(x + s(16), cardY + s(14), x + s(36), cardY + s(34), 
                achievements[i][2].equals("✓") ? PvpClient.themeEngine().accent() : PvpClient.themeEngine().border());
         
         UiTypography.body(g, achievements[i][0], x + s(48), cardY + s(14), PvpClient.themeEngine().text());
         UiTypography.caption(g, achievements[i][1], x + s(48), cardY + s(30), PvpClient.themeEngine().secondary());
         
         // Progress/status
         int statusColor = achievements[i][2].equals("✓") ? PvpClient.themeEngine().accent() :
                            achievements[i][2].equals("◐") ? -263693 : PvpClient.themeEngine().secondary();
         UiTypography.body(g, achievements[i][3], x + s(480), cardY + s(18), statusColor);
      }
      
      UiTypography.label(g, "More achievements coming soon...", x, y + s(300), PvpClient.themeEngine().muted());
   }
   
   @Override
   public void onClose() {
      UiSoundEngine.back();
      if (this.minecraft != null && this.minecraft.player == null) {
         this.minecraft.setScreen(new ParsMainMenuScreen());
      } else if (this.minecraft != null) {
         this.minecraft.setScreen(null);
      }
>>>>>>> 77bc9da847b12cb9940d49afdb8df99b0af11b92
   }
}
