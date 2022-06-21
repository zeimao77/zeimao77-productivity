package top.zeimao77.product.util;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.main.BaseMain;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class AssertUtilTest extends BaseMain {

    @Test
    public void match() {
        LocalDateTime expiryTime = LocalDateTime.now();
        LocalDateTime n = LocalDateTime.MIN;
        logger.info("{}",LocalDateTimeUtil.toDateTime(n));
        long between = ChronoUnit.SECONDS.between(n, expiryTime);
        logger.info("{}",between);
    }

}