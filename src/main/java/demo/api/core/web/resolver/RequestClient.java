package demo.api.core.web.resolver;

import lombok.Data;
import org.apache.commons.lang3.ObjectUtils;

import javax.servlet.http.HttpSession;
import java.io.Serializable;

@Data
public class RequestClient implements Serializable {
    /* 요청 Header 정보 */
    private String clientIp;
    private String sessionId;
    private String userAgent;
    private String requestUrl;
    private String deviceChannel;
    private String referer;

    /* 로그인 정보 */
    private String clientNo;

    private HttpSession httpSession;

    public String getClientIp() {
        return RequestUtils.getClientIp(clientIp);
    }

    public String getDeviceChannel() {
        return RequestUtils.getDeviceChannel(getUserAgent());
    }

    public String getSessionId() {
        return ObjectUtils.isNotEmpty(sessionId)? sessionId:"";
    }

    public String getUserAgent() {
        return ObjectUtils.isNotEmpty(userAgent)? userAgent:"";
    }

    public String getRequestUrl() {
        return ObjectUtils.isNotEmpty(requestUrl)? requestUrl:"";
    }

    public Long getClientNo() {
        return RequestUtils.getClientNo(clientNo);
    }

    public String getReferer() { return ObjectUtils.isNotEmpty(referer)? referer:""; }
}
