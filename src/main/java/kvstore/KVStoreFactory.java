package kvstore;

public class KVStoreFactory {
    public static KVStore forStrategy(KVStoreConfig.KVStoreImplStrategy implStrategy) {
        return switch (implStrategy) {
            case KVStoreConfig.KVStoreImplStrategy.HASHMAP -> new HashMapKVStore();
            case KVStoreConfig.KVStoreImplStrategy.CONCURRENT_HASHMAP -> new ConcurrentHashMapKVStore();
            default -> throw new IllegalArgumentException("invalid kv store implementation provided");
        };
    }
}
