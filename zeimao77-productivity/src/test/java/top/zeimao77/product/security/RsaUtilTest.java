package top.zeimao77.product.security;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.main.BaseMain;
import top.zeimao77.product.model.Pair;
import top.zeimao77.product.util.ByteArrayCoDesUtil;

import java.nio.charset.StandardCharsets;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import static org.junit.jupiter.api.Assertions.*;

class RsaUtilTest extends BaseMain {

    RsaUtil get() {
        Pair<RSAPublicKey, RSAPrivateKey> key = RsaUtil.getKey(2048);
        RsaUtil rsaUtil = new RsaUtil(key);
        return rsaUtil;
    }

    /**
     * 私钥加密 公钥解密
     */
    @Test
    void encode1() {
        RsaUtil rsaUtil = get();
        byte[] bs = "hello world".getBytes(StandardCharsets.UTF_8);
        byte[] bytes = rsaUtil.encode1(bs);
        logger.info("密文:{}", ByteArrayCoDesUtil.base64Encode(bytes));
        byte[] bytes1 = rsaUtil.decode1(bytes);
        logger.info(new String(bytes1,StandardCharsets.UTF_8));
    }

    /**
     * 公钥加密 私钥解密
     */
    @Test
    void encode0() {
        RsaUtil rsaUtil = get();
        byte[] bs = "hello world".getBytes(StandardCharsets.UTF_8);
        byte[] bytes = rsaUtil.encode0(bs);
        logger.info("密文:{}", ByteArrayCoDesUtil.base64Encode(bytes));
        byte[] bytes1 = rsaUtil.decode0(bytes);
        logger.info(new String(bytes1,StandardCharsets.UTF_8));
    }
}