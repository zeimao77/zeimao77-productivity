package top.zeimao77.product.util;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.main.BaseMain;

import java.nio.charset.StandardCharsets;
import java.util.function.Function;

class ByteArrayCoDesUtilTest extends BaseMain {

    @Test
    void zipEncode() {
        RandomStringUtil randomStringUtil = new RandomStringUtil(0x08);
        byte[] bs =randomStringUtil.randomStr(1024 ).getBytes(StandardCharsets.UTF_8);
        logger.info(new String(bs));
        logger.info("bs.len={}",bs.length);
        byte[] zbs = ByteArrayCoDesUtil.zipEncode(bs);
        logger.info("zbs.len={}",zbs.length);
        bs = ByteArrayCoDesUtil.zipDecode(zbs);
        logger.info(new String(bs));
        logger.info("zbs.len={}",bs.length);
    }

    @Test
    void hexEncode() {
        byte[] bytes = "helloworld".getBytes(StandardCharsets.UTF_8);
        String s = ByteArrayCoDesUtil.hexEncode(bytes);
        logger.info(s);
        logger.info(new String(ByteArrayCoDesUtil.hexDecode(s)));
    }

    @Test
    void base64Encode() {
        byte[] bytes = "helloworld".getBytes(StandardCharsets.UTF_8);
        String s = ByteArrayCoDesUtil.base64Encode(bytes);
        logger.info(s);
        logger.info(new String(ByteArrayCoDesUtil.base64Decode(s),StandardCharsets.UTF_8));
    }

    @Test
    void xor() {
        byte[] bs0 = new byte[]{0x01,0x02,0x03,0x04};
        byte[] bs1 = new byte[]{0x00,0x02,0x66};
        byte[] xor = ByteArrayCoDesUtil.xor(bs0, bs1);
        String s = ByteArrayCoDesUtil.hexEncode(xor);
        logger.info(s);
    }

    @Test
    void matchesCheckCode() {
        byte[] bs1 = "hello world!".getBytes(StandardCharsets.UTF_8);
        int i = ByteArrayCoDesUtil.checkCode(bs1);
        logger.info("{}",ByteArrayCoDesUtil.matchesCheckCode(bs1,i));
        byte[] bs2 = "hello world.".getBytes(StandardCharsets.UTF_8);
        logger.info("{}",ByteArrayCoDesUtil.matchesCheckCode(bs2,i));
    }
}