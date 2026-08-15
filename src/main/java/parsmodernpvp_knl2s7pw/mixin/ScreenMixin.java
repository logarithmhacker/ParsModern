package parsmodernpvp_knl2s7pw.mixin;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import parsmodernpvp_knl2s7pw.client.PvpClient;
import parsmodernpvp_knl2s7pw.client.screen.ParsMainMenuScreen;
import parsmodernpvp_knl2s7pw.client.screen.ParsPauseScreen;
import parsmodernpvp_knl2s7pw.client.screen.PvpScreen;
import parsmodernpvp_knl2s7pw.client.ui.UiTypography;

@Mixin(Screen.class)
public abstract class ScreenMixin {

    @Shadow
    public abstract Component getTitle();

    @Inject(
        method = "extractRenderState",
        at = @At("TAIL")
    )
    private void parsmodernpvp$chrome(
        GuiGraphicsExtractor g,
        int mouseX,
        int mouseY,
        float partialTick,
        CallbackInfo info
    ) {
        if ((Object) this instanceof ParsMainMenuScreen
                || (Object) this instanceof ParsPauseScreen
                || (Object) this instanceof PvpScreen) {
            return;
        }

        int width = g.guiWidth();

        g.fill(
            0,
            0,
            width,
            2,
            PvpClient.themeEngine().accent()
        );

        g.fill(
            0,
            2,
            width,
            20,
            -1442311906
        );

        UiTypography.text(
            g,
            "PARS",
            12,
            7,
            PvpClient.themeEngine().accent(),
            0.72F,
            1
        );

        UiTypography.text(
            g,
            "MODERN PVP  //  "
                + this.getTitle().getString().toUpperCase(),
            57,
            7,
            PvpClient.themeEngine().mutedText(),
            0.56F,
            1
        );
    }
}