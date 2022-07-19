package top.zeimao77.product.security;

import top.zeimao77.product.exception.BaseServiceRunException;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * 对秤加密
 */
public class AesUtil {
    private SecretKey secretKey;

    /**
     * 生成密钥 key的长度
     * @param keysize 256
     * @return 密钥
     */
    public static byte[] getKey(int keysize){
        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            throw new BaseServiceRunException("AES密钥生成错误",e);
        }
        keyGenerator.init(keysize);
        SecretKey secretKey = keyGenerator.generateKey();
        byte[] encoded = secretKey.getEncoded();
        return encoded;
    }

    /**
     * @param key 密钥
     */
    public AesUtil(byte[] key){
        this.secretKey = new SecretKeySpec(key, 0, key.length, "AES");
    }

    /**
     * 加密
     * @param source 明文
     * @return 密文
     */
    public byte[] encode(byte[] source) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[]  bs = cipher.doFinal(source);
            return bs;
        } catch (NoSuchAlgorithmException e) {
            throw new BaseServiceRunException("解密错误",e);
        } catch (NoSuchPaddingException e) {
            throw new BaseServiceRunException("解密错误",e);
        } catch (InvalidKeyException e) {
            throw new BaseServiceRunException("解密错误",e);
        } catch (BadPaddingException e) {
            throw new BaseServiceRunException("解密错误",e);
        } catch (IllegalBlockSizeException e) {
            throw new BaseServiceRunException("解密错误",e);
        }
    }

    /**
     * 字符串加密
     * @param rawPassword 明文字符串
     * @return BASE64的密文字符串
     */
    public String encode(CharSequence rawPassword) {
        byte[]  bs = encode(rawPassword.toString().getBytes());
        String s = Base64.getEncoder().encodeToString(bs);
        return s;
    }

    /**
     * 解密
     * @param source 密文
     * @return 明文
     */
    public byte[] decode(byte[] source) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE,secretKey);
            byte[] decode = cipher.doFinal(source);
            return decode;
        }catch (IllegalBlockSizeException e) {
            throw new BaseServiceRunException("非法密文");
        }catch (NoSuchAlgorithmException e) {
            throw new BaseServiceRunException("解密错误",e);
        } catch (InvalidKeyException e) {
            throw new BaseServiceRunException("解密错误",e);
        } catch (NoSuchPaddingException e) {
            throw new BaseServiceRunException("解密错误",e);
        } catch (BadPaddingException e) {
            throw new BaseServiceRunException("解密错误", e);
        }
    }

    /**
     * 字符串解密
     * @param encode BASE64的字符串密文
     * @return 明文字符串
     */
    public String decode(String encode) {
        byte[] decodedBytes = Base64.getDecoder().decode(encode.getBytes());
        byte[] strbs = decode(decodedBytes);
        String decode = new String(strbs);
        return decode;
    }

}
