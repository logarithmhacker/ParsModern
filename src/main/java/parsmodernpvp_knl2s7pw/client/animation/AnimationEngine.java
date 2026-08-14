package parsmodernpvp_knl2s7pw.client.animation;

import parsmodernpvp_knl2s7pw.client.PvpClient;

public final class AnimationEngine {
   private AnimationEngine() {
   }

   public static float progress(long startNanos, long durationNanos, AnimationEngine.Curve curve, boolean reducedMotion) {
      if (!reducedMotion && durationNanos > 0L) {
         float speed = PvpClient.initialized() ? Math.max(0.1F, PvpClient.animationSpeed()) : 1.0F;
         float t = Math.max(0.0F, Math.min(1.0F, (float)(System.nanoTime() - startNanos) * speed / (float)durationNanos));
         return ease(t, curve);
      } else {
         return 1.0F;
      }
   }

   public static float fade(long start, long duration, boolean reduced) {
      return progress(start, duration, AnimationEngine.Curve.EASE_OUT, reduced);
   }

   public static float slide(long start, long duration, AnimationEngine.Curve curve, boolean reduced) {
      return progress(start, duration, curve, reduced);
   }

   public static float scale(long start, long duration, boolean reduced) {
      return progress(start, duration, AnimationEngine.Curve.SPRING, reduced);
   }

   public static float interpolate(float from, float to, float amount) {
      return from + (to - from) * ease(amount, AnimationEngine.Curve.EASE_OUT);
   }

   public static float ease(float t, AnimationEngine.Curve curve) {
      t = Math.max(0.0F, Math.min(1.0F, t));

      return switch (curve) {
         case LINEAR -> t;
         case EASE_IN -> t * t;
         case EASE_OUT -> 1.0F - (1.0F - t) * (1.0F - t);
         case EASE_IN_OUT -> t < 0.5F ? 2.0F * t * t : 1.0F - (float)Math.pow(-2.0F * t + 2.0F, 2.0) / 2.0F;
         case CUBIC -> t * t * t;
         case EXPO -> t == 0.0F ? 0.0F : (float)Math.pow(2.0, 10.0F * t - 10.0F);
         case SPRING -> 1.0F - (float)(Math.exp(-7.0F * t) * Math.cos(9.0F * t));
      };
   }

   public enum Curve {
      LINEAR,
      EASE_IN,
      EASE_OUT,
      EASE_IN_OUT,
      CUBIC,
      EXPO,
      SPRING;
   }
}
