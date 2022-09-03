package top.zeimao77.product.util;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.main.BaseMain;

class JsonBeanUtilTest extends BaseMain{

    @Test
    void read() {
        String json = "{\"abc\":{\"bbc\":123},\"code\":0}";
        String abc = JsonBeanUtil.DEFAULT.read(json, "code");
        logger.info("read:{}",abc);


    }
}