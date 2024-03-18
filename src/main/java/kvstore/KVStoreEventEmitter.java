package kvstore;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public interface KVStoreEventEmitter {
    Set<KVStoreObserver> observers = new HashSet<>();

    default boolean emitPutEvent(String key, Object value) {
        Set<Boolean> allOk = new HashSet<>();
        observers.forEach(observer -> {
            try {
                observer.onPut(key, value);
            } catch (IOException e) {
                allOk.add(false);
            }
        });

        return allOk.isEmpty();
    }

    default void emitGetEvent(String key) {
        observers.forEach(observer -> observer.onGet(key));
    }

    default boolean emitDeleteEvent(String key) {
        Set<Boolean> allOk = new HashSet<>();
        observers.forEach(observer -> {
            try {
                observer.onDelete(key);
            } catch (IOException e) {
                allOk.add(false);
            }
        });
        return allOk.isEmpty();
    }

    default void registerObserver(KVStoreObserver observer) {
        observers.add(observer);
    }
}
