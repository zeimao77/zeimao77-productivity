package top.zeimao77.product.security;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.main.BaseMain;
import top.zeimao77.product.util.ByteArrayCoDesUtil;

import java.nio.charset.StandardCharsets;

class AesUtilTest extends BaseMain {

    @Test
    void encode() {
        byte[] key = AesUtil.getKey(256);
        logger.info("密钥:{}",ByteArrayCoDesUtil.hexEncode(key));
        AesUtil aesUtil = new AesUtil(key);
        byte[] ed = aesUtil.encode("hello world".getBytes(StandardCharsets.UTF_8));
        logger.info(ByteArrayCoDesUtil.hexEncode("hello world".getBytes(StandardCharsets.UTF_8)));
        String encode = ByteArrayCoDesUtil.base64Encode(ed);
        logger.info("加密:{}",encode);
        logger.info("加密:{}",ByteArrayCoDesUtil.hexEncode(ed));
        String decode = aesUtil.decode(encode);
        logger.info("解密:{}",decode);
    }

}