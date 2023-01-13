package demo.api.core.httplogger;

import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletResponse;

public class HttpResponseWrapper extends ContentCachingResponseWrapper {
    public HttpResponseWrapper(HttpServletResponse response) {
        super(response);
    }
}
