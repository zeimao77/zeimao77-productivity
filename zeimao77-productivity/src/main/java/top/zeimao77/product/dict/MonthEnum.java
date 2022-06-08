package top.zeimao77.product.dict;

import top.zeimao77.product.exception.BaseServiceRunException;

public enum MonthEnum implements DictEnum {

    JAN(1,"January","一月"),
    FEB(2,"February","二月"),
    MAR(3,"March","三月"),
    APR(4,"April","四月"),
    MAY(5,"May","五月"),
    JUN(6,"June","六月"),
    JUL(7,"July","七月"),
    AUG(8,"August","八月"),
    SPET(9,"September","九月"),
    OCT(10,"October","十月"),
    NOV(11,"November","十一月"),
    DEC(12,"December","十二月")
    ;

    private int month;
    private String englistName;
    private String chineseName;

    MonthEnum(int month, String englistName, String chineseName) {
        this.month = month;
        this.englistName = englistName;
        this.chineseName = chineseName;
    }

    public int getMonth() {
        return month;
    }

    public String getEnglistName() {
        return englistName;
    }

    public String getChineseName() {
        return chineseName;
    }

    @Override
    public String getKey() {
        return String.valueOf(month);
    }

    @Override
    public String getValue() {
        return chineseName;
    }

    public static MonthEnum getByMonth(int month) {
        for (MonthEnum value : values()) {
            if(value.month == month) {
                return value;
            }
        }
        throw new BaseServiceRunException("错误的月份");
    }

}
