package parsmodernpvp_knl2s7pw.client.hud;

public record HudLayout(int x, int y, float scale, float opacity, boolean enabled, boolean snapToGrid, int color) {
   public HudLayout(int x, int y, float scale, float opacity, boolean enabled, boolean snapToGrid) {
      this(x, y, scale, opacity, enabled, snapToGrid, -1);
   }

   public HudLayout move(int nextX, int nextY) {
      return new HudLayout(nextX, nextY, this.scale, this.opacity, this.enabled, this.snapToGrid, this.color);
   }

   public HudLayout withScale(float value) {
      return new HudLayout(this.x, this.y, Math.max(0.5F, Math.min(2.5F, value)), this.opacity, this.enabled, this.snapToGrid, this.color);
   }

   public HudLayout withOpacity(float value) {
      return new HudLayout(this.x, this.y, this.scale, Math.max(0.1F, Math.min(1.0F, value)), this.enabled, this.snapToGrid, this.color);
   }

   public HudLayout withEnabled(boolean value) {
      return new HudLayout(this.x, this.y, this.scale, this.opacity, value, this.snapToGrid, this.color);
   }

   public HudLayout withSnap(boolean value) {
      return new HudLayout(this.x, this.y, this.scale, this.opacity, this.enabled, value, this.color);
   }

   public HudLayout withColor(int value) {
      return new HudLayout(this.x, this.y, this.scale, this.opacity, this.enabled, this.snapToGrid, value);
   }

   public HudLayout reset() {
      return new HudLayout(8, 8, 1.0F, 1.0F, true, true, -1);
   }
}
