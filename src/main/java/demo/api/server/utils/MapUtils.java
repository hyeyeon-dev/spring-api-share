package demo.api.server.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.*;

public class MapUtils {
    /**
     * Map 객체를 쿼리스트링 형태로 변환
     * @Author hanhyeyeon
     */
    public static String mapToQueryString(Map<String, Object> params) {
        List<NameValuePair> pairs = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(params)) {
            for (Map.Entry<String, Object> param : params.entrySet()) {
                Object value = param.getValue();
                if (ObjectUtils.isNotEmpty(value)) {
                    pairs.add(new BasicNameValuePair(param.getKey(), value.toString()));
                }
            }
        }
        return "?" + URLEncodedUtils.format(pairs, "UTF-8");
    }

    public static Long getOrDefault (Object value, long defaultVal) {
        return ObjectUtils.isNotEmpty(value)? (Long) value : defaultVal;
    }

    public static Integer getOrDefault (Object value, int defaultVal) {
        return ObjectUtils.isNotEmpty(value)? (Integer) value : defaultVal;
    }

    public static String getOrDefault (Object value, String defaultVal) {
        return ObjectUtils.isNotEmpty(value)? String.valueOf(value) : defaultVal;
    }

    public static Double getOrDefault (Object value, double defaultVal) {
        return ObjectUtils.isNotEmpty(value)? (Double) value : defaultVal;
    }

    /**
     * where in 절을 위한 리스트 문자열 만들기
     * @param value "123,123,123..."
     * @param delimiter ","
     * @return "List"
     */
    public static List<String> makeIn(String value, String delimiter) {
        if(value == null || "".equals(value))
            return null;

        return Arrays.asList(value.split(delimiter));
    }

    /**
     * json 문자열을 hashmap 으로 변환
     * @param jsonString json문자열
     * @return hashmap
     */
    public static HashMap<String, Object> jsonStringToHashMap(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonString, new TypeReference<HashMap<String, String>>() {});
    }
}
