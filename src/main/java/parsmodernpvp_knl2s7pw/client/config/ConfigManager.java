package parsmodernpvp_knl2s7pw.client.config;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.LinkedHashMap;
import java.util.Map;
import net.fabricmc.loader.api.FabricLoader;
import parsmodernpvp_knl2s7pw.client.PvpClient;
import parsmodernpvp_knl2s7pw.client.hud.HudLayout;
import parsmodernpvp_knl2s7pw.client.module.Module;
import parsmodernpvp_knl2s7pw.client.module.ModuleManager;
import parsmodernpvp_knl2s7pw.client.profile.ProfileManager;
import parsmodernpvp_knl2s7pw.client.settings.Setting;
import parsmodernpvp_knl2s7pw.client.theme.Theme;
import parsmodernpvp_knl2s7pw.client.ui.UiSoundEngine;

public final class ConfigManager {
   public static final int CURRENT_VERSION = 5;
   private final ModuleManager modules;
   private final ProfileManager profiles;
   private final Path configPath;
   private String lastError = "";

   public ConfigManager(ModuleManager modules, ProfileManager profiles) {
      this.modules = modules;
      this.profiles = profiles;
      this.configPath = FabricLoader.getInstance().getConfigDir().resolve("parsmodernpvp.json");
   }

