package command.controller;

import command.request.Command;
import kvstore.service.KVService;
import kvstore.service.KVServiceImpl;

public abstract class CommandController {
    protected final KVService kvService;

    public CommandController(KVService kvService) {
        this.kvService = kvService;
    }

    public abstract void dispatch(Command command);
}
