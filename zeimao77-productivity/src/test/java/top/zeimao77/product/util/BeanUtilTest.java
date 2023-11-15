package top.zeimao77.product.util;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.main.BaseMain;

import java.util.HashMap;

class BeanUtilTest extends BaseMain {

    @Test
    void getProperty() {
        HashMap<Object, Object> map = new HashMap<>();
<<<<<<< HEAD
        logger.info(BeanUtil.getProperty(map,"abc").toString());
=======
        logger.info("{}",BeanUtil.getProperty(map,"abc"));
>>>>>>> main

    }
}