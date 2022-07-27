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
        List<String> list = List.of("1","2","3");
        for (int i = 0; i < 30; i++) {

            logger.info("{}",CollectionUtil.getRandom(list));
        }
    }

}