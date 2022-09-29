package top.zeimao77.product.security;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.main.BaseMain;
import top.zeimao77.product.util.ByteArrayCoDesUtil;

import java.nio.charset.StandardCharsets;

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

    @Test
    void digest() {
        byte[] bytes = "hello world".getBytes(StandardCharsets.UTF_8);
        String md5 = ByteArrayCoDesUtil.hexEncode(DigestUtil.digest(bytes, "MD5"));
        logger.info("MD5({}):{}",md5.length(),md5);
        String sha = ByteArrayCoDesUtil.hexEncode(DigestUtil.digest(bytes, "SHA"));
        logger.info("SHA({}):{}",sha.length(),sha);
        String sha1 = ByteArrayCoDesUtil.hexEncode(DigestUtil.digest(bytes, "SHA-1"));
        logger.info("SHA1({}):{}",sha1.length(),sha1);
        String sha256 = ByteArrayCoDesUtil.hexEncode(DigestUtil.digest(bytes, "SHA-256"));
        logger.info("SHA256({}):{}",sha256.length(),sha256);
        String sha512 = ByteArrayCoDesUtil.hexEncode(DigestUtil.digest(bytes, "SHA-512"));
        logger.info("SHA512({}):{}",sha512.length(),sha512);


    }
}