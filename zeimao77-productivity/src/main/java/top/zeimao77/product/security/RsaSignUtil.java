package top.zeimao77.product.security;

import top.zeimao77.product.exception.BaseServiceRunException;
import top.zeimao77.product.model.Pair;
import top.zeimao77.product.util.AssertUtil;

import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RsaSignUtil {

    private byte[] publicKey;
    private byte[] privateKey;

    /**
     *
     * @param publicKey 公钥
     * @param privateKey 私钥
     */
    public RsaSignUtil(byte[] publicKey,byte[] privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public RsaSignUtil(Pair<RSAPublicKey, RSAPrivateKey> keys) {
        this(keys.getLeft().getEncoded(),keys.getRight().getEncoded());
    }

    /**
     * 签名
     */
    private Signature __signature;
    public byte[] sign(byte[] source) {
        try{
            if(__signature == null) {
                AssertUtil.notEmpty(privateKey,"签名失败,私钥空");
                PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
                __signature = Signature.getInstance("MD5withRSA");
                __signature.initSign(privateKey);
            }
            __signature.update(source);
            byte[] result = __signature.sign();
            return result;
        } catch (NoSuchAlgorithmException e) {
            throw new BaseServiceRunException("签名错误");
        } catch (SignatureException e) {
            throw new BaseServiceRunException("签名错误");
        } catch (InvalidKeyException e) {
            throw new BaseServiceRunException("签名错误");
        } catch (InvalidKeySpecException e) {
            throw new BaseServiceRunException("签名错误");
        }
    }

    /**
     * 验证签名
     */
    private Signature __maches_signature;
    public boolean matches(byte[] source,byte[] sign) {
        try{
            if(__maches_signature == null){
                AssertUtil.notEmpty(publicKey,"验证失败,公钥空");
                X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
                __maches_signature = Signature.getInstance("MD5withRSA");
                __maches_signature.initVerify(publicKey);
            }
            __maches_signature.update(source);
            boolean verify = __maches_signature.verify(sign);
            return verify;
        } catch (NoSuchAlgorithmException e) {
            throw new BaseServiceRunException("验证错误",e);
        } catch (InvalidKeyException e) {
            throw new BaseServiceRunException("验证错误",e);
        } catch (InvalidKeySpecException e) {
            throw new BaseServiceRunException("验证错误",e);
        } catch (SignatureException e) {
            throw new BaseServiceRunException("验证错误",e);
        }
    }

}
