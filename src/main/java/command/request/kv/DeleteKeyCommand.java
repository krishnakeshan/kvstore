package command.request.kv;

import command.request.Command;
import command.response.CommandResponseCallback;

public class DeleteKeyCommand extends KeyCommand {
    public DeleteKeyCommand(String key, CommandResponseCallback responseCallback) {
        super(key, responseCallback);
    }
}
