package top.zeimao77.product.fileio.oexcel;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.util.ArrayList;
import java.util.List;

public class DelegatingXlsxDocumentBuilder implements XlsxDocumentBuilder{

    private Workbook workbook;
    private List<ExcelXlsxDocumentBuilder> builderList;

    public DelegatingXlsxDocumentBuilder(ExcelXlsxDocumentBuilder... builders) {
        builderList = List.of(builders);
    }

    public DelegatingXlsxDocumentBuilder(List<ExcelXlsxDocumentBuilder> builderList) {
        this.builderList = builderList;
    }

    public boolean register(ExcelXlsxDocumentBuilder jobExecHandler) {
        if(builderList == null)
            builderList = new ArrayList<>();
        return this.builderList.add(jobExecHandler);
    }

    public boolean unregister(ExcelXlsxDocumentBuilder jobExecHandler) {
        return this.builderList.remove(jobExecHandler);
    }

    @Override
    public Workbook build() {
        build(new SXSSFWorkbook());
        return this.workbook;
    }

    @Override
    public void build(Workbook workbook) {
        this.workbook = workbook;
        for (ExcelXlsxDocumentBuilder excelXlsxDocumentBuilder : builderList) {
            excelXlsxDocumentBuilder.build(this.workbook);
        }
    }


}
