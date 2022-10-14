package top.zeimao77.product.dict;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.zeimao77.product.exception.BaseServiceRunException;
import static top.zeimao77.product.exception.ExceptionCodeDefinition.APPERR;
import static top.zeimao77.product.exception.ExceptionCodeDefinition.WRONG_SOURCE;

public enum BoolEnum implements DictEnum {

    Y(1,"true","是"),
    N(0,"false","否"),
    ;

    private static Logger logger = LogManager.getLogger(BoolEnum.class);

    private int bool;
    private String englistName;
    private String chineseName;

    BoolEnum(int bool, String englistName, String chineseName) {
        this.bool = bool;
        this.englistName = englistName;
        this.chineseName = chineseName;
    }

    /**
     * @return 数值类型
     */
    public int getBool() {
        return bool;
    }

    /**
     * @return 英文说明
     */
    public String getEnglistName() {
        return englistName;
    }

    /**
     * @return 中文说明
     */
    public String getChineseName() {
        return chineseName;
    }

    @Override
    public String getKey() {
        return String.valueOf(bool);
    }

    @Override
    public String getValue() {
        return chineseName;
    }

    /**
     * 通过数值解析成枚举
     * @param bool 数值
     * @return 枚举结果
     */
    public static BoolEnum getByBool(int bool) {
        for (BoolEnum value : values()) {
            if(value.bool == bool) {
                return value;
            }
        }
        throw new BaseServiceRunException(APPERR,"错误的布尔值");
    }

    public static boolean parseBool(String boolStr) {
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
        throw new BaseServiceRunException(WRONG_SOURCE,"错误的Bool参数："+boolStr);
    }

}
