package top.zeimao77.product.dict;

import top.zeimao77.product.main.BaseMain;

class WeekEnumTest extends BaseMain {

    public static void main(String[] args) {
        logger.info(WeekEnum.getByWeek(1).getChineseName());
        logger.info(MonthEnum.getByMonth(1).getChineseName());
        logger.info(BoolEnum.getByBool(1).getChineseName());
    }

}