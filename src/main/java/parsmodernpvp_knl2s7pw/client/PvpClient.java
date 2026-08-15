package parsmodernpvp_knl2s7pw.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.InputConstants.Type;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents.ClientStarted;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents.ClientStopping;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping.Category;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import parsmodernpvp_knl2s7pw.Parsmodernpvp;
import parsmodernpvp_knl2s7pw.client.config.ConfigManager;
import parsmodernpvp_knl2s7pw.client.cosmetics.CosmeticsManager;
import parsmodernpvp_knl2s7pw.client.cosmetics.CosmeticsWorldEffects;
import parsmodernpvp_knl2s7pw.client.event.EventBus;
import parsmodernpvp_knl2s7pw.client.hud.HudLayout;
import parsmodernpvp_knl2s7pw.client.module.Module;
import parsmodernpvp_knl2s7pw.client.module.ModuleManager;
import parsmodernpvp_knl2s7pw.client.notification.NotificationCenter;
import parsmodernpvp_knl2s7pw.client.performance.PerformanceMonitor;
import parsmodernpvp_knl2s7pw.client.profile.Profile;
import parsmodernpvp_knl2s7pw.client.profile.ProfileManager;
import parsmodernpvp_knl2s7pw.client.resource.ResourceManager;
import parsmodernpvp_knl2s7pw.client.screen.ParsMainMenuScreen;
import parsmodernpvp_knl2s7pw.client.screen.ParsPauseScreen;
import parsmodernpvp_knl2s7pw.client.screen.PvpScreen;
import parsmodernpvp_knl2s7pw.client.settings.Setting;
import parsmodernpvp_knl2s7pw.client.theme.Theme;
import parsmodernpvp_knl2s7pw.client.theme.ThemeEngine;
import parsmodernpvp_knl2s7pw.client.ui.BackgroundEngine;
import parsmodernpvp_knl2s7pw.client.ui.FontEngine;
import parsmodernpvp_knl2s7pw.client.ui.PARSFontEngine;
import parsmodernpvp_knl2s7pw.client.ui.UiScale;

public final class PvpClient {
   private static ModuleManager modules;
   private static ProfileManager profiles;
   private static ConfigManager config;
   private static PerformanceMonitor performance;
   private static CosmeticsManager cosmetics;
   private static NotificationCenter notifications;
   private static final Map<String, HudLayout> HUD_LAYOUTS = new LinkedHashMap<>();
   private static Theme theme = Theme.PARS_NEON;
   private static final ThemeEngine THEME_ENGINE = new ThemeEngine();
   private static boolean hudEditor;
   private static String selectedHud = "fps";
   private static boolean debugMode;
   private static boolean initialized;
   private static boolean hudRegistered;
   private static boolean guiRegistered;
   private static boolean inputRegistered;
   private static String lastRenderError = "none";
   private static int globalKeybind = 344;
   private static boolean configLoaded;
   private static boolean startupFlowShown;
   private static int accentColor = -14108161;
   private static int secondaryColor = -6525953;
   private static float backgroundOpacity = 0.91F;
   private static float panelOpacity = 0.94F;
    private static float cornerRadius = 4.0F;
    private static float uiScale = UiScale.DEFAULT;
   private static float animationSpeed = 1.0F;
   private static float fontScale = 1.0F;
   private static boolean blur = true;
   private static boolean glow = true;
   private static boolean shadow = true;
   private static boolean motionBlur;
   private static boolean vignette = true;
   private static boolean particles = true;
   private static boolean colorGrading;
   private static boolean screenFade = true;
   private static boolean animatedGradient = true;
   private static boolean grid = true;
   private static boolean parallax = true;
   private static boolean softLighting = true;
   private static boolean panelDepth = true;
   private static String fontWeight = "REGULAR";
   private static String performancePreset = "BALANCED";
   private static boolean reducedMotion;
   private static float hudSpacing = 4.0F;
   private static KeyMapping guiKey;
   private static final Map<String, Boolean> MODULE_KEYS = new HashMap<>();
   private static final EventBus<String> EVENTS = new EventBus<>();

