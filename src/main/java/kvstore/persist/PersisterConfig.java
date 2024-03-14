package kvstore.persist;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PersisterConfig {
    private PersisterImplStrategy implStrategy;

    private Path dataDirectory;

    private boolean recoverState = false;

    public void parseConfig(String key, String value) {
        switch (key) {
            case "persist.implementation" -> implStrategy = PersisterImplStrategy.fromString(value);
            case "persist.data_directory" -> dataDirectory = Paths.get(value);
            case "persist.recover_state" -> recoverState = Boolean.parseBoolean(value);
        }
    }

    public PersisterImplStrategy getImplStrategy() {
        return implStrategy;
    }

    public void setImplStrategy(PersisterImplStrategy implStrategy) {
        this.implStrategy = implStrategy;
    }

    public Path getDataDirectory() {
        return dataDirectory;
    }

    public void setDataDirectory(Path dataDirectory) {
        this.dataDirectory = dataDirectory;
    }

    public boolean isRecoverState() {
        return recoverState;
    }

    public void setRecoverState(boolean recoverState) {
        this.recoverState = recoverState;
    }

    public enum PersisterImplStrategy {
        WAL;

        public static PersisterImplStrategy fromString(String value) {
            for (PersisterImplStrategy strategy : PersisterImplStrategy.values()) {
                if (strategy.toString().equalsIgnoreCase(value))
                    return strategy;
            }
            throw new IllegalArgumentException("invalid persister impl strategy provided: " + value);
        }
    }
}
