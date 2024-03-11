package command.request;

import command.response.CommandResponseCallback;

public abstract class Command {
    protected final CommandResponseCallback responseCallback;

    public Command(CommandResponseCallback responseCallback) {
        this.responseCallback = responseCallback;
    }

    public CommandResponseCallback getResponseCallback() {
        return responseCallback;
    }
}