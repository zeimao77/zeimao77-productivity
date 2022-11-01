package top.zeimao77.product.fileio.oexcel;

import top.zeimao77.product.converter.AbstractNonReFreshConverter;
import top.zeimao77.product.util.BeanUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 通过代码构建一个Table
 * 示例：
 * <pre>
 * Table table = TableCodeConfigBuilder.create("97628f46ba4143058ad73510af8e4fdb", "order")
 *     .column(0,"订单主键", "refid", 24,"@")
 *     .column(1,"中台单号", "refNo", 24,"@")
 *     .column(2,"发送时间", "createDate", 16,"yyyy-m-d hh:mm")
 *     .column(4,"消息体", "body", 220)
 *     .cellRangeValue(0,0,0,3,“@”,"总条数:"+dataList.size())
 *     .build();
 * </pre>
 */
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

    public TableCodeConfigBuilder column(int index,String title,String field,int width) {
        Table.Column column = new Table.Column();
        column.setTitle(title);
        column.setField(field);
        column.setWidth(width);
        column.setIndex(index);
        column.setFormat("@");
        columnList.add(column);
        return this;
    }

    public TableCodeConfigBuilder column(int index,String title,String field,int width,String format) {
        Table.Column column = new Table.Column();
        column.setTitle(title);
        column.setField(field);
        column.setWidth(width);
        column.setIndex(index);
        column.setFormat(format);
        columnList.add(column);
        return this;
    }

    public TableCodeConfigBuilder converter(int index, Table.Converter converter) {
        for (Table.Column column : this.columnList) {
            if(index == column.getIndex()) {
                column.setConverter(converter);
                break;
            }
        }
        return this;
    }

    public TableCodeConfigBuilder converter(int index, String defaultName, Map<String,Object> ruleMap) {
        for (Table.Column column : this.columnList) {
            if(index == column.getIndex()) {
                AbstractNonReFreshConverter<String> converter = new AbstractNonReFreshConverter(ruleMap) {
                    @Override
                    protected void refresh() {}

                    @Override
                    public Object defaultName(Object key) {
                        return defaultName;
                    }
                };
                column.setConverter((o1,o2) -> {
                    Object property = BeanUtil.getProperty(o2, o1.getField());
                    return converter.getName(property.toString());
                });
                break;
            }
        }
        return this;
    }

    public TableCodeConfigBuilder cellRangeValue(int startRow, int startColumn, int endRow, int endColumn,String format, Object value) {
        this.cellRangeValueList.add(new CellRangeValue(startRow,startColumn,endRow,endColumn,format,value));
        return this;
    }

    public Table build() {
        this.table.setColumnList(columnList);
        this.table.setCellRangeValueList(cellRangeValueList);
        return this.table;
    }

}
