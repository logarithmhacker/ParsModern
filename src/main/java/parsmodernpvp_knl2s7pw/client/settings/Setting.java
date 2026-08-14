package parsmodernpvp_knl2s7pw.client.settings;

public final class Setting<T> {
   private final String id;
   private final T defaultValue;
   private T value;

   public Setting(String id, T defaultValue) {
      this.id = id;
      this.defaultValue = defaultValue;
      this.value = defaultValue;
   }

   public String id() {
      return this.id;
   }

   public T defaultValue() {
      return this.defaultValue;
   }

   public T value() {
      return this.value;
   }

   public void setValue(T value) {
      if (value == null || this.defaultValue == null || this.defaultValue.getClass().isInstance(value)) {
         this.value = value;
      }
   }

   public void reset() {
      this.value = this.defaultValue;
   }
}
