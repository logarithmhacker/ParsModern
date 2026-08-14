package parsmodernpvp_knl2s7pw.client.cosmetics;

/**
 * Defines all cosmetic types available in the client.
 */
public enum CosmeticType {
   CAPE("Cape", "cape", Rarity.COMMON),
   WINGS("Wings", "wings", Rarity.RARE),
   HAT("Hat", "hat", Rarity.COMMON),
   ANIMATED_HAT("Animated Hat", "animated_hat", Rarity.EPIC),
   TRAIL("Trail", "trail", Rarity.UNCOMMON),
   KILL_EFFECT("Kill Effect", "kill_effect", Rarity.LEGENDARY),
   EMOTE("Emote", "emote", Rarity.UNCOMMON),
   PARTICLE("Particle", "particle", Rarity.RARE),
   BADGE("Badge", "badge", Rarity.COMMON),
   PROFILE_DECORATION("Profile Decoration", "profile_decoration", Rarity.EPIC),
   CHAT_ICON("Chat Icon", "chat_icon", Rarity.UNCOMMON),
   UI_THEME("UI Theme", "ui_theme", Rarity.LEGENDARY);

   private final String displayName;
   private final String idPrefix;
   private final Rarity defaultRarity;

   CosmeticType(String displayName, String idPrefix, Rarity defaultRarity) {
      this.displayName = displayName;
      this.idPrefix = idPrefix;
      this.defaultRarity = defaultRarity;
   }

   public String getDisplayName() {
      return displayName;
   }

   public String getIdPrefix() {
      return idPrefix;
   }

   public Rarity getDefaultRarity() {
      return defaultRarity;
   }

   public static CosmeticType fromId(String id) {
      if (id == null) return CAPE;
      String prefix = id.split(":")[0];
      for (CosmeticType type : values()) {
         if (type.idPrefix.equals(prefix)) return type;
      }
      return CAPE;
   }
}
