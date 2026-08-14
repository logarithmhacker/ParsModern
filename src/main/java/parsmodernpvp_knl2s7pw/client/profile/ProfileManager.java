package parsmodernpvp_knl2s7pw.client.profile;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import parsmodernpvp_knl2s7pw.client.PvpClient;
import parsmodernpvp_knl2s7pw.client.hud.HudLayout;
import parsmodernpvp_knl2s7pw.client.module.Module;
import parsmodernpvp_knl2s7pw.client.module.ModuleManager;
import parsmodernpvp_knl2s7pw.client.settings.Setting;
import parsmodernpvp_knl2s7pw.client.theme.Theme;

public final class ProfileManager {
   private final List<Profile> profiles = new ArrayList<>(
      List.of(
         Profile.CRYSTAL,
         Profile.MACE,
         Profile.SWORD,
         Profile.AXE,
         Profile.BOW,
         Profile.PRACTICE,
         Profile.SMP,
         Profile.SKYBLOCK,
         Profile.BEDWARS,
         Profile.PERFORMANCE,
         Profile.CUSTOM
      )
   );
   private final Map<String, Map<String, Boolean>> snapshots = new LinkedHashMap<>();
   private final Map<String, Map<String, Integer>> keybindSnapshots = new LinkedHashMap<>();
   private final Map<String, Map<String, HudLayout>> hudSnapshots = new LinkedHashMap<>();
   private final Map<String, String> themeSnapshots = new LinkedHashMap<>();
   private final Map<String, Map<String, String>> customizationSnapshots = new LinkedHashMap<>();
   private final Map<String, Map<String, String>> settingSnapshots = new LinkedHashMap<>();
   private final Map<String, String> customNames = new LinkedHashMap<>();
   private Profile active = Profile.CRYSTAL;
   private boolean restoring;
   private final ModuleManager modules;

   public ProfileManager(ModuleManager modules) {
      this.modules = modules;
   }

   public List<Profile> profiles() {
      return List.copyOf(this.profiles);
   }

   public Profile active() {
      return this.active;
   }

   public void select(Profile profile) {
      if (profile != null && this.profiles.contains(profile)) {
         if (!this.restoring) {
            this.saveActive();
         }

         this.active = profile;
         Map<String, Boolean> snapshot = this.snapshots.get(profile.id());
         if (snapshot == null) {
            this.applyPreset(profile);
         } else {
            snapshot.forEach(this.modules::setEnabled);
         }

         Map<String, Integer> binds = this.keybindSnapshots.get(profile.id());
         if (binds != null) {
            binds.forEach((id, key) -> {
               Module module = this.modules.get(id);
               if (module != null) {
                  module.setKeybind(key);
               }
            });
         }

         Map<String, HudLayout> hud = this.hudSnapshots.get(profile.id());
         if (hud != null) {
            hud.forEach(PvpClient::setHudLayout);
         }

         if (this.themeSnapshots.containsKey(profile.id())) {
            PvpClient.setTheme(Theme.byName(this.themeSnapshots.get(profile.id())));
         }

         if (this.customizationSnapshots.containsKey(profile.id())) {
            PvpClient.applyCustomizationSnapshot(this.customizationSnapshots.get(profile.id()));
         }

         Map<String, String> savedSettings = this.settingSnapshots.get(profile.id());
         if (savedSettings != null) {
            savedSettings.forEach((key, value) -> {
               int split = key.indexOf(58);
               if (split >= 1) {
                  Module module = this.modules.get(key.substring(0, split));
                  if (module != null && module.settings().get(key.substring(split + 1)) != null) {
                     Object base = module.settings().get(key.substring(split + 1)).defaultValue();
                     Object parsed = value;
                     if (base instanceof Boolean) {
                        parsed = Boolean.parseBoolean(value);
                     } else if (base instanceof Integer) {
                        parsed = Integer.parseInt(value);
                     } else if (base instanceof Float) {
                        parsed = Float.parseFloat(value);
                     } else if (base instanceof Double) {
                        parsed = Double.parseDouble(value);
                     }

                     setSetting(module, key.substring(split + 1), parsed);
                  }
               }
            });
         }
      }
   }

   public Profile create(String id, String displayName) {
      String clean = id == null ? "custom" : id.toLowerCase().replaceAll("[^a-z0-9_ -]", "").replace(' ', '_');
      if (!clean.isBlank() && !this.profiles.stream().anyMatch(p -> p.id().equals(clean))) {
         Profile profile = new Profile(clean, displayName != null && !displayName.isBlank() ? displayName : clean);
         this.profiles.add(profile);
         this.customNames.put(clean, profile.displayName());
         this.saveActive();
         this.active = profile;
         this.snapshots.put(clean, this.snapshot());
         return profile;
      } else {
         return this.profiles.stream().filter(p -> p.id().equals(clean)).findFirst().orElse(Profile.CUSTOM);
      }
   }

