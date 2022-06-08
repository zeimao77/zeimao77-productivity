package top.zeimao77.product.http;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.main.BaseMain;

class HttpClientUtilTest extends BaseMain {

    @Test
    void sendGet() {
        HttpClientUtil11 httpClientUtil = new HttpClientUtil11(1);
        String s = httpClientUtil.sendGet("https://www.baidu.com");
        logger.info(s);
        HttpClientUtil8 httpClientUtil8 = new HttpClientUtil8();
        String s1 = httpClientUtil8.sendGet("https://www.baidu.com");
        logger.info(s1);
    }


}