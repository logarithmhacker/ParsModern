package parsmodernpvp_knl2s7pw.client.ui;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

import parsmodernpvp_knl2s7pw.Parsmodernpvp;
import parsmodernpvp_knl2s7pw.client.PvpClient;

public final class FontEngine {

    private static final String NAMESPACE =
            "parsmodernpvp-knl2s7pw";

    /*
     * Minecraft font identifier.
     *
     * This points to:
     *
     * assets/
     *   parsmodernpvp-knl2s7pw/
     *     font/
     *       vazirmatn.json
     *
     * The TTF itself is referenced from vazirmatn.json.
     */
    private static final Identifier FONT_ID =
            Identifier.fromNamespaceAndPath(
                    NAMESPACE,
                    "vazirmatn"
            );

    /*
     * IMPORTANT:
     *
     * Minecraft 26.x expects FontDescription here.
     * Do NOT pass FONT_ID directly to Style.withFont().
     */
    private static final FontDescription FONT_DESCRIPTION =
            new FontDescription.Resource(FONT_ID);

    /* ASCII numerals use the vanilla font definition. This avoids replacement
       boxes on HUD/stat numbers while leaving labels and words on Vazirmatn. */
    private static final FontDescription DEFAULT_FONT_DESCRIPTION =
            new FontDescription.Resource(
                    Identifier.fromNamespaceAndPath("minecraft", "default")
            );

    /*
     * Actual TTF resource.
     *
     * This is:
     *
     * assets/parsmodernpvp-knl2s7pw/font/vazirmatn.ttf
     */
    private static final Identifier TTF_ID =
            Identifier.fromNamespaceAndPath(
                    NAMESPACE,
                    "font/vazirmatn.ttf"
            );

    private static final Map<String, Integer> WIDTH_CACHE =
            new ConcurrentHashMap<>();

    private static final Map<String, Component> TEXT_CACHE =
            new ConcurrentHashMap<>();

    private static volatile boolean initialized = false;

    private static volatile boolean ttfAvailable = false;

    private FontEngine() {
    }

    /**
     * Initializes the font engine.
     */
    public static void initialize() {

        if (initialized) {
            return;
        }

        initialized = true;

        Parsmodernpvp.LOGGER.info(
                "PARS FontEngine initialized: Vazirmatn"
        );
    }

    /**
     * Verifies that the actual TTF resource exists.
     *
     * Called after Minecraft resource manager becomes available.
     */
    public static void verifyResources(
            ResourceManager resources
    ) {

        if (resources == null) {
            return;
        }

        try {

            Map<Identifier, Resource> found =
                    resources.listResources(
                            "font",
                            id ->
                                    id.equals(TTF_ID)
                    );

            boolean available =
                    found.containsKey(TTF_ID);

            if (available != ttfAvailable) {

                ttfAvailable = available;

                clearCache();
            }

            if (ttfAvailable) {

                Parsmodernpvp.LOGGER.info(
                        "PARS Vazirmatn font loaded successfully."
                );

            } else {

                Parsmodernpvp.LOGGER.warn(
                        "PARS Vazirmatn TTF not found: {}",
                        TTF_ID
                );
            }

        } catch (Exception exception) {

            ttfAvailable = false;

            Parsmodernpvp.LOGGER.error(
                    "Failed to verify PARS Vazirmatn font.",
                    exception
            );

            clearCache();
        }
    }

    /**
     * Creates a Minecraft Component using Vazirmatn.
     */
    public static Component component(
            String text
    ) {

        return component(
                text,
                Weight.REGULAR
        );
    }

    /**
     * Creates a Minecraft Component using Vazirmatn.
     *
     * Weight is kept for compatibility with the rest of the
     * PARS UI system.
     *
     * Since the project currently has one TTF,
     * every weight uses the same Vazirmatn font definition.
     */
    public static Component component(
            String text,
            Weight weight
    ) {

        if (text == null || text.isEmpty()) {
            return Component.empty();
        }

        Weight selected =
                weight == null
                        ? Weight.REGULAR
                        : weight;

        String key =
                selected.name()
                        + "|"
                        + text;

        return TEXT_CACHE.computeIfAbsent(
                key,
                ignored -> buildMixedFontComponent(text)
        );
    }

    private static Component buildMixedFontComponent(String text) {
        MutableComponent root = Component.empty();

        for (int i = 0; i < text.length();) {
            int codePoint = text.codePointAt(i);
            int count = Character.charCount(codePoint);
            String glyph = new String(Character.toChars(codePoint));
            boolean digit = codePoint >= '0' && codePoint <= '9';
            FontDescription font = digit ? DEFAULT_FONT_DESCRIPTION : FONT_DESCRIPTION;

            root.append(
                    Component.literal(glyph)
                            .withStyle(style -> style.withFont(font))
            );

            i += count;
        }

        return root;
    }

    /**
     * Basic width method.
     */
    public static int width(
            String text
    ) {

        return width(
                text,
                1.0F,
                0,
                Weight.REGULAR
        );
    }

