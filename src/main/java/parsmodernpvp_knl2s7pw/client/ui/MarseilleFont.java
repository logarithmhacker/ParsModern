package parsmodernpvp_knl2s7pw.client.ui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;

public final class MarseilleFont {

    private MarseilleFont() {
    }

    public static Component component(String text) {
        return FontEngine.component(text);
    }

    public static int width(String text) {
        return FontEngine.width(text);
    }

    public static int width(
            String text,
            float scale,
            int tracking
    ) {
        return FontEngine.width(
                text,
                scale,
                tracking
        );
    }

    public static void draw(
            GuiGraphicsExtractor graphics,
            String text,
            int x,
            int y,
            int color
    ) {
        FontEngine.draw(
                graphics,
                text,
                x,
                y,
                color,
                false
        );
    }

    public static void draw(
            GuiGraphicsExtractor graphics,
            String text,
            int x,
            int y,
            int color,
            boolean shadow
    ) {
        FontEngine.draw(
                graphics,
                text,
                x,
                y,
                color,
                shadow
        );
    }

    public static void draw(
            GuiGraphicsExtractor graphics,
            String text,
            int x,
            int y,
            int color,
            float scale,
            int tracking,
            boolean shadow
    ) {
        FontEngine.draw(
                graphics,
                text,
                x,
                y,
                color,
                scale,
                tracking,
                shadow
        );
    }

    public static void clearCache() {
        FontEngine.clearCache();
    }

    public static String name() {
        return "Marseille";
    }
}