package util;

public class TypeUtils {
    public static Object stringToDataType(String value) {
        Object result = null;
        try {
            result = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            // try parsing into double
            try {
                result = Double.parseDouble(value);
            } catch (NumberFormatException e1) {
                // try parsing into boolean
                if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
                    result = Boolean.parseBoolean(value);
                } else {
                    result = value;
                }
            }
        }
        return result;
    }
}
