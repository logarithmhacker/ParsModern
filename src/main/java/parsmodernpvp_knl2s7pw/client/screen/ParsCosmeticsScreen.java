package parsmodernpvp_knl2s7pw.client.screen;

<<<<<<< HEAD
import java.util.Locale;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.minecraft.client.Minecraft;
=======
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
>>>>>>> 77bc9da847b12cb9940d49afdb8df99b0af11b92
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
<<<<<<< HEAD
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import org.joml.Quaternionf;
import org.joml.Vector3f;
=======
>>>>>>> 77bc9da847b12cb9940d49afdb8df99b0af11b92
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import parsmodernpvp_knl2s7pw.client.PvpClient;
import parsmodernpvp_knl2s7pw.client.cosmetics.Cosmetic;
import parsmodernpvp_knl2s7pw.client.cosmetics.CosmeticType;
import parsmodernpvp_knl2s7pw.client.cosmetics.Rarity;
import parsmodernpvp_knl2s7pw.client.ui.DesignTokens;
import parsmodernpvp_knl2s7pw.client.ui.PARSFramework;
import parsmodernpvp_knl2s7pw.client.ui.UiScale;
import parsmodernpvp_knl2s7pw.client.ui.UiSoundEngine;
import parsmodernpvp_knl2s7pw.client.ui.UiTypography;

/**
 * Premium Cosmetics Screen with category navigation, search, filtering, and preview.
 * Fully functional with equip/unequip, favorites, and rarity-based sorting.
 */
public class ParsCosmeticsScreen extends Screen {
   private static final String[] TABS = {"ALL", "OWNED", "FAVORITES"};
   
   private final List<CosmeticType> categories;
   private CosmeticType selectedCategory;
   private String selectedTab = "ALL";
   private String searchQuery = "";
   private EditBox searchBox;
   private int scrollOffset;
   private int maxScroll;
   private Cosmetic selectedCosmetic;
   private Cosmetic hoveredCosmetic;
   private long lastHoverChange;
   
   public ParsCosmeticsScreen() {
      super(Component.literal("PARS Cosmetics"));
      this.categories = PvpClient.cosmetics().getAllTypes();
      this.selectedCategory = this.categories.isEmpty() ? null : this.categories.get(0);
   }
   
   private static int s(int value) {
      return UiScale.s(value);
   }
<<<<<<< HEAD

   private static float sf(float value) {
      return UiScale.s(value);
   }
=======
>>>>>>> 77bc9da847b12cb9940d49afdb8df99b0af11b92
   
   private int left() {
      return Math.max(s(16), this.width / 2 - s(450));
   }
   
   private int top() {
      return Math.max(s(16), this.height / 2 - s(240));
   }
   
   private int contentLeft() {
      return this.left() + s(200);
   }
   
   @Override
   protected void init() {
      int l = this.left();
      int t = this.top();
      int cl = this.contentLeft();
      
      // Category sidebar buttons
      for (int i = 0; i < this.categories.size(); i++) {
         CosmeticType type = this.categories.get(i);
         int count = PvpClient.cosmetics().getByType(type).size();
         this.addRenderableWidget(Button.builder(Component.empty(), btn -> {
            this.selectedCategory = type;
            this.scrollOffset = 0;
            this.selectedCosmetic = null;
            UiSoundEngine.click();
            this.rebuildWidgets();
         }).bounds(l + s(12), t + s(50 + i * 38), s(176), s(32)).build());
      }
      
      // Tab buttons (ALL, OWNED, FAVORITES)
      for (int i = 0; i < TABS.length; i++) {
         final int idx = i;
         this.addRenderableWidget(Button.builder(Component.literal(TABS[i]), btn -> {
            this.selectedTab = TABS[idx];
            this.scrollOffset = 0;
            this.selectedCosmetic = null;
            UiSoundEngine.click();
            this.rebuildWidgets();
         }).bounds(cl + s(i * 82), t + s(14), s(76), s(26)).build());
      }
      
      // Search box
      this.searchBox = new EditBox(this.font, cl, t + s(48), s(260), s(24), Component.literal("Search"));
      this.searchBox.setHint(Component.literal("Search cosmetics..."));
      this.searchBox.setValue(this.searchQuery);
      this.searchBox.setResponder(query -> {
         this.searchQuery = query;
         this.scrollOffset = 0;
         this.selectedCosmetic = null;
      });
      this.addWidget(this.searchBox);
      
      // Close button
      this.addRenderableWidget(Button.builder(Component.literal("✕"), btn -> this.onClose())
         .bounds(this.width - s(40), s(12), s(28), s(28)).build());
      
      // Reset all alpha values for smooth appearance
      this.children().forEach(child -> {
         if (child instanceof Button btn) {
            btn.setAlpha(0.0F);
         }
      });
      if (this.searchBox != null) {
         this.searchBox.setAlpha(0.0F);
      }
   }
   
