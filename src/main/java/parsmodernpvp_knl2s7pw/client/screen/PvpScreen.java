package parsmodernpvp_knl2s7pw.client.screen;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import parsmodernpvp_knl2s7pw.client.PvpClient;
import parsmodernpvp_knl2s7pw.client.hud.HudLayout;
import parsmodernpvp_knl2s7pw.client.module.Module;
import parsmodernpvp_knl2s7pw.client.profile.Profile;
import parsmodernpvp_knl2s7pw.client.settings.Setting;
import parsmodernpvp_knl2s7pw.client.theme.Theme;
import parsmodernpvp_knl2s7pw.client.ui.PARSFontEngine;
import parsmodernpvp_knl2s7pw.client.ui.PARSFramework;
import parsmodernpvp_knl2s7pw.client.ui.UiNotifications;
import parsmodernpvp_knl2s7pw.client.ui.UiScale;
import parsmodernpvp_knl2s7pw.client.ui.UiSoundEngine;
import parsmodernpvp_knl2s7pw.client.ui.UiTypography;

public final class PvpScreen extends Screen {
   private static final String[] NAV = new String[]{"MODULES", "HUD EDITOR", "PROFILES", "STYLE", "SETTINGS"};
   private static final String[] CATEGORIES = new String[]{"ALL", "COMBAT", "HUD", "VISUAL", "PLAYER", "WORLD", "UTILITY", "PERFORMANCE"};
   private EditBox search;
   private String page = "modules";
   private String category = "ALL";
   private String dragging;
   private int scroll;
   private int dragOffsetX;
   private int dragOffsetY;

   public PvpScreen() {
      this("modules");
   }

   public PvpScreen(String initialPage) {
      super(Component.literal("PARSModernPvP"));
      this.page = initialPage == null ? "modules" : initialPage;
   }

   public void onClose() {
      UiSoundEngine.back();
      if (this.minecraft != null && this.minecraft.player == null) {
         this.minecraft.setScreen(new ParsMainMenuScreen());
      } else if (this.minecraft != null) {
         this.minecraft.setScreen(null);
      }
   }

   /*
    * Applies the central UI scale to a layout value.
    */
   private static int s(int value) {
      return UiScale.s(value);
   }

   private int left() {
      return Math.max(s(12), this.width / 2 - s(450));
   }

   private int top() {
      return Math.max(s(14), this.height / 2 - s(250));
   }

   private int contentLeft() {
      return this.left() + s(178);
   }

   protected void init() {
      int l = this.left();
      int t = this.top();
      int content = this.contentLeft();

      for (int i = 0; i < NAV.length; i++) {
         String target = NAV[i].toLowerCase().replace(" ", "");
         this.addRenderableWidget(Button.builder(Component.empty(), b -> this.selectPage(target)).bounds(l + s(12), t + s(58 + i * 42), s(150), s(34)).build());
      }

      if (this.page.equals("modules")) {
         this.search = new EditBox(this.font, content, t + s(18), s(270), s(24), Component.empty());
         this.search.setHint(Component.literal("Search modules"));
         this.addRenderableWidget(this.search);
         this.addCategories(content, t + s(18));
         this.addModules(content, t + s(58));
      } else if (this.page.equals("hudeditor")) {
         this.addHudEditor(content, t + s(22));
      } else if (this.page.equals("profiles")) {
         this.addProfiles(content, t + s(22));
      } else if (this.page.equals("style")) {
         this.addStyle(content, t + s(22));
         this.addEffectControls(content, t + s(238));
      } else {
         this.addSettings(content, t + s(22));
      }

      this.addRenderableWidget(Button.builder(Component.empty(), b -> this.onClose()).bounds(l + s(804), t + s(444), s(82), s(26)).build());

      for (GuiEventListener child : this.children()) {
         if (child instanceof Button button) {
            button.setAlpha(0.0F);
         }
      }

      if (this.search != null) {
         this.search.setAlpha(0.0F);
      }
   }

   private void selectPage(String target) {
      this.page = target.equals("hudeditor") ? "hudeditor" : target;
      if (this.page.equals("hudeditor")) {
         PvpClient.setHudEditor(true);
      }

      UiSoundEngine.click();
      this.rebuild();
   }

