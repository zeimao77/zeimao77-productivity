package top.zeimao77.product.fileio.oexcel;

import org.apache.poi.ss.usermodel.*;

import java.util.HashMap;
import java.util.Map;

public class CellStyleFactory {

    private Map<String, CellStyle> styleMap = new HashMap<>();

    private Workbook workbook;

    public CellStyleFactory(Workbook workbook) {
        this.workbook = workbook;
    }

    public CellStyle create(String format) {
        CellStyle cellStyle = styleMap.get(format);
        if(cellStyle == null) {
            cellStyle = workbook.createCellStyle();
            DataFormat f = workbook.createDataFormat();
            cellStyle.setDataFormat(f.getFormat(format));
            cellStyle.setAlignment(HorizontalAlignment.RIGHT);
            cellStyle.setVerticalAlignment(VerticalAlignment.BOTTOM);
            styleMap.put(format, cellStyle);
        }
        return cellStyle;
    }


}
