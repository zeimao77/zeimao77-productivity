package top.zeimao77.product.security;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.main.BaseMain;

import static org.junit.jupiter.api.Assertions.*;

class AesUtilTest extends BaseMain {

    @Test
    void encode() {
        byte[] key = AesUtil.getKey(256);
        AesUtil aesUtil = new AesUtil(key);
        String encode = aesUtil.encode("hello world");
        logger.info("加密:{}",encode);
        String decode = aesUtil.decode(encode);
        logger.info("解密:{}",decode);
    }

}