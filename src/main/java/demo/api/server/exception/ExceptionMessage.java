package demo.api.server.exception;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Map;

public enum ExceptionMessage {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "처리 중 오류가 발생했습니다.",null, null)
    ;

    private final HttpStatus httpStatus;
    private final String cause;
    private Map<String, Object> parameters;
    private String errorName;

    ExceptionMessage(HttpStatus httpStatus, String cause, Map<String, Object> parameters, String errorName) {
        this.httpStatus = httpStatus;
        this.cause = cause;
        this.parameters = parameters;
        this.errorName = errorName;
    }

    public HttpStatus httpStatus() {
        return httpStatus;
    }

    public String cause() { return cause; }

    public String parameters() {
        Gson gson = new Gson();
        return gson.toJson(this.parameters);
    }

    public String errorName() { return errorName; }

    private  void setAdditionalInfo(String errorName, Map<String, Object> parameters) {
        this.errorName = errorName;
        this.parameters = parameters;
    }

    static ExceptionMessage of(String jsonException) {
        Gson gson = new GsonBuilder().serializeNulls().create();

        Map<String, Object> map = gson.fromJson(jsonException, Map.class);
        String errorName = (String) map.get("name");
        Map<String, Object> parameters = (Map<String, Object>) map.get("parameters");

        ExceptionMessage exceptionMessage = Arrays.stream(values())
                .filter(v -> errorName.equals(v.name()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("%s는 정의되지 않은 Exception 입니다. ", errorName)));

        exceptionMessage.setAdditionalInfo(errorName, parameters);

        return exceptionMessage;
    }


}
