package parsmodernpvp_knl2s7pw.client.hud;

import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import parsmodernpvp_knl2s7pw.client.PvpClient;
import parsmodernpvp_knl2s7pw.client.module.Module;
import parsmodernpvp_knl2s7pw.client.ui.UiNotifications;
import parsmodernpvp_knl2s7pw.client.ui.UiTypography;

public final class HudRenderer {
   private static long testBannerUntil;

   private HudRenderer() {
   }

   public static void render(GuiGraphicsExtractor graphics) {
      Minecraft minecraft = Minecraft.getInstance();
      LocalPlayer player = minecraft.player;
      if (player != null && !minecraft.options.hideGui) {
         if (enabled("pvp_hud")) {
            PvpClient.markHudRendered();
            if (testBannerUntil == 0L) {
               testBannerUntil = System.currentTimeMillis() + 8000L;
            }

            if (System.currentTimeMillis() < testBannerUntil) {
               int x = graphics.guiWidth() / 2 - 44;
               graphics.fill(x - 4, 8, x + 92, 22, -586673377);
               UiTypography.text(graphics, "PARS TEST HUD", x, 11, PvpClient.theme().accent(), 0.9F, 1);
            }

            try {
               renderElement(
                  graphics,
                  minecraft,
                  "fps",
                  "hud_fps",
                  "FPS",
                  minecraft.getFps() + "  " + String.format(Locale.ROOT, "%.1fms", PvpClient.performance().frameTimeMs()),
                  82,
                  18
               );
               renderElement(graphics, minecraft, "cps", "hud_cps", "CPS", String.valueOf(PvpClient.performance().cps()), 68, 18);
               renderKeystrokes(graphics, minecraft);
               if (moduleBoolean("pvp_hud", "show_coordinates", true)) {
                  renderElement(
                     graphics,
                     minecraft,
                     "coordinates",
                     "hud_coordinates",
                     "COORD",
                     player.getBlockX() + ", " + player.getBlockY() + ", " + player.getBlockZ(),
                     135,
                     18
                  );
               }

               renderElement(graphics, minecraft, "direction", "hud_direction", "DIR", direction(player.getYRot()), 60, 18);
               renderElement(
                  graphics, minecraft, "speed", "hud_speed", "SPEED", String.format(Locale.ROOT, "%.2f b/s", PvpClient.performance().speed()), 96, 18
               );
               renderElement(graphics, minecraft, "ping", "hud_ping", "PING", ping(minecraft, player) + " ms", 76, 18);
               renderArmor(graphics, minecraft, player);
               renderPotions(graphics, minecraft, player);
               renderElement(
                  graphics,
                  minecraft,
                  "totem",
                  "hud_totem",
                  "TOTEM",
                  count(player.getInventory(), Items.TOTEM_OF_UNDYING) + "  " + (player.getOffhandItem().is(Items.TOTEM_OF_UNDYING) ? "OFFHAND" : "INVENTORY"),
                  145,
                  18
               );
               renderItem(graphics, minecraft, player, "held_item", "hud_held_item", "HELD", player.getMainHandItem());
               renderItem(graphics, minecraft, player, "offhand", "hud_offhand", "OFFHAND", player.getOffhandItem());
               if (moduleBoolean("pvp_hud", "show_session", true)) {
                  renderElement(graphics, minecraft, "session", "hud_session", "SESSION", formatTime(PvpClient.performance().sessionSeconds()), 92, 18);
               }

               if (moduleBoolean("pvp_hud", "show_memory", true)) {
                  renderElement(
                     graphics,
                     minecraft,
                     "memory",
                     "hud_memory",
                     "MEM",
                     PvpClient.performance().usedMemoryMb() + "/" + PvpClient.performance().maxMemoryMb() + " MB",
                     130,
                     18
                  );
               }

               renderElement(graphics, minecraft, "crystal", "crystal_hud", "CRYSTAL", awareness(player), 180, 18);
               renderElement(
                  graphics,
                  minecraft,
                  "mace",
                  "mace_hud",
                  "MACE",
                  String.format(Locale.ROOT, "Y %.1f  FALL %.1f", player.getY(), PvpClient.performance().fallDistance()),
                  160,
                  18
               );
               renderElement(
                  graphics, minecraft, "combat", "combat_info", "COMBAT", "CD " + Math.round(player.getAttackStrengthScale(0.0F) * 100.0F) + "%", 110, 18
               );
               renderElement(graphics, minecraft, "ranged", "ranged_hud", "RANGED", "ARROWS " + count(player.getInventory(), Items.ARROW), 140, 18);
               renderElement(
                  graphics,
                  minecraft,
                  "performance",
                  "performance",
                  "PERF",
                  minecraft.getFps() + " FPS  " + String.format(Locale.ROOT, "%.1f ms", PvpClient.performance().frameTimeMs()),
                  160,
                  18
               );
               drawCrosshair(graphics, player);
               if (PvpClient.debugMode()) {
                  renderDebug(graphics, minecraft);
               }

               renderScreenEffects(graphics);
               UiNotifications.render(graphics, graphics.guiWidth(), graphics.guiHeight());
            } catch (RuntimeException exception) {
               PvpClient.renderError(exception.getClass().getSimpleName());
            }
         }
      }
   }

