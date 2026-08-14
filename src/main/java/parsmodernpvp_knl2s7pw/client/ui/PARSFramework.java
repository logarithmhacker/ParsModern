package parsmodernpvp_knl2s7pw.client.ui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import parsmodernpvp_knl2s7pw.client.PvpClient;
import parsmodernpvp_knl2s7pw.client.animation.AnimationEngine;

/**
 * PARS Framework - Core rendering primitives for the premium UI system.
 * All visual elements are rendered through this centralized framework.
 */
public final class PARSFramework {
   private PARSFramework() {
   }

   /**
    * Renders the premium animated background with glassmorphism effects.
    */
   public static void background(GuiGraphicsExtractor g, int width, int height) {
      UiBackground.render(g, width, height, PvpClient.backgroundOpacity());
   }

   /**
    * Renders a premium panel with shadows, borders, and rounded corners.
    */
   public static void panel(GuiGraphicsExtractor g, int x, int y, int width, int height) {
      renderPanel(g, x, y, width, height, DesignTokens.RADIUS_MD);
   }

   /**
    * Renders a panel with custom radius.
    */
   public static void panel(GuiGraphicsExtractor g, int x, int y, int width, int height, float radius) {
      renderPanel(g, x, y, width, height, radius);
   }

   private static void renderPanel(GuiGraphicsExtractor g, int x, int y, int width, int height, float radius) {
      int r = Math.max(0, Math.min((int)radius, Math.min(width, height) / 2));
      int panelColor = PvpClient.themeEngine().color("panel");
      int borderColor = PvpClient.themeEngine().color("border");
      int shadowColor = DesignTokens.SHADOW_COLOR;

      // Layered shadow for depth
      if (PvpClient.shadow() && PvpClient.panelDepth()) {
         g.fill(x + 3, y + 4, x + width + 3, y + height + 4, shadowColor & 0x40FFFFFF | (shadowColor & 0xFFFFFF));
         g.fill(x + 1, y + 2, x + width + 1, y + height + 2, DesignTokens.SHADOW_COLOR_SOFT);
      }

      // Panel body
      g.fill(x + r, y, x + width - r, y + height, panelColor);
      g.fill(x, y + r, x + width, y + height - r, panelColor);
      
      // Rounded corners
      if (r > 0) {
         int bgColor = PvpClient.themeEngine().color("background");
         g.fill(x, y, x + r, y + r, bgColor);
         g.fill(x + width - r, y, x + width, y + r, bgColor);
         g.fill(x, y + height - r, x + r, y + height, bgColor);
         g.fill(x + width - r, y + height - r, x + width, y + height, bgColor);
         
         // Corner fill to complete rounded effect
         g.fill(x + r, y, x + width - r, y + r, panelColor);
         g.fill(x + r, y + height - r, x + width - r, y + height, panelColor);
      }

      // Subtle top border
      g.fill(x + r, y, x + width - r, y + 1, borderColor);
      
      // Accent left border strip
      int accentHeight = height - r * 2;
      g.fill(x, y + r, x + 2, y + r + accentHeight, PvpClient.themeEngine().accent());

      // Inner glow effect
      if (PvpClient.glow() && PvpClient.softLighting()) {
         int glowColor = withAlpha(PvpClient.themeEngine().accent(), 0.08F);
         g.fill(x + 2, y + 2, x + width - 2, y + 4, glowColor);
      }
   }

   /**
    * Renders a premium card component with hover state support.
    */
   public static void card(GuiGraphicsExtractor g, int x, int y, int width, int height, boolean selected) {
      card(g, x, y, width, height, selected, false);
   }

   /**
    * Renders a premium card with hover and selected states.
    */
   public static void card(GuiGraphicsExtractor g, int x, int y, int width, int height, boolean hovered, boolean selected) {
      int r = (int)DesignTokens.effectiveRadius();
      r = Math.min(r, Math.min(width, height) / 2);
      
      int baseColor = selected 
         ? PvpClient.themeEngine().color("hover") 
         : PvpClient.themeEngine().color("card");
      
      // Shadow
      if (PvpClient.shadow()) {
         g.fill(x + 2, y + 3, x + width + 2, y + height + 3, DesignTokens.SHADOW_COLOR_SOFT);
      }

      // Card body
      g.fill(x + r, y, x + width - r, y + height, baseColor);
      g.fill(x, y + r, x + width, y + height - r, baseColor);
      
      // Corners
      if (r > 0) {
         int cornerColor = PvpClient.themeEngine().color("background");
         g.fill(x, y, x + r, y + r, cornerColor);
         g.fill(x + width - r, y, x + width, y + r, cornerColor);
         g.fill(x, y + height - r, x + r, y + height, cornerColor);
         g.fill(x + width - r, y + height - r, x + width, y + height, cornerColor);
         
         g.fill(x + r, y, x + width - r, y + r, baseColor);
         g.fill(x + r, y + height - r, x + width - r, y + height, baseColor);
      }

      // Selection indicator
      int indicatorWidth = selected ? 3 : 1;
      int indicatorColor = selected ? PvpClient.themeEngine().accent() : PvpClient.themeEngine().border();
      g.fill(x, y, x + indicatorWidth, y + height, indicatorColor);

      // Hover glow
      if (hovered && !selected && PvpClient.glow()) {
         int glowColor = withAlpha(PvpClient.themeEngine().accent(), 0.12F);
         g.fill(x + 1, y + 1, x + width - 1, y + 3, glowColor);
      }
   }

