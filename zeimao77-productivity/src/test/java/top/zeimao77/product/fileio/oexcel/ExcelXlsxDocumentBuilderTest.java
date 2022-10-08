package top.zeimao77.product.fileio.oexcel;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.junit.jupiter.api.Test;
import top.zeimao77.product.factory.BeanFactory;
import top.zeimao77.product.main.BaseMain;
import top.zeimao77.product.util.LongIdGenerator;
import top.zeimao77.product.util.RandomStringUtil;
import top.zeimao77.product.util.UuidGenerator;

import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class ExcelXlsxDocumentBuilderTest extends BaseMain {

    @Test
    void build() throws IOException {
        /**
         * 构建table
         * 可以选择 TableXMLConfigBuilder 从xml文件进行构建 参考:
         * @see TableXMLConfigBuilder
         */
        Table table = TableCodeConfigBuilder.create("123","uuid")
                .cellRangeValue(0,0,0,2,"@","总共40条;")
                .column(0,"ID","id",20)
                .column(1,"uuid","uuid",36)
                .column(2,"随机串","rstr",24)
                .column(3,"订单状态","infState",12)

                .build();

        /**
         *  生成表格数据
         */
        RandomStringUtil randomStringUtil = new RandomStringUtil(0x2F);
        BeanFactory.DEFAULT.registerPrototypesFactory("user",() -> {
            HashMap<String,Object> m = new HashMap<>();
            m.put("id", LongIdGenerator.INSTANCE.generate());
            m.put("uuid", UuidGenerator.INSTANCE.generate());
            m.put("rstr",randomStringUtil.randomStr(16));
            return m;
        });
        ArrayList<Map<String,Object>> dataList = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            dataList.add(BeanFactory.DEFAULT.getBean("user",HashMap.class));
        }
        /**
         * 有了table 和 数据 就可以写excel表格了
         */
        File file = new File("C:\\Users\\zeimao77\\Desktop\\test.xlsx");
        FileOutputStream fos = new FileOutputStream(file);
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        ExcelXlsxDocumentBuilder excelXlsxDocumentBuilder = new ExcelXlsxDocumentBuilder(table, dataList);
        excelXlsxDocumentBuilder.build(workbook);
        workbook.write(fos);
        workbook.close();
        fos.close();

    }
}