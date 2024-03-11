import config.Config;
import server.Server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length < 1)
            throw new RuntimeException("Please provide a config file");

        String configFilePath = args[0];
        Config.initialise(configFilePath);

        Server server = new Server();
        server.start();
    }
}
