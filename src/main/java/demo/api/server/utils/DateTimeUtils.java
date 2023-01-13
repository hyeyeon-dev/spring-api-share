package demo.api.server.utils;

import org.apache.commons.lang3.ObjectUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class DateTimeUtils {
    /**
     * LocalDate를 문자열로 변경 시, ".0"이 추가로 붙는 것 방지
     * @author hanhyeyeon
     */
    public static String localDateToStringWithoutComma(LocalDate date) {
        return (ObjectUtils.isNotEmpty(date))? date.toString().replaceAll("\\.\\d+", "") : null;
    }

    /**
    * 날짜 비교
    * @author hanhyeyeon
    * @param  startDate : 시작일자
    * @param  endDate   : 종료일자
    * @param  searchDate: 포함여부를 확인할 날짜(DB의 Date)
    * */
    public static boolean isContainsDate(String startDate, String endDate, Date searchDate) {
        boolean result = false;

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String format = formatter.format(searchDate);

        int startDt = Integer.parseInt(RegexUtils.regexOnlyNumber(startDate));
        int endDt = Integer.parseInt(RegexUtils.regexOnlyNumber(endDate));
        int searchDt = Integer.parseInt(RegexUtils.regexOnlyNumber(format));

        if (startDt <= searchDt && searchDt <= endDt) {
            result = true;
        }

        return result;
    }

    /**
     * 날짜 범위 검사
     * @author kimyounwoong
     * @param  startDate : 시작일자
     * @param  endDate   : 종료일자
     * @param  searchDate: 검사할 날짜
     * */
    public static boolean isContainsDate(Date startDate, Date endDate, Date searchDate) {
        return startDate.before(searchDate) && endDate.after(searchDate);
    }

    /**
     * HashMap String 으로 되어 있는 날짜형식을 LocalDateTime 형태로 변환
     * @author kimyounwoong
     * @param dateTime 날짜형식 (2022-07-19 16:50:00)
     * @return LocalDateTime
     */
    public static LocalDateTime mapToLocalDateTime(String dateTime) {
        if(dateTime == null || dateTime.equals("")) return null;

        LocalDateTime tm;
        if(dateTime.contains("T")) {
            tm = LocalDateTime.parse(dateTime);
        } else {
            List<String> list = Arrays.asList(dateTime.split("\\."));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            tm = LocalDateTime.parse(list.get(0), formatter);
        }

        return tm;
    }

    /**
     * 일자 패턴 변경
     * @param strDate 000607, 19800607, 1980-06-07, 000607 10:00:00, 20010607 10:00:00
     * @param pattern yyyy-MM-dd HH:mm:ss
     * @return String
     */
    public static String outStrDateOfPattern(String strDate, String pattern) {
        String result = "";

        String strDateFormat = "yyMMdd";
        String outDateFormat = ObjectUtils.isEmpty(pattern)?"yyyy-MM-dd HH:mm:ss":pattern;

        if (strDate.length() == 6) {
            strDateFormat = "yyMMdd";
        }
        else if (strDate.length() == 8) {
            strDateFormat = "yyyyMMdd";
        }
        else if (strDate.length() == 10 && strDate.contains("-")) {
            strDateFormat = "yyyy-MM-dd";
        }
        else if (strDate.length() == 15 && strDate.contains(" ") && strDate.contains(":")) {
            strDateFormat = "yyMMdd HH:mm:ss";
        }
        else if (strDate.length() == 17 && strDate.contains(" ") && strDate.contains(":")) {
            strDateFormat = "yyyyMMdd HH:mm:ss";
        }

        SimpleDateFormat inSdf = new SimpleDateFormat(strDateFormat);
        SimpleDateFormat outSdf = new SimpleDateFormat(outDateFormat);

        try {
            Date date = inSdf.parse(strDate);
            result = outSdf.format(date);

        } catch (ParseException pe) {
            LogUtils.logStackTrace(pe);

        }

        return result;
    }

    /**
     * 요일 한글로 변환
     * @param localDate 2022-08-19
     * @return 요일 한글명
     */
    public static String getWeekStrToKr(LocalDate localDate) {
        try {
            switch (localDate.getDayOfWeek().toString()) {
                case "SUNDAY":
                    return "일";
                case "MONDAY":
                    return "월";
                case "TUESDAY":
                    return "화";
                case "WEDNESDAY":
                    return "수";
                case "THURSDAY":
                    return "목";
                case "FRIDAY":
                    return "금";
                case "SATURDAY":
                    return "토";
                default:
                    return null;
            }
        } catch (Exception e) {
            LogUtils.logStackTrace(e);
            return null;
        }
    }

    /**
     * 일자 패턴 변경
     * @param strDate 000607, 19800607, 1980-06-07, 000607 10:00:00, 20010607 10:00:00
     * @return LocalDate
     */
    public static LocalDate outDateOfPattern(String strDate) {
        if (ObjectUtils.isEmpty(strDate)) return null;

        String strDateFormat = "yyMMdd";

        if (strDate.length() == 6) {
            strDateFormat = "yyMMdd";
        }
        else if (strDate.length() == 8) {
            strDateFormat = "yyyyMMdd";
        }
        else if (strDate.length() == 10 && strDate.contains("-")) {
            strDateFormat = "yyyy-MM-dd";
        }
        else if (strDate.length() == 15 && strDate.contains(" ") && strDate.contains(":")) {
            strDateFormat = "yyMMdd HH:mm:ss";
        }
        else if (strDate.length() == 17 && strDate.contains(" ") && strDate.contains(":")) {
            strDateFormat = "yyyyMMdd HH:mm:ss";
        }
        else if (strDate.length() == 19 && strDate.contains(" ") && strDate.contains(":")) {
            strDateFormat = "yyyy-MM-dd HH:mm:ss";
        }

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(strDateFormat);
        LocalDate result = LocalDate.parse(strDate, dateTimeFormatter);

        return result;
    }

    public static String getCurrentDate() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    public static String getCurrentTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    public static boolean setTimeForLocalDateTime (LocalDateTime salesStartTm, int hour, int min, int sec) {
        if (salesStartTm == null) {
            return false;
        }
        salesStartTm.withHour(hour);
        salesStartTm.withMinute(min);
        salesStartTm.withSecond(sec);
        return true;
    }

}
