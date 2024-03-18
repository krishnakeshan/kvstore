import command.controller.CommandController;
import command.controller.CommandControllerImpl;
import config.Config;
import kvstore.KVStore;
import kvstore.KVStoreFactory;
import kvstore.persist.Persister;
import kvstore.persist.PersisterConfig;
import kvstore.persist.PersisterFactory;
import kvstore.service.KVService;
import kvstore.service.KVServiceImpl;
import server.Server;

import java.io.IOException;

public class AppManager {
    private Config config;
    private KVStore kvStore;
    private KVService kvService;
    private Persister persister;
    private Server server;
    private CommandController commandController;

    public AppManager(String[] appArgs) throws IOException {
        initConfig(appArgs);
        initKVService();
        initPersister();
        setupGracefulShutdown();
    }

    public void start() throws IOException {
        startServer();
    }

    private void initConfig(String[] appArgs) throws IOException {
        if (appArgs.length < 1)
            throw new IllegalArgumentException("config file not provided");
        String configFilePath = appArgs[0];
        config = Config.getInstance();
        config.initialise(configFilePath);
    }

    private void initKVService() {
        kvStore = KVStoreFactory.forStrategy(config.getKvStoreConfig().getImplStrategy());
        kvService = new KVServiceImpl(kvStore);
    }

    private void initPersister() throws IOException {
        PersisterConfig persisterConfig = config.getPersisterConfig();
        persister = PersisterFactory.forStrategy(persisterConfig.getImplStrategy());
        persister.setDataDirectory(config.getPersisterConfig().getDataDirectory());
        performRecovery();
        persister.setKvStoreEventEmitter(kvStore);
        persister.start();
    }

    private void performRecovery() throws IOException {
        if (config.getPersisterConfig().isRecoverState()) {
            kvStore = persister.recover();
            kvService.setKvStore(kvStore);
        }
    }

    private void startServer() throws IOException {
        commandController = new CommandControllerImpl(kvService);

        int port = config.getServerConfig().getPort();
        int backlog = config.getServerConfig().getBacklog();
        server = new Server(port, backlog, commandController);
        server.start();
    }

    private void setupGracefulShutdown() {
        Runtime.getRuntime().addShutdownHook(Thread.ofPlatform().unstarted(() -> {
            try {
                shutdown();
            } catch (IOException e) {
                System.err.println("error shutting down kvstore " + e.getMessage());
            }
        }));
    }

    private void shutdown() throws IOException {
        System.out.println("shutting down kvstore");
        server.stop();
        persister.stop();
    }
}
