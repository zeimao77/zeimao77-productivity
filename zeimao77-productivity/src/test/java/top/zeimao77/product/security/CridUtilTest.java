package top.zeimao77.product.security;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.main.BaseMain;

class CridUtilTest extends BaseMain {

    @Test
    void matchesCheckCode() {
        String id = "42052819920802251X";
        logger.info("{}",CridUtil.matchesCheckCode(id));
        logger.info("姓别:{}",CridUtil.getSex(id));
    }
}