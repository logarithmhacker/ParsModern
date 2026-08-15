package parsmodernpvp_knl2s7pw.client.ui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import parsmodernpvp_knl2s7pw.client.PvpClient;
import parsmodernpvp_knl2s7pw.client.animation.AnimationEngine;

/**
 * Core PARS rendering primitives.
 *
 * The framework deliberately avoids noisy borders, oversized shadows and
 * blocky corner hacks.  Surfaces use a small depth stack, one accent color,
 * and restrained highlights so every screen reads as one coherent product.
 */
public final class PARSFramework {
    private PARSFramework() {
    }

    public static void background(GuiGraphicsExtractor g, int width, int height) {
        UiBackground.render(g, width, height, PvpClient.backgroundOpacity());
    }

    public static void panel(GuiGraphicsExtractor g, int x, int y, int width, int height) {
        panel(g, x, y, width, height, DesignTokens.RADIUS_LG);
    }

    public static void panel(GuiGraphicsExtractor g, int x, int y, int width, int height, float radius) {
        if (width <= 0 || height <= 0) {
            return;
        }

        int r = safeRadius(radius, width, height);
        int background = PvpClient.themeEngine().color("background");
        int surface = PvpClient.themeEngine().color("panel");
        int border = PvpClient.themeEngine().color("border");
        int accent = PvpClient.themeEngine().accent();

        if (PvpClient.shadow() && PvpClient.panelDepth()) {
            fillRounded(g, x + 3, y + 4, width, height, r, 0x50000000);
            fillRounded(g, x + 1, y + 2, width, height, r, 0x28000000);
        }

        fillRounded(g, x, y, width, height, r, surface);
        fillRoundedBorder(g, x, y, width, height, r, border, 1, surface);

        // Quiet top highlight instead of the old heavy left stripe.
        g.fill(x + r, y + 1, x + width - r, y + 2, withAlpha(accent, 0.60F));
        g.fill(x + r + 4, y + 3, x + width - r - 4, y + 4, withAlpha(0xFFFFFFFF, 0.035F));

        // Tiny corner pixels make the edge feel intentional without turning
        // the whole panel into a neon rectangle.
        int corner = withAlpha(background, 0.65F);
        int cut = Math.max(1, Math.min(2, r / 4));
        g.fill(x, y, x + cut, y + cut, corner);
        g.fill(x + width - cut, y, x + width, y + cut, corner);
        g.fill(x, y + height - cut, x + cut, y + height, corner);
        g.fill(x + width - cut, y + height - cut, x + width, y + height, corner);
    }

    public static void card(GuiGraphicsExtractor g, int x, int y, int width, int height) {
        card(g, x, y, width, height, false, false);
    }

    public static void card(GuiGraphicsExtractor g, int x, int y, int width, int height, boolean selected) {
        card(g, x, y, width, height, false, selected);
    }

    public static void card(GuiGraphicsExtractor g, int x, int y, int width, int height,
                            boolean hovered, boolean selected) {
        if (width <= 0 || height <= 0) {
            return;
        }

        int r = safeRadius(DesignTokens.RADIUS_MD, width, height);
        int surface = selected
                ? PvpClient.themeEngine().color("hover")
                : PvpClient.themeEngine().color("card");
        int border = selected
                ? withAlpha(PvpClient.themeEngine().accent(), 0.72F)
                : PvpClient.themeEngine().color("border");

        if (PvpClient.shadow()) {
            fillRounded(g, x + 2, y + 3, width, height, r, 0x30000000);
        }

        fillRounded(g, x, y, width, height, r, surface);
        fillRoundedBorder(g, x, y, width, height, r, border, 1, surface);

        if (selected) {
            g.fill(x + 1, y + r, x + 3, y + height - r, PvpClient.themeEngine().accent());
            g.fill(x + r, y + 1, x + width - r, y + 2, withAlpha(PvpClient.themeEngine().accent(), 0.35F));
        } else if (hovered && PvpClient.glow()) {
            g.fill(x + r, y + 1, x + width - r, y + 3,
                    withAlpha(PvpClient.themeEngine().accent(), 0.10F));
        }
    }

