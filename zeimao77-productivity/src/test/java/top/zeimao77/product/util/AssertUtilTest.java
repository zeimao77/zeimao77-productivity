package top.zeimao77.product.util;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.fileio.oexcel.Table;
import top.zeimao77.product.fileio.oexcel.TableXMLConfigBuilder;
import top.zeimao77.product.main.BaseMain;

import java.util.Collection;
import java.util.List;

public class AssertUtilTest extends BaseMain {

    @Test
    public void match() {
        Integer code0 = 0x10000101;
        Integer code1 = 0x01000201;
        Integer i = (code0 & 0x00FFFFFF) ^ (code1 & 0x00FFFFFF);
        logger.info(i);

    }

}