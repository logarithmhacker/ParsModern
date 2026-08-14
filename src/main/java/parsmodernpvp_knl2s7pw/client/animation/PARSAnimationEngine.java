package parsmodernpvp_knl2s7pw.client.animation;

public final class PARSAnimationEngine {
   private PARSAnimationEngine() {
   }

   public static float progress(long startNanos, long durationNanos, PARSAnimationEngine.Easing easing, boolean reducedMotion) {
      return AnimationEngine.progress(startNanos, durationNanos, map(easing), reducedMotion);
   }

   public static float interpolate(float from, float to, float amount) {
      return AnimationEngine.interpolate(from, to, amount);
   }

   public static float spring(float amount) {
      return AnimationEngine.ease(amount, AnimationEngine.Curve.SPRING);
   }

   public static float ease(float amount, PARSAnimationEngine.Easing easing) {
      return AnimationEngine.ease(amount, map(easing));
   }

   private static AnimationEngine.Curve map(PARSAnimationEngine.Easing easing) {
      return switch (easing == null ? PARSAnimationEngine.Easing.EASE_OUT : easing) {
         case LINEAR -> AnimationEngine.Curve.LINEAR;
         case EASE_IN -> AnimationEngine.Curve.EASE_IN;
         case EASE_OUT -> AnimationEngine.Curve.EASE_OUT;
         case EASE_IN_OUT -> AnimationEngine.Curve.EASE_IN_OUT;
         case CUBIC -> AnimationEngine.Curve.CUBIC;
         case EXPO -> AnimationEngine.Curve.EXPO;
         case SPRING -> AnimationEngine.Curve.SPRING;
      };
   }

   public enum Easing {
      LINEAR,
      EASE_IN,
      EASE_OUT,
      EASE_IN_OUT,
      CUBIC,
      EXPO,
      SPRING;
   }
}
