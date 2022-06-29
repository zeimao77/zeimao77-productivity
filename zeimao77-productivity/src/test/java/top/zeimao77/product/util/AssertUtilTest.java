package top.zeimao77.product.util;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.main.BaseMain;

public class AssertUtilTest extends BaseMain {

    @Test
    public void match() {
        String k = "01";
        boolean matches = k.matches("\\d+");
        RandomStringUtil randomStringUtil = new RandomStringUtil(0x0F);
        for (int i = 0; i < 100; i++) {
            logger.info(randomStringUtil.randomStr(64));

            delay_ms(2);
        }
    }

}