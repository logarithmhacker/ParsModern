package parsmodernpvp_knl2s7pw;

import net.fabricmc.api.ClientModInitializer;
import parsmodernpvp_knl2s7pw.client.PvpClient;

public final class ParsmodernpvpClient implements ClientModInitializer {
   public void onInitializeClient() {
      PvpClient.initialize();
   }
}