   public void load() {
      if (Files.isRegularFile(this.configPath)) {
         try {
            JsonObject root = JsonParser.parseString(Files.readString(this.configPath)).getAsJsonObject();
            int version = root.has("version") ? root.get("version").getAsInt() : 1;
            if (version > 5) {
               this.lastError = "Config was created by a newer PARS version; defaults were kept.";
               return;
            }

            JsonObject enabled = root.has("modules") ? root.getAsJsonObject("modules") : new JsonObject();
            this.modules.all().forEach((id, module) -> {
               if (enabled.has(id)) {
                  module.setEnabled(enabled.get(id).getAsBoolean());
               }

               if (root.has("keybinds") && root.getAsJsonObject("keybinds").has(id)) {
                  module.setKeybind(root.getAsJsonObject("keybinds").get(id).getAsInt());
               }

               if (root.has("settings") && root.getAsJsonObject("settings").has(id)) {
                  loadSettings(module, root.getAsJsonObject("settings").getAsJsonObject(id));
               }
            });
            if (root.has("profiles")) {
               this.loadProfiles(root.getAsJsonObject("profiles"));
            }

            if (root.has("activeProfile")) {
               this.profiles.restoreActive(root.get("activeProfile").getAsString());
            }

            if (root.has("theme")) {
               PvpClient.setTheme(Theme.byName(root.get("theme").getAsString()));
            }

            if (root.has("hud")) {
               loadHud(root.getAsJsonObject("hud"));
            }

            if (root.has("globalKeybind")) {
               PvpClient.setGlobalKeybind(root.get("globalKeybind").getAsInt());
            }

            if (root.has("debugMode")) {
               PvpClient.setDebugMode(root.get("debugMode").getAsBoolean());
            }

            if (root.has("customization")) {
               loadCustomization(root.getAsJsonObject("customization"));
            }

            if (root.has("audio")) {
               loadAudio(root.getAsJsonObject("audio"));
            }
         } catch (Exception exception) {
            this.lastError = "Config could not be read safely: " + exception.getClass().getSimpleName();

            try {
               Files.copy(this.configPath, this.configPath.resolveSibling("parsmodernpvp.json.corrupt"), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException var4) {
            }
         }
      }
   }

   public void save() {
      this.profiles.saveActive();
      JsonObject root = new JsonObject();
      root.addProperty("version", 5);
      JsonObject enabled = new JsonObject();
      JsonObject keybinds = new JsonObject();
      JsonObject settings = new JsonObject();
      this.modules.all().forEach((id, module) -> {
         enabled.addProperty(id, module.enabled());
         keybinds.addProperty(id, module.keybind());
         JsonObject moduleSettings = new JsonObject();
         module.settings().values().forEach((settingId, setting) -> addSetting(moduleSettings, settingId, setting.value()));
         settings.add(id, moduleSettings);
      });
      root.add("modules", enabled);
      root.add("keybinds", keybinds);
      root.add("settings", settings);
      root.addProperty("activeProfile", this.profiles.active().id());
      JsonObject profileData = new JsonObject();
      JsonObject custom = new JsonObject();
      this.profiles.customNames().forEach(custom::addProperty);
      JsonObject snapshots = new JsonObject();
      this.profiles.snapshots().forEach((id, values) -> {
         JsonObject snapshot = new JsonObject();
         values.forEach(snapshot::addProperty);
         snapshots.add(id, snapshot);
      });
      JsonObject keySnapshots = new JsonObject();
      this.profiles.keybindSnapshots().forEach((id, values) -> {
         JsonObject snapshot = new JsonObject();
         values.forEach(snapshot::addProperty);
         keySnapshots.add(id, snapshot);
      });
      JsonObject themes = new JsonObject();
      this.profiles.themeSnapshots().forEach(themes::addProperty);
      JsonObject customStates = new JsonObject();
      this.profiles.customizationSnapshots().forEach((id, values) -> {
         JsonObject state = new JsonObject();
         values.forEach(state::addProperty);
         customStates.add(id, state);
      });
      JsonObject settingStates = new JsonObject();
      this.profiles.settingSnapshots().forEach((id, values) -> {
         JsonObject state = new JsonObject();
         values.forEach(state::addProperty);
         settingStates.add(id, state);
      });
      JsonObject hudStates = new JsonObject();
      this.profiles.hudSnapshots().forEach((id, layouts) -> {
         JsonObject state = new JsonObject();
         layouts.forEach((hudId, layout) -> {
            JsonObject value = new JsonObject();
            value.addProperty("x", layout.x());
            value.addProperty("y", layout.y());
            value.addProperty("scale", layout.scale());
            value.addProperty("opacity", layout.opacity());
            value.addProperty("enabled", layout.enabled());
            value.addProperty("snap", layout.snapToGrid());
            value.addProperty("color", layout.color());
            state.add(hudId, value);
         });
         hudStates.add(id, state);
      });
      profileData.add("custom", custom);
      profileData.add("snapshots", snapshots);
      profileData.add("keybindSnapshots", keySnapshots);
      profileData.add("themes", themes);
      profileData.add("customStates", customStates);
      profileData.add("settingStates", settingStates);
      profileData.add("hudStates", hudStates);
      root.add("profiles", profileData);
      root.addProperty("theme", PvpClient.theme().name());
      root.addProperty("globalKeybind", PvpClient.globalKeybind());
      root.addProperty("debugMode", PvpClient.debugMode());
      JsonObject customization = new JsonObject();
      customization.addProperty("accent", PvpClient.accentColor());
      customization.addProperty("secondary", PvpClient.secondaryColor());
      customization.addProperty("backgroundOpacity", PvpClient.backgroundOpacity());
      customization.addProperty("panelOpacity", PvpClient.panelOpacity());
      customization.addProperty("cornerRadius", PvpClient.cornerRadius());
      customization.addProperty("uiScale", PvpClient.uiScale());
      customization.addProperty("animationSpeed", PvpClient.animationSpeed());
      customization.addProperty("fontScale", PvpClient.fontScale());
      customization.addProperty("blur", PvpClient.configuredBlur());
      customization.addProperty("glow", PvpClient.configuredGlow());
      customization.addProperty("shadow", PvpClient.configuredShadow());
      customization.addProperty("hudSpacing", PvpClient.hudSpacing());
      customization.addProperty("reducedMotion", PvpClient.reducedMotion());
      Map<String, String> rawCustomization = PvpClient.customizationSnapshot();
      customization.addProperty("motionBlur", Boolean.parseBoolean(rawCustomization.get("motionBlur")));
      customization.addProperty("vignette", Boolean.parseBoolean(rawCustomization.get("vignette")));
      customization.addProperty("particles", Boolean.parseBoolean(rawCustomization.get("particles")));
      customization.addProperty("colorGrading", Boolean.parseBoolean(rawCustomization.get("colorGrading")));
      customization.addProperty("screenFade", Boolean.parseBoolean(rawCustomization.get("screenFade")));
      customization.addProperty("animatedGradient", Boolean.parseBoolean(rawCustomization.get("animatedGradient")));
      customization.addProperty("grid", Boolean.parseBoolean(rawCustomization.get("grid")));
      customization.addProperty("parallax", Boolean.parseBoolean(rawCustomization.get("parallax")));
      customization.addProperty("softLighting", Boolean.parseBoolean(rawCustomization.get("softLighting")));
      customization.addProperty("panelDepth", Boolean.parseBoolean(rawCustomization.get("panelDepth")));
      customization.addProperty("fontWeight", rawCustomization.get("fontWeight"));
      customization.addProperty("performancePreset", rawCustomization.get("performancePreset"));
      root.add("customization", customization);
      JsonObject audio = new JsonObject();
      audio.addProperty("master", UiSoundEngine.master());
      audio.addProperty("ui", UiSoundEngine.ui());
      audio.addProperty("notifications", UiSoundEngine.notifications());
      audio.addProperty("muted", UiSoundEngine.muted());
      root.add("audio", audio);
      JsonObject hud = new JsonObject();
      PvpClient.hudLayouts().forEach((id, layout) -> {
         JsonObject value = new JsonObject();
         value.addProperty("x", layout.x());
         value.addProperty("y", layout.y());
         value.addProperty("scale", layout.scale());
         value.addProperty("opacity", layout.opacity());
         value.addProperty("enabled", layout.enabled());
         value.addProperty("snap", layout.snapToGrid());
         value.addProperty("color", layout.color());
         hud.add(id, value);
      });
      root.add("hud", hud);

      try {
         Files.createDirectories(this.configPath.getParent());
         Path temp = this.configPath.resolveSibling(this.configPath.getFileName() + ".tmp");
         Files.writeString(temp, new GsonBuilder().setPrettyPrinting().create().toJson(root), StandardCharsets.UTF_8);

         try {
            Files.move(temp, this.configPath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
         } catch (IOException atomicUnsupported) {
            Files.move(temp, this.configPath, StandardCopyOption.REPLACE_EXISTING);
         }

         this.lastError = "";
      } catch (IOException exception) {
         this.lastError = "Config could not be saved: " + exception.getClass().getSimpleName();
      }
   }

   public void exportTo(Path destination) throws IOException {
      this.save();
      Files.copy(this.configPath, destination, StandardCopyOption.REPLACE_EXISTING);
   }

   public void importFrom(Path source) throws IOException {
      Files.copy(source, this.configPath, StandardCopyOption.REPLACE_EXISTING);
      this.load();
   }

   public void reset() {
      this.modules.all().values().forEach(module -> {
         module.setEnabled(false);
         module.settings().reset();
         module.setKeybind(0);
      });
      this.modules.all().values().forEach(module -> module.setEnabled(true));
      PvpClient.resetCustomization();
      PvpClient.setGlobalKeybind(344);
      PvpClient.setDebugMode(false);
      PvpClient.hudLayouts().forEach((id, layout) -> PvpClient.setHudLayout(id, layout.reset()));
      this.save();
   }

   public String lastError() {
      return this.lastError;
   }

   public Path configPath() {
      return this.configPath;
   }

   private static void loadHud(JsonObject hud) {
      hud.entrySet()
         .forEach(
            entry -> {
               JsonObject value = ((JsonElement)entry.getValue()).getAsJsonObject();
               HudLayout fallback = PvpClient.hudLayout((String)entry.getKey());
               PvpClient.setHudLayout(
                  (String)entry.getKey(),
                  new HudLayout(
                     value.has("x") ? value.get("x").getAsInt() : fallback.x(),
                     value.has("y") ? value.get("y").getAsInt() : fallback.y(),
                     value.has("scale") ? value.get("scale").getAsFloat() : fallback.scale(),
                     value.has("opacity") ? value.get("opacity").getAsFloat() : fallback.opacity(),
                     !value.has("enabled") || value.get("enabled").getAsBoolean(),
                     !value.has("snap") || value.get("snap").getAsBoolean(),
                     value.has("color") ? value.get("color").getAsInt() : fallback.color()
                  )
               );
            }
         );
   }

   private static void loadCustomization(JsonObject value) {
      PvpClient.setCustomization(
         value.has("accent") ? value.get("accent").getAsInt() : PvpClient.accentColor(),
         value.has("secondary") ? value.get("secondary").getAsInt() : PvpClient.secondaryColor(),
         value.has("backgroundOpacity") ? value.get("backgroundOpacity").getAsFloat() : PvpClient.backgroundOpacity(),
         value.has("panelOpacity") ? value.get("panelOpacity").getAsFloat() : PvpClient.panelOpacity(),
         value.has("cornerRadius") ? value.get("cornerRadius").getAsFloat() : PvpClient.cornerRadius(),
         value.has("uiScale") ? value.get("uiScale").getAsFloat() : PvpClient.uiScale(),
         value.has("animationSpeed") ? value.get("animationSpeed").getAsFloat() : PvpClient.animationSpeed(),
         value.has("fontScale") ? value.get("fontScale").getAsFloat() : PvpClient.fontScale(),
         !value.has("blur") || value.get("blur").getAsBoolean(),
         !value.has("glow") || value.get("glow").getAsBoolean(),
         !value.has("shadow") || value.get("shadow").getAsBoolean(),
         value.has("hudSpacing") ? value.get("hudSpacing").getAsFloat() : PvpClient.hudSpacing()
      );
      PvpClient.setReducedMotion(value.has("reducedMotion") && value.get("reducedMotion").getAsBoolean());
      PvpClient.applyCustomizationSnapshot(toMap(value));
   }

   private static Map<String, String> toMap(JsonObject value) {
      Map<String, String> result = new LinkedHashMap<>();
      value.entrySet().forEach(entry -> result.put((String)entry.getKey(), ((JsonElement)entry.getValue()).getAsString()));
      return result;
   }

   private static void loadAudio(JsonObject value) {
      if (value.has("master")) {
         UiSoundEngine.setMaster(value.get("master").getAsFloat());
      }

      if (value.has("ui")) {
         UiSoundEngine.setUi(value.get("ui").getAsFloat());
      }

      if (value.has("notifications")) {
         UiSoundEngine.setNotifications(value.get("notifications").getAsFloat());
      }

      if (value.has("muted")) {
         UiSoundEngine.setMuted(value.get("muted").getAsBoolean());
      }
   }

   private void loadProfiles(JsonObject data) {
      if (data.has("custom")) {
         data.getAsJsonObject("custom")
            .entrySet()
            .forEach(entry -> this.profiles.restoreCustom((String)entry.getKey(), ((JsonElement)entry.getValue()).getAsString()));
      }

      if (data.has("snapshots")) {
         Map<String, Map<String, Boolean>> snapshots = new LinkedHashMap<>();
         data.getAsJsonObject("snapshots")
            .entrySet()
            .forEach(
               profile -> {
                  Map<String, Boolean> values = new LinkedHashMap<>();
                  ((JsonElement)profile.getValue())
                     .getAsJsonObject()
                     .entrySet()
                     .forEach(value -> values.put((String)value.getKey(), ((JsonElement)value.getValue()).getAsBoolean()));
                  snapshots.put((String)profile.getKey(), values);
               }
            );
         this.profiles.restoreSnapshots(snapshots);
      }

      if (data.has("keybindSnapshots")) {
         Map<String, Map<String, Integer>> snapshots = new LinkedHashMap<>();
         data.getAsJsonObject("keybindSnapshots")
            .entrySet()
            .forEach(
               profile -> {
                  Map<String, Integer> values = new LinkedHashMap<>();
                  ((JsonElement)profile.getValue())
                     .getAsJsonObject()
                     .entrySet()
                     .forEach(value -> values.put((String)value.getKey(), ((JsonElement)value.getValue()).getAsInt()));
                  snapshots.put((String)profile.getKey(), values);
               }
            );
         this.profiles.restoreKeybindSnapshots(snapshots);
      }

      if (data.has("themes")) {
         Map<String, String> themes = new LinkedHashMap<>();
         data.getAsJsonObject("themes").entrySet().forEach(entry -> themes.put((String)entry.getKey(), ((JsonElement)entry.getValue()).getAsString()));
         this.profiles.restoreThemeSnapshots(themes);
      }

      if (data.has("customStates")) {
         Map<String, Map<String, String>> states = new LinkedHashMap<>();
         data.getAsJsonObject("customStates")
            .entrySet()
            .forEach(
               profile -> {
                  Map<String, String> values = new LinkedHashMap<>();
                  ((JsonElement)profile.getValue())
                     .getAsJsonObject()
                     .entrySet()
                     .forEach(value -> values.put((String)value.getKey(), ((JsonElement)value.getValue()).getAsString()));
                  states.put((String)profile.getKey(), values);
               }
            );
         this.profiles.restoreCustomizationSnapshots(states);
      }

      if (data.has("settingStates")) {
         Map<String, Map<String, String>> states = new LinkedHashMap<>();
         data.getAsJsonObject("settingStates")
            .entrySet()
            .forEach(
               profile -> {
                  Map<String, String> values = new LinkedHashMap<>();
                  ((JsonElement)profile.getValue())
                     .getAsJsonObject()
                     .entrySet()
                     .forEach(value -> values.put((String)value.getKey(), ((JsonElement)value.getValue()).getAsString()));
                  states.put((String)profile.getKey(), values);
               }
            );
         this.profiles.restoreSettingSnapshots(states);
      }

      if (data.has("hudStates")) {
         Map<String, Map<String, HudLayout>> states = new LinkedHashMap<>();
         data.getAsJsonObject("hudStates")
            .entrySet()
            .forEach(
               profile -> {
                  Map<String, HudLayout> layouts = new LinkedHashMap<>();
                  ((JsonElement)profile.getValue())
                     .getAsJsonObject()
                     .entrySet()
                     .forEach(
                        hud -> {
                           JsonObject value = ((JsonElement)hud.getValue()).getAsJsonObject();
                           layouts.put(
                              (String)hud.getKey(),
                              new HudLayout(
                                 value.get("x").getAsInt(),
                                 value.get("y").getAsInt(),
                                 value.get("scale").getAsFloat(),
                                 value.get("opacity").getAsFloat(),
                                 value.get("enabled").getAsBoolean(),
                                 value.get("snap").getAsBoolean(),
                                 value.get("color").getAsInt()
                              )
                           );
                        }
                     );
                  states.put((String)profile.getKey(), layouts);
               }
            );
         this.profiles.restoreHudSnapshots(states);
      }
   }

   private static void addSetting(JsonObject object, String id, Object value) {
      if (value instanceof Number number) {
         object.addProperty(id, number);
      } else if (value instanceof Boolean bool) {
         object.addProperty(id, bool);
      } else if (value != null) {
         object.addProperty(id, value.toString());
      }
   }

   private static void loadSettings(Module module, JsonObject object) {
      object.entrySet().forEach(entry -> {
         Setting<?> setting = module.settings().get((String)entry.getKey());
         if (setting != null) {
            JsonElement value = (JsonElement)entry.getValue();
            Object parsed = value.getAsString();
            Object defaultValue = setting.defaultValue();

            try {
               if (defaultValue instanceof Boolean) {
                  parsed = value.getAsBoolean();
               } else if (defaultValue instanceof Integer) {
                  parsed = value.getAsInt();
               } else if (defaultValue instanceof Long) {
                  parsed = value.getAsLong();
               } else if (defaultValue instanceof Float) {
                  parsed = value.getAsFloat();
               } else if (defaultValue instanceof Double) {
                  parsed = value.getAsDouble();
               }

               setUnchecked(setting, parsed);
            } catch (RuntimeException var7) {
            }
         }
      });
   }

   private static void setUnchecked(Setting setting, Object value) {
      setting.setValue(value);
   }
}
