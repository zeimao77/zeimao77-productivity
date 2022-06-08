package top.zeimao77.product.security;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.main.BaseMain;
import top.zeimao77.product.model.Pair;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

class RsaSignUtilTest extends BaseMain {



    /**
     * 签名
     */
    @Test
    void matches() {
        BaseMain.showBanner();
        String msg = "helloworld!!";
        Pair<RSAPublicKey, RSAPrivateKey> key = RsaUtil.getKey(2048);
        RsaSignUtil rsaSignUtil = new RsaSignUtil(key);

        byte[] sign = rsaSignUtil.sign(msg.getBytes());
        logger.info(rsaSignUtil.matches(msg.getBytes(),sign));
    }

}