package kvstore;

import command.request.Command;
import command.request.kv.DeleteKeyCommand;
import command.request.kv.GetKeyCommand;
import command.request.kv.PutKeyCommand;
import command.response.CommandResponse;
import command.response.CommandResponseCallback;
import command.response.kv.DeleteKeyCommandResponse;
import command.response.kv.GetKeyCommandResponse;
import command.response.kv.PutKeyCommandResponse;

public class KVServiceImpl implements KVService {
    public static KVServiceImpl hashMapKVService() {
        return new KVServiceImpl(new HashMapKVStore());
    }

    private final KVStore kvStore;

    private KVServiceImpl(KVStore kvStore) {
        this.kvStore = kvStore;
    }

    @Override
    public void executeCommand(Command command) {
        CommandResponseCallback responseCallback = command.getResponseCallback();
        switch (command) {
            case PutKeyCommand putKeyCommand -> {
                putKey(putKeyCommand.getKey(), putKeyCommand.getValue());
                responseCallback.onResponse(new PutKeyCommandResponse(true));
            }
            case GetKeyCommand getKeyCommand -> {
                Object value = getKey(getKeyCommand.getKey());
                responseCallback.onResponse(new GetKeyCommandResponse(true, value));
            }
            case DeleteKeyCommand deleteKeyCommand -> {
                deleteKey(deleteKeyCommand.getKey());
                responseCallback.onResponse(new DeleteKeyCommandResponse(true));
            }
            default -> {
            }
        }
    }

    private void putKey(String key, Object value) {
        kvStore.put(key, value);
    }

    private Object getKey(String key) {
        return kvStore.get(key);
    }

    private void deleteKey(String key) {
        kvStore.delete(key);
    }
}
