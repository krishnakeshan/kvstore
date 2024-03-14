package kvstore;

public class KVStoreConfig {
    private KVStoreImplStrategy implStrategy;

    public void parseConfig(String key, String value) {
        // too few config values right now, turn this into a switch if it grows
        if (key.equals("kvstore.implementation")) {
            implStrategy = KVStoreImplStrategy.fromString(value);
        } else {
            System.out.printf("Unknown kvstore config %s=%s%n", key, value);
        }
    }

    public KVStoreImplStrategy getImplStrategy() {
        return implStrategy;
    }

    public enum KVStoreImplStrategy {
        HASHMAP,
        CONCURRENT_HASHMAP;

        public static KVStoreImplStrategy fromString(String value) {
            for (KVStoreImplStrategy strategy : KVStoreImplStrategy.values()) {
                if (strategy.toString().equalsIgnoreCase(value)) {
                    return strategy;
                }
            }
            throw new IllegalArgumentException("invalid enum value provided: " + value);
        }
    }
}