   private List<Cosmetic> getFilteredCosmetics() {
      List<Cosmetic> result = new ArrayList<>();
      List<Cosmetic> source;
      
      // Get source list based on tab
      if (this.selectedCategory != null) {
         source = PvpClient.cosmetics().getByType(this.selectedCategory);
      } else {
         source = PvpClient.cosmetics().getAllSorted();
      }
      
      // Apply tab filter
      for (Cosmetic c : source) {
         boolean matches = true;
         
         if (this.selectedTab.equals("OWNED")) {
            matches = PvpClient.cosmetics().isUnlocked(c.getId());
         } else if (this.selectedTab.equals("FAVORITES")) {
            matches = PvpClient.cosmetics().isFavorite(c.getId());
         }
         
         // Apply search filter
         if (!this.searchQuery.isBlank()) {
            String query = this.searchQuery.toLowerCase();
            matches = matches && (c.getName().toLowerCase().contains(query) || 
                                  c.getDescription().toLowerCase().contains(query));
         }
         
         if (matches) {
            result.add(c);
         }
      }
      
      // Sort by rarity
      result.sort(Rarity.getComparator());
      return result;
   }
   
   @Override
   public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float partialTick) {
      // Background
      PARSFramework.background(g, this.width, this.height);
      
      int l = this.left();
      int t = this.top();
      int cl = this.contentLeft();
      
      // Main panel
      PARSFramework.panel(g, l, t, s(884), s(480));
      
      // Sidebar panel
      PARSFramework.panel(g, l + s(8), t + s(46), s(184), s(426));
      
      // Header
      UiTypography.title(g, "COSMETICS", l + s(20), t + s(20), PvpClient.themeEngine().accent());
      UiTypography.text(g, "Customize your style", l + s(140), t + s(24), PvpClient.themeEngine().secondary(), 0.72F, 0);
      
      // Render category buttons
      for (int i = 0; i < this.categories.size(); i++) {
         CosmeticType type = this.categories.get(i);
         int count = PvpClient.cosmetics().getByType(type).size();
         int owned = (int)PvpClient.cosmetics().getByType(type).stream()
            .filter(c -> PvpClient.cosmetics().isUnlocked(c.getId())).count();
         
         boolean selected = this.selectedCategory == type;
         boolean hovered = this.isMouseOverCategory(mouseX, mouseY, i);
         
         this.renderCategoryButton(g, type, count, owned, l + s(12), t + s(50 + i * 38), selected, hovered);
      }
      
      // Content area
      List<Cosmetic> cosmetics = this.getFilteredCosmetics();
      this.maxScroll = Math.max(0, cosmetics.size() * s(62) - s(380));
      
      // Render cosmetic grid
      int visibleCount = (int)Math.ceil((double)s(380) / s(62));
      int startIdx = this.scrollOffset / s(62);
      int endIdx = Math.min(cosmetics.size(), startIdx + visibleCount + 1);
      
      for (int i = startIdx; i < endIdx; i++) {
         Cosmetic c = cosmetics.get(i);
         int x = cl + s((i % 3) * 214);
         int y = t + s(86 + (i / 3) * 62);
         boolean hovered = this.isMouseOverCosmetic(mouseX, mouseY, x, y);
         boolean equipped = PvpClient.cosmetics().isEquipped(c.getId());
         boolean locked = !PvpClient.cosmetics().isUnlocked(c.getId());
         
         this.renderCosmeticCard(g, c, x, y, hovered, equipped, locked);
         
         if (hovered) {
            this.hoveredCosmetic = c;
            this.lastHoverChange = System.currentTimeMillis();
         }
      }
      
      // Preview panel (right side)
      if (this.selectedCosmetic != null) {
<<<<<<< HEAD
         this.renderPreviewPanel(g, l + s(650), t + s(46), partialTick);
      } else if (this.hoveredCosmetic != null && System.currentTimeMillis() - this.lastHoverChange > 150L) {
         this.renderPreviewPanel(g, l + s(650), t + s(46), partialTick);
=======
         this.renderPreviewPanel(g, l + s(650), t + s(46));
      } else if (this.hoveredCosmetic != null && System.currentTimeMillis() - this.lastHoverChange > 150L) {
         this.renderPreviewPanel(g, l + s(650), t + s(46));
>>>>>>> 77bc9da847b12cb9940d49afdb8df99b0af11b92
      }
      
      // Scroll bar
      if (this.maxScroll > 0) {
         int scrollHeight = Math.max(s(40), s(380) * s(380) / (cosmetics.size() * s(62)));
         int scrollY = t + s(86) + (int)((double)this.scrollOffset / this.maxScroll * (s(380) - scrollHeight));
         g.fill(cl + s(634), scrollY, cl + s(640), scrollY + scrollHeight, PvpClient.themeEngine().accent());
      }
      
      // Stats footer
      int total = PvpClient.cosmetics().getTotalCount();
      int owned = PvpClient.cosmetics().getUnlockedCount();
      UiTypography.text(g, String.format("%d/%d COSMETICS OWNED", owned, total), 
         l + s(20), t + s(450), PvpClient.themeEngine().secondary(), 0.64F, 0);
      
      super.extractRenderState(g, mouseX, mouseY, partialTick);
   }
   
   private void renderCategoryButton(GuiGraphicsExtractor g, CosmeticType type, int totalCount, int ownedCount, 
                                     int x, int y, boolean selected, boolean hovered) {
      int width = s(176);
      int height = s(32);
      
      // Background
      int bgColor = selected ? PvpClient.themeEngine().color("hover") : PvpClient.themeEngine().color("card");
      if (hovered && !selected) {
         bgColor = PvpClient.themeEngine().color("hover");
      }
      
      g.fill(x + s(2), y + s(2), x + width + s(2), y + height + s(2), DesignTokens.SHADOW_COLOR_SOFT);
      g.fill(x, y, x + width, y + height, bgColor);
      
      // Selection indicator
      if (selected) {
         g.fill(x, y + s(2), x + s(3), y + height - s(2), PvpClient.themeEngine().accent());
      }
      
      // Icon placeholder (colored square based on type)
      int iconColor = type.getDefaultRarity().getColor();
      g.fill(x + s(8), y + s(6), x + s(24), y + s(22), iconColor | 0xFF000000);
      
      // Label
      String label = type.getDisplayName();
      int textColor = selected ? PvpClient.themeEngine().text() : PvpClient.themeEngine().secondary();
      UiTypography.text(g, label, x + s(32), y + s(10), textColor, 0.72F, 0);
      
      // Count
      String count = ownedCount + "/" + totalCount;
      UiTypography.text(g, count, x + width - s(42), y + s(12), PvpClient.themeEngine().muted(), 0.64F, 0);
   }
   
   private void renderCosmeticCard(GuiGraphicsExtractor g, Cosmetic c, int x, int y, 
                                   boolean hovered, boolean equipped, boolean locked) {
      int width = s(206);
      int height = s(56);
      
      // Background
      int bgColor = locked ? 0x4D586070 : (equipped ? PvpClient.themeEngine().color("hover") : PvpClient.themeEngine().color("card"));
      if (hovered && !locked) {
         bgColor = PvpClient.themeEngine().color("hover");
      }
      
      g.fill(x + s(2), y + s(2), x + width + s(2), y + height + s(2), DesignTokens.SHADOW_COLOR_SOFT);
      g.fill(x, y, x + width, y + height, bgColor);
      
      // Rarity border
      int rarityColor = c.getRarity().getColor();
      g.fill(x, y, x + s(3), y + height, rarityColor | 0xFF000000);
      
      // Icon
      if (!locked) {
         int iconSize = s(32);
         int iconX = x + s(12);
         int iconY = y + (height - iconSize) / 2;
         g.fill(iconX, iconY, iconX + iconSize, iconY + iconSize, rarityColor | 0x66FFFFFF);
      } else {
         // Lock icon
         g.fill(x + s(12), y + s(12), x + s(28), y + s(28), 0x80404040);
      }
      
      // Name
      int nameColor = locked ? 0x808890A0 : (equipped ? PvpClient.themeEngine().accent() : PvpClient.themeEngine().text());
      UiTypography.text(g, c.getName(), x + s(52), y + s(12), nameColor, 0.76F, 0);
      
      // Type
      UiTypography.text(g, c.getType().getDisplayName(), x + s(52), y + s(28), PvpClient.themeEngine().muted(), 0.62F, 0);
      
      // Equipped badge
      if (equipped) {
         UiTypography.text(g, "EQUIPPED", x + width - s(72), y + s(18), PvpClient.themeEngine().accent(), 0.58F, 0);
      }
      
      // Locked overlay
      if (locked) {
         g.fill(x, y, x + width, y + height, 0x80000000);
      }
   }
   
