package top.zeimao77.product.fileio.oexcel;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.function.BiFunction;

public class CellRangeValue {

    private int startRow;
    private int endRow;
    private int startColumn;
    private int endColumn;
    private Object value;
    private String format;
    private BiFunction<Workbook,CellStyleFactory, CellStyle> formatFunc;

    public CellRangeValue(int startRow, int startColumn) {
        this.startRow = startRow;
        this.endRow = startRow;
        this.startColumn = startColumn;
        this.endColumn = startColumn;
    }

    public CellRangeValue(int startRow, int startColumn, int endRow, int endColumn,String format, Object value) {
        this.startRow = startRow;
        this.startColumn = startColumn;
        this.endRow = endRow;
        this.endColumn = endColumn;
        this.format = format;
        this.value = value;
    }

    public CellRangeValue(int startRow, int startColumn, int endRow, int endColumn,String format,BiFunction<Workbook,CellStyleFactory, CellStyle> formatFunc, Object value) {
        this.startRow = startRow;
        this.startColumn = startColumn;
        this.endRow = endRow;
        this.endColumn = endColumn;
        this.format = format;
        this.formatFunc = formatFunc;
        this.value = value;
    }

    public boolean isMerge() {
        return startRow < endRow || startColumn < endColumn;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getEndRow() {
        return endRow;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    public int getStartColumn() {
        return startColumn;
    }

    public void setStartColumn(int startColumn) {
        this.startColumn = startColumn;
    }

    public int getEndColumn() {
        return endColumn;
    }

    public void setEndColumn(int endColumn) {
        this.endColumn = endColumn;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public BiFunction<Workbook, CellStyleFactory, CellStyle> getFormatFunc() {
        return formatFunc;
    }

    public void setFormatFunc(BiFunction<Workbook, CellStyleFactory, CellStyle> formatFunc) {
        this.formatFunc = formatFunc;
    }
}
