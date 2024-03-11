package command.request.kv;

import command.request.Command;
import command.response.CommandResponseCallback;

public class KeyExistsCommand extends KeyCommand {
    public KeyExistsCommand(String key, CommandResponseCallback responseCallback) {
        super(key, responseCallback);
    }
}
