package kvstore;

public interface KVStore {
    /*
    If the provided key exists in the store it is replaced and if it doesn't it is added.
    Returns: old value associated with key, null if it doesn't exist
    */
    Object put(String key, Object value);
    Object get(String key);
    Object delete(String key);
}
