package top.zeimao77.product.util;

import top.zeimao77.product.exception.BaseServiceException;
import top.zeimao77.product.exception.ExceptionCodeDefinition;

import static top.zeimao77.product.exception.ExceptionCodeDefinition.WRONG_SOURCE;

public class BoolUtil {

    private BoolUtil(){}

    public static boolean parseBool(String boolStr,boolean defaultValue) {
        if("auto".equalsIgnoreCase(boolStr)
            || "automatic".equalsIgnoreCase(boolStr)
            || "default".equalsIgnoreCase(boolStr)) {
            return defaultValue;
        }
        try{
            return parseBool(boolStr);
        } catch (BaseServiceException e) {}
        return defaultValue;
    }

    /**
     *
     * @param boolStr Bool字符串
     * @return boolean
     * @throws BaseServiceException 如果解析失败将抛出些异常 真实code查看
     * @see ExceptionCodeDefinition#WRONG_SOURCE
     */
    public static boolean parseBool(String boolStr) throws BaseServiceException {
        if("TRUE".equalsIgnoreCase(boolStr)
                || "Y".equalsIgnoreCase(boolStr)
                || "YES".equalsIgnoreCase(boolStr)
                || "1".equals(boolStr)
                || "ON".equalsIgnoreCase(boolStr)) {
            return true;
        }
        if("FALSE".equalsIgnoreCase(boolStr)
                || "N".equalsIgnoreCase(boolStr)
                || "NO".equalsIgnoreCase(boolStr)
                || "0".equals(boolStr)
                || "OFF".equalsIgnoreCase(boolStr)) {
            return false;
        }
        throw new BaseServiceException(WRONG_SOURCE,"错误的Bool参数："+boolStr);
    }

}
