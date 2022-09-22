package top.zeimao77.product.fileio.iexcel;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import top.zeimao77.product.main.BaseMain;

import java.io.File;
import java.io.IOException;

class ExcelXlsxDocumentResolveTest extends BaseMain {

    @Test
    void parse() throws IOException, InvalidFormatException {
        Table table = TableCodeConfigBuilder.create(0,1)
                .column(0,"id")
                .column(1,"uuid")
                .column(2,"rstr")
                .build();
        ExcelXlsxDocumentResolve<Object> objectExcelXlsxDocumentResolve = new ExcelXlsxDocumentResolve<>();
        File file = new File("C:\\Users\\zeimao77\\Desktop\\test.xlsx");
        SXSSFWorkbook workbook = new SXSSFWorkbook(new XSSFWorkbook(file));

        objectExcelXlsxDocumentResolve.parse(workbook, table, new ExcelXlsxDocumentResolve.CellConsumer() {
            @Override
            public void accept(int rowNo, Table.Column column, Cell cell) {
                logger.info("第{}行,{}={};",rowNo,column.getField(),cell.getStringCellValue());
            }
        });

    }
}