   private void addCategories(int x, int y) {
      for (int i = 0; i < CATEGORIES.length; i++) {
         String selected = CATEGORIES[i];
         this.addRenderableWidget(Button.builder(Component.literal(selected), b -> {
            this.category = selected;
            this.scroll = 0;
            this.rebuild();
         }).bounds(x + s(280 + i % 4 * 73), y + s(i / 4 * 26), s(68), s(22)).build());
      }
   }

   private List<Module> filteredModules() {
      String filter = this.search == null ? "" : this.search.getValue().toLowerCase();
      List<Module> result = new ArrayList<>();

      for (Module m : PvpClient.modules().all().values()) {
         String uiCategory = uiCategory(m.category());
         boolean cat = this.category.equals("ALL") || uiCategory.equals(this.category);
         boolean text = filter.isBlank()
            || m.displayName().toLowerCase().contains(filter)
            || m.id().contains(filter)
            || uiCategory.toLowerCase().contains(filter);
         if (cat && text) {
            result.add(m);
         }
      }

      return result;
   }

   private static String uiCategory(String category) {
      return switch (category == null ? "" : category.toUpperCase()) {
         case "RENDER", "VISUAL" -> "VISUAL";
         case "INFO", "HUD" -> "HUD";
         case "SYSTEM", "PERFORMANCE" -> "PERFORMANCE";
         case "COMBAT" -> "COMBAT";
         case "PLAYER" -> "PLAYER";
         case "WORLD" -> "WORLD";
         default -> "UTILITY";
      };
   }

   private void addEffectControls(int x, int y) {
      String[] effects = new String[]{
         "motion_blur",
         "blur",
         "glow",
         "vignette",
         "particles",
         "color_grading",
         "screen_fade",
         "animated_gradient",
         "grid",
         "parallax",
         "soft_lighting",
         "panel_depth",
         "font_weight",
         "performance"
      };

      for (int i = 0; i < effects.length; i++) {
         String effect = effects[i];
         this.addRenderableWidget(Button.builder(Component.literal(this.effectLabel(effect)), b -> {
            PvpClient.cycleCustomization(effect);
            PvpClient.config().save();
            this.rebuild();
         }).bounds(x + s(i % 3 * 142), y + s(i / 3 * 26), s(132), s(22)).build());
      }
   }

   private String effectLabel(String effect) {
      if (effect.equals("font_weight")) {
         return "FONT  " + PvpClient.fontWeight();
      } else {
         return effect.equals("performance")
            ? "PRESET  " + PvpClient.performancePreset()
            : effect.replace('_', ' ').toUpperCase() + (this.effectEnabled(effect) ? "  ON" : "  OFF");
      }
   }

   private boolean effectEnabled(String effect) {
      return switch (effect) {
         case "motion_blur" -> PvpClient.motionBlur();
         case "blur" -> PvpClient.blur();
         case "glow" -> PvpClient.glow();
         case "vignette" -> PvpClient.vignette();
         case "particles" -> PvpClient.particles();
         case "color_grading" -> PvpClient.colorGrading();
         case "screen_fade" -> PvpClient.screenFade();
         case "animated_gradient" -> PvpClient.animatedGradient();
         case "grid" -> PvpClient.grid();
         case "parallax" -> PvpClient.parallax();
         case "soft_lighting" -> PvpClient.softLighting();
         case "panel_depth" -> PvpClient.panelDepth();
         default -> false;
      };
   }

   private void addModules(int x, int y) {
      List<Module> visible = this.filteredModules();

      for (int i = 0; i < visible.size(); i++) {
         int row = i / 2 - this.scroll;
         if (row >= 0 && row <= 5) {
            int col = i % 2;
            int cx = x + s(col * 314);
            int cy = y + s(row * 58);
            Module m = visible.get(i);
            this.addRenderableWidget(Button.builder(Component.literal(m.enabled() ? "ON" : "OFF"), b -> {
               PvpClient.modules().toggle(m.id());
               PvpClient.config().save();
               this.rebuild();
            }).bounds(cx + s(10), cy + s(31), s(54), s(18)).build());
            this.addRenderableWidget(Button.builder(Component.literal("EDIT"), b -> this.changeSetting(m)).bounds(cx + s(70), cy + s(31), s(54), s(18)).build());
            this.addRenderableWidget(Button.builder(Component.literal(this.keyName(m.keybind())), b -> {
               m.setKeybind(this.nextKey(m.keybind()));
               PvpClient.config().save();
               this.rebuild();
            }).bounds(cx + s(130), cy + s(31), s(72), s(18)).build());
         }
      }
   }