   /**
    * Renders a premium button with multiple states.
    */
   public static void button(GuiGraphicsExtractor g, String label, int x, int y, int width, int height, 
                            boolean hovered, boolean active, boolean disabled) {
      int r = (int)DesignTokens.RADIUS_SM;
      
      // Determine colors based on state
      int bgColor;
      int textColor;
      
      if (disabled) {
         bgColor = 0x4D586070;
         textColor = 0x808890A0;
      } else if (active) {
         bgColor = withAlpha(PvpClient.themeEngine().accent(), 0.85F);
         textColor = -16313828;
      } else if (hovered) {
         bgColor = PvpClient.themeEngine().color("hover");
         textColor = PvpClient.themeEngine().text();
      } else {
         bgColor = PvpClient.themeEngine().color("card");
         textColor = PvpClient.themeEngine().text();
      }

      // Shadow
      if (PvpClient.shadow() && !disabled) {
         g.fill(x + 2, y + 3, x + width + 2, y + height + 3, DesignTokens.SHADOW_COLOR_SOFT);
      }

      // Button body
      g.fill(x + r, y, x + width - r, y + height, bgColor);
      g.fill(x, y + r, x + width, y + height - r, bgColor);
      
      // Corners
      if (r > 0) {
         int cornerColor = PvpClient.themeEngine().color("background");
         g.fill(x, y, x + r, y + r, cornerColor);
         g.fill(x + width - r, y, x + width, y + r, cornerColor);
         g.fill(x, y + height - r, x + r, y + height, cornerColor);
         g.fill(x + width - r, y + height - r, x + width, y + height, cornerColor);
         
         g.fill(x + r, y, x + width - r, y + r, bgColor);
         g.fill(x + r, y + height - r, x + width - r, y + height, bgColor);
      }

      // Left accent for active state
      if (active) {
         g.fill(x, y + r, x + 2, y + height - r, PvpClient.themeEngine().secondary());
      }

      // Hover pulse animation
      if (hovered && !disabled && !PvpClient.reducedMotion()) {
         float pulse = AnimationEngine.ease(
            (float)((Math.sin(System.nanoTime() / 2.0E8) + 1.0) * 0.5), 
            AnimationEngine.Curve.EASE_IN_OUT
         );
         int pulseColor = withAlpha(PvpClient.themeEngine().secondary(), 0.2F + pulse * 0.2F);
         g.fill(x + 3, y + height - 2, x + width - 3, y + height - 1, pulseColor);
      }

      // Label
      PARSFontEngine.centered(
         g, label, x + width / 2, y + height / 2, textColor, 
         PARSFontEngine.Token.SMALL, PvpClient.shadow(), false
      );
   }

   /**
    * Simplified button rendering for quick use.
    */
   public static void button(GuiGraphicsExtractor g, String label, int x, int y, int width, int height, 
                            boolean hovered, boolean active) {
      button(g, label, x, y, width, height, hovered, active, false);
   }

   /**
    * Renders a premium progress bar.
    */
   public static void progress(GuiGraphicsExtractor g, int x, int y, int width, int height, float value) {
      progress(g, x, y, width, height, value, false);
   }

   /**
    * Renders a premium progress bar with optional glow.
    */
   public static void progress(GuiGraphicsExtractor g, int x, int y, int width, int height, float value, boolean glow) {
      int r = height / 2;
      float clamped = Math.max(0.0F, Math.min(1.0F, value));
      int filledWidth = Math.round(width * clamped);

      // Background track
      int trackColor = 0x4D586070;
      g.fill(x + r, y, x + width - r, y + height, trackColor);
      g.fill(x, y + r, x + width, y + height - r, trackColor);
      if (r > 0) {
         g.fill(x + r, y, x + width - r, y + r, trackColor);
         g.fill(x + r, y + height - r, x + width - r, y + height, trackColor);
      }

      // Fill color with gradient effect
      int fillColor = PvpClient.themeEngine().accent();
      if (glow && PvpClient.glow()) {
         // Outer glow
         int glowColor = withAlpha(fillColor, 0.3F);
         g.fill(x - 1, y - 1, x + filledWidth + 1, y + height + 1, glowColor);
      }
      
      // Main fill
      g.fill(x + r, y, x + filledWidth - r, y + height, fillColor);
      g.fill(x, y + r, x + filledWidth, y + height - r, fillColor);
      
      // Highlight overlay
      int highlight = 0x40FFFFFF;
      g.fill(x + 2, y + 1, x + filledWidth - 2, y + 3, highlight);
   }

   /**
    * Applies alpha channel to a color.
    */
   private static int withAlpha(int color, float alpha) {
      int a = Math.max(0, Math.min(255, Math.round((color >>> 24 & 0xFF) * alpha)));
      return a << 24 | color & 0xFFFFFF;
   }
}
