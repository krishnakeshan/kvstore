package kvstore;

import command.request.Command;

public interface KVService {
    void executeCommand(Command command);
}
