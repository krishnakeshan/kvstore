package command.controller;

import command.request.Command;
import kvstore.KVServiceImpl;

public abstract class CommandController {
    private static CommandController defaultInstance;

    public static CommandController getDefault() {
        if (defaultInstance == null)
            defaultInstance = new CommandControllerImpl(KVServiceImpl.hashMapKVService());
        return defaultInstance;
    }

    public abstract void dispatch(Command command);
}
