package parsmodernpvp_knl2s7pw.client.resource;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public final class ResourceManager {
   private static final Set<String> warmed = new LinkedHashSet<>();

   private ResourceManager() {
   }

   public static void warmCaches() {
      warmed.clear();
      warmed.add("hud");
      warmed.add("crosshair");
      warmed.add("click_gui");
      warmed.add("themes");
      warmed.add("pars_premium_font");
      warmed.add("ui_sound_bus");
      warmed.add("vanilla_item_atlas");
   }

   public static Set<String> warmedResources() {
      return Collections.unmodifiableSet(warmed);
   }
}
