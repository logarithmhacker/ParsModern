package parsmodernpvp_knl2s7pw.client.event;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public final class EventBus<T> {
   private final List<Consumer<T>> listeners = new CopyOnWriteArrayList<>();

   public void subscribe(Consumer<T> listener) {
      if (listener != null) {
         this.listeners.add(listener);
      }
   }

   public void post(T event) {
      this.listeners.forEach(listener -> listener.accept(event));
   }
}