<<<<<<< HEAD
   private void renderPreviewPanel(GuiGraphicsExtractor g, int x, int y, float partialTick) {
      Cosmetic c = this.selectedCosmetic != null ? this.selectedCosmetic : this.hoveredCosmetic;
      if (c == null) return;

      int width = s(220);
      int height = s(380);
      int rarityColor = c.getRarity().getColor();

      PARSFramework.panel(g, x, y, width, height);

      int previewLeft = x + s(10);
      int previewTop = y + s(10);
      int previewRight = x + width - s(10);
      int previewBottom = y + s(190);

      // Actual player model — this is Minecraft's real rendered player state.
      g.fill(previewLeft, previewTop, previewRight, previewBottom,
              PvpClient.themeEngine().color("background"));
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player != null) {
         EntityRenderDispatcher dispatcher = minecraft.getEntityRenderDispatcher();
         var state = dispatcher.extractEntity(minecraft.player, partialTick);
         float angle = (float)(System.nanoTime() / 1_800_000_000.0D);
         g.entity(
                 state,
                 sf(5.2F),
                 new Vector3f(0.0F, 0.82F, 0.0F),
                 new Quaternionf().rotationY(-angle),
                 new Quaternionf(),
                 previewLeft,
                 previewTop,
                 previewRight,
                 previewBottom
         );
      }

      // Animated rarity frame around the real preview.
      float pulse = PvpClient.reducedMotion()
              ? 0.55F
              : 0.45F + 0.35F * (float)Math.sin(System.nanoTime() / 280_000_000.0D);
      int glow = ((int)(Math.max(0.0F, Math.min(1.0F, pulse)) * 120.0F) << 24)
              | (rarityColor & 0x00FFFFFF);
      g.fill(previewLeft, previewTop, previewRight, previewTop + s(2), glow);
      g.fill(previewLeft, previewBottom - s(2), previewRight, previewBottom, glow);

      UiTypography.heading(g, c.getName(), x + s(16), y + s(204),
              PvpClient.themeEngine().text());
      UiTypography.text(g, c.getType().getDisplayName().toUpperCase(Locale.ROOT),
              x + s(16), y + s(226), PvpClient.themeEngine().secondary(), 0.66F, 0);
      UiTypography.text(g, c.getRarity().getDisplayName().toUpperCase(Locale.ROOT),
              x + s(16), y + s(244), rarityColor, 0.58F, 0);

      if (!c.getDescription().isBlank()) {
         List<String> lines = wrap(c.getDescription(), 30, 3);
         int yy = y + s(262);
         for (String line : lines) {
            UiTypography.text(g, line, x + s(16), yy,
                    PvpClient.themeEngine().muted(), 0.55F, 0);
            yy += s(14);
         }
      }

      boolean unlocked = PvpClient.cosmetics().isUnlocked(c.getId());
      boolean equipped = PvpClient.cosmetics().isEquipped(c.getId());
      boolean favorite = PvpClient.cosmetics().isFavorite(c.getId());
      int buttonY = y + height - s(66);
      int equipColor = equipped
              ? PvpClient.themeEngine().secondary()
              : PvpClient.themeEngine().accent();
      if (!unlocked) equipColor = PvpClient.themeEngine().color("card");

      g.fill(x + s(14), buttonY, x + width - s(14), buttonY + s(26), equipColor);
      g.fill(x + s(14), buttonY + s(32), x + width - s(14), buttonY + s(54),
              PvpClient.themeEngine().color("card"));

      UiTypography.text(g,
              equipped ? "UNEQUIP" : (unlocked ? "EQUIP" : "LOCKED"),
              x + s(24), buttonY + s(7),
              unlocked ? PvpClient.themeEngine().text() : PvpClient.themeEngine().muted(),
              0.68F, 0);
      UiTypography.text(g,
              favorite ? "★ FAVORITED" : "☆ FAVORITE",
              x + s(24), buttonY + s(38),
              PvpClient.themeEngine().secondary(), 0.60F, 0);

      UiTypography.caption(g,
              equipped ? "EQUIPPED • LIVE" : "SELECTED • PREVIEW",
              x + s(16), y + height - s(14),
              equipped ? rarityColor : PvpClient.themeEngine().muted());
   }



   private List<String> wrap(String text, int maxChars, int maxLines) {
      List<String> lines = new ArrayList<>();
      if (text == null || text.isBlank() || maxChars <= 0 || maxLines <= 0) {
         return lines;
      }

      String remaining = text.trim();
      while (!remaining.isEmpty() && lines.size() < maxLines) {
         if (remaining.length() <= maxChars) {
            lines.add(remaining);
            break;
         }

         int cut = remaining.lastIndexOf(' ', maxChars);
         if (cut <= 0) {
            cut = maxChars;
         }

         lines.add(remaining.substring(0, cut).trim());
         remaining = remaining.substring(cut).trim();
      }
      return lines;
   }

