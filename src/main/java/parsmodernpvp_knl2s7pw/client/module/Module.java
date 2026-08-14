package parsmodernpvp_knl2s7pw.client.module;

import parsmodernpvp_knl2s7pw.client.settings.SettingsStore;

public final class Module {
   private final String id;
   private final String displayName;
   private final String category;
   private boolean enabled;
   private int keybind;
   private final SettingsStore settings = new SettingsStore();

   public Module(String id, String displayName, String category) {
      this.id = id;
      this.displayName = displayName;
      this.category = category;
   }

   public String id() {
      return this.id;
   }

   public String displayName() {
      return this.displayName;
   }

   public String category() {
      return this.category;
   }

   public boolean enabled() {
      return this.enabled;
   }

   public void setEnabled(boolean enabled) {
      this.enabled = enabled;
   }

   public int keybind() {
      return this.keybind;
   }

   public void setKeybind(int keybind) {
      this.keybind = Math.max(0, keybind);
   }

   public SettingsStore settings() {
      return this.settings;
   }
}
