package top.zeimao77.product.http;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.main.BaseMain;

class HttpClientUtilTest extends BaseMain {

    @Test
    void sendGet() {
        HttpClientUtil8 httpClientUtil = new HttpClientUtil8();
        String s = httpClientUtil.sendGet("https://www.baidu.com");
        logger.info(s);
        s = new HttpClientUtil8().sendGet("https://www.zeimao77.top/stadir/66", null, 1000);
        logger.info(s);
    }

}