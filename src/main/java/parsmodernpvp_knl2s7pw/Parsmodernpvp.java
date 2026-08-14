package parsmodernpvp_knl2s7pw;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Parsmodernpvp implements ModInitializer {
   public static final String MOD_ID = "parsmodernpvp-knl2s7pw";
   public static final Logger LOGGER = LoggerFactory.getLogger("parsmodernpvp-knl2s7pw");

   public void onInitialize() {
      LOGGER.info("Initializing {} - legitimate client-side PvP toolkit", "parsmodernpvp-knl2s7pw");
   }
}
