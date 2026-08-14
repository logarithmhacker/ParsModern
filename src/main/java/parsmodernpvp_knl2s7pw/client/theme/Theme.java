package parsmodernpvp_knl2s7pw.client.theme;

public record Theme(String name, int background, int panel, int accent, int secondary, int text, int border) {
   public static final Theme PARS_NEON = new Theme("PARS", -385216480, -267051467, -14108161, -6525953, -854017, 1713945087);
   public static final Theme BLUE = new Theme("BLUE", -385346010, -267114172, -11949825, -8136449, -1378817, 1716103423);
   public static final Theme PURPLE = new Theme("PURPLE", -384430811, -265677244, -5084161, -1995777, -594433, 1722969087);
   public static final Theme CYAN = new Theme("CYAN", -385411040, -267242688, -9967367, -12392796, -1573377, 1718085881);
   public static final Theme MIDNIGHT = new Theme("MIDNIGHT", -117044716, -15722457, -9459457, -6510081, -722945, 1718593791);
   public static final Theme PARS_BLUE = BLUE;
   public static final Theme PARS_PURPLE = PURPLE;
   public static final Theme COMPETITIVE = new Theme("COMPETITIVE", -384822246, -266723286, -44188, -14249, -722950, 1728009060);
   public static final Theme CRYSTAL = new Theme("CRYSTAL", -385411040, -267242688, -9967367, -4786177, -1573377, 1718085881);
   public static final Theme MACE = new Theme("MACE", -384101880, -264952814, -19641, -11930, -2842, 1728033607);
   public static final Theme MINIMAL = new Theme("MINIMAL", -653258736, -651877083, -1, -4342339, -986896, 1728053247);
   public static final Theme HIGH_CONTRAST = new Theme("DARK", -117440512, -15658735, -256, -24576, -1, 1728053247);

   public Theme(String name, int background, int panel, int accent, int text) {
      this(name, background, panel, accent, accent, text, accent);
   }

   public static Theme byName(String name) {
      return switch (name == null ? "" : name) {
         case "PARS Blue", "BLUE" -> BLUE;
         case "PARS Purple", "PURPLE" -> PURPLE;
         case "CYAN" -> CYAN;
         case "MIDNIGHT" -> MIDNIGHT;
         case "Competitive", "COMPETITIVE" -> COMPETITIVE;
         case "Crystal", "CRYSTAL" -> CRYSTAL;
         case "Mace", "MACE" -> MACE;
         case "Minimal", "MINIMAL" -> MINIMAL;
         case "High Contrast", "DARK" -> HIGH_CONTRAST;
         default -> PARS_NEON;
      };
   }
}