   private void addHudEditor(int x, int y) {
      this.addRenderableWidget(Button.builder(Component.literal("SAVE"), b -> PvpClient.config().save()).bounds(x, y, s(58), s(20)).build());
      this.addRenderableWidget(Button.builder(Component.literal("SNAP"), b -> {
         HudLayout h = PvpClient.hudLayout(PvpClient.selectedHud());
         PvpClient.setHudLayout(PvpClient.selectedHud(), h.withSnap(!h.snapToGrid()));
         this.rebuild();
      }).bounds(x + s(64), y, s(58), s(20)).build());
      this.addRenderableWidget(Button.builder(Component.literal("RESET"), b -> {
         PvpClient.setHudLayout(PvpClient.selectedHud(), PvpClient.hudLayout(PvpClient.selectedHud()).reset());
         PvpClient.config().save();
         this.rebuild();
      }).bounds(x + s(128), y, s(66), s(20)).build());
      this.addRenderableWidget(Button.builder(Component.literal("SCALE +"), b -> this.adjustScale(0.1F)).bounds(x + s(200), y, s(72), s(20)).build());
      this.addRenderableWidget(Button.builder(Component.literal("SCALE -"), b -> this.adjustScale(-0.1F)).bounds(x + s(278), y, s(72), s(20)).build());
      this.addRenderableWidget(Button.builder(Component.literal("OPACITY"), b -> this.adjustOpacity(0.1F)).bounds(x + s(356), y, s(78), s(20)).build());
      this.addRenderableWidget(Button.builder(Component.literal("COLOR"), b -> this.cycleHudColor()).bounds(x + s(440), y, s(64), s(20)).build());
      this.addRenderableWidget(Button.builder(Component.literal("TOGGLE"), b -> {
         HudLayout h = PvpClient.hudLayout(PvpClient.selectedHud());
         PvpClient.setHudLayout(PvpClient.selectedHud(), h.withEnabled(!h.enabled()));
         PvpClient.config().save();
         this.rebuild();
      }).bounds(x + s(510), y, s(70), s(20)).build());
      int i = 0;

      for (String id : this.hudIds()) {
         int row = i++ - this.scroll;
         if (row >= 0 && row <= 10) {
            String selected = id;
            this.addRenderableWidget(Button.builder(Component.literal(id.toUpperCase()), b -> {
               PvpClient.setSelectedHud(selected);
               this.rebuild();
            }).bounds(x, y + s(30 + row * 23), s(150), s(20)).build());
         }
      }
   }

   private void addProfiles(int x, int y) {
      int i = 0;

      for (Profile p : PvpClient.profiles().profiles()) {
         int col = i % 3;
         int row = i++ / 3;
         if (row <= 5) {
            this.addRenderableWidget(Button.builder(Component.literal(p.displayName()), b -> {
               PvpClient.profiles().select(p);
               PvpClient.config().save();
               UiSoundEngine.confirm();
               this.rebuild();
            }).bounds(x + s(col * 190), y + s(row * 54), s(178), s(42)).build());
         }
      }

      this.addRenderableWidget(Button.builder(Component.literal("NEW PROFILE"), b -> {
         PvpClient.profiles().create("custom_" + System.currentTimeMillis() % 1000L, "Custom Profile");
         PvpClient.config().save();
         this.rebuild();
      }).bounds(x, y + s(326), s(150), s(22)).build());
      this.addRenderableWidget(Button.builder(Component.literal("RESET ACTIVE"), b -> {
         PvpClient.profiles().reset(PvpClient.profiles().active());
         PvpClient.config().save();
         this.rebuild();
      }).bounds(x + s(160), y + s(326), s(150), s(22)).build());
   }

