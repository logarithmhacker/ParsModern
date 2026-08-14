package parsmodernpvp_knl2s7pw.client.cosmetics;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class CosmeticsManager {
   private static final List<String> ORIGINAL_COSMETICS = List.of(
      "cape:aurora", "wings:ion", "particles:cyan_sparks", "aura:neon_ring", "trail:prism", "emote:salute"
   );
   private final Set<String> enabled = new HashSet<>();

   public void toggle(String cosmetic) {
      if (!this.enabled.add(cosmetic)) {
         this.enabled.remove(cosmetic);
      }
   }

   public boolean enabled(String cosmetic) {
      return this.enabled.contains(cosmetic);
   }

   public List<String> available() {
      return ORIGINAL_COSMETICS;
   }

   public Set<String> enabled() {
      return Set.copyOf(this.enabled);
   }

   public void setEnabled(String cosmetic, boolean state) {
      if (state) {
         this.enabled.add(cosmetic);
      } else {
         this.enabled.remove(cosmetic);
      }
   }
}
