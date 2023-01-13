package demo.api.server.exception;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class ExceptionResponse {
    private String errorName;
    private String cause;
    private String parameters;
    private LocalDateTime timestamp;
}
