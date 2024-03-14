package kvstore.service;

import command.request.Command;
import command.request.kv.DeleteKeyCommand;
import command.request.kv.GetKeyCommand;
import command.request.kv.PutKeyCommand;
import command.response.CommandResponseCallback;
import command.response.kv.DeleteKeyCommandResponse;
import command.response.kv.GetKeyCommandResponse;
import command.response.kv.PutKeyCommandResponse;
import kvstore.ConcurrentHashMapKVStore;
import kvstore.HashMapKVStore;
import kvstore.KVStore;

public class KVServiceImpl implements KVService {
    private KVStore kvStore;

    public KVServiceImpl(KVStore kvStore) {
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
                System.out.println("unknown command");
            }
        }
    }

    @Override
    public void setKvStore(KVStore kvStore) {
        this.kvStore = kvStore;
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
