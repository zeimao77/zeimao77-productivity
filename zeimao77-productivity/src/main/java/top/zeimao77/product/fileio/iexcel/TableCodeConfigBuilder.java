package top.zeimao77.product.fileio.iexcel;

import java.util.ArrayList;
import java.util.List;

public class TableCodeConfigBuilder {

    private Table table;
    private List<Table.Column> columnList = new ArrayList<>();

    public static TableCodeConfigBuilder create(int sheetIndex,int startRow) {
        TableCodeConfigBuilder builder = new TableCodeConfigBuilder();
        builder.table = new Table();
        builder.table.setStartRow(startRow);
        builder.table.setSheetIndex(sheetIndex);
        return builder;
    }

    public TableCodeConfigBuilder column(int index, String field) {
        Table.Column column = new Table.Column();
        column.setIndex(index);
        column.setField(field);
        this.columnList.add(column);
        return this;
    }

    public TableCodeConfigBuilder column(int index, String field,Class<?> javaType) {
        Table.Column column = new Table.Column();
        column.setIndex(index);
        column.setField(field);
        column.setJavaType(javaType);
        this.columnList.add(column);
        return this;
    }

    public Table build() {
        this.table.setColumnList(columnList);
        return this.table;
    }

}
