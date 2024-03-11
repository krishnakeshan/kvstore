package command.request.kv;

import command.request.Command;
import command.response.CommandResponseCallback;

public class GetKeyCommand extends KeyCommand {
    public GetKeyCommand(String key, CommandResponseCallback responseCallback) {
        super(key, responseCallback);
    }
}