   private PvpClient() {
   }

   public static void initialize() {
      if (!initialized) {
         FontEngine.initialize();
         modules = new ModuleManager();
         registerModules();
         profiles = new ProfileManager(modules);
         config = new ConfigManager(modules, profiles);
         performance = new PerformanceMonitor();
         cosmetics = new CosmeticsManager();
         notifications = new NotificationCenter();
         HUD_LAYOUTS.put("main", new HudLayout(8, 8, 1.0F, 1.0F, true, true, -1));
         HUD_LAYOUTS.put("crosshair", new HudLayout(0, 0, 1.0F, 1.0F, true, false, theme.accent()));
         String[] hudIds = new String[]{
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
         };

         for (int i = 0; i < hudIds.length; i++) {
            HUD_LAYOUTS.put(hudIds[i], new HudLayout(8 + i % 2 * 150, 8 + i / 2 * 28, 1.0F, 1.0F, true, true, -1));
         }

         guiKey = new KeyMapping("key.parsmodernpvp.click_gui", Type.KEYSYM, 344, Category.MISC);
         KeyMappingHelper.registerKeyMapping(guiKey);
         guiRegistered = true;
         inputRegistered = true;
         ClientTickEvents.END_CLIENT_TICK.register((EndTick)client -> {
            ensureConfigLoaded(client);
            performance.tick();
            if (client.player != null) {
               performance.observe(client.player);
            }

            performance.observeMouse(client.mouseHandler.isLeftPressed(), client.mouseHandler.isRightPressed());
            CosmeticsWorldEffects.tick(client);
            activateTitleScreen(client);
            if (client.player != null && client.screen instanceof PauseScreen) {
               client.setScreen(new ParsPauseScreen());
            }

            if (guiKey.consumeClick() && client.player != null) {
               if (client.screen instanceof PvpScreen) {
                  client.setScreen(null);
               } else if (client.screen == null || client.screen instanceof ParsPauseScreen) {
                  client.setScreen(new PvpScreen());
               }
            }

            updateModuleKeybinds(client);
         });
         ClientLifecycleEvents.CLIENT_STARTED.register((ClientStarted)client -> {
            ensureConfigLoaded(client);
            FontEngine.verifyResources(client.getResourceManager());
            ResourceManager.warmCaches();
            activateTitleScreen(client);
         });
         ClientLifecycleEvents.CLIENT_STOPPING.register((ClientStopping)client -> config.save());
         modules.onChange((module, enabled) -> {
            notifications.push(module.displayName() + (enabled ? " enabled" : " disabled"));
            EVENTS.post(module.id());
            if (config != null) {
               config.save();
            }
         });
         Parsmodernpvp.LOGGER.info("PARSModernPvP client ready: fair-play HUD, profiles and Click GUI");
         initialized = true;
      }
   }

   private static void ensureConfigLoaded(Minecraft client) {
      if (!configLoaded && config != null) {
         config.load();
         configLoaded = true;
         ResourceManager.warmCaches();
      }
   }

   private static void activateTitleScreen(Minecraft client) {
      if (!startupFlowShown && client.player == null && client.screen instanceof TitleScreen) {
         startupFlowShown = true;
         client.setScreen(new ParsMainMenuScreen());
      }
   }