   public boolean delete(Profile profile) {
      if (profile != null && profile != Profile.CUSTOM && profile != Profile.CRYSTAL) {
         if (!this.profiles.remove(profile)) {
            return this.profiles.contains(profile);
         }

         this.snapshots.remove(profile.id());
         this.keybindSnapshots.remove(profile.id());
         this.hudSnapshots.remove(profile.id());
         this.themeSnapshots.remove(profile.id());
         this.customizationSnapshots.remove(profile.id());
         this.settingSnapshots.remove(profile.id());
         this.customNames.remove(profile.id());
         if (this.active.equals(profile)) {
            this.active = Profile.CRYSTAL;
         }

         return true;
      } else {
         return profile != null && profile != Profile.CUSTOM && profile != Profile.CRYSTAL;
      }
   }

   public void rename(Profile profile, String displayName) {
      if (profile != null && displayName != null && !displayName.isBlank()) {
         int index = this.profiles.indexOf(profile);
         if (index >= 0) {
            Profile renamed = new Profile(profile.id(), displayName.trim());
            this.profiles.set(index, renamed);
            if (this.customNames.containsKey(profile.id())) {
               this.customNames.put(profile.id(), renamed.displayName());
            }

            if (this.active.equals(profile)) {
               this.active = renamed;
            }
         }
      }
   }

   public void saveActive() {
      this.snapshots.put(this.active.id(), this.snapshot());
      Map<String, Integer> binds = new LinkedHashMap<>();
      this.modules.all().values().forEach(m -> binds.put(m.id(), m.keybind()));
      this.keybindSnapshots.put(this.active.id(), binds);
      this.hudSnapshots.put(this.active.id(), new LinkedHashMap<>(PvpClient.hudLayouts()));
      this.themeSnapshots.put(this.active.id(), PvpClient.theme().name());
      this.customizationSnapshots.put(this.active.id(), PvpClient.customizationSnapshot());
      Map<String, String> settings = new LinkedHashMap<>();
      this.modules.all().values().forEach(m -> m.settings().values().forEach((id, value) -> settings.put(m.id() + ":" + id, String.valueOf(value.value()))));
      this.settingSnapshots.put(this.active.id(), settings);
   }

   public void reset(Profile profile) {
      if (profile != null) {
         this.snapshots.remove(profile.id());
         if (this.active.equals(profile)) {
            this.applyPreset(profile);
         }
      }
   }

   public Map<String, Map<String, Boolean>> snapshots() {
      return this.snapshots;
   }

   public Map<String, Map<String, Integer>> keybindSnapshots() {
      return this.keybindSnapshots;
   }

   public Map<String, Map<String, HudLayout>> hudSnapshots() {
      return this.hudSnapshots;
   }

   public Map<String, String> themeSnapshots() {
      return this.themeSnapshots;
   }

   public Map<String, Map<String, String>> customizationSnapshots() {
      return this.customizationSnapshots;
   }

   public Map<String, Map<String, String>> settingSnapshots() {
      return this.settingSnapshots;
   }

   public Map<String, String> customNames() {
      return Map.copyOf(this.customNames);
   }

   public void restoreCustom(String id, String name) {
      if (id != null && name != null && !this.profiles.stream().anyMatch(p -> p.id().equals(id))) {
         this.profiles.add(new Profile(id, name));
         this.customNames.put(id, name);
      }
   }

   public void restoreSnapshots(Map<String, Map<String, Boolean>> data) {
      this.snapshots.clear();
      if (data != null) {
         this.snapshots.putAll(data);
      }
   }

   public void restoreKeybindSnapshots(Map<String, Map<String, Integer>> data) {
      this.keybindSnapshots.clear();
      if (data != null) {
         this.keybindSnapshots.putAll(data);
      }
   }

   public void restoreHudSnapshots(Map<String, Map<String, HudLayout>> data) {
      this.hudSnapshots.clear();
      if (data != null) {
         this.hudSnapshots.putAll(data);
      }
   }

   public void restoreThemeSnapshots(Map<String, String> data) {
      this.themeSnapshots.clear();
      if (data != null) {
         this.themeSnapshots.putAll(data);
      }
   }

   public void restoreCustomizationSnapshots(Map<String, Map<String, String>> data) {
      this.customizationSnapshots.clear();
      if (data != null) {
         this.customizationSnapshots.putAll(data);
      }
   }

   public void restoreSettingSnapshots(Map<String, Map<String, String>> data) {
      this.settingSnapshots.clear();
      if (data != null) {
         this.settingSnapshots.putAll(data);
      }
   }

   public void restoreActive(String id) {
      this.restoring = true;

      try {
         this.profiles.stream().filter(p -> p.id().equals(id)).findFirst().ifPresent(this::select);
      } finally {
         this.restoring = false;
      }
   }

   private Map<String, Boolean> snapshot() {
      Map<String, Boolean> data = new LinkedHashMap<>();

      for (Module module : this.modules.all().values()) {
         data.put(module.id(), module.enabled());
      }

      return data;
   }

   private static void setSetting(Module module, String id, Object value) {
      ((Setting<Object>)module.settings().get(id)).setValue(value);
   }

   private void applyPreset(Profile profile) {
      this.modules.all().values().forEach(m -> this.modules.setEnabled(m.id(), !m.category().equals("VISUAL")));
      if (profile == Profile.PERFORMANCE) {
         this.modules.setEnabled("performance", true);
      }

      if (profile == Profile.SMP) {
         this.modules.setEnabled("crystal_hud", false);
      }

      this.saveActive();
   }
}
