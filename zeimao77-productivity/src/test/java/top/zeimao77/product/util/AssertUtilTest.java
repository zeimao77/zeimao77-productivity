package top.zeimao77.product.util;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.main.BaseMain;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.regex.Pattern;

public class AssertUtilTest extends BaseMain {

    @Test
    public void match() {
        String k = "01";
        boolean matches = k.matches("\\d+");
        if(matches) {
            logger.info("YES");
        } else {
            logger.info("NO");
        }
    }

}