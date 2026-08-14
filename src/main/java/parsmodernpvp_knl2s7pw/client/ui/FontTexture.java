package parsmodernpvp_knl2s7pw.client.ui;

import net.minecraft.resources.Identifier;

public final class FontTexture {

    private final Identifier id;
    private final int width;
    private final int height;

    public FontTexture(
            Identifier id,
            int width,
            int height
    ) {
        this.id = id;
        this.width = width;
        this.height = height;
    }

    public Identifier id() {
        return id;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }
}