package top.zeimao77.product.util;

import top.zeimao77.product.exception.BaseServiceRunException;

import static top.zeimao77.product.exception.ExceptionCodeDefinition.APPERR;

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
    public static void assertTrue(boolean expression,String message) {
       assertTrue(expression,APPERR,message);
    }

    public static void assertTrue(boolean expression,String format,Object...args) {
        assertTrue(expression,APPERR,format,args);
    }

    /**
     *
     * @param expression 断言表达式
     * @param code 错误编码
     * @param message 消息
     */
    public static void assertTrue(boolean expression,Integer code,String message) {
        if(!expression) {
            throw new BaseServiceRunException(code,message);
        }
    }

    /**
     * 使用这个函数可以减少不必要的错误消息字符串拼接
     * @param expression 断言表达式
     * @param code 错误编码
     * @param format 错误消息格式
     * @param args 错误消息参数
     */
    public static void assertTrue(boolean expression,Integer code,String format,Object... args) {
        if(!expression) {
            String message = String.format(format,args);
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
            throw new BaseServiceRunException(APPERR,message);
        }
    }

    /**
     * 断言对象不为null
     * @param obj 对象
     * @param message 异常信息
     */
    public static void notNull(Object obj,String message) {
        if(obj == null) {
            throw new BaseServiceRunException(APPERR,message);
        }
    }

}
