package top.zeimao77.product.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.zeimao77.product.exception.BaseServiceRunException;
import top.zeimao77.product.model.MutablePair;
import top.zeimao77.product.model.Pair;
import top.zeimao77.product.util.AssertUtil;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * 非对秤加密
 */
public class RsaUtil {

    private static Logger logger = LogManager.getLogger(RsaUtil.class);
    private byte[] publicKey;
    private byte[] privateKey;

    /**
     * @param publicKey 公钥
     * @param privateKey 私钥
     */
    public RsaUtil(byte[] publicKey,byte[] privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public RsaUtil(Pair<RSAPublicKey, RSAPrivateKey> keys) {
        this(keys.getLeft().getEncoded(),keys.getRight().getEncoded());
    }

    /**
     * 生成公钥私钥密钥对
     * @param keySize 长度
     * @return 密钥对
     */
    public static Pair<RSAPublicKey, RSAPrivateKey> getKey(int keySize){
        KeyPairGenerator keyPairGenerator = null;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new BaseServiceRunException("RSA密钥生成错误",e);
        }
        keyPairGenerator.initialize(keySize);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey)keyPair.getPrivate();
        RSAPublicKey publicKey = (RSAPublicKey)keyPair.getPublic();
        return new MutablePair<>(publicKey,privateKey);
    }

    /**
     * 私钥加密
     * @param source 明文
     * @return 密文
     */
    private Cipher __encode1_cipher;
    public byte[] encode1(byte[] source) {
        try{
            if(__encode1_cipher == null) {
                AssertUtil.notEmpty(privateKey,"加密失败,私钥空");
                PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                PrivateKey key = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
                __encode1_cipher = Cipher.getInstance("RSA");
                __encode1_cipher.init(Cipher.ENCRYPT_MODE,key);
            }
            byte[] result = __encode1_cipher.doFinal(source);
            return result;
        } catch (NoSuchAlgorithmException e) {
            throw new BaseServiceRunException("加密出错",e);
        } catch (InvalidKeyException e) {
            throw new BaseServiceRunException("加密出错",e);
        } catch (NoSuchPaddingException e) {
            throw new BaseServiceRunException("加密出错",e);
        } catch (BadPaddingException e) {
            throw new BaseServiceRunException("加密出错",e);
        } catch (IllegalBlockSizeException e) {
            throw new BaseServiceRunException("加密出错",e);
        } catch (InvalidKeySpecException e) {
            throw new BaseServiceRunException("加密出错",e);
        }
    }

    /**
     * 公钥解密
     * @param source 密文
     * @return 明文
     */
    private Cipher __decode1_cipher;
    public byte[] decode1(byte[] source) {
        try{
            if(__decode1_cipher == null) {
                AssertUtil.notEmpty(publicKey,"解密失败,公钥空");
                X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                PublicKey key = keyFactory.generatePublic(x509EncodedKeySpec);
                __decode1_cipher = Cipher.getInstance("RSA");
                __decode1_cipher.init(Cipher.DECRYPT_MODE,key);
            }
            byte[] result = __decode1_cipher.doFinal(source);
            return result;
        } catch (NoSuchAlgorithmException e) {
            throw new BaseServiceRunException("解密出错",e);
        } catch (InvalidKeyException e) {
            throw new BaseServiceRunException("解密出错",e);
        } catch (NoSuchPaddingException e) {
            throw new BaseServiceRunException("解密出错",e);
        } catch (BadPaddingException e) {
            throw new BaseServiceRunException("解密出错",e);
        } catch (IllegalBlockSizeException e) {
            throw new BaseServiceRunException("解密出错",e);
        } catch (InvalidKeySpecException e) {
            throw new BaseServiceRunException("解密出错",e);
        }
    }

    /**
     * 公钥加密
     * @param source 明文
     * @return 密文
     */
    private Cipher __encode0_cipher;
    public byte[] encode0(byte[] source) {
        try{
            if(__encode0_cipher == null) {
                AssertUtil.notEmpty(publicKey,"加密失败,公钥空");
                X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
                __encode0_cipher = Cipher.getInstance("RSA");
                __encode0_cipher.init(Cipher.ENCRYPT_MODE,publicKey);
            }
            byte[] result = __encode0_cipher.doFinal(source);
            return result;
        } catch (NoSuchAlgorithmException e) {
            throw new BaseServiceRunException("加密出错",e);
        } catch (InvalidKeyException e) {
            throw new BaseServiceRunException("加密出错",e);
        } catch (NoSuchPaddingException e) {
            throw new BaseServiceRunException("加密出错",e);
        } catch (InvalidKeySpecException e) {
            throw new BaseServiceRunException("加密出错",e);
        } catch (BadPaddingException e) {
            throw new BaseServiceRunException("加密出错",e);
        } catch (IllegalBlockSizeException e) {
            throw new BaseServiceRunException("加密出错",e);
        }
    }

    /**
     * 私钥解密
     * @param source 密文
     * @return 明文
     */
    private Cipher __decode0_cipher;
    public byte[] decode0(byte[] source) {
        try{
            if(__decode0_cipher == null ){
                AssertUtil.notEmpty(privateKey,"解密失败,私钥空");
                PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
                __decode0_cipher  = Cipher.getInstance("RSA");
                __decode0_cipher.init(Cipher.DECRYPT_MODE,privateKey);
            }
            byte[] result = __decode0_cipher.doFinal(source);
            return result;
        } catch (NoSuchAlgorithmException e) {
            throw new BaseServiceRunException("解密出错",e);
        } catch (InvalidKeyException e) {
            throw new BaseServiceRunException("解密出错",e);
        } catch (NoSuchPaddingException e) {
            throw new BaseServiceRunException("解密出错",e);
        } catch (InvalidKeySpecException e) {
            throw new BaseServiceRunException("解密出错",e);
        } catch (BadPaddingException e) {
            throw new BaseServiceRunException("解密出错",e);
        } catch (IllegalBlockSizeException e) {
            throw new BaseServiceRunException("解密出错",e);
        }
    }

}
