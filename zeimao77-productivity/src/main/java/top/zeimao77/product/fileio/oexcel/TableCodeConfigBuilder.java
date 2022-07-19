package top.zeimao77.product.fileio.oexcel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TableCodeConfigBuilder{

    private Table table;
    private List<Table.Column> columnList = new ArrayList<>();
    private List<CellRangeValue> cellRangeValueList = new ArrayList<>();

    public static TableCodeConfigBuilder create(String id,String tableName) {
        TableCodeConfigBuilder builder = new TableCodeConfigBuilder();
        builder.table = new Table(tableName,id);
        return builder;
    }

    public TableCodeConfigBuilder select(String selectSql) {
        this.table.setSelect(selectSql);
        return this;
    }

    public TableCodeConfigBuilder columnList(List<Table.Column> columnList) {
        this.columnList = columnList;
        return this;
    }

    public TableCodeConfigBuilder column(int index,String field) {
        Table.Column column = new Table.Column();
        column.setIndex(index);
        column.setField(field);
        this.columnList.add(column);
        return this;
    }

    public TableCodeConfigBuilder column(String title,String field,int width) {
        Table.Column column = new Table.Column();
        column.setTitle(title);
        column.setField(field);
        column.setFormat("@");
        column.setWidth(width);
        column.setIndex(columnList.size());
        columnList.add(column);
        return this;
    }

    public TableCodeConfigBuilder column(String title,String field,int width,String format) {
        Table.Column column = new Table.Column();
        column.setTitle(title);
        column.setField(field);
        column.setFormat(format);
        column.setWidth(width);
        column.setIndex(columnList.size());
        columnList.add(column);
        return this;
    }

    public TableCodeConfigBuilder column(int index,String title,String field,int width,String format) {
        Table.Column column = new Table.Column();
        column.setTitle(title);
        column.setField(field);
        column.setFormat(format);
        column.setWidth(width);
        column.setIndex(index);
        columnList.add(column);
        return this;
    }

    public TableCodeConfigBuilder converter(int index, String defaultName, Map<String,Object> ruleMap) {
        for (Table.Column column : this.columnList) {
            if(index == column.getIndex()) {
                Table.Converter converter = new Table.Converter();
                converter.setDefaultValue(defaultName);
                converter.setRule(ruleMap);
                column.setConverter(converter);
                break;
            }
        }
        return this;
    }

    public TableCodeConfigBuilder cellRangeValue(int startRow, int startColumn, int endRow, int endColumn, Object value) {
        this.cellRangeValueList.add(new CellRangeValue(startRow,startColumn,endRow,endColumn,value));
        return this;
    }

    public Table build() {
        this.table.setColumnList(columnList);
        this.table.setCellRangeValueList(cellRangeValueList);
        return this.table;
    }

}
