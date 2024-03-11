package config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Config {
    private static Config config;

    private Config() {}

    private int serverPort;

    public static Config getInstance() {
        return config;
    }

    public static void initialise(String configFilePath) throws IOException {
        config = new Config();

        List<String> lines = Files.readAllLines(Paths.get(configFilePath));
        for (String line : lines) {
            processConfigLine(line);
        }
    }

    private static void processConfigLine(String configLine) {
        String[] parts = configLine.split(":");
        String key = parts[0].trim();
        String value = parts[1].trim();

        switch (key) {
            case "server.port":
                config.serverPort = Integer.parseInt(value);
                break;
        }
    }

    public int getServerPort() {
        return serverPort;
    }
}
