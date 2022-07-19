package top.zeimao77.product.fileio.oexcel;

import java.util.List;
import java.util.Map;

public class Table {

    private String tableName;
    private String id;
    private String select;
    private List<Column> columnList;

    private List<CellRangeValue> cellRangeValueList;

    public Table() {
    }

    public Table(String tableName, String id) {
        this.tableName = tableName;
        this.id = id;
    }

    public static class Converter{
        private String converterId;
        private String defaultValue;
        private Map<String,Object> rule;

        public String getConverterId() {
            return converterId;
        }

        public void setConverterId(String converterId) {
            this.converterId = converterId;
        }

        public String getDefaultValue() {
            return defaultValue;
        }

        public void setDefaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
        }

        public Map<String, Object> getRule() {
            return rule;
        }

        public void setRule(Map<String, Object> rule) {
            this.rule = rule;
        }
    }

    public static class Column {
        private String title;
        private String format;
        private int width;
        private String field;
        private int index;
        private Converter converter;

        public Converter getConverter() {
            return converter;
        }

        public void setConverter(Converter converter) {
            this.converter = converter;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getFormat() {
            return format;
        }

        public void setFormat(String format) {
            this.format = format;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

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

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSelect() {
        return select;
    }

    public void setSelect(String select) {
        this.select = select;
    }

    public List<Column> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<Column> columnList) {
        this.columnList = columnList;
    }

    public List<CellRangeValue> getCellRangeValueList() {
        return cellRangeValueList;
    }

    public void setCellRangeValueList(List<CellRangeValue> cellRangeValueList) {
        this.cellRangeValueList = cellRangeValueList;
    }
}
