package parsmodernpvp_knl2s7pw.client.performance;

import net.minecraft.client.player.LocalPlayer;

public final class PerformanceMonitor {
   private long lastNanos = System.nanoTime();
   private long sessionStart = System.currentTimeMillis();
   private float frameTimeMs;
   private int swings;
   private int lastSecondSwings;
   private long cpsWindow = System.currentTimeMillis();
   private double speed;
   private double fallDistance;
   private boolean lastSwing;
   private boolean lastLeftClick;
   private boolean lastRightClick;

   public void tick() {
      long now = System.nanoTime();
      float sample = Math.min(1000.0F, (float)(now - this.lastNanos) / 1000000.0F);
      this.frameTimeMs = this.frameTimeMs == 0.0F ? sample : this.frameTimeMs * 0.9F + sample * 0.1F;
      this.lastNanos = now;
      long wall = System.currentTimeMillis();
      if (wall - this.cpsWindow >= 1000L) {
         this.lastSecondSwings = this.swings;
         this.swings = 0;
         this.cpsWindow = wall;
      }
   }

   public void observe(LocalPlayer player) {
      boolean swing = player.swinging;
      if (swing && !this.lastSwing) {
         this.swings++;
      }

      this.lastSwing = swing;
      this.speed = Math.sqrt(player.getDeltaMovement().x * player.getDeltaMovement().x + player.getDeltaMovement().z * player.getDeltaMovement().z) * 20.0;
      this.fallDistance = Math.max(0.0, player.fallDistance);
   }

   public void observeMouse(boolean left, boolean right) {
      if (left && !this.lastLeftClick) {
         this.swings++;
      }

      if (right && !this.lastRightClick) {
         this.swings++;
      }

      this.lastLeftClick = left;
      this.lastRightClick = right;
   }

   public float frameTimeMs() {
      return this.frameTimeMs;
   }

   public int cps() {
      return this.lastSecondSwings;
   }

   public double speed() {
      return this.speed;
   }

   public double fallDistance() {
      return this.fallDistance;
   }

   public long sessionSeconds() {
      return Math.max(0L, (System.currentTimeMillis() - this.sessionStart) / 1000L);
   }

   public long usedMemoryMb() {
      return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576L;
   }

   public long maxMemoryMb() {
      return Runtime.getRuntime().maxMemory() / 1048576L;
   }

   public void resetSession() {
      this.sessionStart = System.currentTimeMillis();
   }
}
