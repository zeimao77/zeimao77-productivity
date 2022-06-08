package top.zeimao77.product.dict;

import top.zeimao77.product.exception.BaseServiceRunException;

public enum WeekEnum implements DictEnum {

    MON(1,"Monday","星期一"),
    TUES(2,"Tuesday","星期二"),
    WED(3,"Wednesday","星期三"),
    THUR(4,"Thursday","星期四"),
    FRI(5,"Friday","星期五"),
    SAT(6,"Saturday","星期六"),
    SUN(7,"Sunday","星期日")
    ;

    private int week;
    private String englistName;
    private String chineseName;

    WeekEnum(int week, String englistName, String chineseName) {
        this.week = week;
        this.englistName = englistName;
        this.chineseName = chineseName;
    }

    public int getWeek() {
        return week;
    }

    public String getEnglistName() {
        return englistName;
    }

    public String getChineseName() {
        return chineseName;
    }

    @Override
    public String getKey() {
        return String.valueOf(week);
    }

    @Override
    public String getValue() {
        return chineseName;
    }

    public static WeekEnum getByWeek(int week) {
        for (WeekEnum value : values()) {
            if(value.week == week) {
                return value;
            }
        }
        throw new BaseServiceRunException("错误的星期");
    }

}
