package kvstore;

import java.util.*;

public class HashMapKVStore extends KVStore {
    private final Map<String, Object> map = Collections.synchronizedMap(new HashMap<>());

    public Object put(String key, Object value) {
        boolean success = emitPutEvent(key, value);
        if (!success)
            return null;

        Object oldValue = map.get(key);
        map.put(key, value);
        return oldValue;
    }

    public Object get(String key) {
        emitGetEvent(key);

        return map.get(key);
    }

    public Object delete(String key) {
        boolean success = emitDeleteEvent(key);
        if (!success)
            return null;

        return map.remove(key);
    }

    @Override
    public int keyCount() {
        return map.size();
    }
}
