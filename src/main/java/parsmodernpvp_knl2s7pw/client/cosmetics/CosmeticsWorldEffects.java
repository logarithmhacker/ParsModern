package parsmodernpvp_knl2s7pw.client.cosmetics;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;

/** Client-side 3D cosmetic effects. Every equipped cosmetic produces a real world effect. */
public final class CosmeticsWorldEffects {
   private static long tick;
   private CosmeticsWorldEffects() {}

   public static void tick(Minecraft client) {
      if (client == null || client.player == null || client.level == null || client.screen != null) return;
      tick++;
      if ((tick & 1L) == 0L) emitTrail(client.player, PvpCosmetics(client, CosmeticType.TRAIL));
      if ((tick % 3L) == 0L) {
         emitOrbit(client.player, PvpCosmetics(client, CosmeticType.PARTICLE));
         emitWings(client.player, PvpCosmetics(client, CosmeticType.WINGS));
         emitHat(client.player, PvpCosmetics(client, CosmeticType.ANIMATED_HAT));
      }
      if ((tick % 4L) == 0L) {
         emitCape(client.player, PvpCosmetics(client, CosmeticType.CAPE));
         emitBadge(client.player, PvpCosmetics(client, CosmeticType.BADGE));
      }
   }

   private static Cosmetic PvpCosmetics(Minecraft mc, CosmeticType type) {
      CosmeticsManager manager = parsmodernpvp_knl2s7pw.client.PvpClient.cosmetics();
      return manager == null ? null : manager.getEquipped(type);
   }

   private static void emitTrail(LocalPlayer p, Cosmetic c) {
      if (c == null) return;
      ParticleOptions particle = switch (c.getId()) {
         case "trail:flame" -> ParticleTypes.FLAME;
         case "trail:ice" -> ParticleTypes.SNOWFLAKE;
         case "trail:void" -> ParticleTypes.REVERSE_PORTAL;
         default -> ParticleTypes.END_ROD;
      };
      double backX = p.getX() - p.getDeltaMovement().x * 2.4D;
      double backZ = p.getZ() - p.getDeltaMovement().z * 2.4D;
      p.level().addParticle(particle, backX, p.getY() + 0.15D, backZ, 0, 0.015D, 0);
   }

   private static void emitOrbit(LocalPlayer p, Cosmetic c) {
      if (c == null) return;
      double a = tick * 0.18D;
      double r = 0.52D;
      ParticleOptions particle = c.getId().equals("particle:hearts") ? ParticleTypes.HEART :
            c.getId().equals("particle:stars") ? ParticleTypes.END_ROD : ParticleTypes.ELECTRIC_SPARK;
      for (int i = 0; i < 2; i++) {
         double aa = a + Math.PI * i;
         p.level().addParticle(particle, p.getX() + Math.cos(aa) * r, p.getY() + 1.1D + Math.sin(aa * 0.7D) * 0.25D, p.getZ() + Math.sin(aa) * r, 0, 0.01, 0);
      }
   }

   private static void emitWings(LocalPlayer p, Cosmetic c) {
      if (c == null) return;
      ParticleOptions particle = c.getId().equals("wings:angel") ? ParticleTypes.END_ROD :
            c.getId().equals("wings:demon") ? ParticleTypes.SMOKE :
            c.getId().equals("wings:dragon") ? ParticleTypes.FLAME : ParticleTypes.ELECTRIC_SPARK;
      double flap = Math.sin(tick * 0.20D) * 0.10D;
      for (int i = 0; i < 4; i++) {
         double y = p.getY() + 1.05D + i * 0.23D;
         double span = 0.28D + i * 0.13D + flap;
         double z = p.getZ() - 0.28D;
         p.level().addParticle(particle, p.getX() - span, y, z, 0, 0.005, 0);
         p.level().addParticle(particle, p.getX() + span, y, z, 0, 0.005, 0);
      }
   }

   private static void emitHat(LocalPlayer p, Cosmetic c) {
      if (c == null) return;
      double a = tick * 0.16D;
      double r = c.getId().contains("planets") ? 0.55D : 0.36D;
      p.level().addParticle(ParticleTypes.END_ROD, p.getX() + Math.cos(a) * r, p.getY() + 2.18D, p.getZ() + Math.sin(a) * r, 0, 0, 0);
   }

   private static void emitCape(LocalPlayer p, Cosmetic c) {
      if (c == null) return;
      ParticleOptions particle = c.getId().equals("cape:crystal") ? ParticleTypes.END_ROD :
            c.getId().equals("cape:aurora") ? ParticleTypes.GLOW :
            c.getId().equals("cape:shadow") ? ParticleTypes.SMOKE : ParticleTypes.ELECTRIC_SPARK;
      double sway = Math.sin(tick * 0.12D) * 0.12D;
      for (int i = 0; i < 3; i++) {
         p.level().addParticle(particle, p.getX() + sway * i, p.getY() + 1.0D + i * 0.20D, p.getZ() - 0.22D, 0, 0.008, 0);
      }
   }

   private static void emitBadge(LocalPlayer p, Cosmetic c) {
      if (c != null) p.level().addParticle(ParticleTypes.HAPPY_VILLAGER, p.getX(), p.getY() + 1.75D, p.getZ(), 0, 0.01, 0);
   }
}
