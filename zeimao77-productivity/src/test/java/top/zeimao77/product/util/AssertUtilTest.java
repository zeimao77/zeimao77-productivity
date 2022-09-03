package top.zeimao77.product.util;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.main.BaseMain;

public class AssertUtilTest extends BaseMain {

    @Test
    public void match() {
        Integer code0 = 0x00003001;
        Integer flag  = 0x00001001;
        logger.info((code0 & flag) == flag);

    }

}