package kvstore;

import java.io.IOException;

public interface KVStoreObserver {
    void onPut(String key, Object value) throws IOException;

    void onGet(String key);

    void onDelete(String key) throws IOException;
}
