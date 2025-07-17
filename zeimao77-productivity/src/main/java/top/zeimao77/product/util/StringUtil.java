package top.zeimao77.product.util;

import java.util.regex.Pattern;

/**
 * 字符串工具
 */
public class StringUtil {

    public static final String EMPTY_SUFFIX = "";
    public static final String DEFAULT_SUFFIX = "...";

    public static final Pattern TRIMSPACE_PATTERN = Pattern.compile("^[ \u3000]+|[ \u3000]+$");

    private StringUtil() {}

    /**
     * 截取字符串
     * @param str 原字符中
     * @param len 截取后的长度
     * @param suffix 截取后的后缀
     * @see StringUtil#DEFAULT_SUFFIX 缺省值
     * @return 截取后的字符串
     */
    public static String cut(String str,int len,String suffix) {
        if(str == null)
            return null;
        if(str.length() <= len)
            return str;
        if(suffix == null)
            suffix = EMPTY_SUFFIX;
        len = len >= str.length() ? str.length() : len - suffix.length();
        String s = str.substring(0, len);
        if (len < str.length()) {
            s = s.concat(suffix);
        }
        return s;
    }

    public static String cut(String str,int len) {
        return cut(str,len,DEFAULT_SUFFIX);
    }

    public static String subString(String str,int start,int end) {
        if(str == null)
            return null;
        if(start < 0)
            start = 0;
        if(end > str.length())
            end = str.length();
        if(start > end)
            return "";
        return str.substring(start,end);
    }

    public static String rightSubString(String str,int len) {
        if(str == null)
            return null;
        if (len >= str.length())
            return str;
        return str.substring(str.length() - len);
    }

    public static String printNullableObject(Object o) {
        return o == null ? "null" : o.toString();
    }

    public static String printNullableObject(Object o,String defaultValue) {
        return o == null ? defaultValue : o.toString();
    }

    public static String trimSpaces(String str) {
        if(str == null) return str;
        if(str.startsWith(" ") || str.startsWith("\u3000") || str.endsWith(" ") || str.endsWith("\u3000"))
            return TRIMSPACE_PATTERN.matcher(str).replaceAll("");
        return str;
    }

    public static int compare(String str1,String str2) {
        if(str1 != null && str2 != null) return str1.compareTo(str2);
        if(str1 == null && str2 != null) return -1;
        if(str1 != null && str2 == null) return 1;
        return 0;
    }

    public static String removeLeadingZeros(String str) {
        if(str == null)
            return null;
        int i = 0;
        while(i < str.length() && str.charAt(i) == '0') {
            i++;
        }
        return str.substring(i);
    }

}
