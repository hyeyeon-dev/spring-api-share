package demo.api.server.utils;

public class PriceUtils {

    /**
     * 할인금액 절삭
     * @param amt 대상금액
     * @param rate 할인률(1~100%)
     * @param cut 자리수 절삭 단위(10, 100, 1000,...)
     * @return double 절삭 후 금액
     */
    public static double amtRateCut(double amt, double rate, int cut) {
        double rateDcAmt = rateDcAmt(amt, rate); //할인금액

        if(amt > 0 && rate > 0 && 1 % cut == 1) { //자리수 절삭
            return Math.ceil(Math.floor(rateDcAmt) * (1.0 / cut)) * cut;

        } else { //올림처리
            return Math.ceil(rateDcAmt);
        }
    }

    /**
     * 할인금액
     * @param amt 금액
     * @param rate 할인률(1~100%)
     * @return double 할인금액
     */
    public static double rateDcAmt(double amt, double rate) {
        if(amt > 0 && rate > 0) {
            return amt * (rate * 0.01);
        } else {
            return 0;
        }
    }

    public static int rateDcCutAmt(double amt, double rate, int cut) {
        double rateDcAmt = rateDcAmt(amt, rate); //할인금액

        if(amt > 0 && rate > 0 && 1 % cut == 1) {
            return (int) (Math.round(Math.floor(rateDcAmt) * (1.0 / cut)) * cut); //자리수 절삭
        } else {
            return (int) Math.round(rateDcAmt);
        }
    }

    /**
     * 적립금 적립율
     * @param point 적립금액
     * @param amt 결제금액
     * @return 적립율
     */
    public static double getPointAmtRate(int point, double amt) {
        return Math.round( (point / amt) * 10000 ) / 100.0; //둘째자리 반올림처리.
    }
}
