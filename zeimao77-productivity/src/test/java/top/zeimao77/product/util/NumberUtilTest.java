package top.zeimao77.product.util;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.main.BaseMain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class NumberUtilTest extends BaseMain {

    @Test
    void decimalStr() {
        showBanner();
        NumberUtil numberUtil = new NumberUtil(2, RoundingMode.FLOOR);
        Consumer<Number> con = o -> {
            logger.info("{}取舍之后的结果是:{}",o,numberUtil.format(o));
        };
        con.accept(new BigDecimal("1.1111"));
        con.accept(-1.1111D);
        con.accept(1.1161D);
        con.accept(-1);

    }
}