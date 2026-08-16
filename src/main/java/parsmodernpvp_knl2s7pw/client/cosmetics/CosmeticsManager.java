package parsmodernpvp_knl2s7pw.client.cosmetics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
<<<<<<< HEAD
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
=======
>>>>>>> 77bc9da847b12cb9940d49afdb8df99b0af11b92
import parsmodernpvp_knl2s7pw.client.PvpClient;
import parsmodernpvp_knl2s7pw.client.ui.UiNotifications;

/**
 * Complete cosmetics management system with categories, filtering, and persistence.
 * Architecture supports future backend integration for server-synced cosmetics.
 */
public final class CosmeticsManager {
   private final Map<String, Cosmetic> cosmeticsRegistry = new HashMap<>();
   private final Set<String> unlockedCosmetics = new HashSet<>();
   private final Map<CosmeticType, String> equippedCosmetics = new HashMap<>();
   private final Set<String> favorites = new HashSet<>();
   
   // Initialize default cosmetics catalog
   public CosmeticsManager() {
      initializeCatalog();
      loadDefaults();
<<<<<<< HEAD
      loadState();
=======
>>>>>>> 77bc9da847b12cb9940d49afdb8df99b0af11b92
   }
   
   /**
    * Initialize the cosmetics catalog with all available items.
    * This is local data - backend integration point for server-synced cosmetics.
    */
   private void initializeCatalog() {
      // Capes
      register(new Cosmetic("cape:aurora", "Aurora Cape", CosmeticType.CAPE, Rarity.LEGENDARY, true, "Shimmering northern lights pattern"));
      register(new Cosmetic("cape:neon", "Neon Cape", CosmeticType.CAPE, Rarity.RARE, false, "Vibrant neon stripes"));
      register(new Cosmetic("cape:shadow", "Shadow Cape", CosmeticType.CAPE, Rarity.EPIC, true, "Darkness itself flows behind you"));
      register(new Cosmetic("cape:crystal", "Crystal Cape", CosmeticType.CAPE, Rarity.MYTHIC, true, "Crystalline shards float around"));
      
      // Wings
      register(new Cosmetic("wings:ion", "Ion Wings", CosmeticType.WINGS, Rarity.LEGENDARY, true, "Electric energy forms wings"));
      register(new Cosmetic("wings:dragon", "Dragon Wings", CosmeticType.WINGS, Rarity.MYTHIC, true, "Majestic dragon wings"));
      register(new Cosmetic("wings:angel", "Angel Wings", CosmeticType.WINGS, Rarity.EPIC, true, "Pure white feathered wings"));
      register(new Cosmetic("wings:demon", "Demon Wings", CosmeticType.WINGS, Rarity.EPIC, true, "Dark corrupted wings"));
      
      // Hats
      register(new Cosmetic("hat:crown", "Crown", CosmeticType.HAT, Rarity.LEGENDARY, false, "Royal golden crown"));
      register(new Cosmetic("hat:cap", "Baseball Cap", CosmeticType.HAT, Rarity.COMMON, false, "Classic baseball cap"));
      register(new Cosmetic("hat:tophat", "Top Hat", CosmeticType.HAT, Rarity.RARE, false, "Elegant black top hat"));
      register(new Cosmetic("hat:wizard", "Wizard Hat", CosmeticType.HAT, Rarity.EPIC, true, "Mystical pointed hat with stars"));
      
      // Animated Hats
      register(new Cosmetic("animated_hat:halo", "Halo", CosmeticType.ANIMATED_HAT, Rarity.MYTHIC, true, "Floating golden halo"));
      register(new Cosmetic("animated_hat:planets", "Mini Planets", CosmeticType.ANIMATED_HAT, Rarity.LEGENDARY, true, "Tiny planets orbit your head"));
      
      // Trails
      register(new Cosmetic("trail:prism", "Prism Trail", CosmeticType.TRAIL, Rarity.RARE, true, "Rainbow particle trail"));
      register(new Cosmetic("trail:flame", "Flame Trail", CosmeticType.TRAIL, Rarity.EPIC, true, "Fire particles follow you"));
      register(new Cosmetic("trail:ice", "Ice Trail", CosmeticType.TRAIL, Rarity.RARE, true, "Frosty blue trail"));
      register(new Cosmetic("trail:void", "Void Trail", CosmeticType.TRAIL, Rarity.LEGENDARY, true, "Dark matter particles"));
      
      // Kill Effects
      register(new Cosmetic("kill_effect:explosion", "Explosive KO", CosmeticType.KILL_EFFECT, Rarity.RARE, true, "Enemy explodes on defeat"));
      register(new Cosmetic("kill_effect:lightning", "Lightning Strike", CosmeticType.KILL_EFFECT, Rarity.EPIC, true, "Lightning strikes fallen foes"));
      register(new Cosmetic("kill_effect:portal", "Portal Banish", CosmeticType.KILL_EFFECT, Rarity.LEGENDARY, true, "Enemies vanish into a portal"));
      
      // Emotes
      register(new Cosmetic("emote:salute", "Salute", CosmeticType.EMOTE, Rarity.COMMON, false, "Respectful military salute"));
      register(new Cosmetic("emote:wave", "Wave", CosmeticType.EMOTE, Rarity.COMMON, false, "Friendly wave"));
      register(new Cosmetic("emote:dance", "Victory Dance", CosmeticType.EMOTE, Rarity.UNCOMMON, true, "Celebratory dance moves"));
      register(new Cosmetic("emote:taunt", "Taunt", CosmeticType.EMOTE, Rarity.RARE, false, "Confident taunt gesture"));
      
      // Particles
      register(new Cosmetic("particle:cyan_sparks", "Cyan Sparks", CosmeticType.PARTICLE, Rarity.RARE, true, "Bright cyan sparkles"));
      register(new Cosmetic("particle:hearts", "Hearts", CosmeticType.PARTICLE, Rarity.UNCOMMON, true, "Floating heart particles"));
      register(new Cosmetic("particle:stars", "Stars", CosmeticType.PARTICLE, Rarity.RARE, true, "Twinkling star particles"));
      
      // Badges
      register(new Cosmetic("badge:veteran", "Veteran Badge", CosmeticType.BADGE, Rarity.EPIC, false, "Awarded to long-time players"));
      register(new Cosmetic("badge:champion", "Champion Badge", CosmeticType.BADGE, Rarity.LEGENDARY, false, "Tournament winner badge"));
      register(new Cosmetic("badge:supporter", "Supporter Badge", CosmeticType.BADGE, Rarity.RARE, false, "Community supporter"));
      
      // Profile Decorations
      register(new Cosmetic("profile:border_gold", "Golden Border", CosmeticType.PROFILE_DECORATION, Rarity.EPIC, false, "Golden profile frame"));
      register(new Cosmetic("profile:border_diamond", "Diamond Border", CosmeticType.PROFILE_DECORATION, Rarity.LEGENDARY, true, "Sparkling diamond frame"));
      
      // Chat Icons
      register(new Cosmetic("chat_icon:star", "Star Icon", CosmeticType.CHAT_ICON, Rarity.UNCOMMON, false, "Golden star in chat"));
      register(new Cosmetic("chat_icon:lightning", "Lightning Icon", CosmeticType.CHAT_ICON, Rarity.RARE, false, "Light bolt in chat"));
      
      // UI Themes (cosmetic for client UI)
      register(new Cosmetic("ui_theme:pars_neon", "PARS Neon", CosmeticType.UI_THEME, Rarity.COMMON, false, "Classic PARS neon theme"));
      register(new Cosmetic("ui_theme:midnight", "Midnight", CosmeticType.UI_THEME, Rarity.RARE, false, "Deep purple dark theme"));
   }
   
