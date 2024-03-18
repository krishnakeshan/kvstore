package kvstore.persist;

import config.Config;
import kvstore.KVStore;
import kvstore.KVStoreConfig;
import kvstore.KVStoreFactory;
import util.FileUtils;
import util.TypeUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class WALPersister extends Persister {
    private Path walDirectory;

    private Path logFile;

    private BufferedWriter logFileWriter;

    private static final String LOG_FIELD_SEPARATOR = ",";

    @Override
    public void start() throws IOException {
        walDirectory = dataDirectory.resolve("wal");
        FileUtils.ensureDirectoryExists(walDirectory);
        setLogFile(createNewLogFile());
    }

    @Override
    public void stop() throws IOException {
        logFileWriter.flush();
    }

    @Override
    public void onPut(String key, Object value) throws IOException {
        String logEntry = logEntryForOperation("put", key, value);
        writeLogToFile(logEntry);
    }

    @Override
    public void onDelete(String key) throws IOException {
        String logEntry = logEntryForOperation("delete", key, null);
        writeLogToFile(logEntry);
    }

    private String logEntryForOperation(String operation, String key, Object value) {
        String timestamp = String.format("%d", System.nanoTime());
        return String.join(LOG_FIELD_SEPARATOR, timestamp, operation, key, value.toString());
    }

    private Path createNewLogFile() throws IOException {
        String fileName = String.format("log_%d", System.currentTimeMillis());
        Path logFilePath = walDirectory.resolve(fileName);
        FileUtils.ensureFileExists(logFilePath);
        return logFilePath;
    }

    private synchronized void writeLogToFile(String log) throws IOException {
        logFileWriter.write(log);
        logFileWriter.newLine();
    }

    private void setLogFile(Path logFile) throws IOException {
        if (logFileWriter != null) {
            logFileWriter.flush();
            logFileWriter.close();
        }

        this.logFile = logFile;
        logFileWriter = new BufferedWriter(new FileWriter(logFile.toFile()));
    }

    @Override
    public KVStore recover() throws IOException {
        System.out.println("starting recovery");
        KVStore kvStore = KVStoreFactory.forStrategy(Config.getInstance().getKvStoreConfig().getImplStrategy());

        walDirectory = dataDirectory.resolve("wal");
        if (Files.exists(walDirectory)) {
            Comparator<Path> fileNameComparator = (path1, path2) -> {
                String fileName1 = path1.getFileName().toString();
                String fileName2 = path2.getFileName().toString();
                return fileName1.compareTo(fileName2);
            };

            try (Stream<Path> logFiles = Files.walk(walDirectory)) {
                logFiles.sorted(fileNameComparator)
                        .forEach(path -> {
                            if (Files.isDirectory(path) || !path.getFileName().toString().startsWith("log_"))
                                return;
                            try {
                                replayLogOperations(path, kvStore);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
            }
        }

        return kvStore;
    }

    private void replayLogOperations(Path logFile, KVStore kvStore) throws IOException {
        System.out.println("replaying logs from " + logFile);
        List<String> lines = Files.readAllLines(logFile);
        lines.sort((s1, s2) -> {
            long timestamp1 = Long.parseLong(s1.split(LOG_FIELD_SEPARATOR)[0]);
            long timestamp2 = Long.parseLong(s2.split(LOG_FIELD_SEPARATOR)[0]);
            return Long.compare(timestamp1, timestamp2);
        });

        // apply operations
        lines.forEach(line -> performLogOperation(line, kvStore));
    }

    private void performLogOperation(String operation, KVStore kvStore) {
        String[] parts = operation.split(LOG_FIELD_SEPARATOR, 4);
        String op = parts[1];
        String key = parts[2];
        String value = parts[3].trim();

        switch (op) {
            case "put" -> {
                kvStore.put(key, TypeUtils.stringToDataType(value));
            }
            case "delete" -> {
                kvStore.delete(key);
            }
        }
    }
}
