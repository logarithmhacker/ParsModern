package parsmodernpvp_knl2s7pw.client.ui;

import parsmodernpvp_knl2s7pw.client.PvpClient;

/*
 * Central UI scale for the entire PARS interface.
 *
 * 1.0F  = the previous (large) layout.
 * 0.78F = the compact default chosen for this client.
 *
 * Every screen and shared component scales its layout through
 * this class so panels, buttons, spacing, hover areas and click
 * areas all move together. Widget positions and their rendered
 * shapes use the SAME scaled values, which keeps mouse hitboxes
 * perfectly aligned with what is drawn.
 *
 * Text size is intentionally NOT multiplied here: typography is
 * driven by PARSFontEngine tokens (and the user's font scale) so
 * that smaller panels never force glyphs below readable size.
 */
public final class UiScale {

   private UiScale() {
   }

   /*
    * Compact default. Chosen after inspecting the current UI:
    * at 0.78 the 840x400 click-GUI panel becomes ~655x312 while
    * keeping every control readable and every hitbox aligned.
    */
   public static final float DEFAULT = 0.50F;

   /*
    * Effective UI scale. PvpClient.uiScale() is the persisted
    * user setting; UiScale just applies it everywhere.
    */
   public static float value() {
      return PvpClient.uiScale();
   }

   /*
    * Scales a layout value to the current UI scale.
    * Integer rounding keeps everything on crisp pixel boundaries.
    */
   public static int s(float value) {
      return Math.round(value * value());
   }

   public static int s(int value) {
      return Math.round(value * value());
   }
}
