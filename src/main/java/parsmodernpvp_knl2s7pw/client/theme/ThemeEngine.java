package parsmodernpvp_knl2s7pw.client.theme;

import parsmodernpvp_knl2s7pw.client.ui.DesignTokens;

public final class ThemeEngine {
   private Theme active = Theme.PARS_NEON;
   private Integer accentOverride;
   private Integer secondaryOverride;
   private Float backgroundOpacity;
   private Float panelOpacity;

   public Theme active() {
      return this.active;
   }

   public void setActive(Theme theme) {
      if (theme != null) {
         this.active = theme;
      }
   }

   public void setOverrides(int accent, int secondary, float background, float panel) {
      this.accentOverride = accent;
      this.secondaryOverride = secondary;
      this.backgroundOpacity = Math.max(0.2F, Math.min(1.0F, background));
      this.panelOpacity = Math.max(0.2F, Math.min(1.0F, panel));
   }

   public int color(String role) {
      return switch (role == null ? "text" : role) {
         case "background" -> this.backgroundOpacity == null ? this.active.background() : withAlpha(this.active.background(), this.backgroundOpacity);
         case "panel" -> this.panelOpacity == null ? this.active.panel() : withAlpha(this.active.panel(), this.panelOpacity);
         case "card" -> (this.panelOpacity == null ? this.active.panel() : withAlpha(this.active.panel(), this.panelOpacity)) & -134217729;
         case "text", "primaryText" -> this.active.text();
         case "muted", "mutedText" -> -8220248;
         case "accent", "primary" -> this.accentOverride == null ? this.active.accent() : this.accentOverride;
         case "secondary" -> this.secondaryOverride == null ? this.active.secondary() : this.secondaryOverride;
         case "border" -> this.active.border();
         case "hover" -> mix(this.color("accent"), this.color("panel"), 0.28F);
         case "active" -> this.color("accent");
         case "success" -> -12392796;
         case "warning" -> -14249;
         case "error" -> -44188;
         case "disabled" -> -9338730;
         case "shadow" -> 1711276032;
         case "glow" -> withAlpha(this.color("accent"), 0.22F);
         case "blur" -> 570425344;
         case "notification" -> mix(this.color("secondary"), this.color("panel"), 0.72F);
         default -> this.active.text();
      };
   }

   public int background() {
      return this.color("background");
   }

   public int panel() {
      return this.color("panel");
   }

   public int accent() {
      return this.color("accent");
   }

   public int secondary() {
      return this.color("secondary");
   }

   public int text() {
      return this.color("text");
   }

   public int mutedText() {
      return this.color("mutedText");
   }

   public int muted() {
      return this.color("muted");
   }

   public int border() {
      return this.color("border");
   }

   /**
    * Returns a muted version of any color by reducing its opacity.
    * Used for secondary text, disabled states, and subtle UI elements.
    */
   public int muted(int baseColor) {
      return withAlpha(baseColor, DesignTokens.OPACITY_MUTED);
   }

   public void setTheme(String name) {
      this.setActive(Theme.byName(name));
   }

   public Theme withOpacity(float backgroundOpacity, float panelOpacity, int accent) {
      return this.withOpacity(backgroundOpacity, panelOpacity, accent, this.active.secondary());
   }

   public Theme withOpacity(float backgroundOpacity, float panelOpacity, int accent, int secondary) {
      return new Theme(
         this.active.name(),
         withAlpha(this.active.background(), backgroundOpacity),
         withAlpha(this.active.panel(), panelOpacity),
         accent,
         secondary,
         this.active.text(),
         this.active.border()
      );
   }

   private static int withAlpha(int color, float opacity) {
      int alpha = Math.max(0, Math.min(255, Math.round((color >>> 24 & 0xFF) * opacity)));
      return alpha << 24 | color & 16777215;
   }

   private static int mix(int first, int second, float amount) {
      float t = Math.max(0.0F, Math.min(1.0F, amount));
      int a = Math.round((first >>> 24 & 0xFF) * (1.0F - t) + (second >>> 24 & 0xFF) * t);
      int r = Math.round((first >>> 16 & 0xFF) * (1.0F - t) + (second >>> 16 & 0xFF) * t);
      int g = Math.round((first >>> 8 & 0xFF) * (1.0F - t) + (second >>> 8 & 0xFF) * t);
      int b = Math.round((first & 0xFF) * (1.0F - t) + (second & 0xFF) * t);
      return a << 24 | r << 16 | g << 8 | b;
   }
}
