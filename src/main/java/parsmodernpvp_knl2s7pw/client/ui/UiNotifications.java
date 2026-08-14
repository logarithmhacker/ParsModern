package parsmodernpvp_knl2s7pw.client.ui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import parsmodernpvp_knl2s7pw.client.PvpClient;
import parsmodernpvp_knl2s7pw.client.notification.NotificationCenter;

public final class UiNotifications {
   private UiNotifications() {
   }

   public static void render(GuiGraphicsExtractor graphics, int width, int height) {
      if (PvpClient.notifications() != null) {
         int y = height - UiScale.s(34);
         long now = System.currentTimeMillis();

         for (NotificationCenter.Notification notification : PvpClient.notifications().active()) {
            int textWidth = UiTypography.width(notification.message(), 1.0F, 0);
            int boxWidth = Math.min(UiScale.s(300), Math.max(UiScale.s(150), textWidth + UiScale.s(28)));
            int x = width - boxWidth - UiScale.s(18);
            float fade = notification.progress(now) > 0.78F ? (1.0F - notification.progress(now)) / 0.22F : 1.0F;
            int alpha = Math.max(0, Math.min(255, (int)(220.0F * fade)));
            graphics.fill(x + 2, y + 2, x + boxWidth + 2, y + UiScale.s(26), alpha / 3 << 24);
            graphics.fill(x, y, x + boxWidth, y + UiScale.s(24), alpha << 24 | PvpClient.themeEngine().color("notification") & 16777215);
            graphics.fill(x, y, x + 3, y + UiScale.s(24), alpha << 24 | PvpClient.theme().accent() & 16777215);
            UiTypography.text(graphics, notification.message(), x + UiScale.s(11), y + UiScale.s(8), alpha << 24 | PvpClient.theme().text() & 16777215, 1.0F, 0);
            PARSProgressBar.draw(graphics, x + UiScale.s(11), y + UiScale.s(20), boxWidth - UiScale.s(20), 1.0F - notification.progress(now));
            y -= UiScale.s(30);
         }
      }
   }
}