   private static void registerModules() {
      modules.register("pvp_hud", "PvP Awareness HUD", "HUD").setEnabled(true);
      modules.register("crystal_hud", "Crystal PvP HUD", "HUD").setEnabled(true);
      modules.register("mace_hud", "Mace PvP HUD", "HUD").setEnabled(true);
      modules.register("sword_axe_hud", "Sword / Axe HUD", "HUD").setEnabled(true);
      modules.register("ranged_hud", "Bow / Crossbow HUD", "HUD").setEnabled(true);
      modules.register("potion_hud", "Potion HUD", "HUD").setEnabled(true);
      modules.register("armor_hud", "Armor HUD", "HUD").setEnabled(true);
      modules.register("crosshair", "Crosshair", "RENDER").setEnabled(true);
      modules.register("combat_info", "Combat Information", "INFO").setEnabled(true);
      modules.register("performance", "Performance", "SYSTEM").setEnabled(true);
      modules.register("cosmetics", "Cosmetics", "VISUAL").setEnabled(false);
      modules.register("hud_fps", "FPS", "HUD").setEnabled(true);
      modules.register("hud_cps", "CPS", "HUD").setEnabled(true);
      modules.register("hud_keystrokes", "Keystrokes", "HUD").setEnabled(true);
      modules.register("hud_coordinates", "Coordinates", "HUD").setEnabled(true);
      modules.register("hud_direction", "Direction", "HUD").setEnabled(true);
      modules.register("hud_speed", "Speed", "HUD").setEnabled(true);
      modules.register("hud_ping", "Ping", "HUD").setEnabled(true);
      modules.register("hud_armor", "Armor", "HUD").setEnabled(true);
      modules.register("hud_potions", "Potions", "HUD").setEnabled(true);
      modules.register("hud_totem", "Totem", "HUD").setEnabled(true);
      modules.register("hud_held_item", "Held Item", "HUD").setEnabled(true);
      modules.register("hud_offhand", "Offhand", "HUD").setEnabled(true);
      modules.register("hud_session", "Session", "HUD").setEnabled(true);
      modules.register("hud_memory", "Memory", "HUD").setEnabled(true);
      modules.get("pvp_hud").settings().register("show_memory", true);
      modules.get("pvp_hud").settings().register("show_coordinates", true);
      modules.get("pvp_hud").settings().register("show_session", true);
      modules.get("pvp_hud").settings().register("armor_vertical", false);
      modules.get("crystal_hud").settings().register("show_radius", true);
      modules.get("crystal_hud").settings().register("show_damage", true);
      modules.get("crosshair").settings().register("style", "Plus");
      modules.get("crosshair").settings().register("size", 7);
      modules.get("crosshair").settings().register("thickness", 2);
      modules.get("crosshair").settings().register("gap", 3);
      modules.get("crosshair").settings().register("outline", true);
      modules.get("crosshair").settings().register("opacity", 85);
      modules.get("crosshair").settings().register("hit_marker", true);
      modules.get("crosshair").settings().register("attack_indicator", true);
      modules.get("crosshair").settings().register("color", accentColor);
      modules.get("crosshair").settings().register("custom_style", "Plus");
   }

   public static ModuleManager modules() {
      return modules;
   }

   public static ProfileManager profiles() {
      return profiles;
   }

   public static ConfigManager config() {
      return config;
   }

   public static PerformanceMonitor performance() {
      return performance;
   }

   public static CosmeticsManager cosmetics() {
      return cosmetics;
   }

   public static Theme theme() {
      return THEME_ENGINE.withOpacity(backgroundOpacity, panelOpacity, accentColor, secondaryColor);
   }

   public static ThemeEngine themeEngine() {
      return THEME_ENGINE;
   }

   public static void setTheme(Theme next) {
      if (next != null) {
         theme = next;
         THEME_ENGINE.setActive(next);
         accentColor = next.accent();
         secondaryColor = next.secondary();
         THEME_ENGINE.setOverrides(accentColor, secondaryColor, backgroundOpacity, panelOpacity);
         FontEngine.clearCache();
      }
   }

   public static NotificationCenter notifications() {
      return notifications;
   }

   public static EventBus<String> events() {
      return EVENTS;
   }

   public static HudLayout hudLayout(String id) {
      return HUD_LAYOUTS.getOrDefault(id, HUD_LAYOUTS.get("main"));
   }

   public static void setHudLayout(String id, HudLayout layout) {
      if (id != null && layout != null) {
         HUD_LAYOUTS.put(id, layout);
      }
   }

   public static Map<String, HudLayout> hudLayouts() {
      return Map.copyOf(HUD_LAYOUTS);
   }

