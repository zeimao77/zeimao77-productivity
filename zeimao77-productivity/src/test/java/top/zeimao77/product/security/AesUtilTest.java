package top.zeimao77.product.security;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.exception.BaseServiceRunException;
import top.zeimao77.product.main.BaseMain;
import top.zeimao77.product.util.ByteArrayCoDesUtil;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidParameterSpecException;
import java.util.Random;

import static top.zeimao77.product.exception.ExceptionCodeDefinition.CUSTOM;

class AesUtilTest extends BaseMain {

    @Test
    void encode() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidParameterSpecException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] key = ByteArrayCoDesUtil.hexDecode("5F65674615986D45836261410CE3FF92CD750B2839BB551C37C1D36DAEB69813");
        byte[] iv = ByteArrayCoDesUtil.hexDecode("6F856743C5986D458362C141ACE3FF91");

        AesUtil aesUtil = new AesUtil(key, iv);
        byte[] ed = "hello world11223344".getBytes(StandardCharsets.UTF_8);
        byte[] encode = aesUtil.encode(ed);
        logger.info("加密:{}",ByteArrayCoDesUtil.hexEncode(encode));


        String decode = new String(aesUtil.decode(encode));
        logger.info("解密:{}",decode);
    }

}