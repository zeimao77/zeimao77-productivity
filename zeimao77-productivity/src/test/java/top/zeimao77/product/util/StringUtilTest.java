package top.zeimao77.product.util;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.main.BaseMain;

class StringUtilTest extends BaseMain {

    @Test
    void cut() {
        // 随机生成字符串
        RandomStringUtil randomStringUtil = new RandomStringUtil(0x2F);
        String s = randomStringUtil.randomStr(32);
        logger.info(s);
        // 截取字符串
        String cut = StringUtil.cut(s, 12,StringUtil.DEFAULT_SUFFIX);
        logger.info(cut);
    }

}