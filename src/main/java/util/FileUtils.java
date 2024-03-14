package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtils {
    public static void ensureDirectoryExists(Path path) throws IOException {
        if (Files.notExists(path)) {
            Files.createDirectories(path);
        }
    }

    public static void ensureFileExists(Path path) throws IOException {
        if (Files.notExists(path)) {
            Files.createFile(path);
        }
    }
}