   private void addStyle(int x, int y) {
      String[] options = new String[]{
         "accent",
         "secondary",
         "background",
         "panel",
         "radius",
         "ui_scale",
         "animation",
         "font",
         "blur",
         "glow",
         "shadow",
         "spacing",
         "background_mode",
         "background_preset"
      };

      for (int i = 0; i < options.length; i++) {
         String option = options[i];
         this.addRenderableWidget(Button.builder(Component.literal(option.toUpperCase()), b -> {
            PvpClient.cycleCustomization(option);
            PvpClient.config().save();
            this.rebuild();
         }).bounds(x + s(i % 2 * 210), y + s(i / 2 * 30), s(192), s(24)).build());
      }

      String[] themes = new String[]{"PARS", "BLUE", "PURPLE", "CYAN", "MIDNIGHT", "MINIMAL", "COMPETITIVE", "CRYSTAL", "MACE"};

      for (int i = 0; i < themes.length; i++) {
         String name = themes[i];
         this.addRenderableWidget(Button.builder(Component.literal(name), b -> {
            this.applyTheme(name);
            PvpClient.config().save();
            this.rebuild();
         }).bounds(x + s(450 + i % 2 * 88), y + s(i / 2 * 30), s(80), s(24)).build());
      }

      this.addRenderableWidget(
         Button.builder(Component.literal("FONT TEST"), b -> this.minecraft.setScreen(new PARSFontTestScreen())).bounds(x + s(450), y + s(156), s(168), s(24)).build()
      );
      this.addRenderableWidget(Button.builder(Component.literal("REDUCED MOTION"), b -> {
         PvpClient.setReducedMotion(!PvpClient.reducedMotion());
         PvpClient.config().save();
         this.rebuild();
      }).bounds(x, y + s(190), s(192), s(24)).build());
   }

   private void addSettings(int x, int y) {
      String[] sections = new String[]{"GENERAL", "INTERFACE", "HUD", "CROSSHAIR", "PERFORMANCE", "PROFILES", "KEYBINDS", "AUDIO"};

      for (int i = 0; i < sections.length; i++) {
         String section = sections[i];
         this.addRenderableWidget(Button.builder(Component.literal(section), b -> this.settingAction(section)).bounds(x, y + s(i * 30), s(190), s(24)).build());
      }

      this.addRenderableWidget(Button.builder(Component.literal("GLOBAL KEY"), b -> {
         PvpClient.setGlobalKeybind(PvpClient.globalKeybind() == 344 ? 345 : 344);
         PvpClient.config().save();
         this.rebuild();
      }).bounds(x + s(214), y, s(190), s(24)).build());
      this.addRenderableWidget(Button.builder(Component.literal("DEBUG"), b -> {
         PvpClient.setDebugMode(!PvpClient.debugMode());
         PvpClient.config().save();
         this.rebuild();
      }).bounds(x + s(214), y + s(30), s(190), s(24)).build());
      this.addRenderableWidget(
         Button.builder(Component.literal("FONT TEST"), b -> this.minecraft.setScreen(new PARSFontTestScreen())).bounds(x + s(214), y + s(60), s(190), s(24)).build()
      );
      this.addRenderableWidget(Button.builder(Component.literal("RESET STYLE"), b -> {
         PvpClient.resetCustomization();
         PvpClient.config().save();
         this.rebuild();
      }).bounds(x + s(214), y + s(90), s(190), s(24)).build());
      this.addRenderableWidget(Button.builder(Component.literal("MASTER VOLUME"), b -> {
         UiSoundEngine.setMaster(UiSoundEngine.master() >= 1.0F ? 0.25F : UiSoundEngine.master() + 0.25F);
         PvpClient.config().save();
         this.rebuild();
      }).bounds(x + s(214), y + s(126), s(190), s(24)).build());
      this.addRenderableWidget(Button.builder(Component.literal("NOTIFICATION VOLUME"), b -> {
         UiSoundEngine.setNotifications(UiSoundEngine.notifications() >= 1.0F ? 0.25F : UiSoundEngine.notifications() + 0.25F);
         PvpClient.config().save();
         this.rebuild();
      }).bounds(x + s(214), y + s(156), s(190), s(24)).build());
   }

   private void settingAction(String section) {
      if (section.equals("HUD")) {
         this.page = "hudeditor";
         PvpClient.setHudEditor(true);
      } else if (section.equals("CROSSHAIR")) {
         PvpClient.modules().toggle("crosshair");
      } else if (section.equals("PERFORMANCE")) {
         PvpClient.modules().toggle("performance");
      } else if (section.equals("INTERFACE")) {
         PvpClient.cycleCustomization("ui_scale");
      } else if (section.equals("AUDIO")) {
         UiSoundEngine.setMuted(!UiSoundEngine.muted());
         UiSoundEngine.toggle();
         PvpClient.notifications().pushUi(UiSoundEngine.muted() ? "UI audio muted" : "UI audio active");
      }

      PvpClient.config().save();
      this.rebuild();
   }

