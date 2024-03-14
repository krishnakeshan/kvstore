package config;

import kvstore.KVStoreConfig;
import kvstore.persist.PersisterConfig;
import server.ServerConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Config {
    private static Config instance;

    private Config() {
        kvStoreConfig = new KVStoreConfig();
        persisterConfig = new PersisterConfig();
        serverConfig = new ServerConfig();
    }

    private KVStoreConfig kvStoreConfig;

    private PersisterConfig persisterConfig;

    private ServerConfig serverConfig;

    public static Config getInstance() {
        if (instance == null)
            instance = new Config();
        return instance;
    }

    public void initialise(String configFilePath) throws IOException {
        System.out.println("Setting KV Store config");
        List<String> lines = Files.readAllLines(Paths.get(configFilePath));
        for (String line : lines) {
            if (!line.isEmpty())
                processConfigLine(line);
        }
    }

    private void processConfigLine(String configLine) {
        String[] parts = configLine.split(":");
        String key = parts[0].trim();
        String value = parts[1].trim();
        System.out.printf("%s: %s%n", key, value);

        String configCategory = key.split("\\.")[0];
        switch (configCategory) {
            case "kvstore" -> kvStoreConfig.parseConfig(key, value);
            case "persist" -> persisterConfig.parseConfig(key, value);
            case "server" -> serverConfig.parseConfig(key, value);
        }
    }

    public KVStoreConfig getKvStoreConfig() {
        return kvStoreConfig;
    }

    public PersisterConfig getPersisterConfig() {
        return persisterConfig;
    }

    public ServerConfig getServerConfig() {
        return serverConfig;
    }
}
