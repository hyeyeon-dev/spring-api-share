package demo.api.server.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpUtils {
    public static HashMap<String, Object> post(String reqUrl, String reqParam, Map<String, String> headerMap) throws Exception {

        URL apiUrl = new URL(reqUrl);
        HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();

        connection.setRequestMethod("POST");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        connection.setDoOutput(true);

        boolean isContentTypeYn = false;
        if (headerMap != null && !headerMap.isEmpty()) {
            for (String key : headerMap.keySet()) {
                connection.setRequestProperty(key, headerMap.get(key));
                if (key.toLowerCase().equals("content-type")) {
                    isContentTypeYn = true;
                }
            }
        }
        if (!isContentTypeYn) {
            connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        }

        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.write(reqParam.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();

        int responseCode = connection.getResponseCode();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();

        String inputLine;
        while ((inputLine = bufferedReader.readLine()) != null) {
            stringBuilder.append(inputLine);
        }
        bufferedReader.close();

        String response = stringBuilder.toString();

        HashMap<String, Object> map = new HashMap<>();
        map.put("status", responseCode);
        map.put("response", response);

        return map;
    }

    public static HashMap<String, Object> get(String reqUrl, String queryString, Map<String, String> headerMap) throws Exception {

        URL apiUrl = new URL(reqUrl + "?" + queryString);
        HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();

        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        connection.setDoOutput(true);

        boolean isContentTypeYn = false;
        if (headerMap != null && !headerMap.isEmpty()) {
            for (String key : headerMap.keySet()) {
                connection.setRequestProperty(key, headerMap.get(key));
                if (key.toLowerCase().equals("content-type")) {
                    isContentTypeYn = true;
                }
            }
        }
        if (!isContentTypeYn) {
            connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        }

        int responseCode = connection.getResponseCode();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();

        String inputLine;
        while ((inputLine = bufferedReader.readLine()) != null) {
            stringBuilder.append(inputLine);
        }
        bufferedReader.close();

        String response = stringBuilder.toString();

        HashMap<String, Object> map = new HashMap<>();
        map.put("status", responseCode);
        map.put("response", response);

        return map;
    }
}
