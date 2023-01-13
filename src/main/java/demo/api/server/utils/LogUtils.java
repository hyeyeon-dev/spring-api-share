package demo.api.server.utils;

import demo.api.core.httplogger.HttpRequestWrapper;
import demo.api.server.exception.ExceptionMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Slf4j
public class LogUtils {
    /**
     * Exception 발생시 에러 로그에 상세 내용 기록
     * @Author hanhyeyeon
     */
    public static void logStackTrace(Exception e) {
        StringBuilderUtils sb = new StringBuilderUtils();
        sb.appendLine("");
        sb.appendLine("-----------------------------------------");
        for (StackTraceElement element : e.getStackTrace()) {
            sb.appendLine(element.toString());
        }
        sb.appendLine("-----------------------------------------");

        log.error(sb.toString());
    }

    /**
     * ExceptionMessage 출력
     * @Author hanhyeyeon
     */
    public static void logExceptionMessage(ExceptionMessage exceptionMessage) {
        StringBuilderUtils sb = new StringBuilderUtils();
        sb.appendLine("");
        sb.appendLine("-----------------------------------------");
        sb.appendLine("[\uD83D\uDE31 Exception Message \uD83D\uDE31]");
        sb.appendLine(" ErrorName : " + exceptionMessage.errorName());
        sb.appendLine(" Cause     : " + exceptionMessage.cause());
        sb.appendLine(" Parameters: " + exceptionMessage.parameters());
        sb.appendLine("-----------------------------------------");

        log.error(sb.toString());
    }

    /**
     * ExceptionMessage 출력(header 포함 출력)
     * @Author hanhyeyeon
     */
    public static void logExceptionMessage(WebRequest webRequest, ExceptionMessage exceptionMessage) {
        ServletWebRequest servletWebRequest = ((ServletWebRequest)webRequest);

        Iterator<String> headerNames = servletWebRequest.getHeaderNames();
        String headerLog = "";
        if (ObjectUtils.isNotEmpty(servletWebRequest.getHeaderNames())) {
            while (headerNames.hasNext()) {
                String name = headerNames.next();
                String value = servletWebRequest.getHeader(name);
                headerLog += name + ":" + value + ",";
            }
        }
        headerLog = ObjectUtils.isNotEmpty(headerLog)? headerLog.substring(0, headerLog.length()-1) : "";

        String cookieLog = "";
        if (ObjectUtils.isNotEmpty(servletWebRequest.getRequest().getCookies())) {
            Cookie[] cookies = servletWebRequest.getRequest().getCookies();
            for (Cookie cookie : cookies) {
                String name = cookie.getName();
                String value = cookie.getValue();
                cookieLog += name + ":" + value + ",";
            }
        }
        cookieLog = ObjectUtils.isNotEmpty(cookieLog)? cookieLog.substring(0, cookieLog.length()-1) : "";

        Iterator<String> params = servletWebRequest.getParameterNames();
        String paramLog = "";
        if (ObjectUtils.isNotEmpty(servletWebRequest.getParameterNames())) {
            while (params.hasNext()) {
                String name = params.next();
                String value = servletWebRequest.getParameter(name);
                paramLog += name + ":" + value + ",";
            }
        }
        paramLog = ObjectUtils.isNotEmpty(paramLog)? paramLog.substring(0, paramLog.length()-1) : "";

        String bodyLog = "";
        Scanner scanner = null;
        try {
            scanner = new Scanner(servletWebRequest.getRequest().getInputStream(), "UTF-8").useDelimiter("\\A");
            bodyLog = (scanner.hasNext()? RegexUtils.regexRemoveSpace(scanner.next()):"");
        } catch (IOException ie){
            logStackTrace(ie);
        }

        StringBuilderUtils sb = new StringBuilderUtils();
        sb.appendLine("");
        sb.appendLine("-----------------------------------------");
        sb.appendLine("[\uD83D\uDE31 Exception Message \uD83D\uDE31]");
        sb.appendLine(" ClientIp     : " + RegexUtils.regexExtractIp(servletWebRequest.getHeader("clientIp")));
        sb.appendLine(" SessionId    : " + servletWebRequest.getHeader("sessionId"));
        sb.appendLine(" Header       : " + headerLog);
        sb.appendLine(" Description  : " + servletWebRequest.getDescription(true));
        sb.appendLine(" Cookie       : " + cookieLog);
        sb.appendLine(" QueryString  : " + servletWebRequest.getRequest().getQueryString());
        sb.appendLine(" Method       : " + servletWebRequest.getHttpMethod());
        sb.appendLine(" Parameter    : " + paramLog);
        sb.appendLine(" Body         : " + bodyLog);
        sb.appendLine(" ErrorName    : " + exceptionMessage.errorName());
        sb.appendLine(" Cause        : " + exceptionMessage.cause());
        sb.appendLine(" Parameters   : " + exceptionMessage.parameters());
        sb.appendLine("-----------------------------------------");

        log.error(sb.toString());
    }

