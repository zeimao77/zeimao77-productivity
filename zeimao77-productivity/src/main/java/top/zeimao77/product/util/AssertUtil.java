package top.zeimao77.product.util;

import top.zeimao77.product.exception.BaseServiceRunException;
import static top.zeimao77.product.exception.ExceptionCodeDefinition.CUSTOM;

import java.util.regex.Pattern;

/**
 * 断言工具
 */
public class AssertUtil {

    private AssertUtil(){}

    /**
     * 断言为真
     * @param expression 断言表达式
     * @param message 异常返回的信息
     */
    public static void assertTure(boolean expression,String message) {
       assertTrue(expression,CUSTOM,message);
    }

    public static void assertTrue(boolean expression,Integer code,String message) {
        if(!expression) {
            throw new BaseServiceRunException(code,message);
        }
    }

    /**
     * 判断对象不为null或空字符串
     * @param obj 对象
     * @return 判断结果
     */
    public static boolean isNotEmpty(Object obj) {
        return obj != null && !"".equals(obj.toString());
    }

    /**
     * 校验通配 支持?表示一个字符，*表示任意多个字符
     * @param str 匹配的字符串
     * @param pattern  通配字符串  例如：a?c
     * @return 是否匹配成功
     */
    public static boolean match(String str,String pattern){
        String patternstr = null;
        patternstr = pattern.replaceAll("\\?",".");
        patternstr = patternstr.replaceAll("\\*",".*");
        Pattern p = Pattern.compile(patternstr);
        return p.matcher(str).matches();
    }

    /**
     * 判断对象为null 或者 空字符串
     * @param obj 对象
     * @return 判断结果
     */
    public static boolean isEmpty(Object obj) {
        return obj == null || "".equals(obj.toString());
    }

    /**
     * 判断对象为空或空字串 去除头尾空字符
     * @param obj 对象
     * @return 判断结果
     */
    public static boolean isBlack(CharSequence obj) {
        return obj == null || "".equals(obj.toString().trim());
    }

    /**
     * 断言对象不为null 或者 空字符串
     * @param obj 对象
     * @param message 异常信息
     */
    public static void notEmpty(Object obj,String message) {
        if(isEmpty(obj)) {
            throw new BaseServiceRunException(message);
        }
    }

    /**
     * 断言对象不为null
     * @param obj 对象
     * @param message 异常信息
     */
    public static void notNull(Object obj,String message) {
        if(obj == null) {
            throw new BaseServiceRunException(message);
        }
    }

}
