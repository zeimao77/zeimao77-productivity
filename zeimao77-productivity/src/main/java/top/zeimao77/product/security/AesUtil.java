package top.zeimao77.product.security;

import top.zeimao77.product.exception.BaseServiceRunException;

import static top.zeimao77.product.exception.ExceptionCodeDefinition.APPERR;
import static top.zeimao77.product.exception.ExceptionCodeDefinition.CUSTOM;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidParameterSpecException;
import java.util.Base64;

/**
 * 对秤加密
 */
public class AesUtil {
    private SecretKey secretKey;
    private AlgorithmParameters algorithmParameters;
    Cipher cipher;

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
            throw new BaseServiceRunException(CUSTOM,"AES密钥生成错误",e);
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
        this("AES/ECB/PKCS5Padding",key,null);
    }

    public AesUtil(byte [] key,byte[] iv){
        this("AES/CTR/NoPadding",key,iv);
    }

    public AesUtil(String mode,byte [] key,byte[] iv){
        try {
            this.cipher = Cipher.getInstance(mode);
        } catch (NoSuchAlgorithmException e) {
            throw new BaseServiceRunException(CUSTOM,"解密错误",e);
        } catch (NoSuchPaddingException e) {
            throw new BaseServiceRunException(CUSTOM,"解密错误",e);
        }
        this.secretKey = new SecretKeySpec(key, 0, key.length, "AES");
        if(iv != null) {
            try {
                this.algorithmParameters = AlgorithmParameters.getInstance("AES");
                this.algorithmParameters.init(new IvParameterSpec(iv));
            } catch (NoSuchAlgorithmException e) {
            } catch (InvalidParameterSpecException e) {
                throw new BaseServiceRunException(CUSTOM,"解密错误",e);
            }
        }

    }

    /**
     * 加密
     * @param source 明文
     * @return 密文
     */
    public byte[] encode(byte[] source) {
        try {
            if(algorithmParameters != null) {
                cipher.init(Cipher.ENCRYPT_MODE, secretKey,algorithmParameters);
            } else {
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            }
            byte[]  bs = cipher.doFinal(source);
            return bs;
        } catch (InvalidKeyException e) {
            throw new BaseServiceRunException(CUSTOM,"解密错误",e);
        } catch (BadPaddingException e) {
            throw new BaseServiceRunException(CUSTOM,"解密错误",e);
        } catch (IllegalBlockSizeException e) {
            throw new BaseServiceRunException(CUSTOM,"解密错误",e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new BaseServiceRunException(CUSTOM,"解密错误",e);
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
            if(algorithmParameters != null) {
                cipher.init(Cipher.ENCRYPT_MODE, secretKey,algorithmParameters);
            } else {
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            }
            byte[] decode = cipher.doFinal(source);
            return decode;
        }catch (IllegalBlockSizeException e) {
            throw new BaseServiceRunException(APPERR,"非法密文",e);
        } catch (InvalidKeyException e) {
            throw new BaseServiceRunException(CUSTOM,"解密错误",e);
        } catch (BadPaddingException e) {
            throw new BaseServiceRunException(CUSTOM,"解密错误", e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new BaseServiceRunException(CUSTOM,"解密错误",e);
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