   private static void renderScreenEffects(GuiGraphicsExtractor g) {
      int width = g.guiWidth();
      int height = g.guiHeight();
      if (PvpClient.motionBlur()) {
         int accent = PvpClient.theme().accent() & 16777215;
         int secondary = PvpClient.theme().secondary() & 16777215;

         for (int i = 1; i <= 4; i++) {
            int alpha = Math.max(3, 24 - i * 5);
            g.fill(0, height / 3 - i, width, height / 3 - i + 1, accent | alpha << 24);
            g.fill(0, height * 2 / 3 + i, width, height * 2 / 3 + i + 1, secondary | alpha << 24);
         }
      }

      if (PvpClient.blur()) {
         g.fill(0, 0, width, height, 134746136);
         g.fill(0, 1, width, height + 1, 84809252);
      }

      if (PvpClient.glow() && PvpClient.softLighting()) {
         int accent = PvpClient.theme().accent() & 16777215;
         int cx = width / 2;
         int cy = height / 2;

         for (int i = 5; i > 0; i--) {
            int radius = i * 22;
            g.fill(cx - radius, cy - 1, cx + radius, cy + 1, accent | 4 + i * 2 << 24);
         }
      }

      if (PvpClient.colorGrading()) {
         g.fill(0, 0, width, height, PvpClient.theme().secondary() & 16777215 | 167772160);
      }

      if (PvpClient.animatedGradient()) {
         int alpha = 5 + (int)(3.0 * Math.sin(System.nanoTime() / 2.4E8));
         g.fill(0, 0, width, height / 2, PvpClient.theme().accent() & 16777215 | alpha << 24);
      }

      if (PvpClient.screenFade()) {
         g.fill(0, 0, width, height, 100861968);
      }

      if (PvpClient.vignette()) {
         g.fill(0, 0, width, 2, 570425344);
         g.fill(0, height - 2, width, height, 570425344);
         g.fill(0, 0, 2, height, 570425344);
         g.fill(width - 2, 0, width, height, 570425344);
      }
   }

   private static void renderElement(GuiGraphicsExtractor g, Minecraft m, String id, String moduleId, String label, String value, int width, int height) {
      if (enabled(moduleId)) {
         HudLayout layout = PvpClient.hudLayout(id);
         if (layout.enabled()) {
            begin(g, layout);
            panel(g, width, height, layout.opacity());
            UiTypography.text(g, label + "  " + value, 5, 5, textColor(layout), 0.82F, 0);
            end(g);
         }
      }
   }

   private static void renderKeystrokes(GuiGraphicsExtractor g, Minecraft m) {
      if (enabled("hud_keystrokes")) {
         HudLayout layout = PvpClient.hudLayout("keystrokes");
         if (layout.enabled()) {
            begin(g, layout);
            panel(g, 76, 52, layout.opacity());
            key(g, m, "W", 26, 3, m.options.keyUp.isDown());
            key(g, m, "A", 3, 26, m.options.keyLeft.isDown());
            key(g, m, "S", 26, 26, m.options.keyDown.isDown());
            key(g, m, "D", 49, 26, m.options.keyRight.isDown());
            UiTypography.text(g, "SPACE", 17, 43, textColor(layout), 0.65F, 0);
            end(g);
         }
      }
   }

