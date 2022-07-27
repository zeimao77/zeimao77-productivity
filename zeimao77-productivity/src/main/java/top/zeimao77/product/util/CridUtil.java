package top.zeimao77.product.util;

import top.zeimao77.product.util.LocalDateTimeUtil;

import java.time.LocalDate;

public class CridUtil {

    public static final int[] SCORE = {7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2};
    public static final char[] CODE = {'1','0','X','9','8','7','6','5','4','3','2'};

    public static boolean matchesCheckCode(String id) {
        int s = 0;
        for (int i = 0; i < 17; i++) {
            int numericValue = Character.getNumericValue(id.charAt(i));
            s += numericValue * SCORE[i];
        }
        s %= 11;
        char cc = CODE[s];
        return cc == id.charAt(17);
    }

    public static String getRegion(String id) {
        return id.substring(0,6);
    }

    public static LocalDate getBirthDay(String id) {
        String substring = id.substring(6, 14);
        return LocalDate.parse(substring, LocalDateTimeUtil.NUMBERDATEFORMATTER);
    }

    /**
     * @param id 身份证号
     * @return 1 男; 0 女
     */
    public static int getSex(String id) {
        return Character.getNumericValue(id.charAt(16)) % 2 == 1 ? 1 : 0;
    }


}
