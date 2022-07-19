package top.zeimao77.product.fileio.oexcel;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.junit.jupiter.api.Test;
import top.zeimao77.product.factory.BeanFactory;
import top.zeimao77.product.main.BaseMain;
import top.zeimao77.product.util.CollectionUtil;
import top.zeimao77.product.util.LongIdGenerator;
import top.zeimao77.product.util.RandomStringUtil;

import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ExcelXlsxDocumentBuilderTest extends BaseMain {

    @Test
    void build() throws IOException {
        TableXMLConfigBuilder builder = new TableXMLConfigBuilder("C:\\Users\\zeimao77\\Desktop\\excel_table.xml");
        Table table = builder.build();

        List<String> ts = List.of("1","2","","");
        BeanFactory.DEFAULT.registerPrototypesFactory("user",() -> {
            HashMap<String,Object> m = new HashMap<>();
            m.put("id", LongIdGenerator.INSTANCE.generate());
            m.put("name","test666");
            m.put("type", CollectionUtil.getRandom(ts));
            return m;
        });

        ArrayList<Map<String,Object>> dataList = new ArrayList<>();

        for (int i = 0; i < 40; i++) {
            dataList.add(BeanFactory.DEFAULT.getBean("user",HashMap.class));
        }

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