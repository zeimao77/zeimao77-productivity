package top.zeimao77.product.fileio.iexcel;

import java.util.List;

public class Table {

    private int sheetIndex;
    private int startRow;
    private List<Column> columnList;

    public Table() {
    }

    public Table(int sheetIndex, int startRow) {
        this.sheetIndex = sheetIndex;
        this.startRow = startRow;
    }

    public int getSheetIndex() {
        return sheetIndex;
    }

    public void setSheetIndex(int sheetIndex) {
        this.sheetIndex = sheetIndex;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public static class Column {
        private String field;
        private int index;

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }

    public List<Column> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<Column> columnList) {
        this.columnList = columnList;
    }
}
