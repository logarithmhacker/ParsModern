package parsmodernpvp_knl2s7pw.client.settings;

import java.util.LinkedHashMap;
import java.util.Map;

public final class SettingsStore {
   private final Map<String, Setting<?>> values = new LinkedHashMap<>();

   public <T> Setting<T> register(String id, T defaultValue) {
      Setting<T> existing = (Setting<T>)this.values.get(id);
      if (existing != null) {
         return existing;
      }

      Setting<T> setting = new Setting<>(id, defaultValue);
      this.values.put(id, setting);
      return setting;
   }

   public Setting<?> get(String id) {
      return this.values.get(id);
   }

   public Map<String, Setting<?>> values() {
      return Map.copyOf(this.values);
   }

   public void reset() {
      this.values.values().forEach(Setting::reset);
   }
}
