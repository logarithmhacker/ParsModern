package parsmodernpvp_knl2s7pw.client.ui;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class GlyphCache {

    private final Map<Integer, Glyph> glyphs =
            new ConcurrentHashMap<>();

    public GlyphCache() {
    }

    public Glyph get(int codePoint) {
        return glyphs.get(codePoint);
    }

    public void put(Glyph glyph) {
        if (glyph == null) {
            return;
        }

        glyphs.put(
                glyph.codePoint(),
                glyph
        );
    }

    public boolean contains(int codePoint) {
        return glyphs.containsKey(codePoint);
    }

    public void remove(int codePoint) {
        glyphs.remove(codePoint);
    }

    public void clear() {
        glyphs.clear();
    }

    public int size() {
        return glyphs.size();
    }
}