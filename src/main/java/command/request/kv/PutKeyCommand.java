package command.request.kv;

import command.response.CommandResponseCallback;

public class PutKeyCommand extends KeyCommand {
    private final Object value;

    public PutKeyCommand(String key, Object value, CommandResponseCallback responseCallback) {
        super(key, responseCallback);
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
}
