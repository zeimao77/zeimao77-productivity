package top.zeimao77.product.util;

import org.apache.logging.log4j.Logger;

import org.junit.jupiter.api.Test;
public class AssertUtilTest2{

    private static Logger logger;

    static  {
    }

    @Test
    public void match() {
        String k = "01";
        boolean matches = k.matches("\\d+");
        RandomStringUtil randomStringUtil = new RandomStringUtil(0x0F);
        for (int i = 0; i < 10000; i++) {
            logger.info(randomStringUtil.randomStr(64));
        }
    }

}