    private static boolean isVisible(MediaType mediaType) {
        final List<MediaType> VISIBLE_TYPES = Arrays.asList(
                MediaType.valueOf("text/*"),
                MediaType.APPLICATION_FORM_URLENCODED,
                MediaType.APPLICATION_JSON,
                MediaType.APPLICATION_XML,
                MediaType.valueOf("application/*+json"),
                MediaType.valueOf("application/*+xml"),
                MediaType.MULTIPART_FORM_DATA
        );

        return VISIBLE_TYPES.stream()
                .anyMatch(visibleType -> visibleType.includes(mediaType));
    }

    private static String getPayLoad(boolean isVisible, InputStream inputStream) throws IOException {
        String log = "";
        if (isVisible) {
            byte[] content = StreamUtils.copyToByteArray(inputStream);
            if (content.length > 0) {
                log = new String(content);
            }
        } else {
            log = "Binary Content";
        }

        return log;
    }

    public static void logRequest(HttpRequestWrapper request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        String headerLog = "";
        for (String name : Collections.list(headerNames)) {
            String value = request.getHeader(name);
            headerLog += name + ":" + value + ",";
        }
        headerLog = ObjectUtils.isNotEmpty(headerLog)? headerLog.substring(0, headerLog.length()-1) : "";

        String cookieLog = "";
        if (ObjectUtils.isNotEmpty(request.getCookies())) {
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                String name = cookie.getName();
                String value = cookie.getValue();
                cookieLog += name + ":" + value + ",";
            }
        }
        cookieLog = ObjectUtils.isNotEmpty(cookieLog)? cookieLog.substring(0, cookieLog.length()-1) : "";

        Enumeration<String> params = request.getParameterNames();
        String paramLog = "";
        for (String name : Collections.list(params)) {
            String value = request.getParameter(name);
            paramLog += name + ":" + value + ",";
        }
        paramLog = ObjectUtils.isNotEmpty(paramLog)? paramLog.substring(0, paramLog.length()-1) : "";

        boolean isVisible = isVisible(MediaType.valueOf(request.getContentType() == null ? "application/json" : request.getContentType()));

        String bodyLog = "";
        try {
            bodyLog = RegexUtils.regexRemoveSpace(getPayLoad(isVisible, request.getInputStream()));
        } catch (IOException ie){
            logStackTrace(ie);
        }
        StringBuilderUtils sb = new StringBuilderUtils();
        sb.appendLine("");
        sb.appendLine("-----------------------------------------");
        sb.appendLine("[\uD83D\uDE36 Request \uD83D\uDE36]");
        sb.appendLine(" ClientIp          : " + RegexUtils.regexExtractIp(request.getHeader("clientIp")));
        sb.appendLine(" SessionId         : " + request.getHeader("sessionId"));
        sb.appendLine(" Header            : " + headerLog);
        sb.appendLine(" RequestUri        : " + request.getRequestURI());
        sb.appendLine(" Cookie            : " + cookieLog);
        sb.appendLine(" QueryString       : " + request.getQueryString());
        sb.appendLine(" Method            : " + request.getMethod());
        sb.appendLine(" Parameter         : " + paramLog);
        sb.appendLine(" Body              : " + bodyLog);
        sb.appendLine("-----------------------------------------");

        log.info(sb.toString());
    }

    public static void logResponse(HttpRequestWrapper request, ContentCachingResponseWrapper response) {
        boolean isVisible = isVisible(MediaType.valueOf(request.getContentType() == null ? "application/json" : request.getContentType()));

        String resLog = "";
        try {
            resLog = getPayLoad(isVisible, response.getContentInputStream());
        } catch (IOException ie){
            logStackTrace(ie);
        }

        StringBuilderUtils sb = new StringBuilderUtils();
        sb.appendLine("");
        sb.appendLine("-----------------------------------------");
        sb.appendLine("[\uD83E\uDD17 Response \uD83E\uDD17]");
        sb.appendLine(" ClientIp          : " + RegexUtils.regexExtractIp(request.getHeader("clientIp")));
        sb.appendLine(" SessionId         : " + request.getHeader("sessionId"));
        sb.appendLine(" RequestUri        : " + request.getRequestURI());
        sb.appendLine(" Status            : " + response.getStatusCode());
        sb.appendLine(" Response          : " + resLog);
        sb.appendLine("-----------------------------------------");

        log.info(sb.toString());
    }

}
