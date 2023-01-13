package demo.api.server.exception;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

public class CustomizedStatusException extends RuntimeException{
    public CustomizedStatusException(ExceptionMessage exceptionMessage) {
        super(exceptionCustomize(exceptionMessage, null));
    }

    public CustomizedStatusException(ExceptionMessage exceptionMessage, Map<String, Object> parameters) {
        super(exceptionCustomize(exceptionMessage, parameters));
    }

    public static String exceptionCustomize(ExceptionMessage exceptionMessage, Map<String, Object> parameters) {
        Gson gson = new GsonBuilder().serializeNulls().create();

        Map<String, Object> map = new HashMap<>();
        map.put("name", exceptionMessage.name());
        map.put("parameters", parameters);

        return gson.toJson(map);
    }
}
