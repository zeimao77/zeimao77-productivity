package top.zeimao77.product.security;

import top.zeimao77.product.exception.BaseServiceRunException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new BaseServiceRunException("摘要错误",e);
        }
        byte[] md5Bytes = md.digest(bs);
        return md5Bytes;
    }

    /**
     * SHA摘要
     * @param bs 字节数组
     * @return SHA摘要字节数组结果
     */
    public static byte[] sha(byte[] bs) {
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            throw new BaseServiceRunException("摘要错误",e);
        }
        sha.update(bs);
        return sha.digest();
    }

}