   private static void renderArmor(GuiGraphicsExtractor g, Minecraft m, LocalPlayer p) {
      if (enabled("hud_armor")) {
         HudLayout layout = PvpClient.hudLayout("armor");
         if (layout.enabled()) {
            boolean vertical = moduleBoolean("pvp_hud", "armor_vertical", false);
            begin(g, layout);
            panel(g, vertical ? 42 : 100, vertical ? 112 : 38, layout.opacity());
            UiTypography.text(g, "ARMOR", 4, 3, textColor(layout), 0.72F, 0);

            for (int i = 0; i < 4; i++) {
               ItemStack stack = p.getInventory().getItem(39 - i);
               int x = vertical ? 4 : 4 + i * 20;
               int y = vertical ? 15 + i * 24 : 15;
               g.item(stack, x, y);
               UiTypography.text(g, stack.isEmpty() ? "-" : durability(stack) + "%", x, y + 15, textColor(layout), 0.62F, 0);
            }

            end(g);
         }
      }
   }

   private static void renderPotions(GuiGraphicsExtractor g, Minecraft m, LocalPlayer p) {
      if (enabled("hud_potions")) {
         HudLayout layout = PvpClient.hudLayout("potions");
         if (layout.enabled()) {
            begin(g, layout);
            int effectCount = p.getActiveEffects().size();
            panel(g, 180, 18 + Math.min(3, effectCount) * 12, layout.opacity());
            UiTypography.text(g, "POTIONS", 4, 3, textColor(layout), 0.72F, 0);
            int y = 16;
            int count = 0;

            for (MobEffectInstance effect : p.getActiveEffects()) {
               if (count++ >= 3) {
                  break;
               }

               String name = ((MobEffect)effect.getEffect().value()).getDisplayName().getString();
               UiTypography.text(g, name + " " + effect.getDuration() / 20 + "s L" + (effect.getAmplifier() + 1), 4, y, textColor(layout), 0.7F, 0);
               y += 12;
            }

            if (count == 0) {
               UiTypography.text(g, "none", 62, 3, textColor(layout), 0.7F, 0);
            }

            end(g);
         }
      }
   }

   private static void renderItem(GuiGraphicsExtractor g, Minecraft m, LocalPlayer p, String id, String moduleId, String label, ItemStack stack) {
      if (enabled(moduleId)) {
         HudLayout layout = PvpClient.hudLayout(id);
         if (layout.enabled()) {
            begin(g, layout);
            panel(g, 112, 38, layout.opacity());
            UiTypography.text(g, label, 4, 3, textColor(layout), 0.72F, 0);
            g.item(stack, 4, 15);
            UiTypography.text(g, shortName(stack), 24, 18, textColor(layout), 0.68F, 0);
            end(g);
         }
      }
   }

   private static void key(GuiGraphicsExtractor g, Minecraft m, String text, int x, int y, boolean down) {
      g.fill(x, y, x + 20, y + 20, down ? PvpClient.theme().accent() : -2009906091);
      UiTypography.text(g, text, x + 7, y + 6, down ? -15722464 : PvpClient.theme().text(), 0.75F, 0);
   }

   private static void panel(GuiGraphicsExtractor g, int width, int height, float opacity) {
      int bg = alpha(PvpClient.theme().panel(), opacity);
      int radius = (int)PvpClient.cornerRadius();
      if (PvpClient.shadow()) {
         g.fill(2, 2, width + 2, height + 2, 1426063360);
      }

      g.fill(0, 0, width, height, bg);
      if (radius > 0) {
         int corner = alpha(PvpClient.theme().background(), opacity);
         g.fill(0, 0, radius, radius, corner);
         g.fill(width - radius, 0, width, radius, corner);
         g.fill(0, height - radius, radius, height, corner);
         g.fill(width - radius, height - radius, width, height, corner);
      }

      if (PvpClient.glow()) {
         float pulse = 0.7F + 0.3F * (float)Math.sin(System.nanoTime() / 1.0E9 * PvpClient.animationSpeed());
         g.fill(0, 0, Math.max(2, (int)(3.0F * pulse)), height, alpha(PvpClient.theme().accent(), opacity));
         g.fill(width - 2, 0, width, height, alpha(PvpClient.secondaryColor(), opacity));
      }
   }

