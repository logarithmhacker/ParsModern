package parsmodernpvp_knl2s7pw.client.profile;

public record Profile(String id, String displayName) {
   public static final Profile CRYSTAL = new Profile("crystal", "Crystal PvP");
   public static final Profile MACE = new Profile("mace", "Mace PvP");
   public static final Profile SWORD = new Profile("sword", "Sword PvP");
   public static final Profile AXE = new Profile("axe", "Axe PvP");
   public static final Profile BOW = new Profile("bow", "Bow PvP");
   public static final Profile PRACTICE = new Profile("practice", "Practice");
   public static final Profile SMP = new Profile("smp", "SMP");
   public static final Profile SKYBLOCK = new Profile("skyblock", "SkyBlock");
   public static final Profile BEDWARS = new Profile("bedwars", "BedWars");
   public static final Profile PERFORMANCE = new Profile("performance", "Performance");
   public static final Profile CUSTOM = new Profile("custom", "Custom");
}
