package top.zeimao77.product.security;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.main.BaseMain;
import top.zeimao77.product.util.ByteArrayCoDesUtil;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class DigestUtilTest extends BaseMain {

    @Test
    void md5() {
        byte[] bytes = "hello world".getBytes(StandardCharsets.UTF_8);
        byte[] bytes1 = DigestUtil.md5(bytes);
        logger.info("摘要:{}", ByteArrayCoDesUtil.hexEncode(bytes1));
    }

    @Test
    void sha() {
        byte[] bytes = "hello world".getBytes(StandardCharsets.UTF_8);
        byte[] bytes1 = DigestUtil.sha(bytes);
        logger.info("摘要:{}", ByteArrayCoDesUtil.hexEncode(bytes1));
    }
}