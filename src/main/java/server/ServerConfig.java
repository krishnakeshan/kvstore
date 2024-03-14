package server;

public class ServerConfig {
    private int port;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void parseConfig(String key, String value) {
        // too few config values right now, turn this into a switch if it grows
        if (key.equals("server.port")) {
            setPort(Integer.parseInt(value));
        }
    }
}
