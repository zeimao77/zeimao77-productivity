package top.zeimao77.product.fileio.oexcel;

import org.apache.poi.ss.usermodel.Workbook;

public interface XlsxDocumentBuilder {

    Workbook build();

    void build(Workbook workbook);

}
