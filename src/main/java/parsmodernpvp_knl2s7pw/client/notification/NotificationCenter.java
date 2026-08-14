package parsmodernpvp_knl2s7pw.client.notification;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;
import parsmodernpvp_knl2s7pw.client.ui.UiSoundEngine;

public final class NotificationCenter {
   private final Deque<String> messages = new ArrayDeque<>();
   private final Deque<NotificationCenter.Notification> timeline = new ArrayDeque<>();

   public void push(String message) {
      this.push(message, 3500L);
   }

   public void push(String message, long lifetimeMs) {
      if (message != null && !message.isBlank()) {
         if (this.messages.size() == 8) {
            this.messages.removeFirst();
         }

         this.messages.addLast(message);
         if (this.timeline.size() == 8) {
            this.timeline.removeFirst();
         }

         this.timeline.addLast(new NotificationCenter.Notification(message, System.currentTimeMillis(), Math.max(800L, lifetimeMs)));
         UiSoundEngine.notification();
      }
   }

   public void pushUi(String message) {
      this.push(message);
   }

   public Deque<String> recent() {
      return new ArrayDeque<>(this.messages);
   }

   public List<NotificationCenter.Notification> active() {
      long now = System.currentTimeMillis();
      this.timeline.removeIf(notification -> now - notification.createdAt() > notification.lifetimeMs());
      return this.timeline.stream().collect(Collectors.toList());
   }

   public record Notification(String message, long createdAt, long lifetimeMs) {
      public float progress(long now) {
         return Math.max(0.0F, Math.min(1.0F, (float)(now - this.createdAt) / (float)this.lifetimeMs));
      }
   }
}
