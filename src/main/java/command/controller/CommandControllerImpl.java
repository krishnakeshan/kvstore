package command.controller;

import command.request.Command;
import kvstore.KVService;

public class CommandControllerImpl extends CommandController {
    private final KVService kvService;

    public CommandControllerImpl(KVService kvService) {
        this.kvService = kvService;
    }

    @Override
    public void dispatch(Command command) {
        kvService.executeCommand(command);
    }
}