   private void changeSetting(Module m) {
      if (m != null) {
         if (m.id().equals("crosshair")) {
            Setting<String> s = (Setting<String>)m.settings().get("style");

            s.setValue(switch ((String)s.value()) {
               case "Plus" -> "Dot";
               case "Dot" -> "Circle";
               case "Circle" -> "Minimal";
               default -> "Plus";
            });
         } else {
            Setting s = m.settings().get("show_memory");
            if (s != null) {
               s.setValue(!(Boolean)s.value());
            }
         }

         PvpClient.config().save();
         this.rebuild();
      }
   }

   private void cycleHudColor() {
      HudLayout h = PvpClient.hudLayout(PvpClient.selectedHud());
      int n = h.color() == -1 ? PvpClient.accentColor() : (h.color() == PvpClient.accentColor() ? PvpClient.secondaryColor() : -1);
      PvpClient.setHudLayout(PvpClient.selectedHud(), h.withColor(n));
      PvpClient.config().save();
      this.rebuild();
   }

   private void adjustScale(float n) {
      HudLayout h = PvpClient.hudLayout(PvpClient.selectedHud());
      PvpClient.setHudLayout(PvpClient.selectedHud(), h.withScale(h.scale() + n));
      PvpClient.config().save();
      this.rebuild();
   }

   private void adjustOpacity(float n) {
      HudLayout h = PvpClient.hudLayout(PvpClient.selectedHud());
      PvpClient.setHudLayout(PvpClient.selectedHud(), h.withOpacity(h.opacity() + n));
      PvpClient.config().save();
      this.rebuild();
   }

   private void applyTheme(String n) {
      PvpClient.setTheme(switch (n) {
         case "BLUE" -> Theme.BLUE;
         case "PURPLE" -> Theme.PURPLE;
         case "CYAN" -> Theme.CYAN;
         case "MIDNIGHT" -> Theme.MIDNIGHT;
         case "MINIMAL" -> Theme.MINIMAL;
         case "COMPETITIVE" -> Theme.COMPETITIVE;
         case "CRYSTAL" -> Theme.CRYSTAL;
         case "MACE" -> Theme.MACE;
         default -> Theme.PARS_NEON;
      });
   }

