package command.response;

public abstract class CommandResponse {
    protected boolean successful;

    public CommandResponse() {}

    public CommandResponse(boolean successful) {
        this.successful = successful;
    }

    public boolean isSuccessful() {
        return successful;
    }
}
