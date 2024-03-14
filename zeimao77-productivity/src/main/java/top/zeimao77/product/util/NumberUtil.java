package top.zeimao77.product.util;

import top.zeimao77.product.exception.BaseServiceRunException;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import static top.zeimao77.product.exception.ExceptionCodeDefinition.WRONG_SOURCE;

public class NumberUtil {

    NumberFormat numberFormat;

    public static final char DECIMAL = 0x01;
    public static final char PERCENT = 0x02;
    public static final char CURRENCY = 0x03;

    /**
     * @param type
     * @see NumberUtil#DECIMAL 小数 缺省
     * @see NumberUtil#PERCENT 百分比
     * @see NumberUtil#CURRENCY 货币
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
    public NumberUtil(final char type,final boolean groupingUsed,final int fractionDigits,final RoundingMode roundingMode) {
        switch (type) {
            case PERCENT:
                this.numberFormat = NumberFormat.getPercentInstance();
                break;
            case CURRENCY:
                this.numberFormat = NumberFormat.getCurrencyInstance();
                break;
            default:
                this.numberFormat = NumberFormat.getInstance();
                break;
        }
        numberFormat.setMaximumFractionDigits(fractionDigits);
        numberFormat.setMinimumFractionDigits(fractionDigits);
        numberFormat.setGroupingUsed(groupingUsed);
        numberFormat.setRoundingMode(roundingMode);
    }

    public NumberUtil(NumberFormat numberFormat) {
        this.numberFormat = numberFormat;
    }

    public String format(Number number) {
        return this.numberFormat.format(number);
    }

    public static String percent(Number number,int fractionDigits, RoundingMode roundingMode) {
        NumberFormat numberFormat = NumberFormat.getPercentInstance();
        numberFormat.setMaximumFractionDigits(fractionDigits);
        numberFormat.setMinimumIntegerDigits(fractionDigits);
        numberFormat.setRoundingMode(roundingMode);
        numberFormat.setGroupingUsed(false);
        return numberFormat.format(number);
    }


    public Number parse(String source) {
        try {
            return this.numberFormat.parse(source);
        } catch (ParseException e) {
            throw new BaseServiceRunException(WRONG_SOURCE,"解析数值错误："+source,e);
        }
    }

    public NumberFormat getNumberFormat() {
        return numberFormat;
    }

    public static String format(Number number, int fractionDigits, RoundingMode roundingMode) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(fractionDigits);
        numberFormat.setMinimumFractionDigits(fractionDigits);
        numberFormat.setRoundingMode(roundingMode);
        numberFormat.setGroupingUsed(false);
        return numberFormat.format(number);
    }

    public void setGroupingUsed(boolean newValue) {
        this.numberFormat.setGroupingUsed(newValue);
    }

}
