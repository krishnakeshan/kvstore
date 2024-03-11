package server;

public enum HttpMethod {
    GET,
    POST,
    PUT,
    DELETE,
    INVALID;

    public static HttpMethod forValue(String method) {
        method = method.toLowerCase();
        return switch (method) {
            case "get" -> GET;
            case "post" -> POST;
            case "put" -> PUT;
            case "delete" -> DELETE;
            default -> INVALID;
        };
    }
}
