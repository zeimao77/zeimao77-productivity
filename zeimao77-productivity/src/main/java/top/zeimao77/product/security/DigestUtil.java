package top.zeimao77.product.security;

import top.zeimao77.product.exception.BaseServiceRunException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static top.zeimao77.product.exception.ExceptionCodeDefinition.*;

/**
 * 摘要
 */
public class DigestUtil {

    /**
     * MD5 摘要
     * @param bs 字节数组
     * @return MD5字节组数结果
     */
    public static byte[] md5(byte[] bs) {
        return digest(bs,"MD5");
    }

    /**
     * SHA摘要
     * @param bs 字节数组
     * @return SHA摘要字节数组结果
     */
    public static byte[] sha(byte[] bs) {
        return digest(bs,"SHA");
    }

    /**
     * 消息摘要
     * @param bs 字节数组
     * @param algorithm MD5(16B),SHA-1(20B),SHA-256(32B),SHA384(48B),SHA512(64B)
     * @return 摘要结果
     */
    public static byte[] digest(byte[] bs, String algorithm){
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new BaseServiceRunException(CUSTOM,"摘要错误",e);
        }
        sha.update(bs);
        return sha.digest();
    }



}