    /**
     * Width with scale and tracking.
     */
    public static int width(
            String text,
            float scale,
            int tracking
    ) {

        return width(
                text,
                scale,
                tracking,
                Weight.REGULAR
        );
    }

    /**
     * Full width calculation.
     */
    public static int width(
            String text,
            float scale,
            int tracking,
            Weight weight
    ) {

        if (text == null || text.isEmpty()) {
            return 0;
        }

        if (scale <= 0.0F) {
            return 0;
        }

        Weight selected =
                weight == null
                        ? Weight.REGULAR
                        : weight;

        String key =
                selected.name()
                        + "|"
                        + text;

        int baseWidth =
                WIDTH_CACHE.computeIfAbsent(
                        key,
                        ignored -> {

                            Minecraft minecraft =
                                    Minecraft.getInstance();

                            if (minecraft == null
                                    || minecraft.font == null) {
                                return 0;
                            }

                            return Math.round(
                                    minecraft.font.width(
                                            component(
                                                    text,
                                                    selected
                                            )
                                    )
                            );
                        }
                );

        /*
         * Tracking is intentionally NOT added here.
         *
         * Minecraft's native font renderer has no per-character
         * tracking, so draw() never actually applies it. Adding it
         * to width() while rendering ignores it caused centered
         * text to be misaligned. Width and rendering now agree.
         */
        return Math.max(
                0,
                Math.round(
                        baseWidth * scale
                )
        );
    }

    /**
     * Simple draw method.
     */
    public static void draw(
            GuiGraphicsExtractor graphics,
            String text,
            int x,
            int y,
            int color,
            boolean shadow
    ) {

        draw(
                graphics,
                text,
                x,
                y,
                color,
                1.0F,
                0,
                Weight.REGULAR,
                shadow,
                false
        );
    }

    /**
     * Draw with scale and tracking.
     */
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

        draw(
                graphics,
                text,
                x,
                y,
                color,
                scale,
                tracking,
                Weight.REGULAR,
                shadow,
                false
        );
    }

    /**
     * Full draw method.
     */
    public static void draw(
            GuiGraphicsExtractor graphics,
            String text,
            int x,
            int y,
            int color,
            float scale,
            int tracking,
            Weight weight,
            boolean shadow,
            boolean outline
    ) {

        if (graphics == null) {
            return;
        }

        if (text == null || text.isEmpty()) {
            return;
        }

        if (scale <= 0.0F) {
            return;
        }

        Minecraft minecraft =
                Minecraft.getInstance();

        if (minecraft == null
                || minecraft.font == null) {
            return;
        }

        Weight selected =
                weight == null
                        ? Weight.REGULAR
                        : weight;

        Component component =
                component(
                        text,
                        selected
                );

        graphics.pose().pushMatrix();

        try {

            graphics.pose().translate(
                    x,
                    y
            );

            graphics.pose().scale(
                    scale,
                    scale
            );

            /*
             * Tracking is handled separately.
             *
             * For normal Minecraft text rendering, tracking = 0
             * keeps the native glyph layout.
             *
             * Positive tracking is intentionally not injected
             * into Component styling because Minecraft's font
             * renderer does not expose per-character tracking.
             */
            if (outline) {

                /*
                 * Subtle outline: four low-alpha dark passes so the
                 * text gains a soft edge instead of a thick stroke.
                 */
                int outlineColor =
                        0x40000000 | 0x000A0A0A;

                graphics.text(
                        minecraft.font,
                        component,
                        -1,
                        0,
                        outlineColor,
                        false
                );

                graphics.text(
                        minecraft.font,
                        component,
                        1,
                        0,
                        outlineColor,
                        false
                );

                graphics.text(
                        minecraft.font,
                        component,
                        0,
                        -1,
                        outlineColor,
                        false
                );

                graphics.text(
                        minecraft.font,
                        component,
                        0,
                        1,
                        outlineColor,
                        false
                );
            }

            graphics.text(
                    minecraft.font,
                    component,
                    0,
                    0,
                    color,
                    shadow
            );

        } finally {

            graphics.pose().popMatrix();
        }
    }

    /**
     * Clears all cached Components and widths.
     */
    public static void clearCache() {

        TEXT_CACHE.clear();

        WIDTH_CACHE.clear();
    }

    /**
     * Returns number of cached text Components.
     */
    public static int cachedGlyphCount() {

        return TEXT_CACHE.size();
    }

    /**
     * Returns whether the TTF was found.
     */
    public static boolean isVazirmatnAvailable() {

        return ttfAvailable;
    }

    /**
     * Returns the Minecraft font description used by PARS.
     */
    public static FontDescription fontDescription() {

        return FONT_DESCRIPTION;
    }

    /**
     * Returns the Minecraft font identifier.
     */
    public static Identifier fontId() {

        return FONT_ID;
    }

    /**
     * Available logical weights.
     *
     * All currently resolve to the same Vazirmatn resource because
     * the project has a single vazirmatn.ttf.
     */
    public enum Weight {

        REGULAR,

        MEDIUM,

        SEMIBOLD,

        BOLD,

        PREMIUM
    }
}