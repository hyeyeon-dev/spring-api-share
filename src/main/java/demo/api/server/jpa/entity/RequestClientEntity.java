package demo.api.server.jpa.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "TB_REQUEST_LOG_MST")
@Builder
public class RequestClientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SEQ")
    @Schema(description = "일련 번호")
    private long seq;

    @Column(name = "CLIENT_IP")
    @Schema(description = "접속 IP")
    private String clientIp;

    @Column(name = "SESSION_ID")
    @Schema(description = "세션 ID")
    private String sessionId;

    @Column(name = "USER_AGENT")
    @Schema(description = "USER-AGENT")
    private String userAgent;

    @Column(name = "REQUEST_URL")
    @Schema(description = "요청 URL")
    private String requestUrl;

    @Column(name = "DEVICE_CHANNEL")
    @Schema(description = "단말채널")
    private String deviceChannel;

    @Column(name = "REFERER")
    @Schema(description = "referer 정보")
    private String referer;
}