   public static boolean hudEditor() {
      return hudEditor;
   }

   public static void setHudEditor(boolean editing) {
      hudEditor = editing;
   }

   public static String selectedHud() {
      return selectedHud;
   }

   public static void setSelectedHud(String id) {
      if (HUD_LAYOUTS.containsKey(id)) {
         selectedHud = id;
      }
   }

   public static boolean debugMode() {
      return debugMode;
   }

   public static void setDebugMode(boolean value) {
      debugMode = value;
   }

   public static int globalKeybind() {
      return globalKeybind;
   }

   public static void setGlobalKeybind(int key) {
      globalKeybind = key;
      if (guiKey != null) {
         guiKey.setKey(Type.KEYSYM.getOrCreate(key));
      }
   }

   public static KeyMapping guiKey() {
      return guiKey;
   }

   public static int accentColor() {
      return accentColor;
   }

   public static int secondaryColor() {
      return secondaryColor;
   }

   public static float backgroundOpacity() {
      return backgroundOpacity;
   }

   public static float panelOpacity() {
      return panelOpacity;
   }

   public static float cornerRadius() {
      return cornerRadius;
   }

   public static float uiScale() {
      return uiScale;
   }

   public static float animationSpeed() {
      return animationSpeed;
   }

   public static float fontScale() {
      return fontScale;
   }

   public static boolean blur() {
      return blur && !lowPerformance();
   }

   public static boolean glow() {
      return glow && !"LOW".equals(performancePreset);
   }

   public static boolean shadow() {
      return shadow && panelDepth;
   }

   public static boolean configuredBlur() {
      return blur;
   }

   public static boolean configuredGlow() {
      return glow;
   }

   public static boolean configuredShadow() {
      return shadow;
   }

   public static boolean reducedMotion() {
      return reducedMotion;
   }

   public static boolean motionBlur() {
      return motionBlur && !lowPerformance();
   }

   public static boolean vignette() {
      return vignette;
   }

   public static boolean particles() {
      return particles && !lowPerformance();
   }

   public static boolean colorGrading() {
      return colorGrading && !"LOW".equals(performancePreset);
   }

   public static boolean screenFade() {
      return screenFade && !reducedMotion;
   }

   public static boolean animatedGradient() {
      return animatedGradient && !reducedMotion;
   }

   public static boolean grid() {
      return grid && !"LOW".equals(performancePreset);
   }

   public static boolean parallax() {
      return parallax && !reducedMotion;
   }

   public static boolean softLighting() {
      return softLighting && !"LOW".equals(performancePreset);
   }

   public static boolean panelDepth() {
      return panelDepth && !lowPerformance();
   }

   public static String fontWeight() {
      return fontWeight;
   }

   public static String performancePreset() {
      return performancePreset;
   }

   public static void setReducedMotion(boolean value) {
      reducedMotion = value;
   }

   public static float hudSpacing() {
      return hudSpacing;
   }

   public static void setCustomization(
      int accent,
      int secondary,
      float bg,
      float panel,
      float radius,
      float scale,
      float animation,
      float font,
      boolean blurValue,
      boolean glowValue,
      boolean shadowValue,
      float spacing
   ) {
      accentColor = accent;
      secondaryColor = secondary;
      backgroundOpacity = clamp(bg, 0.2F, 1.0F);
      panelOpacity = clamp(panel, 0.2F, 1.0F);
      cornerRadius = clamp(radius, 0.0F, 12.0F);
      uiScale = clamp(scale, 0.75F, 1.5F);
      animationSpeed = clamp(animation, 0.1F, 3.0F);
      fontScale = clamp(font, 0.75F, 1.5F);
      blur = blurValue;
      glow = glowValue;
      shadow = shadowValue;
      hudSpacing = clamp(spacing, 0.0F, 16.0F);
      THEME_ENGINE.setOverrides(accentColor, secondaryColor, backgroundOpacity, panelOpacity);
      PARSFontEngine.clearCaches();
   }

