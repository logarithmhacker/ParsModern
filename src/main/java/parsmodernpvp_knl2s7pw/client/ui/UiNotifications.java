package parsmodernpvp_knl2s7pw.client.ui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import parsmodernpvp_knl2s7pw.client.PvpClient;
import parsmodernpvp_knl2s7pw.client.animation.AnimationEngine;

/**
 * Premium notification system with smooth animations and multiple types.
 */
public final class UiNotifications {
   private static final int MAX_NOTIFICATIONS = 5;
   private static Notification[] notifications = new Notification[MAX_NOTIFICATIONS];
   
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
      shiftNotifications();
      notifications[0] = new Notification(message, type, System.nanoTime(), durationMs);
      UiSoundEngine.notification();
   }

   public static void success(String message) {
      push(message, NotificationType.SUCCESS);
   }

   public static void warning(String message) {
      push(message, NotificationType.WARNING);
   }

   public static void error(String message) {
      push(message, NotificationType.ERROR);
   }

   public static void render(GuiGraphicsExtractor g, int screenWidth, int screenHeight) {
      int x = screenWidth - DesignTokens.SPACE_16 - 280;
      int y = screenHeight - DesignTokens.SPACE_16 - 60;
      
      for (int i = MAX_NOTIFICATIONS - 1; i >= 0; i--) {
         Notification n = notifications[i];
         if (n != null && !n.isExpired()) {
            renderNotification(g, n, x, y - i * 52);
         }
      }
   }

   private static void renderNotification(GuiGraphicsExtractor g, Notification n, int x, int y) {
      int width = 280;
      int height = 48;
      float radius = DesignTokens.RADIUS_MD;
      long age = System.nanoTime() - n.created;
      float fadeOut = AnimationEngine.progress(n.created + 2500000000L, 500000000L, AnimationEngine.Curve.EASE_OUT, PvpClient.reducedMotion());
      float fadeIn = Math.min(1.0F, AnimationEngine.progress(n.created, 200000000L, AnimationEngine.Curve.EASE_OUT, PvpClient.reducedMotion()));
      float alpha = fadeIn * (1.0F - fadeOut);
      
      if (alpha < 0.01F) return;
      
      int r = Math.max(0, Math.min((int)radius, height / 2));
      
      // Background
      int bgColor = n.type.backgroundColor;
      g.fill(x + r, y, x + width - r, y + height, bgColor);
      g.fill(x, y + r, x + width, y + height - r, bgColor);
      if (r > 0) {
         g.fill(x + r, y, x + width - r, y + r, bgColor);
         g.fill(x + r, y + height - r, x + width - r, y + height, bgColor);
      }
      
      // Left accent bar
      int accentColor = n.type.accentColor;
      g.fill(x, y + r, x + 3, y + height - r, accentColor);
      
      // Icon
      String icon = n.type.icon;
      PARSFontEngine.draw(g, icon, x + 12, y + 14, accentColor, PARSFontEngine.Token.BODY, false, false);
      
      // Message
      PARSFontEngine.draw(g, n.message, x + 32, y + 14, -1, PARSFontEngine.Token.SMALL, PvpClient.shadow(), false);
      
      // Time indicator
      int timeWidth = Math.round(width * (1.0F - fadeOut));
      if (timeWidth > 2) {
         int timeColor = withAlpha(accentColor, 0.3F);
         g.fill(x + width - timeWidth, y + height - 2, x + width, y + height - 1, timeColor);
      }
   }

   private static void shiftNotifications() {
      for (int i = MAX_NOTIFICATIONS - 1; i > 0; i--) {
         notifications[i] = notifications[i - 1];
      }
   }

   public enum NotificationType {
      INFO("i", 0x992563EB, 0xFF2563EB),
      SUCCESS("✓", 0x9905966B, 0xFF05966B),
      WARNING("!", 0x99D9770E, 0xFFD9770E),
      ERROR("✕", 0x99DC2626, 0xFFDC2626);

      final String icon;
      final int backgroundColor;
      final int accentColor;

      NotificationType(String icon, int backgroundColor, int accentColor) {
         this.icon = icon;
         this.backgroundColor = backgroundColor;
         this.accentColor = accentColor;
      }
   }

   private static class Notification {
      final String message;
      final NotificationType type;
      final long created;
      final long durationMs;

      Notification(String message, NotificationType type, long created) {
         this(message, type, created, 3000L);
      }
      
      Notification(String message, NotificationType type, long created, long durationMs) {
         this.message = message;
         this.type = type;
         this.created = created;
         this.durationMs = durationMs;
      }

      boolean isExpired() {
         return System.nanoTime() - created > durationMs * 1000000L;
      }
   }

   private static int withAlpha(int color, float alpha) {
      int a = Math.max(0, Math.min(255, (int)((color >>> 24 & 0xFF) * alpha)));
      return a << 24 | color & 0xFFFFFF;
   }
}
