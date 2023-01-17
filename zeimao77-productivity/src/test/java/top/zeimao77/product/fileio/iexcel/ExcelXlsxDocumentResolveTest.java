package top.zeimao77.product.fileio.iexcel;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zeimao77.product.exception.BaseServiceRunException;
import top.zeimao77.product.main.BaseMain;
import top.zeimao77.product.util.JsonBeanUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

class ExcelXlsxDocumentResolveTest extends BaseMain {

    private static Logger logger = LoggerFactory.getLogger(ExcelXlsxDocumentResolveTest.class);

    @Test
    void parse() throws IOException, InvalidFormatException {
        // new BaseMain();
        logger.info("------------------------------");

        /**
         * 构建表格模型
         */
        Table table = TableCodeConfigBuilder.create(0,1)
                .column(0,"id",Long.class)
                .column(1,"uuid",String.class)
                .column(2,"rstr",String.class)
                .build();
        ExcelXlsxDocumentResolve<Object> objectExcelXlsxDocumentResolve = new ExcelXlsxDocumentResolve<>(false);
        File file = new File("C:\\Users\\zeimao77\\Desktop\\test.xlsx");
        SXSSFWorkbook workbook = new SXSSFWorkbook(new XSSFWorkbook(file));

        /**
         * 按单元格消费EXCEL
         */
        objectExcelXlsxDocumentResolve.parse(workbook, table, new ExcelXlsxDocumentResolve.CellConsumer() {
            @Override
            public void accept(int rowNo, Table.Column column, Cell cell) {
                logger.info("第{}行,{}={};",rowNo,column.getField(),cell.getStringCellValue());
            }
        });

        /**
         * 解析到Map
         */
        ArrayList<Map<String,Object>> list = new ArrayList<>(64);
        ArrayList<ErrorMsg> errorMsgs = new ArrayList<>();
        try {
            objectExcelXlsxDocumentResolve.parseMap(workbook,table,null,errorMsgs);
        }catch (BaseServiceRunException e){
            logger.error("错误:{}",e.getMessage());
        }

        for (Map<String, Object> stringObjectMap : list) {
            logger.info(JsonBeanUtil.DEFAULT.toJsonString(stringObjectMap));
        }

        for (ErrorMsg errorMsg : errorMsgs) {
            logger.info(errorMsg.describe());
        }
    }
}