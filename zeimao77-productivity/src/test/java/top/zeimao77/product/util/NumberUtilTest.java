package top.zeimao77.product.util;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.main.BaseMain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class NumberUtilTest extends BaseMain {

    @Test
    void decimalStr() {
        showBanner("0.0.1");
        NumberUtil numberUtil = new NumberUtil(NumberUtil.CURRENCY,2, RoundingMode.FLOOR);
        Consumer<Number> con = o -> {
            logger.info("{}取舍之后的结果是:{}",o,numberUtil.format(o));
        };
        con.accept(new BigDecimal("1.1111"));
        con.accept(-1.1111D);
        con.accept(1.1161D);
        con.accept(-1);

    }

    @Test
    void parse() {
        NumberUtil numberUtil = new NumberUtil(NumberUtil.DECIMAL,2, RoundingMode.FLOOR);
        logger.info("{}",numberUtil.parse("20.067889").longValue());
    }

    @Test
    void percent() {
        String percent = NumberUtil.percent(0.32459, 2, RoundingMode.FLOOR);
        logger.info(percent);
    }
}