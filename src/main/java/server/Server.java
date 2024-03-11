package server;

import com.sun.net.httpserver.*;
import command.controller.CommandController;
import command.request.kv.DeleteKeyCommand;
import command.request.kv.GetKeyCommand;
import command.request.kv.PutKeyCommand;
import command.response.kv.DeleteKeyCommandResponse;
import command.response.kv.GetKeyCommandResponse;
import command.response.kv.PutKeyCommandResponse;
import config.Config;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Server {
    private final Config config = Config.getInstance();

    private final HttpServer httpServer;

    private final CommandController commandController;

    private static final HttpMethodFilter
            getMethodFilter = new HttpMethodFilter(HttpMethod.GET),
            postMethodFilter = new HttpMethodFilter(HttpMethod.POST),
            putMethodFilter = new HttpMethodFilter(HttpMethod.PUT),
            deleteMethodFilter = new HttpMethodFilter(HttpMethod.DELETE);

    public Server() throws IOException {
        int port = config.getServerPort();
        this.httpServer = HttpServer.create(new InetSocketAddress(port), 0);
        this.commandController = CommandController.getDefault();

        registerEndpoints();
    }

    public void start() {
        InetSocketAddress address = httpServer.getAddress();
        System.out.printf("Starting HTTP server at %s:%d%n", address.getHostName(), address.getPort());
        httpServer.start();
    }

    private void registerEndpoints() {
        Objects.requireNonNull(httpServer);
        registerKVStoreEndpoints();
    }

    private void registerKVStoreEndpoints() {
        httpServer.createContext("/keys", exchange -> {
            switch (HttpMethod.forValue(exchange.getRequestMethod())) {
                case POST -> {
                    handlePostKey(exchange);
                }
                case GET -> {
                    handleGetKey(exchange);
                }
                case DELETE -> {
                    handleDeleteKey(exchange);
                }
            }
        });
    }

    private void handlePostKey(HttpExchange exchange) {
        String key = getKeyFromUri(exchange.getRequestURI());
        Object value = getValueFromRequestBody(exchange.getRequestBody());
        System.out.println("POST key " + key + " " + value);
        PutKeyCommand putKeyCommand = new PutKeyCommand(key, value, commandResponse -> {
            if (commandResponse instanceof PutKeyCommandResponse response) {
                if (response.isSuccessful()) {
                    try {
                        exchange.sendResponseHeaders(200, -1);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    try {
                        exchange.sendResponseHeaders(500, -1);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                exchange.close();
            }
        });
        commandController.dispatch(putKeyCommand);
    }

    private void handleGetKey(HttpExchange exchange) {
        String key = getKeyFromUri(exchange.getRequestURI());
        System.out.println("GET key " + key);
        GetKeyCommand getKeyCommand = new GetKeyCommand(key, commandResponse -> {
            if (commandResponse instanceof GetKeyCommandResponse response) {
                if (response.isSuccessful()) {
                    try {
                        writeObjectToResponse(response.getValue(), exchange, true);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    try {
                        exchange.sendResponseHeaders(500, -1);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        commandController.dispatch(getKeyCommand);
    }

    private void handleDeleteKey(HttpExchange exchange) {
        String key = getKeyFromUri(exchange.getRequestURI());
        System.out.println("DELETE key " + key);
        DeleteKeyCommand deleteKeyCommand = new DeleteKeyCommand(key, commandResponse -> {
            if (commandResponse instanceof DeleteKeyCommandResponse response) {
                if (response.isSuccessful()) {
                    try {
                        exchange.sendResponseHeaders(200, -1);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    try {
                        exchange.sendResponseHeaders(500, -1);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        commandController.dispatch(deleteKeyCommand);
    }

    private static String getKeyFromUri(URI uri) {
        return getKeyFromPath(uri.getPath());
    }

    private static String getKeyFromPath(String path) {
        String[] parts = path.split("/keys/");
        if (parts.length > 1) {
            return parts[1];
        }
        return null;
    }

    private static Object getValueFromRequestBody(InputStream inputStream) {
        Object result = null;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String line = bufferedReader.readLine();

            try {
                result = Integer.valueOf(line);
            } catch (NumberFormatException e) {
                // ignore
            }

            try {
                result = Double.valueOf(line);
            } catch (NumberFormatException e) {
                // ignore
            }

            if (line.equalsIgnoreCase("true") || line.equalsIgnoreCase("false")) {
                result = Boolean.valueOf(line);
            }

            result = line;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private static int writeObjectToResponse(Object value, HttpExchange exchange, boolean close) throws IOException {
        byte[] respBytes = value.toString().getBytes();
        exchange.sendResponseHeaders(200, respBytes.length);
        exchange.getResponseBody().write(respBytes);

        if (close)
            exchange.getResponseBody().close();
        return respBytes.length;
    }

    public static class HttpMethodFilter extends Filter {
        private final Set<HttpMethod> allowedMethods;

        public HttpMethodFilter(HttpMethod... httpMethods) {
            super();
            this.allowedMethods = new HashSet<>();
            this.allowedMethods.addAll(List.of(httpMethods));
        }

        @Override
        public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
            HttpMethod requestMethod = HttpMethod.forValue(exchange.getRequestMethod());
            if (allowedMethods.contains(requestMethod))
                chain.doFilter(exchange);
            else {
                exchange.sendResponseHeaders(405, -1);
                exchange.close();
            }
        }

        @Override
        public String description() {
            return "Enforces that this exchange has the method GET";
        }
    }
}
