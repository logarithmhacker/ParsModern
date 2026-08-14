package parsmodernpvp_knl2s7pw.client.module;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public final class ModuleManager {
   private final Map<String, Module> modules = new LinkedHashMap<>();
   private BiConsumer<Module, Boolean> changeListener = (module, enabled) -> {};

   public Module register(String id, String name, String category) {
      Module m = new Module(id, name, category);
      this.modules.put(id, m);
      return m;
   }

   public Module get(String id) {
      return this.modules.get(id);
   }

   public Map<String, Module> all() {
      return Map.copyOf(this.modules);
   }

   public void onChange(BiConsumer<Module, Boolean> listener) {
      this.changeListener = listener == null ? (m, e) -> {} : listener;
   }

   public void setEnabled(String id, boolean enabled) {
      Module module = this.get(id);
      if (module != null && module.enabled() != enabled) {
         module.setEnabled(enabled);
         this.changeListener.accept(module, enabled);
      }
   }

   public void toggle(String id) {
      Module module = this.get(id);
      if (module != null) {
         this.setEnabled(id, !module.enabled());
      }
   }
}
