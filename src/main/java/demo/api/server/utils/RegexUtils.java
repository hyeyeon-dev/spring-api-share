package demo.api.server.utils;

import org.apache.commons.lang3.ObjectUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {
    /**
     * 문자열에서 숫자값만 추출
     * @Author hanhyeyeon
     */
    public static String regexOnlyNumber(String str) {
        return ObjectUtils.isNotEmpty(str)? str.replaceAll("[^0-9]", "") : null;
    }

    /**
     * 문자열 앞 뒤 공백 제거
     * @Author hanhyeyeon
     */
    public static String trim(String str) {
        return str.trim();
    }

    /**
     * 문자열에서 태그 제거 (※태그만 제거하고 내용은 제거하지 않음)
     * @param str 태그가 포함된 문자열
     * @return String
     */
    public static String tagRemove(String str) {
        return str.replaceAll("<[^>]+>", "");
    }

    /**
     * 문자열 전체 공백 제거
     * @Author hanhyeyeon
     */
    public static String regexRemoveSpace (String str) {
        return ObjectUtils.isNotEmpty(str)? str.replaceAll("\\s","") : null;
    }

    /**
     * IP 주소 일치(IPv4, IPv6)
     */
    public static String regexIp (String str) {
        String result = "";
        Pattern pattern = Pattern.compile("(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}");
        Matcher matcher = pattern.matcher(str);

        if (matcher.matches()) {
            result = matcher.group();
        } else {
            pattern = Pattern.compile("(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))");
            matcher = pattern.matcher(str);

            if (matcher.matches()) {
                result = matcher.group();
            }
        }

        return result;
    }

    /**
     * IP 주소만 추출(IPv4, IPv6)
     * Input    ::ffff:192.168.0.151(IPv6)
     * Output   192.168.0.151
     */
    public static String regexExtractIp(String str) {
        if (ObjectUtils.isEmpty(str)) return "";

        Pattern pattern = Pattern.compile("((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})");
        Matcher matcher = pattern.matcher(str);

        StringBuffer stringBuffer = new StringBuffer();
        while(matcher.find()) {
            stringBuffer.append(matcher.group()+" ");
        }

        String result[] = matcher.reset().find()? stringBuffer.toString().split(" ") : new String[0];

        return String.join("",result);
    }

    public static String mobileNumberHyphenAdd(String mobileNo, boolean isMask) {
        String formatNum = "";
        if (ObjectUtils.isEmpty(mobileNo)) return formatNum;
        mobileNo = mobileNo.replaceAll("-","");

        if (mobileNo.length() == 11) {
            if (isMask) {
                formatNum = mobileNo.replaceAll("(\\d{3})(\\d{3,4})(\\d{4})", "$1-****-$3");
            }else{
                formatNum = mobileNo.replaceAll("(\\d{3})(\\d{3,4})(\\d{4})", "$1-$2-$3");
            }
        }else if(mobileNo.length()==8){
            formatNum = mobileNo.replaceAll("(\\d{4})(\\d{4})", "$1-$2");
        }else{
            if(mobileNo.indexOf("02")==0){
                if(isMask){
                    formatNum = mobileNo.replaceAll("(\\d{2})(\\d{3,4})(\\d{4})", "$1-****-$3");
                }else{
                    formatNum = mobileNo.replaceAll("(\\d{2})(\\d{3,4})(\\d{4})", "$1-$2-$3");
                }
            }else{
                if(isMask){
                    formatNum = mobileNo.replaceAll("(\\d{3})(\\d{3,4})(\\d{4})", "$1-****-$3");
                }else{
                    formatNum = mobileNo.replaceAll("(\\d{3})(\\d{3,4})(\\d{4})", "$1-$2-$3");
                }
            }
        }
        return formatNum;
    }
}
