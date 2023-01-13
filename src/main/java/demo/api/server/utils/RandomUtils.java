package demo.api.server.utils;

import java.util.Random;

public class RandomUtils {
    /**
     * 임의의 영문/숫자 혼합값을 자리수(digits)만큼 추출
     * @History 2022.06 영어 대문자/숫자 혼합 가능 (소문자까지 혼합이 필요한 경우 하단 주석 해제)
     * @Author hanhyeyeon
     */
    public static String randomMixedString(int digits) {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i=0; i < digits; i++) {
            if (random.nextBoolean()) {
                sb.append((char)(random.nextInt(26) + 65));
            } else {
                sb.append((random.nextInt(10)));
            }

//            int index = random.nextInt(3);
//
//            switch (index) {
//                case 0:
//                    sb.append((char)(random.nextInt(26) + 97));
//                    break;
//                case 1:
//                    sb.append((char)(random.nextInt(26) + 65));
//                    break;
//                case 2:
//                    sb.append((random.nextInt(10)));
//                    break;
//            }
        }
        return sb.toString();
    }

    /**
     * 임의의 숫자값을 자리수(digits)만큼 추출
     * @Author hanhyeyeon
     */
    public static String randomNumber(int digits) {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i=0; i < digits; i++) {
            sb.append((random.nextInt(10)));
        }

        return sb.toString();
    }
}
