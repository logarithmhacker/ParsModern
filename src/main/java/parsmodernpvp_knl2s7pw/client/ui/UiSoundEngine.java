package parsmodernpvp_knl2s7pw.client.ui;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public final class UiSoundEngine {
   private static final Map<String, Long> LAST_PLAYED = new HashMap<>();
   private static float master = 1.0F;
   private static float ui = 0.45F;
   private static float notifications = 0.65F;
   private static boolean muted;

   private UiSoundEngine() {
   }

   public static void play(String id, float volume, float pitch) {
      if (!muted && !(master <= 0.0F)) {
         long now = System.currentTimeMillis();
         Long last = LAST_PLAYED.get(id);
         if (last == null || now - last >= 55L) {
            LAST_PLAYED.put(id, now);
            Minecraft client = Minecraft.getInstance();
            client.getSoundManager()
               .play(
                  SimpleSoundInstance.forUI(
                     SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath("parsmodernpvp-knl2s7pw", id)), volume * master * ui, pitch
                  )
               );
         }
      }
   }

   public static void startup() {
      play("cinematic_startup", 0.85F, 0.72F);
   }

   public static void click() {
      play("pars_ui_sound_pack", 0.34F, 1.18F);
   }

   public static void hover() {
      play("pars_ui_sound_pack", 0.12F, 1.42F);
   }

   public static void open() {
      play("ui_sound_suite", 0.24F, 1.05F);
   }

   public static void close() {
      play("ui_sound_suite", 0.2F, 0.82F);
   }

   public static void back() {
      play("ui_sound_suite", 0.18F, 0.72F);
   }

   public static void toggle() {
      play("pars_ui_sound_pack", 0.25F, 1.02F);
   }

   public static void slider() {
      play("pars_ui_sound_pack", 0.16F, 1.22F);
   }

   public static void notification() {
      play("ui_sound_suite", notifications, 1.08F);
   }

   public static void error() {
      play("ui_sound_suite", 0.3F, 0.48F);
   }

   public static void profileSwitch() {
      play("ui_sound_suite", 0.28F, 0.94F);
   }

   public static void transition() {
      play("ui_sound_suite", 0.16F, 1.16F);
   }

   public static void confirm() {
      play("pars_ui_sound_pack", 0.42F, 0.92F);
   }

   public static void setMaster(float value) {
      master = clamp(value);
   }

   public static void setUi(float value) {
      ui = clamp(value);
   }

   public static void setNotifications(float value) {
      notifications = clamp(value);
   }

   public static void setMuted(boolean value) {
      muted = value;
   }

   public static float master() {
      return master;
   }

   public static float ui() {
      return ui;
   }

   public static float notifications() {
      return notifications;
   }

   public static boolean muted() {
      return muted;
   }

   private static float clamp(float value) {
      return Math.max(0.0F, Math.min(1.0F, value));
   }
}
