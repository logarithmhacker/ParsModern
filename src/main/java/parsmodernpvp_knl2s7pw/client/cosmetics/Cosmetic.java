package parsmodernpvp_knl2s7pw.client.cosmetics;

/**
 * Represents a cosmetic item with type, rarity, and metadata.
 */
public class Cosmetic {
   private final String id;
   private final String name;
   private final CosmeticType type;
   private final Rarity rarity;
   private final boolean animated;
   private final String description;

   public Cosmetic(String id, String name, CosmeticType type, Rarity rarity) {
      this(id, name, type, rarity, false, "");
   }

   public Cosmetic(String id, String name, CosmeticType type, Rarity rarity, boolean animated, String description) {
      this.id = id;
      this.name = name;
      this.type = type;
      this.rarity = rarity;
      this.animated = animated;
      this.description = description;
   }

   public String getId() {
      return id;
   }

   public String getName() {
      return name;
   }

   public CosmeticType getType() {
      return type;
   }

   public Rarity getRarity() {
      return rarity;
   }

   public boolean isAnimated() {
      return animated;
   }

   public String getDescription() {
      return description;
   }
}
