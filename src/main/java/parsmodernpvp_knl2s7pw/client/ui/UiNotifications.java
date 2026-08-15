package parsmodernpvp_knl2s7pw.client.ui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import parsmodernpvp_knl2s7pw.client.PvpClient;
import parsmodernpvp_knl2s7pw.client.animation.AnimationEngine;

/** Lightweight toast stack with the same compact PARS surface language. */
public final class UiNotifications {
    private static final int MAX_NOTIFICATIONS = 5;
    private static final Notification[] notifications = new Notification[MAX_NOTIFICATIONS];

    private UiNotifications() {
    }

    public static void push(String message) {
        push(message, NotificationType.INFO);
    }

    public static void push(String message, long durationMs) {
        push(message, NotificationType.INFO, durationMs);
    }

    public static void push(String message, NotificationType type) {
        push(message, type, 3000L);
    }

    public static void push(String message, NotificationType type, long durationMs) {
        if (message == null || message.isBlank()) return;
        shiftNotifications();
        notifications[0] = new Notification(message, type == null ? NotificationType.INFO : type,
                System.nanoTime(), Math.max(500L, durationMs));
        UiSoundEngine.notification();
    }

    public static void success(String message) { push(message, NotificationType.SUCCESS); }
    public static void warning(String message) { push(message, NotificationType.WARNING); }
    public static void error(String message) { push(message, NotificationType.ERROR); }

    public static void render(GuiGraphicsExtractor g, int screenWidth, int screenHeight) {
        int width = UiScale.s(300);
        int height = UiScale.s(52);
        int gap = UiScale.s(8);
        int right = UiScale.s(18);
        int bottom = UiScale.s(18);
        int x = screenWidth - right - width;
        int y = screenHeight - bottom - height;

        for (int i = MAX_NOTIFICATIONS - 1; i >= 0; i--) {
            Notification notification = notifications[i];
            if (notification != null && !notification.isExpired()) {
                renderNotification(g, notification, x, y - i * (height + gap), width, height);
            }
        }
    }

    private static void renderNotification(GuiGraphicsExtractor g, Notification n,
                                           int x, int y, int width, int height) {
        long now = System.nanoTime();
        long age = now - n.created;
        long duration = n.durationMs * 1_000_000L;
        float fadeIn = Math.min(1.0F,
                AnimationEngine.progress(n.created, 180_000_000L,
                        AnimationEngine.Curve.EASE_OUT, PvpClient.reducedMotion()));
        float fadeOut = age > duration - 450_000_000L
                ? AnimationEngine.progress(n.created + duration - 450_000_000L, 450_000_000L,
                AnimationEngine.Curve.EASE_IN, PvpClient.reducedMotion())
                : 0.0F;
        float alpha = fadeIn * (1.0F - fadeOut);
        if (alpha <= 0.01F) return;

        int r = UiScale.s(9);
        int surface = withAlpha(PvpClient.themeEngine().color("panel"), alpha * 0.96F);
        int border = withAlpha(n.type.accentColor, alpha * 0.55F);

        if (PvpClient.shadow()) {
            fillRounded(g, x + UiScale.s(2), y + UiScale.s(3), width, height, r,
                    withAlpha(0x000000, alpha * 0.35F));
        }
        fillRounded(g, x, y, width, height, r, surface);
        fillRoundedBorder(g, x, y, width, height, r, border,
                withAlpha(PvpClient.themeEngine().color("panel"), alpha * 0.96F));

        int accentWidth = UiScale.s(3);
        g.fill(x + 1, y + r, x + accentWidth, y + height - r,
                withAlpha(n.type.accentColor, alpha));

        int iconX = x + UiScale.s(12);
        int textX = x + UiScale.s(34);
        PARSFontEngine.draw(g, n.type.icon, iconX, y + UiScale.s(15),
                withAlpha(n.type.accentColor, alpha), PARSFontEngine.Token.BODY, false, false);
        PARSFontEngine.draw(g, n.message, textX, y + UiScale.s(16),
                withAlpha(PvpClient.themeEngine().text(), alpha), PARSFontEngine.Token.SMALL,
                PvpClient.shadow(), false);

        float remaining = Math.max(0.0F, 1.0F - age / (float) duration);
        int barWidth = Math.max(0, Math.round((width - UiScale.s(24)) * remaining));
        g.fill(x + UiScale.s(12), y + height - UiScale.s(4),
                x + UiScale.s(12) + barWidth, y + height - UiScale.s(2),
                withAlpha(n.type.accentColor, alpha * 0.65F));
    }

    private static void shiftNotifications() {
        for (int i = MAX_NOTIFICATIONS - 1; i > 0; i--) {
            notifications[i] = notifications[i - 1];
        }
    }

    public enum NotificationType {
        INFO("i", 0xFF5B8CFF),
        SUCCESS("✓", 0xFF42D392),
        WARNING("!", 0xFFFFB454),
        ERROR("×", 0xFFFF647C);

        final String icon;
        final int accentColor;

        NotificationType(String icon, int accentColor) {
            this.icon = icon;
            this.accentColor = accentColor;
        }
    }

    private static final class Notification {
        final String message;
        final NotificationType type;
        final long created;
        final long durationMs;

        Notification(String message, NotificationType type, long created, long durationMs) {
            this.message = message;
            this.type = type;
            this.created = created;
            this.durationMs = durationMs;
        }

        boolean isExpired() {
            return System.nanoTime() - created > durationMs * 1_000_000L;
        }
    }

    private static int withAlpha(int color, float alpha) {
        return (Math.max(0, Math.min(255, Math.round(alpha * 255.0F))) << 24) | (color & 0xFFFFFF);
    }

    private static void fillRounded(GuiGraphicsExtractor g, int x, int y, int width,
                                    int height, int radius, int color) {
        if (width <= 0 || height <= 0) return;
        if (radius <= 0) {
            g.fill(x, y, x + width, y + height, color);
            return;
        }
        for (int row = 0; row < height; row++) {
            int fromEdge = Math.min(row, height - row - 1);
            int inset = fromEdge < radius
                    ? radius - (int) Math.floor(Math.sqrt(radius * radius -
                    (radius - fromEdge - 0.5) * (radius - fromEdge - 0.5)))
                    : 0;
            g.fill(x + inset, y + row, x + width - inset, y + row + 1, color);
        }
    }

    private static void fillRoundedBorder(GuiGraphicsExtractor g, int x, int y, int width,
                                           int height, int radius, int border, int inner) {
        fillRounded(g, x, y, width, height, radius, border);
        fillRounded(g, x + 1, y + 1, width - 2, height - 2,
                Math.max(0, radius - 1), inner);
    }
}
