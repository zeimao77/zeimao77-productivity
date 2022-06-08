package top.zeimao77.product.dict;

import top.zeimao77.product.exception.BaseServiceRunException;

public enum BoolEnum implements DictEnum {

    Y(1,"true","是"),
    N(0,"false","否"),
    ;

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
        throw new BaseServiceRunException("错误的布尔值");
    }

}
