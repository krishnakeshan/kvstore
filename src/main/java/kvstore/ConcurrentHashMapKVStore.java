package kvstore;

import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapKVStore extends KVStore {
    private final ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>();

    @Override
    public Object put(String key, Object value) {
        boolean success = emitPutEvent(key, value);
        if (!success)
            return null;

        Object oldValue = map.get(key);
        map.put(key, value);
        return oldValue;
    }

    @Override
    public Object get(String key) {
        emitGetEvent(key);

        return map.get(key);
    }

    @Override
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