   private static float clamp(float value, float min, float max) {
      return Math.max(min, Math.min(max, value));
   }

   private static int withAlpha(int color, float alpha) {
      return (int)((color >>> 24 & 0xFF) * alpha) << 24 | color & 16777215;
   }

   public static void cycleCustomization(String option) {
      switch (option) {
         case "accent":
            accentColor = accentColor == -14108161 ? -6525953 : (accentColor == -6525953 ? -45158 : -14108161);
            break;
         case "secondary":
            secondaryColor = secondaryColor == -6525953 ? -14249 : (secondaryColor == -14249 ? -12392796 : -6525953);
            break;
         case "background":
            backgroundOpacity = backgroundOpacity >= 0.95F ? 0.65F : backgroundOpacity + 0.1F;
            break;
         case "panel":
            panelOpacity = panelOpacity >= 0.95F ? 0.55F : panelOpacity + 0.1F;
            break;
         case "radius":
            cornerRadius = cornerRadius >= 12.0F ? 0.0F : cornerRadius + 4.0F;
            break;
         case "ui_scale":
            uiScale = uiScale >= 1.5F ? 0.75F : uiScale + 0.25F;
            break;
         case "animation":
            animationSpeed = animationSpeed >= 3.0F ? 0.25F : animationSpeed + 0.5F;
            break;
         case "font":
            fontScale = fontScale >= 1.5F ? 0.75F : fontScale + 0.25F;
            break;
         case "blur":
            blur = !blur;
            break;
         case "glow":
            glow = !glow;
            break;
         case "shadow":
            shadow = !shadow;
            break;
         case "spacing":
            hudSpacing = hudSpacing >= 16.0F ? 0.0F : hudSpacing + 4.0F;
            break;
         case "reduced_motion":
            reducedMotion = !reducedMotion;
            break;
         case "motion_blur":
            motionBlur = !motionBlur;
            break;
         case "vignette":
            vignette = !vignette;
            break;
         case "particles":
            particles = !particles;
            break;
         case "color_grading":
            colorGrading = !colorGrading;
            break;
         case "screen_fade":
            screenFade = !screenFade;
            break;
         case "animated_gradient":
            animatedGradient = !animatedGradient;
            break;
         case "grid":
            grid = !grid;
            break;
         case "parallax":
            parallax = !parallax;
            break;
         case "soft_lighting":
            softLighting = !softLighting;
            break;
         case "panel_depth":
            panelDepth = !panelDepth;
            break;
         case "font_weight":
            fontWeight = switch (fontWeight) {
               case "REGULAR" -> "MEDIUM";
               case "MEDIUM" -> "SEMIBOLD";
               case "SEMIBOLD" -> "BOLD";
               default -> "REGULAR";
            };
            break;
         case "performance":
            setPerformancePreset(switch (performancePreset) {
               case "LOW" -> "BALANCED";
               case "BALANCED" -> "HIGH";
               case "HIGH" -> "ULTRA";
               default -> "LOW";
            });
            break;
         case "background_mode":
            BackgroundEngine.setType(switch (BackgroundEngine.type()) {
               case NEBULA -> BackgroundEngine.Type.PARTICLES;
               case PARTICLES -> BackgroundEngine.Type.GRID;
               case GRID -> BackgroundEngine.Type.STARS;
               case STARS -> BackgroundEngine.Type.MINIMAL;
               default -> BackgroundEngine.Type.NEBULA;
            });
            break;
         case "background_preset":
            BackgroundEngine.setPreset(switch (BackgroundEngine.preset()) {
               case LOW -> BackgroundEngine.Preset.BALANCED;
               case BALANCED -> BackgroundEngine.Preset.HIGH;
               case HIGH -> BackgroundEngine.Preset.ULTRA;
               default -> BackgroundEngine.Preset.LOW;
            });
      }
   }

