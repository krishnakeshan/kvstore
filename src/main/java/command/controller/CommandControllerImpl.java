package command.controller;

import command.request.Command;
import kvstore.service.KVService;

public class CommandControllerImpl extends CommandController {

    public CommandControllerImpl(KVService kvService) {
        super(kvService);
    }

    @Override
    public void dispatch(Command command) {
        kvService.executeCommand(command);
    }
}
