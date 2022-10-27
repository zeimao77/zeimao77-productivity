package top.zeimao77.product.util;

import java.math.RoundingMode;
import java.text.NumberFormat;

public class NumberUtil {

    NumberFormat numberFormat;

    /**
     * @param fractionDigits 小数位数;
     * @param roundingMode 舍入模式
     * @see RoundingMode#UP 正数向上取 负数向下取;
     * @see RoundingMode#DOWN 正数向下取 负数向上取;
     * @see RoundingMode#CEILING 向上取;
     * @see RoundingMode#FLOOR 向下取;
     * @see RoundingMode#HALF_UP 四舍五入;
     * @see RoundingMode#HALF_DOWN 五舍六入;
     * @see RoundingMode#HALF_EVEN 整数部分为奇数 则四舍六入 整数部分为偶数 则五舍六入;
     * @see RoundingMode#UNNECESSARY 不能丢失精度的格式化小数;
     */
    public NumberUtil(int fractionDigits,RoundingMode roundingMode) {
        this.numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(fractionDigits);
        numberFormat.setMinimumFractionDigits(fractionDigits);
        numberFormat.setRoundingMode(roundingMode);
    }

    public NumberUtil(NumberFormat numberFormat) {
        this.numberFormat = numberFormat;
    }

    public String format(Number number) {
        return this.numberFormat.format(number);
    }

    public NumberFormat getNumberFormat() {
        return numberFormat;
    }

    public static String format(Number number, int fractionDigits, RoundingMode roundingMode) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(fractionDigits);
        numberFormat.setMinimumFractionDigits(fractionDigits);
        numberFormat.setRoundingMode(roundingMode);
        return numberFormat.format(number);
    }


}
