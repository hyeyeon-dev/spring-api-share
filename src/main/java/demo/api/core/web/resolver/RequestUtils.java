package demo.api.core.web.resolver;

import demo.api.server.utils.LogUtils;
import demo.api.server.utils.RegexUtils;
import org.apache.commons.lang3.ObjectUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class RequestUtils {
    public static Long getClientNo(String clientNo) {
        String clientNoOnlyNumber = RegexUtils.regexOnlyNumber(clientNo);
        if (ObjectUtils.isNotEmpty(clientNoOnlyNumber)) {
            return Long.valueOf(clientNo);
        } else {
            return 0L;
        }
    }

    public static String getClientIp(String clientIp) {
        if (ObjectUtils.isNotEmpty(clientIp)) {
            return RegexUtils.regexExtractIp(clientIp);
        } else {
            return "";
        }
    }

    public static String getDeviceChannel(String userAgent) {
        boolean isMobile = userAgent.matches(".*(iPhone|iPod|iPad|BlackBerry|Android|Windows CE|LG|MOT|SAMSUNG|SonyEricsson).*");

        if (isMobile) {
            return "M";
        } else {
            return "W";
        }
    };

    public static Map<String, String> parseUtmTag(String url) {
        return getUtmTagParseMap(url);
    };

    public static Map<String, String> getUtmTagParseMap(String url) {
        if (ObjectUtils.isNotEmpty(url)) {
            try {
                Map<String, String> utmMap = new HashMap<>();

                Map<String, String> queryMap = getQueryMap(new URL(url).getQuery());
                if (queryMap != null) {
                    for (String key : queryMap.keySet()) {
                        if (key.contains("utm")) utmMap.put(key, queryMap.get(key));
                    }
                }

                return utmMap;

            } catch (MalformedURLException me) {
                LogUtils.logStackTrace(me);
            }
        }

        return null;
    };

    public static Map<String, String> getQueryMap(String query)
    {
        if (query==null) return null;

        int pos1=query.indexOf("?");
        if (pos1>=0) {
            query=query.substring(pos1+1);
        }

        String[] params = query.split("&");
        Map<String, String> map = new HashMap<>();
        for (String param : params)
        {
            String[] paramArr = param.split("=");
            String name = paramArr[0];
            String value = "";
            if (paramArr.length > 1) {
                value = param.split("=")[1];
            }
            map.put(name, value);
        }
        return map;
    };
}
