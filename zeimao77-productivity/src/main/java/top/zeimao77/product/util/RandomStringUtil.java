package top.zeimao77.product.util;

import java.util.concurrent.ThreadLocalRandom;
import static top.zeimao77.product.exception.ExceptionCodeDefinition.APPERR;

public class RandomStringUtil {

    public static final char[] RANGE_NUMBER = new char[]{0x30,0x31,0x32,0x33,0x34,0x35,0x36,0x37,0x38,0x39};
    public static final char[] RANGE_UPPER = new char[]{0x41,0x42,0x43,0x44,0x45,0x46,0x47,0x48,0x49,0x4a,0x4b,0x4c,0x4d,0x4e,0x4f,0x50,0x51,0x52,0x53,0x54,0x55,0x56,0x57,0x58,0x59,0x5a};
    public static final char[] RANGE_LOWER = new char[]{0x61,0x62,0x63,0x64,0x65,0x66,0x67,0x68,0x69,0x6a,0x6b,0x6c,0x6d,0x6e,0x6f,0x70,0x71,0x72,0x73,0x74,0x75,0x76,0x77,0x78,0x79,0x7a};
    public static final char[] RANGE_PUNCTUATE = new char[]{0x21,0x40,0x23,0x24,0x25,0x5E,0x26,0x2A,0x28,0x29};
    private char[] range;

    public RandomStringUtil(int bmap) {
        AssertUtil.assertTrue(bmap >= 0x01 && bmap <= 0x0F,APPERR,"取值范围:[0x01 ~ 0x0F]");
        this.range = range(bmap);
    }

    public RandomStringUtil(final char[] range) {
        this.range = range;
    }

    /**
     * 生成随机字符串
     * @param len 长度
     * @return
     */
    public String randomStr(int len) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        StringBuilder returnBuilder = new StringBuilder(len);
        for (int j = 0; j < len; j++) {
            int i1 = random.nextInt(this.range.length);
            returnBuilder.append(this.range[i1]);
        }
        return returnBuilder.toString();
    }

    /**
     * 生成范围
     * @param bmap 用于生成范围的标记位图,取值范围:[0x01 ~ 0x0F];
     *  位图    符号    大字   小字    数字
     *          0      0      0      1     =   0x01  数字
     *          0      0      1      0     =   0x02  小写字母
     *          0      1      0      0     =   0x04  大写字母
     *          1      0      0      0     =   0x08  标点符号
     * @return 字符范围枚举数组
     */
    public static final char[] range(int bmap) {
        int l = 0;
        char[] range = null;
        if(bmap == 0x01) {
            return RANGE_NUMBER;
        } else if(bmap == 0x02) {
            return RANGE_LOWER;
        } else if(bmap == 0x04) {
            return RANGE_UPPER;
        } else if(bmap == 0x08) {
            return RANGE_PUNCTUATE;
        }
        l = (bmap & 0x01) > 0 ? l + RANGE_NUMBER.length : l;
        l = (bmap & 0x02) > 0 ? l + RANGE_LOWER.length : l;
        l = (bmap & 0x04) > 0 ? l + RANGE_UPPER.length : l;
        l = (bmap & 0x08) > 0 ? l + RANGE_PUNCTUATE.length : l;
        range = new char[l];
        l = 0;
        if((bmap & 0x01) > 0) {
            for (char c : RANGE_NUMBER)
                range[l++] = c;
        }
        if((bmap & 0x02) > 0) {
            for (char c : RANGE_LOWER)
                range[l++] = c;
        }
        if((bmap & 0x04) > 0) {
            for (char c : RANGE_UPPER)
                range[l++] = c;
        }
        if((bmap & 0x08) > 0) {
            for (char c : RANGE_PUNCTUATE)
                range[l++] = c;
        }
        return range;
    }

}