   private void loadDefaults() {
      // All cosmetics are unlocked by default for testing
      // Backend integration point: Replace with actual unlock logic
      for (String id : cosmeticsRegistry.keySet()) {
         unlockedCosmetics.add(id);
      }
   }
   
   private void register(Cosmetic cosmetic) {
      cosmeticsRegistry.put(cosmetic.getId(), cosmetic);
   }
   
   /**
    * Toggle equip/unequip a cosmetic.
    */
   public void toggle(String cosmeticId) {
      Cosmetic cosmetic = cosmeticsRegistry.get(cosmeticId);
      if (cosmetic == null || !unlockedCosmetics.contains(cosmeticId)) {
         return;
      }
      
      CosmeticType type = cosmetic.getType();
      String currentlyEquipped = equippedCosmetics.get(type);
      
      if (currentlyEquipped != null && currentlyEquipped.equals(cosmeticId)) {
         equippedCosmetics.remove(type);
         UiNotifications.push("Unequipped " + cosmetic.getName(), 2000L);
<<<<<<< HEAD
         saveState();
      } else {
         equippedCosmetics.put(type, cosmeticId);
         UiNotifications.push("Equipped " + cosmetic.getName(), 2000L);
         saveState();
=======
      } else {
         equippedCosmetics.put(type, cosmeticId);
         UiNotifications.push("Equipped " + cosmetic.getName(), 2000L);
>>>>>>> 77bc9da847b12cb9940d49afdb8df99b0af11b92
      }
   }
   
