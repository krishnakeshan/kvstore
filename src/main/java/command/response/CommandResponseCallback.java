package command.response;

import java.io.IOException;

public interface CommandResponseCallback {
    void onResponse(CommandResponse commandResponse);
}
