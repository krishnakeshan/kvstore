package server;

public class ServerConfig {
    private int port;
    private int backlog;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getBacklog() {
        return backlog;
    }

    public void setBacklog(int backlog) {
        this.backlog = backlog;
    }

    public void parseConfig(String key, String value) {
        // too few config values right now, turn this into a switch if it grows
        switch (key) {
            case "server.port" -> setPort(Integer.parseInt(value));
            case "server.backlog" -> setBacklog(Integer.parseInt(value));
        }
    }
}
