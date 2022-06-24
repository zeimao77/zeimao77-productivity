package top.zeimao77.product.security;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.main.BaseMain;

import static org.junit.jupiter.api.Assertions.*;

class DesensitizationUtilTest extends BaseMain {

    @Test
    void identityCard() {

        String s = DesensitizationUtil.identityCard("42052819920802251X");
        logger.info("{}",s);

    }
}