   public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float partialTick) {
      int l = this.left();
      int t = this.top();
      int right = l + s(900);
      int bottom = t + s(480);
      PARSFramework.background(g, this.width, this.height);
      PARSFramework.panel(g, l, t, right - l, bottom - t);
      g.fill(l + s(172), t + s(16), l + s(173), bottom - s(16), PvpClient.theme().border());
      UiTypography.title(g, "PARS", l + s(20), t + s(18), PvpClient.theme().accent());
      UiTypography.text(g, "CONTROL CENTER", l + s(21), t + s(38), -8220248, 0.6F, 0);
      UiTypography.text(g, "v13  //  " + PvpClient.profiles().active().displayName(), l + s(20), bottom - s(24), -8220248, 0.6F, 0);
      this.drawSidebar(g, l, t, mouseX, mouseY);
      UiTypography.text(g, this.pageTitle(), this.contentLeft(), t + s(20), PvpClient.theme().text(), 0.85F * PvpClient.fontScale(), 0);
      UiTypography.text(g, this.pageSubtitle(), this.contentLeft(), t + s(36), -8220248, 0.6F, 0);
      if (this.page.equals("modules")) {
         this.drawModules(g, this.contentLeft(), t + s(18));
      } else if (this.page.equals("hudeditor")) {
         this.drawHud(g, this.contentLeft(), t + s(18));
      } else if (this.page.equals("profiles")) {
         this.drawProfiles(g, this.contentLeft(), t + s(18));
      } else if (this.page.equals("style")) {
         this.drawStyle(g, this.contentLeft(), t + s(18));
      } else {
         this.drawSettings(g, this.contentLeft(), t + s(18));
      }

      super.extractRenderState(g, mouseX, mouseY, partialTick);
      if (this.search != null) {
         this.drawSearch(g, this.search.getX(), this.search.getY(), this.search.getWidth(), this.search.getHeight());
      }

      this.drawButtons(g, mouseX, mouseY);
      UiNotifications.render(g, this.width, this.height);
   }

   private String pageTitle() {
      return switch (this.page) {
         case "hudeditor" -> "HUD EDITOR";
         case "profiles" -> "PROFILES";
         case "style" -> "STYLE LAB";
         case "settings" -> "SETTINGS";
         default -> "MODULES";
      };
   }

   private String pageSubtitle() {
      return switch (this.page) {
         case "hudeditor" -> "DRAG ELEMENTS IN WORLD  //  LIVE PREVIEW";
         case "profiles" -> "INSTANT PRESETS  //  SAVED LOCALLY";
         case "style" -> "COLORS, MOTION, TYPE  //  LIVE PREVIEW";
         case "settings" -> "CLIENT BEHAVIOUR  //  NO GAMEPLAY AUTOMATION";
         default -> "FAIR-PLAY TOOLS  //  SELECT A CATEGORY";
      };
   }

   private void drawSidebar(GuiGraphicsExtractor g, int l, int t, int mx, int my) {
      for (int i = 0; i < NAV.length; i++) {
         int x = l + s(12);
         int y = t + s(58 + i * 42);
         String target = NAV[i].toLowerCase().replace(" ", "");
         boolean selected = this.page.equals(target);
         boolean hover = mx >= x && mx < x + s(150) && my >= y && my < y + s(34);
         if (selected || hover) {
            g.fill(x, y, x + s(150), y + s(34), selected ? PvpClient.theme().accent() : 1144680337);
         }

         g.fill(x, y, x + (selected ? s(3) : s(1)), y + s(34), selected ? PvpClient.theme().secondary() : PvpClient.theme().border());
         UiTypography.text(g, NAV[i], x + s(12), y + s(11), selected ? -16313828 : PvpClient.theme().text(), 0.64F, 0);
      }
   }

   private void drawModules(GuiGraphicsExtractor g, int x, int y) {
      List<Module> list = this.filteredModules();

      for (int i = 0; i < list.size(); i++) {
         int row = i / 2 - this.scroll;
         if (row >= 0 && row <= 5) {
            Module m = list.get(i);
            int cx = x + s(i % 2 * 314);
            int cy = y + s(40 + row * 58);
            g.fill(cx + s(2), cy + s(2), cx + s(306), cy + s(52), 1140850688);
            g.fill(cx, cy, cx + s(304), cy + s(50), -871359959);
            g.fill(cx, cy, cx + s(2), cy + s(50), m.enabled() ? PvpClient.theme().accent() : -13023654);
            UiTypography.text(g, m.displayName(), cx + s(10), cy + s(6), PvpClient.theme().text(), 0.68F, 0);
            UiTypography.text(
               g,
               m.category() + "  //  " + (m.enabled() ? "ACTIVE" : "IDLE"),
               cx + s(10),
               cy + s(19),
               m.enabled() ? PvpClient.theme().secondary() : -9338730,
               0.54F,
               0
            );
         }
      }

      UiTypography.text(
         g, list.isEmpty() ? "NO MODULES MATCH THIS FILTER" : "SCROLL TO EXPLORE  •  " + list.size() + " MODULES", x, y + s(382), -9338730, 0.56F, 0
      );
      UiTypography.text(g, "FILTER  /  " + this.category, x + s(280), y + s(24), PvpClient.theme().secondary(), 0.56F, 0);
   }

   private void drawSearch(GuiGraphicsExtractor g, int x, int y, int w, int h) {
      g.fill(x + s(2), y + s(2), x + w + s(2), y + h + s(2), 1426063360);
      g.fill(x, y, x + w, y + h, -586410718);
      g.fill(x, y, x + s(2), y + h, PvpClient.theme().accent());
      UiTypography.text(
         g,
         this.search.getValue().isBlank() ? "SEARCH MODULES" : this.search.getValue(),
         x + s(10),
         y + s(8),
         this.search.getValue().isBlank() ? -9338730 : PvpClient.theme().text(),
         0.62F,
         0
      );
   }

   private void drawHud(GuiGraphicsExtractor g, int x, int y) {
      HudLayout h = PvpClient.hudLayout(PvpClient.selectedHud());
      g.fill(x + s(180), y + s(42), x + s(650), y + s(352), -1442312165);
      g.fill(x + s(180), y + s(42), x + s(650), y + s(44), PvpClient.theme().border());
      UiTypography.text(g, "LIVE PREVIEW", x + s(196), y + s(56), PvpClient.theme().secondary(), 0.6F, 0);
      UiTypography.text(
         g, PvpClient.selectedHud().toUpperCase() + "  " + (h.enabled() ? "VISIBLE" : "HIDDEN"), x + s(196), y + s(76), PvpClient.theme().text(), 0.72F, 0
      );
      UiTypography.text(g, "X " + h.x() + "   Y " + h.y() + "   SCALE " + String.format(Locale.ROOT, "%.1f", h.scale()), x + s(196), y + s(98), -8220248, 0.6F, 0);

      for (String id : this.hudIds()) {
         HudLayout q = PvpClient.hudLayout(id);
         int bx = x + s(160) + q.x() / 5;
         int by = y + s(125) + q.y() / 5;
         g.fill(bx, by, bx + s(76), by + s(16), id.equals(PvpClient.selectedHud()) ? PvpClient.theme().accent() : -1439414694);
         UiTypography.text(g, id.toUpperCase(), bx + s(4), by + s(5), -1, 0.5F, 0);
      }

      UiTypography.text(g, "SELECT AN ELEMENT ON THE LEFT, THEN DRAG IN PREVIEW", x, y + s(350), -9338730, 0.56F, 0);
   }

   private void drawProfiles(GuiGraphicsExtractor g, int x, int y) {
      UiTypography.text(g, "ACTIVE  /  " + PvpClient.profiles().active().displayName().toUpperCase(), x, y + s(52), PvpClient.theme().accent(), 0.62F, 0);
      int i = 0;

      for (Profile p : PvpClient.profiles().profiles()) {
         int col = i % 3;
         int row = i++ / 3;
         if (row <= 5) {
            int bx = x + s(col * 190);
            int by = y + s(70 + row * 54);
            boolean active = p.equals(PvpClient.profiles().active());
            g.fill(bx, by, bx + s(178), by + s(42), active ? PvpClient.theme().accent() : -871359959);
            UiTypography.text(g, p.displayName(), bx + s(12), by + s(13), active ? -16313828 : PvpClient.theme().text(), 0.64F, 0);
            UiTypography.text(g, active ? "ACTIVE" : "SELECT", bx + s(12), by + s(27), active ? -16313828 : -9338730, 0.5F, 0);
         }
      }
   }

   private void drawStyle(GuiGraphicsExtractor g, int x, int y) {
      String[] labels = new String[]{
         "ACCENT", "SECONDARY", "BACKGROUND", "PANEL", "RADIUS", "UI SCALE", "ANIMATION", "FONT SCALE", "BLUR", "GLOW", "SHADOW", "HUD SPACING"
      };
      String[] values = new String[]{
         Integer.toHexString(PvpClient.accentColor()),
         Integer.toHexString(PvpClient.secondaryColor()),
         String.format("%.2f", PvpClient.backgroundOpacity()),
         String.format("%.2f", PvpClient.panelOpacity()),
         String.format("%.0f", PvpClient.cornerRadius()),
         String.format("%.2f", PvpClient.uiScale()),
         String.format("%.2f", PvpClient.animationSpeed()),
         String.format("%.2f", PvpClient.fontScale()),
         String.valueOf(PvpClient.blur()),
         String.valueOf(PvpClient.glow()),
         String.valueOf(PvpClient.shadow()),
         String.format("%.0f", PvpClient.hudSpacing())
      };

      for (int i = 0; i < labels.length; i++) {
         int bx = x + s(i % 2 * 210);
         int by = y + s(52 + i / 2 * 30);
         UiTypography.text(g, labels[i], bx + s(10), by + s(9), PvpClient.theme().text(), 0.6F, 0);
         UiTypography.text(g, values[i], bx + s(148), by + s(9), PvpClient.theme().secondary(), 0.56F, 0);
      }

      UiTypography.text(g, "THEMES", x + s(450), y + s(48), PvpClient.theme().secondary(), 0.6F, 0);
      UiTypography.text(g, "REDUCED MOTION  /  " + (PvpClient.reducedMotion() ? "ON" : "OFF"), x, y + s(252), -8220248, 0.6F, 0);
   }

   private void drawSettings(GuiGraphicsExtractor g, int x, int y) {
      UiTypography.text(g, "CLIENT SETTINGS", x + s(214), y + s(52), PvpClient.theme().secondary(), 0.62F, 0);
      UiTypography.text(g, "GLOBAL KEY   " + this.keyName(PvpClient.globalKeybind()), x + s(214), y + s(82), PvpClient.theme().text(), 0.64F, 0);
      UiTypography.text(g, "DEBUG        " + (PvpClient.debugMode() ? "ON" : "OFF"), x + s(214), y + s(112), PvpClient.theme().text(), 0.64F, 0);
      UiTypography.text(g, "RIGHT SHIFT opens this panel in-game", x + s(214), y + s(162), -9338730, 0.56F, 0);
   }

   private void drawButtons(GuiGraphicsExtractor g, int mx, int my) {
      for (GuiEventListener child : this.children()) {
         if (child instanceof Button b) {
            int x = b.getX();
            int y = b.getY();
            int w = b.getWidth();
            int h = b.getHeight();
            boolean hover = mx >= x && mx < x + w && my >= y && my < y + h;
            int fill = hover ? PvpClient.theme().accent() : -1441455563;
            g.fill(x + s(2), y + s(2), x + w + s(2), y + h + s(2), 1140850688);
            g.fill(x, y, x + w, y + h, fill);
            g.fill(x, y, x + (hover ? s(3) : s(1)), y + h, hover ? PvpClient.theme().secondary() : PvpClient.theme().border());
            PARSFontEngine.centered(
               g, b.getMessage().getString(), x + w / 2, y + h / 2, hover ? -16313828 : PvpClient.theme().text(), PARSFontEngine.Token.SMALL, PvpClient.shadow(), false
            );
         }
      }
   }

   public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
      this.scroll = Math.max(0, this.scroll - (int)scrollY);
      this.rebuild();
      return true;
   }

   public boolean mouseClicked(MouseButtonEvent e, boolean doubleClick) {
      if (this.page.equals("hudeditor") && e.button() == 0) {
         for (String id : this.hudIds()) {
            HudLayout h = PvpClient.hudLayout(id);
            int canvasX = this.contentLeft() + s(180);
            int canvasY = this.top() + s(18) + s(42);
            int chipX = canvasX + h.x() / 5;
            int chipY = canvasY + h.y() / 5;
            if (e.x() >= chipX && e.x() <= chipX + s(76) && e.y() >= chipY && e.y() <= chipY + s(16)) {
               PvpClient.setSelectedHud(id);
               this.dragging = id;
               this.dragOffsetX = (int)e.x() - chipX;
               this.dragOffsetY = (int)e.y() - chipY;
               return true;
            }
         }
      }

      return super.mouseClicked(e, doubleClick);
   }

   public boolean mouseDragged(MouseButtonEvent e, double dx, double dy) {
      if (this.page.equals("hudeditor") && this.dragging != null && e.button() == 0) {
         HudLayout h = PvpClient.hudLayout(this.dragging);
         int canvasX = this.contentLeft() + s(180);
         int canvasY = this.top() + s(18) + s(42);
         int px = Math.max(0, (int)e.x() - canvasX - this.dragOffsetX);
         int py = Math.max(0, (int)e.y() - canvasY - this.dragOffsetY);
         int x = Math.clamp(px * 5, 0, 800);
         int y = Math.clamp(py * 5, 0, 480);
         if (h.snapToGrid()) {
            x = x / 8 * 8;
            y = y / 8 * 8;
         }
         PvpClient.setHudLayout(this.dragging, h.move(x, y));
         return true;
      } else {
         return super.mouseDragged(e, dx, dy);
      }
   }

   public boolean mouseReleased(MouseButtonEvent e) {
      this.dragging = null;
      if (this.page.equals("hudeditor")) {
         PvpClient.config().save();
      }

      return super.mouseReleased(e);
   }

   private List<String> hudIds() {
      return new ArrayList<>(
         List.of(
            "fps",
            "cps",
            "keystrokes",
            "coordinates",
            "direction",
            "speed",
            "ping",
            "armor",
            "potions",
            "totem",
            "held_item",
            "offhand",
            "session",
            "memory",
            "crystal",
            "mace",
            "combat",
            "ranged",
            "performance"
         )
      );
   }

   private int nextKey(int key) {
      return key == 0 ? 344 : (key == 344 ? 345 : (key == 345 ? 342 : 0));
   }

   private String keyName(int key) {
      return key == 344 ? "RSHIFT" : (key == 345 ? "RCTRL" : (key == 342 ? "F8" : "NONE"));
   }

   private void rebuild() {
      this.clearWidgets();
      this.init();
   }
}
