package top.zeimao77.product.util;

/**
 * 字符串工具
 */
public class StringUtil {

    public static final String EMPTY_SUFFIX = "";
    public static final String DEFAULT_SUFFIX = "...";

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

    public static String printNullableObject(Object o) {
        return o == null ? "null" : o.toString();
    }

    public static String printNullableObject(Object o,String defaultValue) {
        return o == null ? defaultValue : o.toString();
    }

}
