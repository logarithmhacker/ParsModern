package parsmodernpvp_knl2s7pw.client.utils;

public class MathUtils {
    
    public static float lerp(float start, float end, float t) {
        return start + (end - start) * t;
    }
    
    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }
    
    public static float map(float value, float start1, float stop1, float start2, float stop2) {
        return start2 + (stop2 - start2) * ((value - start1) / (stop1 - start1));
    }
    
    public static float smoothstep(float t) {
        return t * t * (3.0f - 2.0f * t);
    }
    
    public static float smootherstep(float t) {
        return t * t * t * (t * (t * 6.0f - 15.0f) + 10.0f);
    }
    
    public static boolean approxEquals(float a, float b, float epsilon) {
        return Math.abs(a - b) < epsilon;
    }
}