    public static void button(GuiGraphicsExtractor g, String label, int x, int y,
                              int width, int height, boolean hovered, boolean active,
                              boolean disabled) {
        if (width <= 0 || height <= 0) {
            return;
        }

        int r = safeRadius(DesignTokens.RADIUS_MD, width, height);
        int accent = PvpClient.themeEngine().accent();
        int textColor = PvpClient.themeEngine().text();
        int surface;
        int border;

        if (disabled) {
            surface = withAlpha(PvpClient.themeEngine().color("card"), 0.45F);
            border = withAlpha(PvpClient.themeEngine().color("border"), 0.45F);
            textColor = withAlpha(textColor, 0.40F);
        } else if (active) {
            surface = withAlpha(accent, 0.88F);
            border = withAlpha(accent, 0.95F);
            textColor = 0xFFFFFFFF;
        } else if (hovered) {
            surface = PvpClient.themeEngine().color("hover");
            border = withAlpha(accent, 0.70F);
        } else {
            surface = PvpClient.themeEngine().color("card");
            border = PvpClient.themeEngine().color("border");
        }

        if (PvpClient.shadow() && !disabled) {
            fillRounded(g, x + 2, y + 3, width, height, r, 0x30000000);
        }

        fillRounded(g, x, y, width, height, r, surface);
        fillRoundedBorder(g, x, y, width, height, r, border, 1, surface);

        if (!disabled) {
            // A restrained top sheen gives the button a premium material feel.
            g.fill(x + r, y + 1, x + width - r, y + 2,
                    withAlpha(active ? 0xFFFFFFFF : accent, active ? 0.18F : 0.12F));

            if (hovered && !active && !PvpClient.reducedMotion()) {
                float pulse = AnimationEngine.ease(
                        (float) ((Math.sin(System.nanoTime() / 2.2E8) + 1.0) * 0.5),
                        AnimationEngine.Curve.EASE_IN_OUT
                );
                g.fill(x + r, y + height - 2, x + width - r, y + height - 1,
                        withAlpha(accent, 0.10F + pulse * 0.10F));
            }
        }

        PARSFontEngine.centered(
                g,
                label,
                x + width / 2,
                y + height / 2,
                textColor,
                active ? PARSFontEngine.Token.BODY : PARSFontEngine.Token.SMALL,
                !disabled && PvpClient.shadow(),
                false
        );
    }

    public static void button(GuiGraphicsExtractor g, String label, int x, int y,
                              int width, int height, boolean hovered, boolean active) {
        button(g, label, x, y, width, height, hovered, active, false);
    }

    public static void progress(GuiGraphicsExtractor g, int x, int y, int width, int height, float value) {
        progress(g, x, y, width, height, value, false);
    }

    public static void progress(GuiGraphicsExtractor g, int x, int y, int width, int height,
                                float value, boolean glow) {
        if (width <= 0 || height <= 0) {
            return;
        }

        float clamped = Math.max(0.0F, Math.min(1.0F, value));
        int r = Math.min(height / 2, 6);
        int fillWidth = Math.round(width * clamped);
        int track = withAlpha(PvpClient.themeEngine().color("border"), 0.55F);
        int accent = PvpClient.themeEngine().accent();

        fillRounded(g, x, y, width, height, r, track);

        if (fillWidth > 0) {
            if (glow && PvpClient.glow()) {
                fillRounded(g, x - 1, y - 1, fillWidth + 2, height + 2, r + 1,
                        withAlpha(accent, 0.16F));
            }
            fillRounded(g, x, y, fillWidth, height, Math.min(r, fillWidth / 2), accent);
            if (fillWidth > 4) {
                g.fill(x + 2, y + 1, Math.max(x + 2, x + fillWidth - 2), y + 2,
                        withAlpha(0xFFFFFFFF, 0.20F));
            }
        }
    }

    private static int safeRadius(float radius, int width, int height) {
        return Math.max(0, Math.min((int) radius, Math.min(width, height) / 2));
    }

    private static void fillRounded(GuiGraphicsExtractor g, int x, int y, int width,
                                    int height, int radius, int color) {
        if (width <= 0 || height <= 0) {
            return;
        }
        if (radius <= 0) {
            g.fill(x, y, x + width, y + height, color);
            return;
        }

        for (int row = 0; row < height; row++) {
            int inset = 0;
            if (row < radius) {
                inset = radius - (int) Math.floor(Math.sqrt(radius * radius -
                        (radius - row - 0.5) * (radius - row - 0.5)));
            } else if (row >= height - radius) {
                int fromBottom = height - row - 1;
                inset = radius - (int) Math.floor(Math.sqrt(radius * radius -
                        (radius - fromBottom - 0.5) * (radius - fromBottom - 0.5)));
            }
            inset = Math.max(0, Math.min(radius, inset));
            g.fill(x + inset, y + row, x + width - inset, y + row + 1, color);
        }
    }

    private static void fillRoundedBorder(GuiGraphicsExtractor g, int x, int y, int width,
                                           int height, int radius, int color, int thickness,
                                           int innerColor) {
        if (thickness <= 0) {
            return;
        }
        fillRounded(g, x, y, width, height, radius, color);
        int inner = Math.max(0, radius - thickness);
        fillRounded(g, x + thickness, y + thickness,
                Math.max(0, width - thickness * 2),
                Math.max(0, height - thickness * 2), inner,
                innerColor);
    }

    public static int withAlpha(int color, float alpha) {
        int rgb = color & 0xFFFFFF;
        int a = Math.max(0, Math.min(255, Math.round(alpha * 255.0F)));
        return (a << 24) | rgb;
    }
}
