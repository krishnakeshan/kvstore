package kvstore.persist;

import kvstore.KVStore;
import kvstore.KVStoreEventEmitter;
import kvstore.KVStoreObserver;
import util.FileUtils;

import java.io.IOException;
import java.nio.file.Path;

public abstract class Persister implements KVStoreObserver {
    protected Path dataDirectory;

    protected KVStoreEventEmitter kvStoreEventEmitter;

    public abstract void start() throws IOException;

    public abstract void stop() throws IOException;

    public abstract KVStore recover() throws IOException;

    public void setDataDirectory(Path dataDirectory) throws IOException {
        this.dataDirectory = dataDirectory;
        FileUtils.ensureDirectoryExists(dataDirectory);
    }

    public void setKvStoreEventEmitter(KVStoreEventEmitter kvStoreEventEmitter) {
        this.kvStoreEventEmitter = kvStoreEventEmitter;
        kvStoreEventEmitter.registerObserver(this);
    }

    // default implementations of KVStoreObserver so subclasses can override just the methods they're interested in
    public void onPut(String key, Object value) throws IOException {}

    public void onGet(String key) {}

    public void onDelete(String key) throws IOException {}
}
