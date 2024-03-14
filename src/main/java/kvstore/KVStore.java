package kvstore;

public abstract class KVStore implements KVStoreEventEmitter {
    public abstract Object put(String key, Object value);

    public abstract Object get(String key);

    public abstract Object delete(String key);

    public abstract int keyCount();
}
