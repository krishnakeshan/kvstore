import java.io.IOException;

public class Main {
    private static AppManager appManager;

    public static void main(String[] args) throws IOException {
        appManager = new AppManager(args);
        appManager.start();
    }
}
