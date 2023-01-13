package demo.api.core.httplogger;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import demo.api.core.web.resolver.RequestUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.json.simple.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HttpRequestLogVo {
    private Long seqIdx;
    private String reqUrl;
    private String reqApi;
    private String querystring;
    private String userAgent;
    private String sessId;
    private String routeId;
    private String referer;
    private Long clientNo;
    private String clientIp;
    private String deviceType;

    private String utmSource;
    private String utmMedium;
    private String utmCampaign;
    private String utmContent;
    private String utmTerm;

    private Double secForRes;

    private Integer reqYm;
    private Integer reqYmd;
    private Integer reqYmdHm;

    private LocalDateTime reqTm;
    private LocalDateTime regTm;

    private HttpRequestLogVo(RequestBuilder requestBuilder) {
        LocalDateTime reqTm = LocalDateTime.parse(requestBuilder.datetime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        this.reqUrl = requestBuilder.requestUrl;
        this.reqApi = requestBuilder.requestApi;
        this.querystring = requestBuilder.queryString;
        this.userAgent = requestBuilder.userAgent;
        this.sessId = requestBuilder.sessionId;
        this.routeId = requestBuilder.routeId;
        this.referer = requestBuilder.referer;
        this.clientNo = RequestUtils.getClientNo(requestBuilder.clientNo);
        this.clientIp = RequestUtils.getClientIp(requestBuilder.clientIp);
        this.deviceType = RequestUtils.getDeviceChannel(requestBuilder.userAgent);
        this.secForRes = ObjectUtils.isNotEmpty(requestBuilder.timeTakenToServe)? Double.valueOf(requestBuilder.timeTakenToServe.replaceAll("\\(sec\\)","")):0d;
        this.reqTm = reqTm;

        // parsing UTM
        Map<String, String> utmParseMap = RequestUtils.getUtmTagParseMap(requestBuilder.requestUrl);
        if (ObjectUtils.isNotEmpty(utmParseMap)) {
            for (String key : utmParseMap.keySet()) {
                if ("utm_source".equals(key)) this.utmSource = utmParseMap.get(key);
                else if ("utm_medium".equals(key)) this.utmMedium = utmParseMap.get(key);
                else if ("utm_campaign".equals(key)) this.utmCampaign = utmParseMap.get(key);
                else if ("utm_content".equals(key)) this.utmContent = utmParseMap.get(key);
                else if ("utm_term".equals(key)) this.utmTerm = utmParseMap.get(key);
            }
        }
    }

    public static class RequestBuilder {
        private String requestUrl;
        private String requestApi;
        private String queryString;
        private String userAgent;
        private String sessionId;
        private String routeId;
        private String referer;
        private String clientNo;
        private String clientIp;
        private String datetime;
        private String timeTakenToServe;

        public RequestBuilder(JSONObject json) {
            this.requestUrl = getJsonValue(json.get("requestUrl"));
            this.requestApi = getJsonValue(json.get("requestApi"));
            this.queryString = getJsonValue(json.get("queryString"));
            this.userAgent = getJsonValue(json.get("userAgent"));
            this.sessionId = getJsonValue(json.get("sessionId"));
            this.routeId = getJsonValue(json.get("routeId"));
            this.referer = getJsonValue(json.get("referer"));
            this.clientNo = getJsonValue(json.get("clientNo"));
            this.clientIp = getJsonValue(json.get("clientIp"));
            this.datetime = getJsonValue(json.get("datetime"));
            this.timeTakenToServe = getJsonValue(json.get("timeTakenToServe"));
        }

        public String getJsonValue(Object val) {
            if (ObjectUtils.isEmpty(val) || ("null".equals(val) || "-".equals(val))) {
                return "";
            } else {
                return val+"";
            }
        }

        public RequestBuilder setRequestUrl(String requestUrl) {
            this.requestUrl = requestUrl;
            return this;
        }

        public RequestBuilder setRequestApi(String requestApi) {
            this.requestApi = requestApi;
            return this;
        }

        public RequestBuilder setQueryString(String queryString) {
            this.queryString = queryString;
            return this;
        }

        public RequestBuilder setUserAgent(String userAgent) {
            this.userAgent = userAgent;
            return this;
        }

        public RequestBuilder setSessionId(String sessionId) {
            this.sessionId = sessionId;
            return this;
        }

        public RequestBuilder setRouteId(String routeId) {
            this.routeId = routeId;
            return this;
        }

        public RequestBuilder setReferer(String referer) {
            this.referer = referer;
            return this;
        }

        public RequestBuilder setClientNo(String clientNo) {
            this.clientNo = clientNo;
            return this;
        }

        public RequestBuilder setClientIp(String clientIp) {
            this.clientIp = clientIp;
            return this;
        }

        public RequestBuilder setDatetime(String datetime) {
            this.datetime = datetime;
            return this;
        }

        public RequestBuilder setTimeTakenToServe(String timeTakenToServe) {
            this.timeTakenToServe = timeTakenToServe;
            return this;
        }

        public HttpRequestLogVo build() {
            return new HttpRequestLogVo(this);
        }
    }
}
