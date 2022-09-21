package top.zeimao77.product.util;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.main.BaseMain;

class StringUtilTest extends BaseMain {

    @Test
    void cut() {
        RandomStringUtil randomStringUtil = new RandomStringUtil(0x0F);
        logger.info(randomStringUtil.randomStr(32));
    }

}