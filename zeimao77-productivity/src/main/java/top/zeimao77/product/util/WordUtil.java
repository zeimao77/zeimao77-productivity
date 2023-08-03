package top.zeimao77.product.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordUtil {

    private static final Pattern humpPattern = Pattern.compile("[A-Z]");
    private static final Pattern linePattern = Pattern.compile("_(\\w)");

    private WordUtil(){}

    /**
     * 首字母转大写
     * @param str 串
     * @return 结果
     */
    public static String initialCase(String str) {
        char[] cs=str.toCharArray();
        if(cs[0] >= 0x61 && cs[0] <= 0x7A) {
            cs[0]-=0x20;
        }
        return String.valueOf(cs);
    }

    /**
     * 首字母转小写
     * @param str 串
     * @return 结果
     */
    public static String initialLow(String str) {
        char[] cs=str.toCharArray();
        if(cs[0] >= 0x41 && cs[0] <= 0x5A) {
            cs[0]+=0x20;
        }
        return String.valueOf(cs);
    }


    /**
     * 下划线转驼峰
     * @param str 串
     * @return 结果
     */
    public static String lineToHump(String str) {
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }


    /**
     * 驼峰转下划线
     * @param str 串
     * @return 结果
     */
    public static String humpToLine(String str) {
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        String s = sb.toString();
        if(s.startsWith("_")) {
            s = s.substring(1);
        }
        return s;
    }

}