   private static void begin(GuiGraphicsExtractor g, HudLayout layout) {
      g.pose().pushMatrix();
      g.pose().translate(layout.x(), layout.y() + PvpClient.hudSpacing());
      g.pose().scale(layout.scale() * PvpClient.fontScale(), layout.scale() * PvpClient.fontScale());
   }

   private static void end(GuiGraphicsExtractor g) {
      g.pose().popMatrix();
   }

   private static int textColor(HudLayout layout) {
      int color = layout.color() == -1 ? PvpClient.theme().text() : layout.color();
      return alpha(color, layout.opacity());
   }

   private static int alpha(int color, float opacity) {
      return (int)((color >>> 24 & 0xFF) * opacity) << 24 | color & 16777215;
   }

   private static boolean enabled(String id) {
      Module module = PvpClient.modules() == null ? null : PvpClient.modules().get(id);
      return module != null && module.enabled();
   }

   private static boolean moduleBoolean(String moduleId, String settingId, boolean fallback) {
      Module module = PvpClient.modules() == null ? null : PvpClient.modules().get(moduleId);
      return module == null ? fallback : boolValue(module, settingId, fallback);
   }

   private static String awareness(LocalPlayer player) {
      int crystals = count(player.getInventory(), Items.END_CRYSTAL);
      int obsidian = count(player.getInventory(), Items.OBSIDIAN);
      Module crystal = PvpClient.modules().get("crystal_hud");
      StringBuilder value = new StringBuilder("C ").append(crystals).append("  O ").append(obsidian);
      if (boolValue(crystal, "show_radius", true)) {
         value.append("  R 12");
      }

      if (boolValue(crystal, "show_damage", true)) {
         value.append("  DMG legit");
      }

      return value.toString();
   }

   private static void drawCrosshair(GuiGraphicsExtractor g, LocalPlayer p) {
      Module m = PvpClient.modules().get("crosshair");
      if (m != null && m.enabled()) {
         int cx = g.guiWidth() / 2;
         int cy = g.guiHeight() / 2;
         int size = intValue(m, "size", 7);
         int thick = intValue(m, "thickness", 2);
         int gap = intValue(m, "gap", 3);
         int color = alpha(intValue(m, "color", PvpClient.accentColor()), intValue(m, "opacity", 85) / 100.0F);
         String style = stringValue(m, "style", "Plus");
         if (style.equalsIgnoreCase("Dot")) {
            g.fill(cx - thick, cy - thick, cx + thick + 1, cy + thick + 1, color);
         } else if (style.equalsIgnoreCase("Minimal")) {
            g.fill(cx - 1, cy - 1, cx + 2, cy + 2, color);
         } else if (style.equalsIgnoreCase("Circle")) {
            g.fill(cx - size, cy - 1, cx - size + thick, cy + 1, color);
            g.fill(cx + size - thick, cy - 1, cx + size, cy + 1, color);
            g.fill(cx - 1, cy - size, cx + 1, cy - size + thick, color);
            g.fill(cx - 1, cy + size - thick, cx + 1, cy + size, color);
         } else {
            g.fill(cx - thick / 2, cy - size, cx + thick, cy - gap, color);
            g.fill(cx - thick / 2, cy + gap, cx + thick, cy + size, color);
            g.fill(cx - size, cy - thick / 2, cx - gap, cy + thick, color);
            g.fill(cx + gap, cy - thick / 2, cx + size, cy + thick, color);
            if (boolValue(m, "hit_marker", true) && p.hurtTime > 0) {
               g.fill(cx - size, cy - size, cx - size + 2, cy - size + 2, -48043);
               g.fill(cx + size - 2, cy - size, cx + size, cy - size + 2, -48043);
            }

            if (boolValue(m, "attack_indicator", true)) {
               g.fill(cx - 12, cy + size + 5, cx - 12 + (int)(24.0F * p.getAttackStrengthScale(0.0F)), cy + size + 7, color);
            }
         }
      }
   }