   /**
    * Equip a specific cosmetic.
    */
   public void equip(String cosmeticId) {
      Cosmetic cosmetic = cosmeticsRegistry.get(cosmeticId);
      if (cosmetic == null || !unlockedCosmetics.contains(cosmeticId)) {
         return;
      }
      
      equippedCosmetics.put(cosmetic.getType(), cosmeticId);
      UiNotifications.push("Equipped " + cosmetic.getName(), 2000L);
<<<<<<< HEAD
      saveState();
=======
>>>>>>> 77bc9da847b12cb9940d49afdb8df99b0af11b92
   }
   
   /**
    * Unequip a cosmetic by type.
    */
   public void unequip(CosmeticType type) {
      equippedCosmetics.remove(type);
<<<<<<< HEAD
      saveState();
=======
>>>>>>> 77bc9da847b12cb9940d49afdb8df99b0af11b92
   }
   
   /**
    * Check if a cosmetic is unlocked.
    */
   public boolean isUnlocked(String cosmeticId) {
      return unlockedCosmetics.contains(cosmeticId);
   }
   
   /**
    * Check if a cosmetic is currently equipped.
    */
   public boolean isEquipped(String cosmeticId) {
      Cosmetic cosmetic = cosmeticsRegistry.get(cosmeticId);
      if (cosmetic == null) return false;
      String equipped = equippedCosmetics.get(cosmetic.getType());
      return cosmeticId.equals(equipped);
   }
   
   /**
    * Check if any cosmetic of a type is equipped.
    */
   public boolean isTypeEquipped(CosmeticType type) {
      return equippedCosmetics.containsKey(type);
   }
   
   /**
    * Get currently equipped cosmetic for a type.
    */
   public Cosmetic getEquipped(CosmeticType type) {
      String id = equippedCosmetics.get(type);
      return id != null ? cosmeticsRegistry.get(id) : null;
   }
   
   /**
    * Get all cosmetics of a specific type.
    */
   public List<Cosmetic> getByType(CosmeticType type) {
      return cosmeticsRegistry.values().stream()
         .filter(c -> c.getType() == type)
         .collect(Collectors.toList());
   }
   
   /**
    * Get all unlocked cosmetics.
    */
   public List<Cosmetic> getUnlocked() {
      return unlockedCosmetics.stream()
         .map(cosmeticsRegistry::get)
         .filter(c -> c != null)
         .collect(Collectors.toList());
   }
   
   /**
    * Get all locked cosmetics.
    */
   public List<Cosmetic> getLocked() {
      return cosmeticsRegistry.values().stream()
         .filter(c -> !unlockedCosmetics.contains(c.getId()))
         .collect(Collectors.toList());
   }
   
   /**
    * Get cosmetics filtered by rarity.
    */
   public List<Cosmetic> getByRarity(Rarity rarity) {
      return cosmeticsRegistry.values().stream()
         .filter(c -> c.getRarity() == rarity)
         .collect(Collectors.toList());
   }
   
   /**
    * Search cosmetics by name.
    */
   public List<Cosmetic> search(String query) {
      if (query == null || query.isEmpty()) {
         return new ArrayList<>(cosmeticsRegistry.values());
      }
      String lowerQuery = query.toLowerCase();
      return cosmeticsRegistry.values().stream()
         .filter(c -> c.getName().toLowerCase().contains(lowerQuery) || 
                      c.getDescription().toLowerCase().contains(lowerQuery))
         .collect(Collectors.toList());
   }
   
   /**
    * Get favorites.
    */
   public List<Cosmetic> getFavorites() {
      return favorites.stream()
         .map(cosmeticsRegistry::get)
         .filter(c -> c != null)
         .collect(Collectors.toList());
   }
   
   /**
    * Toggle favorite status.
    */
   public void toggleFavorite(String cosmeticId) {
      if (favorites.contains(cosmeticId)) {
         favorites.remove(cosmeticId);
      } else {
         favorites.add(cosmeticId);
      }
   }
   
