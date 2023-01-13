package demo.api.core.web.resolver;

import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Component
public class RequestDataArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return true;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);

        RequestClient requestClient = new RequestClient();
        requestClient.setClientIp(httpServletRequest.getHeader("clientIp"));
        requestClient.setSessionId(httpServletRequest.getHeader("sessionId"));
        requestClient.setUserAgent(httpServletRequest.getHeader("userAgent"));
        requestClient.setRequestUrl(httpServletRequest.getHeader("requestUrl"));
        requestClient.setReferer(httpServletRequest.getHeader("referer"));
        requestClient.setClientNo(httpServletRequest.getHeader("clientNo"));
        requestClient.setHttpSession(httpServletRequest.getSession());

        return requestClient;
    }
}