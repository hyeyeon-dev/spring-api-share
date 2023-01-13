package demo.api.server.exception;

import demo.api.server.utils.LogUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@RestController
@ControllerAdvice
public class ExceptionResponseHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomizedStatusException.class)
    public final ResponseEntity<Object> handleCustomizedStatusExceptions(Exception exception, WebRequest webRequest) {
        ExceptionMessage exceptionMessage = ExceptionMessage.of(exception.getMessage());
        LogUtils.logExceptionMessage(webRequest, exceptionMessage);

        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .errorName(exceptionMessage.errorName())
                .cause(exceptionMessage.cause())
                .parameters(exceptionMessage.parameters())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(exceptionResponse, exceptionMessage.httpStatus());
    }

}
