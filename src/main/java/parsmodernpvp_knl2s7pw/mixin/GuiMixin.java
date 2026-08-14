package parsmodernpvp_knl2s7pw.mixin;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import parsmodernpvp_knl2s7pw.client.PvpClient;
import parsmodernpvp_knl2s7pw.client.hud.HudRenderer;

@Mixin(Gui.class)
public abstract class GuiMixin {
   @Inject(method = "extractCrosshair", at = @At("HEAD"), cancellable = true)
   private void parsmodernpvp$replaceCrosshair(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo info) {
      if (PvpClient.isCustomCrosshairEnabled()) {
         info.cancel();
      }
   }

   @Inject(method = "extractRenderState", at = @At("TAIL"))
   private void parsmodernpvp$renderHud(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo info) {
      HudRenderer.render(graphics);
   }
}
