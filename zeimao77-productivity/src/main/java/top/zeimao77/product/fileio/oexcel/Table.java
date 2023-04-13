package top.zeimao77.product.fileio.oexcel;

import top.zeimao77.product.util.BeanUtil;
import top.zeimao77.product.util.CalendarDateUtil;
import top.zeimao77.product.util.LocalDateTimeUtil;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class Table {

    private String tableName;
    private String id;
    /**
     * 通过select指定如何获取数据 接口/Select查表
     * 再加上入参就可以获取参数了
     */
    private String select;
    private List<Column> columnList;

    private List<CellRangeValue> cellRangeValueList;

    public Table() {
    }

    public Table(String tableName, String id) {
        this.tableName = tableName;
        this.id = id;
    }

    public interface Converter {
        Object getPrintValue(Column column,Object line);
    }

    public static final Converter DATETIMECONVERTER = (o1,o2) -> {
        Object v = BeanUtil.getProperty(o2,o1.getField());
        if(v instanceof Date d) {
            return CalendarDateUtil.toDateTime(d);
        }
        if(v instanceof java.sql.Timestamp d) {
            return LocalDateTimeUtil.toDateTime(d.toLocalDateTime());
        }
        if(v instanceof java.sql.Date d) {
            return LocalDateTimeUtil.toDateTime(d.toLocalDate());
        }
        if(v instanceof LocalDateTime d) {
            return LocalDateTimeUtil.toDateTime(d);
        }
        return v;
    };

    public static final Converter DATECONVERTER = (o1,o2) -> {
        Object v = BeanUtil.getProperty(o2,o1.getField());
        if(v instanceof Date d) {
            return CalendarDateUtil.toDate(d);
        }
        if(v instanceof java.sql.Timestamp d) {
            return LocalDateTimeUtil.toDate(d.toLocalDateTime());
        }
        if(v instanceof java.sql.Date d) {
            return LocalDateTimeUtil.toDate(d.toLocalDate());
        }
        if(v instanceof LocalDateTime d) {
            return LocalDateTimeUtil.toDate(d);
        }
        return v;
    };

    public static class MapperConverter implements Converter {

        @Override
        public Object getPrintValue(Column column, Object line) {
            Object v = BeanUtil.getProperty(line,column.getField());
            return v;
        }
    }

    public static class CodeNameConverter implements Converter{

        private String codeField;
        private String nameField;

        public CodeNameConverter(String codeField, String nameField) {
            this.codeField = codeField;
            this.nameField = nameField;
        }

        public String getCodeField() {
            return codeField;
        }

        public String getNameField() {
            return nameField;
        }

        @Override
        public Object getPrintValue(Column column, Object line) {
            Object c = BeanUtil.getProperty(line, codeField);
            Object n = BeanUtil.getProperty(line, nameField);
            return String.format("[%s]%s",c == null ? "" : c.toString(),n == null ? "" : n.toString());
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