   public static void resetCustomization() {
      setCustomization(-14108161, -6525953, 0.91F, 0.94F, 4.0F, UiScale.DEFAULT, 1.0F, 1.0F, true, true, true, 4.0F);
      motionBlur = false;
      vignette = true;
      particles = true;
      colorGrading = false;
      screenFade = true;
      animatedGradient = true;
      grid = true;
      parallax = true;
      softLighting = true;
      panelDepth = true;
      fontWeight = "REGULAR";
      setPerformancePreset("BALANCED");
   }

   public static Map<String, String> customizationSnapshot() {
      Map<String, String> values = new HashMap<>();
      values.put("accent", String.valueOf(accentColor));
      values.put("secondary", String.valueOf(secondaryColor));
      values.put("background", String.valueOf(backgroundOpacity));
      values.put("panel", String.valueOf(panelOpacity));
      values.put("radius", String.valueOf(cornerRadius));
      values.put("uiScale", String.valueOf(uiScale));
      values.put("animation", String.valueOf(animationSpeed));
      values.put("font", String.valueOf(fontScale));
      values.put("blur", String.valueOf(blur));
      values.put("glow", String.valueOf(glow));
      values.put("shadow", String.valueOf(shadow));
      values.put("spacing", String.valueOf(hudSpacing));
      values.put("reducedMotion", String.valueOf(reducedMotion));
      values.put("motionBlur", String.valueOf(motionBlur));
      values.put("vignette", String.valueOf(vignette));
      values.put("particles", String.valueOf(particles));
      values.put("colorGrading", String.valueOf(colorGrading));
      values.put("screenFade", String.valueOf(screenFade));
      values.put("animatedGradient", String.valueOf(animatedGradient));
      values.put("grid", String.valueOf(grid));
      values.put("parallax", String.valueOf(parallax));
      values.put("softLighting", String.valueOf(softLighting));
      values.put("panelDepth", String.valueOf(panelDepth));
      values.put("fontWeight", fontWeight);
      values.put("performancePreset", performancePreset);
      return values;
   }

   public static void applyCustomizationSnapshot(Map<String, String> values) {
      if (values != null) {
         setCustomization(
            Integer.parseInt(values.getOrDefault("accent", String.valueOf(accentColor))),
            Integer.parseInt(values.getOrDefault("secondary", String.valueOf(secondaryColor))),
            Float.parseFloat(values.getOrDefault("background", String.valueOf(backgroundOpacity))),
            Float.parseFloat(values.getOrDefault("panel", String.valueOf(panelOpacity))),
            Float.parseFloat(values.getOrDefault("radius", String.valueOf(cornerRadius))),
            Float.parseFloat(values.getOrDefault("uiScale", String.valueOf(uiScale))),
            Float.parseFloat(values.getOrDefault("animation", String.valueOf(animationSpeed))),
            Float.parseFloat(values.getOrDefault("font", String.valueOf(fontScale))),
            Boolean.parseBoolean(values.getOrDefault("blur", String.valueOf(blur))),
            Boolean.parseBoolean(values.getOrDefault("glow", String.valueOf(glow))),
            Boolean.parseBoolean(values.getOrDefault("shadow", String.valueOf(shadow))),
            Float.parseFloat(values.getOrDefault("spacing", String.valueOf(hudSpacing)))
         );
         reducedMotion = Boolean.parseBoolean(values.getOrDefault("reducedMotion", String.valueOf(reducedMotion)));
         motionBlur = Boolean.parseBoolean(values.getOrDefault("motionBlur", "false"));
         vignette = Boolean.parseBoolean(values.getOrDefault("vignette", "true"));
         particles = Boolean.parseBoolean(values.getOrDefault("particles", "true"));
         colorGrading = Boolean.parseBoolean(values.getOrDefault("colorGrading", "false"));
         screenFade = Boolean.parseBoolean(values.getOrDefault("screenFade", "true"));
         animatedGradient = Boolean.parseBoolean(values.getOrDefault("animatedGradient", "true"));
         grid = Boolean.parseBoolean(values.getOrDefault("grid", "true"));
         parallax = Boolean.parseBoolean(values.getOrDefault("parallax", "true"));
         softLighting = Boolean.parseBoolean(values.getOrDefault("softLighting", "true"));
         panelDepth = Boolean.parseBoolean(values.getOrDefault("panelDepth", "true"));
         fontWeight = values.getOrDefault("fontWeight", "REGULAR");
         setPerformancePreset(values.getOrDefault("performancePreset", "BALANCED"));
      }
   }

