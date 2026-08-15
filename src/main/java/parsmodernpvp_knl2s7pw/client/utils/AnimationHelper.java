package parsmodernpvp_knl2s7pw.client.utils;

public class AnimationHelper {
    
    private float currentValue;
    private float targetValue;
    private float speed;
    private long startTime;
    private long duration;
    private boolean isAnimating;
    private EasingType easingType;
    
    public enum EasingType {
        LINEAR,
        EASE_IN_QUAD,
        EASE_OUT_QUAD,
        EASE_IN_OUT_QUAD,
        EASE_IN_CUBIC,
        EASE_OUT_CUBIC,
        EASE_IN_OUT_CUBIC,
        EASE_OUT_ELASTIC,
        EASE_OUT_BOUNCE
    }
    
    public AnimationHelper() {
        this.currentValue = 0.0f;
        this.targetValue = 0.0f;
        this.speed = 0.3f;
        this.isAnimating = false;
        this.easingType = EasingType.EASE_OUT_QUAD;
    }
    
    public AnimationHelper(float initialValue) {
        this.currentValue = initialValue;
        this.targetValue = initialValue;
        this.speed = 0.3f;
        this.isAnimating = false;
        this.easingType = EasingType.EASE_OUT_QUAD;
    }
    
    public AnimationHelper(float initialValue, float speed) {
        this.currentValue = initialValue;
        this.targetValue = initialValue;
        this.speed = speed;
        this.isAnimating = false;
        this.easingType = EasingType.EASE_OUT_QUAD;
    }
    
    public void setTarget(float target) {
        if (this.targetValue != target) {
            this.targetValue = target;
            this.isAnimating = true;
            this.startTime = System.currentTimeMillis();
            this.duration = (long) (Math.abs(target - this.currentValue) / speed * 1000);
        }
    }
    
    public void setTargetImmediate(float target) {
        this.currentValue = target;
        this.targetValue = target;
        this.isAnimating = false;
    }
    
    public void setDuration(long durationMs) {
        this.duration = durationMs;
    }
    
    public void setEasing(EasingType type) {
        this.easingType = type;
    }
    
    public void update() {
        if (!isAnimating) return;
        
        long elapsed = System.currentTimeMillis() - startTime;
        float t = Math.min(1.0f, (float) elapsed / duration);
        
        if (t >= 1.0f) {
            currentValue = targetValue;
            isAnimating = false;
            return;
        }
        
        float easedT = applyEasing(t, easingType);
        currentValue = MathUtils.lerp(currentValue, targetValue, easedT);
    }
    
    private float applyEasing(float t, EasingType type) {
        return switch (type) {
            case LINEAR -> t;
            case EASE_IN_QUAD -> t * t;
            case EASE_OUT_QUAD -> t * (2 - t);
            case EASE_IN_OUT_QUAD -> t < 0.5f ? 2 * t * t : -1 + (4 - 2 * t) * t;
            case EASE_IN_CUBIC -> t * t * t;
            case EASE_OUT_CUBIC -> {
                float f = t - 1;
                yield f * f * f + 1;
            }
            case EASE_IN_OUT_CUBIC -> t < 0.5f ? 4 * t * t * t : (t - 1) * (2 * t - 2) * (2 * t - 2) + 1;
            default -> t;
        };
    }
    
    public float getValue() {
        return currentValue;
    }
    
    public float getTarget() {
        return targetValue;
    }
    
    public boolean isAnimating() {
        return isAnimating;
    }
    
    public static float lerp(float start, float end, float t) {
        return MathUtils.lerp(start, end, t);
    }
    
    public static float smoothStep(float t) {
        return MathUtils.smoothstep(t);
    }
}
