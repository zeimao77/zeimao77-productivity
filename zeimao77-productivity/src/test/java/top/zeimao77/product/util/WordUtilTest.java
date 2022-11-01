package top.zeimao77.product.util;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.main.BaseMain;

class WordUtilTest extends BaseMain {

    @Test
    void initialCase() {
        logger.info("首字母转大写:{}",WordUtil.initialCase("hello world"));
    }

    @Test
    void initialLow() {
        logger.info("首字母转小写:{}",WordUtil.initialLow("HELLO WORLD"));
    }

    @Test
    void lineToHump() {
        logger.info("下划线转驼峰:{}",WordUtil.lineToHump("hello_world"));
    }

    @Test
    void humpToLine() {
        logger.info("驼峰转下划线:{}",WordUtil.humpToLine("helloWorld"));
    }
}