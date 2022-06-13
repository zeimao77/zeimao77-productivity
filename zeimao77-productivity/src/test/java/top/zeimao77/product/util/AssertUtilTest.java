package top.zeimao77.product.util;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.main.BaseMain;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class AssertUtilTest extends BaseMain {

    @Test
    public void match() {
        logger.info(String.class.isAssignableFrom(CharSequence.class));
        logger.info(CharSequence.class.isAssignableFrom(String.class));
        logger.info(LocalDateTime.ofInstant(Instant.ofEpochMilli(1654756287153L),ZoneId.systemDefault()));
    }

}