=======
   private void renderPreviewPanel(GuiGraphicsExtractor g, int x, int y) {
      Cosmetic c = this.selectedCosmetic != null ? this.selectedCosmetic : this.hoveredCosmetic;
      if (c == null) return;
      
      int width = s(220);
      int height = s(380);
      
      PARSFramework.panel(g, x, y, width, height);
      
      // Rarity gradient background
      int rarityColor = c.getRarity().getColor();
      g.fill(x + s(10), y + s(10), x + width - s(10), y + s(100), rarityColor | 0x33FFFFFF);
      
      // Preview icon (large)
      int iconSize = s(80);
      int iconX = x + (width - iconSize) / 2;
      int iconY = y + s(30);
      g.fill(iconX, iconY, iconX + iconSize, iconY + iconSize, rarityColor | 0x88FFFFFF);
      
      // Name
      UiTypography.heading(g, c.getName(), x + s(16), y + s(110), PvpClient.themeEngine().text());
      
      // Type
      UiTypography.text(g, c.getType().getDisplayName(), x + s(16), y + s(130), PvpClient.themeEngine().secondary(), 0.68F, 0);
      
      // Rarity
      int rarityTextColor = c.getRarity().getColor();
      UiTypography.text(g, c.getRarity().getDisplayName(), x + s(16), y + s(148), rarityTextColor | 0xFF000000, 0.64F, 0);
      
      // Description
      if (!c.getDescription().isBlank()) {
         g.fill(x + s(16), y + s(168), x + width - s(16), y + s(170), PvpClient.themeEngine().border());
         UiTypography.body(g, c.getDescription(), x + s(16), y + s(180), PvpClient.themeEngine().muted());
      }
      
      // Action buttons
      boolean unlocked = PvpClient.cosmetics().isUnlocked(c.getId());
      boolean equipped = PvpClient.cosmetics().isEquipped(c.getId());
      boolean favorite = PvpClient.cosmetics().isFavorite(c.getId());
      
      int btnY = y + s(260);
      
      // Equip/Unequip button
      String equipLabel = equipped ? "UNEQUIP" : (unlocked ? "EQUIP" : "LOCKED");
      boolean canEquip = unlocked && !equipped;
      this.addRenderableWidget(Button.builder(Component.literal(equipLabel), btn -> {
         if (unlocked) {
            PvpClient.cosmetics().toggle(c.getId());
         }
         UiSoundEngine.confirm();
         this.rebuildWidgets();
      }).bounds(x + s(16), btnY, s(188), s(32)).build());
      
      // Favorite button
      String favLabel = favorite ? "★ UNFAVORITE" : "☆ FAVORITE";
      this.addRenderableWidget(Button.builder(Component.literal(favLabel), btn -> {
         PvpClient.cosmetics().toggleFavorite(c.getId());
         UiSoundEngine.click();
         this.rebuildWidgets();
      }).bounds(x + s(16), btnY + s(40), s(188), s(28)).build());
      
      // Animated indicator
      if (c.isAnimated()) {
         UiTypography.text(g, "⟡ ANIMATED", x + s(16), y + s(320), PvpClient.themeEngine().accent(), 0.58F, 0);
      }
   }
   
