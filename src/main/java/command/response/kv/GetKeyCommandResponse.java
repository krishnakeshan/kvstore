package command.response.kv;

import command.response.CommandResponse;

public class GetKeyCommandResponse extends CommandResponse {
    private final Object value;

    public GetKeyCommandResponse(boolean successful, Object value) {
        super(successful);
        this.value = value;
    }

    public Object getValue() {
        return this.value;
    }
}
