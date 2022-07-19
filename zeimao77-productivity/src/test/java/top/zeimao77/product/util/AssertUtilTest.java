package top.zeimao77.product.util;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.fileio.oexcel.Table;
import top.zeimao77.product.fileio.oexcel.TableXMLConfigBuilder;
import top.zeimao77.product.main.BaseMain;

public class AssertUtilTest extends BaseMain {

    @Test
    public void match() {
        TableXMLConfigBuilder builder = new TableXMLConfigBuilder("C:\\Users\\zeimao77\\Desktop\\excel_table.xml");
        Table build = builder.build();
        logger.info(build);
    }

}