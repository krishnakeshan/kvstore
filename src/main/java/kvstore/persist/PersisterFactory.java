package kvstore.persist;

public class PersisterFactory {
    public static Persister forStrategy(PersisterConfig.PersisterImplStrategy strategy) {
        return switch (strategy) {
            case PersisterConfig.PersisterImplStrategy.WAL -> new WALPersister();
        };
    }
}