   private static void renderDebug(GuiGraphicsExtractor g, Minecraft m) {
      int x = g.guiWidth() - 285;
      int y = 8;
      g.fill(x, y, x + 277, y + 142, -871888110);
      int text = PvpClient.theme().text();
      UiTypography.text(g, "PARS DEBUG", x + 6, y + 5, PvpClient.theme().accent(), 0.7F, 0);
      UiTypography.text(g, "MC " + m.getLaunchedVersion() + "  FABRIC", x + 6, y + 17, text, 0.65F, 0);
      UiTypography.text(g, "INIT " + pass(PvpClient.initialized()) + "  INPUT " + pass(PvpClient.inputRegistered()), x + 6, y + 29, text, 0.65F, 0);
      UiTypography.text(g, "HUD REGISTERED " + pass(PvpClient.hudRegistered()) + "  GUI " + pass(PvpClient.guiRegistered()), x + 6, y + 41, text, 0.65F, 0);
      UiTypography.text(
         g,
         "MODULES " + PvpClient.modules().all().size() + " / ACTIVE " + PvpClient.modules().all().values().stream().filter(Module::enabled).count(),
         x + 6,
         y + 53,
         text,
         0.65F,
         0
      );
      UiTypography.text(g, "HUD COUNT " + PvpClient.hudLayouts().size() + "  KEY " + PvpClient.globalKeybind(), x + 6, y + 65, text, 0.65F, 0);
      UiTypography.text(g, "PROFILE " + PvpClient.profiles().active().displayName(), x + 6, y + 77, text, 0.65F, 0);
      UiTypography.text(g, "CONFIG " + PvpClient.configPath(), x + 6, y + 89, text, 0.65F, 0);
      UiTypography.text(
         g, "HUD RENDER TEST: " + (PvpClient.hudRegistered() ? "PASS" : "FAIL"), x + 6, y + 101, PvpClient.hudRegistered() ? -12392796 : -44188, 0.65F, 0
      );
      UiTypography.text(g, "LAST ERROR " + PvpClient.lastRenderError(), x + 6, y + 113, PvpClient.theme().text(), 0.65F, 0);
   }

   private static String pass(boolean value) {
      return value ? "PASS" : "FAIL";
   }

   private static int count(Inventory inventory, Item item) {
      int total = 0;

      for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
         ItemStack stack = inventory.getItem(slot);
         if (stack.is(item)) {
            total += stack.getCount();
         }
      }

      return total;
   }

   private static String shortName(ItemStack stack) {
      if (stack.isEmpty()) {
         return "EMPTY";
      }

      String name = stack.getHoverName().getString();
      return name.length() > 12 ? name.substring(0, 12) : name;
   }

   private static int durability(ItemStack stack) {
      return !stack.isEmpty() && stack.isDamageableItem() ? Math.max(0, 100 - stack.getDamageValue() * 100 / stack.getMaxDamage()) : 100;
   }

   private static int ping(Minecraft minecraft, LocalPlayer player) {
      if (minecraft.getConnection() == null) {
         return 0;
      }

      PlayerInfo info = minecraft.getConnection().getPlayerInfo(player.getUUID());
      return info == null ? 0 : info.getLatency();
   }

   private static String direction(float yaw) {
      String[] names = new String[]{"S", "W", "N", "E"};
      return names[Math.floorMod((int)Math.floor(yaw * 4.0F / 360.0F + 0.5F), 4)];
   }

   private static String formatTime(long seconds) {
      return String.format(Locale.ROOT, "%02d:%02d", seconds / 60L, seconds % 60L);
   }

   private static String stringValue(Module module, String id, String fallback) {
      Object value = module.settings().get(id) == null ? fallback : module.settings().get(id).value();
      return value == null ? fallback : value.toString();
   }

   private static int intValue(Module module, String id, int fallback) {
      return (module.settings().get(id) == null ? fallback : module.settings().get(id).value()) instanceof Number number ? number.intValue() : fallback;
   }

   private static boolean boolValue(Module module, String id, boolean fallback) {
      return (module.settings().get(id) == null ? fallback : module.settings().get(id).value()) instanceof Boolean bool ? bool : fallback;
   }
}
