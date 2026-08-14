package parsmodernpvp_knl2s7pw.client.cosmetics;

import java.util.Comparator;

/**
 * Cosmetic rarity system with visual indicators and unlock requirements.
 */
public enum Rarity implements Comparable<Rarity> {
   COMMON("Common", 0x9DA0A6, 1),
   UNCOMMON("Uncommon", 0x54A663, 2),
   RARE("Rare", 0x4B69E6, 3),
   EPIC("Epic", 0xA84BC4, 4),
   LEGENDARY("Legendary", 0xE6A632, 5),
   MYTHIC("Mythic", 0xE6325C, 6);

   private final String displayName;
   private final int color;
   private final int tier;

   Rarity(String displayName, int color, int tier) {
      this.displayName = displayName;
      this.color = color;
      this.tier = tier;
   }

   public String getDisplayName() {
      return displayName;
   }

   public int getColor() {
      return color;
   }

   public int getTier() {
      return tier;
   }

   public static Rarity byTier(int tier) {
      for (Rarity r : values()) {
         if (r.tier == tier) return r;
      }
      return COMMON;
   }

   public static Comparator<Cosmetic> getComparator() {
      return Comparator.comparingInt(c -> -c.getRarity().getTier());
   }
}