   /**
    * Check if cosmetic is favorited.
    */
   public boolean isFavorite(String cosmeticId) {
      return favorites.contains(cosmeticId);
   }
   
   /**
    * Get all cosmetics sorted by rarity.
    */
   public List<Cosmetic> getAllSorted() {
      List<Cosmetic> all = new ArrayList<>(cosmeticsRegistry.values());
      all.sort(Rarity.getComparator());
      return all;
   }
   
   /**
    * Get all categories with counts.
    */
   public Map<CosmeticType, Integer> getCategoryCounts() {
      Map<CosmeticType, Integer> counts = new HashMap<>();
      for (CosmeticType type : CosmeticType.values()) {
         counts.put(type, 0);
      }
      for (Cosmetic cosmetic : cosmeticsRegistry.values()) {
         counts.merge(cosmetic.getType(), 1, Integer::sum);
      }
      return counts;
   }
   
   /**
    * Get total cosmetics count.
    */
   public int getTotalCount() {
      return cosmeticsRegistry.size();
   }
   
   /**
    * Get unlocked count.
    */
   public int getUnlockedCount() {
      return unlockedCosmetics.size();
   }
   
   /**
    * Get a specific cosmetic by ID.
    */
   public Cosmetic getCosmetic(String id) {
      return cosmeticsRegistry.get(id);
   }
   
   /**
    * Get all cosmetic types.
    */
   public List<CosmeticType> getAllTypes() {
      List<CosmeticType> types = new ArrayList<>();
      Collections.addAll(types, CosmeticType.values());
      return types;
   }
   
   /**
    * Get all currently equipped cosmetics across all types.
    */
   public List<Cosmetic> getAllEquipped() {
      List<Cosmetic> result = new ArrayList<>();
      for (CosmeticType type : CosmeticType.values()) {
         Cosmetic equipped = getEquipped(type);
         if (equipped != null) {
            result.add(equipped);
         }
      }
      return result;
   }
<<<<<<< HEAD
   private Path statePath() {
      Minecraft mc = Minecraft.getInstance();
      Path root = mc != null ? mc.gameDirectory.toPath() : Path.of(".");
      return root.resolve("config").resolve("parsmodernpvp-cosmetics.json");
   }

   private void saveState() {
      try {
         Path path = statePath();
         Files.createDirectories(path.getParent());
         JsonObject root = new JsonObject();
         JsonArray fav = new JsonArray();
         for (String id : favorites) fav.add(id);
         root.add("favorites", fav);
         JsonObject equipped = new JsonObject();
         for (Map.Entry<CosmeticType, String> entry : equippedCosmetics.entrySet()) {
            if (entry.getKey() != null && entry.getValue() != null) {
               equipped.addProperty(entry.getKey().name(), entry.getValue());
            }
         }
         root.add("equipped", equipped);
         Files.writeString(path, root.toString(), StandardCharsets.UTF_8);
      } catch (Exception ignored) {
      }
   }

   private void loadState() {
      try {
         Path path = statePath();
         if (!Files.exists(path)) return;
         JsonObject root = JsonParser.parseString(Files.readString(path, StandardCharsets.UTF_8)).getAsJsonObject();
         if (root.has("favorites") && root.get("favorites").isJsonArray()) {
            for (var entry : root.getAsJsonArray("favorites")) {
               if (entry.isJsonPrimitive() && cosmeticsRegistry.containsKey(entry.getAsString())) {
                  favorites.add(entry.getAsString());
               }
            }
         }
         if (root.has("equipped") && root.get("equipped").isJsonObject()) {
            for (Map.Entry<String, com.google.gson.JsonElement> entry : root.getAsJsonObject("equipped").entrySet()) {
               try {
                  CosmeticType type = CosmeticType.valueOf(entry.getKey());
                  String id = entry.getValue().getAsString();
                  Cosmetic cosmetic = cosmeticsRegistry.get(id);
                  if (cosmetic != null && cosmetic.getType() == type && unlockedCosmetics.contains(id)) {
                     equippedCosmetics.put(type, id);
                  }
               } catch (Exception ignored) {
               }
            }
         }
      } catch (Exception ignored) {
      }
   }

=======
>>>>>>> 77bc9da847b12cb9940d49afdb8df99b0af11b92
}
