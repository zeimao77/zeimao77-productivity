package top.zeimao77.product.util;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.main.BaseMain;

import java.util.HashMap;

class BeanUtilTest extends BaseMain {

    @Test
    void getProperty() {
        HashMap<Object, Object> map = new HashMap<>();
        logger.info("{}",BeanUtil.getProperty(map,"abc"));

    }
}