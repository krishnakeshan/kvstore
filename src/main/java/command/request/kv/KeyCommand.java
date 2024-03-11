package command.request.kv;

import command.request.Command;
import command.response.CommandResponseCallback;

public abstract class KeyCommand extends Command {
    protected final String key;

    public KeyCommand(String key, CommandResponseCallback responseCallback) {
        super(responseCallback);
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
