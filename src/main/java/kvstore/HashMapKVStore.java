package kvstore;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HashMapKVStore implements KVStore {
    private Map<String, Object> map = Collections.synchronizedMap(new HashMap<>());

    public Object put(String key, Object value) {
        Object oldValue = map.get(key);
        map.put(key, value);
        return oldValue;
    }

    public Object get(String key) {
        return map.get(key);
    }

    public Object delete(String key) {
        return map.remove(key);
    }
}
