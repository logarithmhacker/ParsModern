package parsmodernpvp_knl2s7pw.client.ui;

public final class Glyph {

    private final int codePoint;
    private final float advance;
    private final float width;
    private final float height;
    private final float xOffset;
    private final float yOffset;

    public Glyph(
            int codePoint,
            float advance,
            float width,
            float height,
            float xOffset,
            float yOffset
    ) {
        this.codePoint = codePoint;
        this.advance = advance;
        this.width = width;
        this.height = height;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public int codePoint() {
        return codePoint;
    }

    public float advance() {
        return advance;
    }

    public float width() {
        return width;
    }

    public float height() {
        return height;
    }

    public float xOffset() {
        return xOffset;
    }

    public float yOffset() {
        return yOffset;
    }
}