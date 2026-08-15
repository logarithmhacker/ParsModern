package parsmodernpvp_knl2s7pw.client.ui;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

/**
 * PARS UI audio layer.
 * Uses the client's own PARS sound events first and falls back to a vanilla
 * button sound if a custom resource is unavailable.
 */
public final class UiSoundEngine {
   private static final Map<String, Long> LAST_PLAYED = new HashMap<>();
   private static final String NAMESPACE = "parsmodernpvp-knl2s7pw";
   private static final long MIN_INTERVAL_MS = 45L;

   private static float master = 1.0F;
   private static float ui = 0.55F;
   private static float notifications = 0.65F;
   private static boolean muted;

   private UiSoundEngine() {}

   private static void playInternal(String key, String customEvent, float volume, float pitch) {
      if (muted || master <= 0.0F) return;
      long now = System.currentTimeMillis();
      Long last = LAST_PLAYED.get(key);
      if (last != null && now - last < MIN_INTERVAL_MS) return;
      LAST_PLAYED.put(key, now);

      Minecraft mc = Minecraft.getInstance();
      if (mc == null || mc.getSoundManager() == null) return;

      float finalVolume = clamp(volume) * master * ui;
      if (finalVolume <= 0.0F) return;
      float finalPitch = Math.max(0.25F, Math.min(2.0F, pitch));

      try {
         Identifier id = Identifier.fromNamespaceAndPath(NAMESPACE, customEvent);
         SoundEvent sound = SoundEvent.createVariableRangeEvent(id);
         mc.getSoundManager().play(SimpleSoundInstance.forUI(sound, finalVolume, finalPitch));
      } catch (Throwable ignored) {
         mc.getSoundManager().play(
            SimpleSoundInstance.forUI(
               SoundEvents.UI_BUTTON_CLICK.value(),
               finalVolume,
               finalPitch
            )
         );
      }
   }

   public static void play(String id, float volume, float pitch) {
      playInternal(id == null ? "ui" : id, id == null ? "pars_ui_sound_pack" : id, volume, pitch);
   }

   public static void startup() { playInternal("startup", "cinematic_startup", 0.85F, 0.72F); }
   public static void click() { playInternal("click", "pars_ui_sound_pack", 0.34F, 1.18F); }
   public static void hover() { playInternal("hover", "pars_ui_sound_pack", 0.12F, 1.42F); }
   public static void open() { playInternal("open", "ui_sound_suite", 0.24F, 1.05F); }
   public static void close() { playInternal("close", "ui_sound_suite", 0.20F, 0.82F); }
   public static void back() { playInternal("back", "ui_sound_suite", 0.18F, 0.72F); }
   public static void toggle() { playInternal("toggle", "pars_ui_sound_pack", 0.25F, 1.02F); }
   public static void slider() { playInternal("slider", "pars_ui_sound_pack", 0.16F, 1.22F); }
   public static void notification() { playInternal("notification", "ui_sound_suite", notifications, 1.08F); }
   public static void error() { playInternal("error", "ui_sound_suite", 0.30F, 0.48F); }
   public static void profileSwitch() { playInternal("profile_switch", "ui_sound_suite", 0.28F, 0.94F); }
   public static void transition() { playInternal("transition", "ui_sound_suite", 0.16F, 1.16F); }
   public static void confirm() { playInternal("confirm", "pars_ui_sound_pack", 0.42F, 0.92F); }

   public static void setMaster(float value) { master = clamp(value); }
   public static void setUi(float value) { ui = clamp(value); }
   public static void setNotifications(float value) { notifications = clamp(value); }
   public static void setMuted(boolean value) { muted = value; }
   public static float master() { return master; }
   public static float ui() { return ui; }
   public static float notifications() { return notifications; }
   public static boolean muted() { return muted; }

   private static float clamp(float value) {
      if (Float.isNaN(value) || Float.isInfinite(value)) return 0.0F;
      return Math.max(0.0F, Math.min(1.0F, value));
   }
}
