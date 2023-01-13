package demo.api.server.utils;

import org.apache.commons.lang3.ObjectUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateUtils {
    /**
     * 유효성검사 : 휴대폰 번호
     * 1) 숫자값이 없음 불가
     * 2) 숫자값의 길이가 10~11자
     * 3) 문자열에 공백 포함 불가
     * @Author hanhyeyeon
     */
    public static boolean isMobileNo(String mobileNo) {
        boolean result = true;

        if (mobileNo.contains(" ")) {
            result = false;

        } else {
            String mNo = RegexUtils.regexOnlyNumber(mobileNo);
            if (ObjectUtils.isEmpty(mNo) || mNo.length() == 0) {
                result = false;

            } else {
                Pattern pattern = Pattern.compile("^\\d{3}\\d{3,4}\\d{4}$");
                Matcher matcher = pattern.matcher(mNo);
                if (!matcher.matches()) {
                    result = false;
                }
            }
        }

        return result;
    }

    /**
     * 유효성검사 : 전화번호
     * 1) 숫자값이 없음 불가
     * 2) 숫자값의 길이가 8자 혹은 10~11자
     * 3) 문자열에 공백 포함 불가
     * @Author hanhyeyeon
     */
    public static boolean isTelNo(String mobileNo) {
        boolean result = true;

        if (mobileNo.contains(" ")) {
            result = false;

        } else {
            String mNo = RegexUtils.regexOnlyNumber(mobileNo);
            if (ObjectUtils.isEmpty(mNo) || mNo.length() == 0) {
                result = false;

            } else {
                Pattern pattern1 = Pattern.compile("^\\d{3}\\d{3,4}\\d{4}$");
                Matcher matcher1 = pattern1.matcher(mNo);

                Pattern pattern2 = Pattern.compile("^\\d{4}\\d{4}$");
                Matcher matcher2 = pattern2.matcher(mNo);

                if (!matcher1.matches() && !matcher2.matches()) {
                    result = false;
                }
            }
        }

        return result;
    }

    /**
     * 유효성검사 : 회원명
     * 1) 값이 비어있거나 공백 불가
     * 2) 문자열 앞/뒤에 공백 불가
     * 3) 10자 이내
     * @Author hanhyeyeon
     */
    public static boolean isMemName(String memName) {
        boolean result = true;

        if (ObjectUtils.isEmpty(memName) || RegexUtils.trim(memName).length() == 0) {
            result = false;

        } else if (memName.length() != RegexUtils.trim(memName).length()) {
            result = false;

        } else if (RegexUtils.trim(memName).length() > 10) {
            result = false;

        }

        return result;
    }

    /**
     * 유효성검사 : 아이디
     * 1) 값이 비어있거나 공백 불가
     * 2) 문자열에 공백 포함 불가
     * 3) 5~20자 영문, 숫자 조합 (영문으로 시작해야함)
     * @Author hanhyeyeon
     */
    public static boolean isMemId(String memId) {
        boolean result = true;

        if (ObjectUtils.isEmpty(memId) || RegexUtils.trim(memId).length() == 0) {
            result = false;

        } else if (memId.contains(" ")) {
            result = false;

        } else {
//            Pattern pattern = Pattern.compile("^[a-z]+[a-z0-9]{5,20}$");
//            Matcher matcher = pattern.matcher(memId);
//            if (!matcher.matches()) {
//                result = false;
//            }
        }

        return result;
    }

    /**
     * 유효성검사 : 비밀번호
     * 1) 값이 비어있거나 공백 불가
     * 2) 문자열에 공백 포함 불가
     * 3) 10~16자 영문, 숫자, 특수문자 조합
     * @Author hanhyeyeon
     */
    public static boolean isPwd(String pwd) {
        boolean result = true;

        if (ObjectUtils.isEmpty(pwd) || RegexUtils.trim(pwd).length() == 0) {
            result = false;

        } else if (pwd.contains(" ")) {
            result = false;

        } else if (pwd.length()<10 || pwd.length()>16) {
            // 비밀번호 제약조건 : 10~16자
            result = false;

        } else {
//            // 비밀번호 제약조건 : 영문, 숫자, 특수문자 포함
//            Pattern pattern = Pattern.compile(".*[a-zA-Z].*");
//            Matcher matcher = pattern.matcher(pwd);
//            if (!matcher.matches()) result = false;
//
//            pattern = Pattern.compile(".*\\d.*");
//            matcher = pattern.matcher(pwd);
//            if (!matcher.matches()) result = false;
//
//            pattern = Pattern.compile(".*\\W.*");
//            matcher = pattern.matcher(pwd);
//            if (!matcher.matches()) result = false;
        }
        return result;
    }

    /**
     * 유효성검사 : 이메일주소
     * 1) 값이 비어있거나 공백 불가
     * 2) 문자열에 공백 포함 불가
     * 3) xxx@xxx.xxx
     * @Author hanhyeyeon
     */
    public static boolean isEmail(String email) {
        boolean result = true;

        if (ObjectUtils.isEmpty(email) || RegexUtils.trim(email).length() == 0) {
            result = false;

        } else if (email.contains(" ")) {
            result = false;

        } else {
            Pattern pattern = Pattern.compile("^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$");
            Matcher matcher = pattern.matcher(email);
            if (!matcher.matches()) {
                result = false;
            }
        }

        return result;
    }

    /**
     * 유효성검사 : 날짜
     * 1) 숫자값이 없음 불가
     * 2) 숫자값의 길이가 8자
     * 3) 문자열에 공백 포함 불가
     * @Author hanhyeyeon
     */
    public static boolean isDate(String birthDay) {
        boolean result = true;

        if (birthDay.contains(" ")) {
            result = false;

        } else {
            String mNo = RegexUtils.regexOnlyNumber(birthDay);
            if (ObjectUtils.isEmpty(mNo) || mNo.length() != 8) {
                result = false;

            }
        }

        return result;
    }

    /**
     * 유효성검사 : 시분초
     * 1) 숫자값이 없음 불가
     * 2) 숫자값의 길이가 6자
     * 3) 문자열에 공백 포함 불가
     * @Author hanhyeyeon
     */
    public static boolean isTime(String time) {
        boolean result = true;

        if (time.contains(" ")) {
            result = false;

        } else {
            String mNo = RegexUtils.regexOnlyNumber(time);
            if (ObjectUtils.isEmpty(mNo) || mNo.length() != 6) {
                result = false;
            }
        }

        return result;
    }

    /**
     * 우편번호 검사
     * @param zipCode 5자리 숫자값 문자열
     * @return boolean
     */
    public static boolean isZipCode(String zipCode) {
        if (ObjectUtils.isEmpty(zipCode) || RegexUtils.trim(zipCode).length() == 0) {
            return false;
        }

        Pattern pattern = Pattern.compile("^[0-9]{5}+$");
        Matcher matcher = pattern.matcher(zipCode);
        return matcher.matches();
    }

    /**
     * 태그가 포함 여부 검사
     * @param str 태그형태 문자열
     * @return boolean
     */
    public static boolean isTag(String str) {
        if(str.matches("(.*)<[^>]+>(.*)")) {
            return true;
        }
        return false;
    }

    /**
     * 유효성검사 : 회원여부
     * 1) 0이상만 허용
     * @Author hanhyeyeon
     */
    public static boolean isMem(long memNo) {
        return memNo>0;
    }

    /**
     * 유효성검사 : Y/N여부
     * 1) 대문자만 허용
     * @Author hanhyeyeon
     */
    public static boolean isYn(String yn) {
        boolean result = true;
        if (!yn.equals("Y") && !yn.equals("N")) result = false;

        return result;
    }

    /**
     * 유효성검사 : 관리자 아이디
     * 1) 값이 비어있거나 공백 불가
     * 2) 문자열에 공백 포함 불가
     * @Author hanhyeyeon
     */
    public static boolean isAdmId(String admId) {
        boolean result = true;

        if (ObjectUtils.isEmpty(admId) || RegexUtils.trim(admId).length() == 0) {
            result = false;

        } else if (admId.contains(" ")) {
            result = false;

        }

        return result;
    }

    /**
     * 유효성검사 : 성별
     * 1) 값이 비어있거나 공백 불가
     * 2) 'W' or 'M'
     * @Author hanhyeyeon
     */
    public static boolean isGender(String gender) {
        boolean result = true;

        if (ObjectUtils.isEmpty(gender) || RegexUtils.trim(gender).length() == 0) {
            result = false;

        } else if (!gender.equals("W") && !gender.equals("M")) {
            result = false;

        }

        return result;
    }
    
}