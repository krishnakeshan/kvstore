package kvstore.service;

import command.request.Command;
import kvstore.KVStore;

public interface KVService {
    void setKvStore(KVStore kvStore);

    void executeCommand(Command command);
}