>>>>>>> 77bc9da847b12cb9940d49afdb8df99b0af11b92
   private boolean isMouseOverCategory(int mouseX, int mouseY, int index) {
      int x = this.left() + s(12);
      int y = this.top() + s(50 + index * 38);
      return mouseX >= x && mouseX <= x + s(176) && mouseY >= y && mouseY <= y + s(32);
   }
   
   private boolean isMouseOverCosmetic(int mouseX, int mouseY, int cardX, int cardY) {
      return mouseX >= cardX && mouseX <= cardX + s(206) && mouseY >= cardY && mouseY <= cardY + s(56);
   }
   
   @Override
   public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      if (verticalAmount != 0 && this.maxScroll > 0) {
         this.scrollOffset = (int)Math.clamp(
            this.scrollOffset - verticalAmount * s(30),
            0,
            this.maxScroll
         );
         return true;
      }
      return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
   }
   
   @Override
   public boolean mouseClicked(MouseButtonEvent e, boolean doubleClick) {
      double mouseX = e.x();
      double mouseY = e.y();
      int button = e.button();
      
      // Check cosmetic card clicks
      List<Cosmetic> cosmetics = this.getFilteredCosmetics();
      int visibleCount = (int)Math.ceil((double)s(380) / s(62));
      int startIdx = this.scrollOffset / s(62);
      int endIdx = Math.min(cosmetics.size(), startIdx + visibleCount + 1);
      
      for (int i = startIdx; i < endIdx; i++) {
         Cosmetic c = cosmetics.get(i);
         int x = this.contentLeft() + s((i % 3) * 214);
         int y = this.top() + s(86 + (i / 3) * 62);
         
         if (this.isMouseOverCosmetic((int)mouseX, (int)mouseY, x, y)) {
            if (button == 0) { // Left click - select
               this.selectedCosmetic = c;
               UiSoundEngine.click();
               return true;
            } else if (button == 1) { // Right click - quick equip
               if (PvpClient.cosmetics().isUnlocked(c.getId())) {
                  PvpClient.cosmetics().toggle(c.getId());
                  UiSoundEngine.confirm();
               }
               return true;
            }
         }
      }
      
<<<<<<< HEAD
      // Preview controls are handled here instead of mutating widgets during render.
      Cosmetic preview = this.selectedCosmetic != null ? this.selectedCosmetic : this.hoveredCosmetic;
      if (preview != null) {
         int px = this.left() + s(650);
         int py = this.top() + s(46);
         int btnY = py + s(260);
         if (mouseX >= px + s(16) && mouseX <= px + s(204) && mouseY >= btnY && mouseY <= btnY + s(30)) {
            if (PvpClient.cosmetics().isUnlocked(preview.getId())) {
               PvpClient.cosmetics().toggle(preview.getId());
               UiSoundEngine.confirm();
            }
            return true;
         }
         if (mouseX >= px + s(16) && mouseX <= px + s(204) && mouseY >= btnY + s(36) && mouseY <= btnY + s(62)) {
            PvpClient.cosmetics().toggleFavorite(preview.getId());
            UiSoundEngine.click();
            return true;
         }
      }

=======
>>>>>>> 77bc9da847b12cb9940d49afdb8df99b0af11b92
      return super.mouseClicked(e, doubleClick);
   }
}
