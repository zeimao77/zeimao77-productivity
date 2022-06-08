package top.zeimao77.product.util;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.main.BaseMain;

class StringUtilTest extends BaseMain {

    @Test
    void cut() {
        String str = "hello world!!!!";
        String cut = StringUtil.cut(str, 10, StringUtil.DEFAULT_SUFFIX);
        logger.info(cut);
    }

}