   public static void setPerformancePreset(String value) {
      performancePreset = switch (value == null ? "BALANCED" : value.toUpperCase()) {
         case "LOW", "HIGH", "ULTRA" -> value.toUpperCase();
         default -> "BALANCED";
      };
      BackgroundEngine.setPreset(BackgroundEngine.Preset.valueOf(performancePreset));
   }

   public static boolean lowPerformance() {
      return "LOW".equals(performancePreset);
   }

   public static String configPath() {
      return config == null ? "config/parsmodernpvp.json" : config.configPath().toString();
   }

   public static void renderError(String error) {
      lastRenderError = error == null ? "unknown" : error;
      if (notifications != null) {
         notifications.push("HUD render error: " + lastRenderError);
      }
   }

   public static String lastRenderError() {
      return lastRenderError;
   }

   public static void openGui() {
      Minecraft.getInstance().setScreen(new PvpScreen());
   }

   public static boolean isCustomCrosshairEnabled() {
      return modules != null && modules.get("crosshair") != null && modules.get("crosshair").enabled();
   }

   public static void markHudRendered() {
      hudRegistered = true;
   }

   public static boolean initialized() {
      return initialized;
   }

   public static boolean hudRegistered() {
      return hudRegistered;
   }

   public static boolean guiRegistered() {
      return guiRegistered;
   }

   public static boolean inputRegistered() {
      return inputRegistered;
   }

   public static void applyProfilePreset(Profile profile) {
      if (profile != null && modules != null) {
         if (profile == Profile.CRYSTAL) {
            setTheme(Theme.PARS_NEON);
            setCrosshair("Plus", 7, 2);
         } else if (profile == Profile.MACE) {
            setTheme(Theme.PARS_PURPLE);
            setCrosshair("Circle", 9, 2);
         } else if (profile == Profile.SWORD || profile == Profile.AXE) {
            setTheme(Theme.PARS_BLUE);
            setCrosshair("Plus", 6, 2);
         } else if (profile == Profile.PERFORMANCE) {
            setTheme(Theme.MINIMAL);
            setCrosshair("Minimal", 4, 1);
            setCustomization(accentColor, secondaryColor, backgroundOpacity, panelOpacity, 0.0F, 0.9F, 0.5F, 0.9F, false, false, false, 2.0F);
         } else if (profile != Profile.SKYBLOCK && profile != Profile.BEDWARS) {
            setTheme(Theme.PARS_NEON);
            setCrosshair("Plus", 7, 2);
         } else {
            setTheme(Theme.PARS_BLUE);
            setCrosshair("Dot", 4, 2);
         }
      }
   }

   private static void setCrosshair(String style, int size, int thickness) {
      Module crosshair = modules.get("crosshair");
      if (crosshair != null) {
         ((Setting<String>)crosshair.settings().get("style")).setValue(style);
         ((Setting<Integer>)crosshair.settings().get("size")).setValue(size);
         ((Setting<Integer>)crosshair.settings().get("thickness")).setValue(thickness);
      }
   }

   private static void updateModuleKeybinds(Minecraft client) {
      if (client.player != null && client.screen == null) {
         modules.all().values().forEach(module -> {
            int key = module.keybind();
            if (key > 0 && key != globalKeybind) {
               boolean down = InputConstants.isKeyDown(client.getWindow(), key);
               boolean previous = MODULE_KEYS.getOrDefault(module.id(), false);
               if (down && !previous) {
                  modules.toggle(module.id());
               }

               MODULE_KEYS.put(module.id(), down);
            }
         });
      } else {
         MODULE_KEYS.clear();
      }
   }
}
