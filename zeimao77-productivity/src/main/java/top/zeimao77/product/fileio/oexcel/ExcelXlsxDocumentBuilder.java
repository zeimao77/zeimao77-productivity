package top.zeimao77.product.fileio.oexcel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import top.zeimao77.product.converter.IConverter;
import top.zeimao77.product.util.AssertUtil;
import top.zeimao77.product.util.BeanUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;

public class ExcelXlsxDocumentBuilder implements XlsxDocumentBuilder{

    protected Workbook workbook;
    protected Sheet sheet;
    protected int rowNum = 0;
    protected List<?> dataList;

    protected CellStyleFactory cellStyleFactory = null;

    private Map<Table.Column, IConverter> converterMap;

    protected Table table;
    /**
     * 构建前的前置处理函数 通常用于设置新的rowNum来空行
     */
    protected Consumer<ExcelXlsxDocumentBuilder> before;
    /**
     * 构建后的后置处理函数
     */
    protected Consumer<ExcelXlsxDocumentBuilder> after;

    /**
     * @param table 表结构定义
     * @param dataList 数据列表
     */
    public ExcelXlsxDocumentBuilder(Table table, List<?> dataList) {
        this.table = table;
        this.dataList = dataList;
    }


    /**
     * @param table 表结构定义
     * @param dataList 数据列表
     * @param rowNum 表格起始行 通常用于给合并单元格让位
     */
    public ExcelXlsxDocumentBuilder(Table table, List<?> dataList,int rowNum) {
        this.table = table;
        this.dataList = dataList;
        this.rowNum = rowNum;
    }

    /**
     * 默认构造xlsx文件
     * @return 07版本
     */
    @Override
    public Workbook build() {
        build(new SXSSFWorkbook());
        return this.workbook;
    }

    /**
     * 构建一个excel文件
     * 示例:
     * <pre>
     * File file = new File(path);
     * FileOutputStream fos = new FileOutputStream(file);
     * SXSSFWorkbook workbook = new SXSSFWorkbook();
     * ExcelXlsxDocumentBuilder excelXlsxDocumentBuilder = new ExcelXlsxDocumentBuilder(table, dataList);
     * excelXlsxDocumentBuilder.build(workbook);
     * workbook.write(fos);
     * workbook.close();
     * fos.close();
     * </pre>
     * @param workbook 工作簿
     */
    @Override
    public void build(Workbook workbook) {
        this.workbook = workbook;
        this.sheet = workbook.createSheet(table.getTableName());
        if(this.cellStyleFactory == null)
            this.cellStyleFactory = new CellStyleFactory(workbook);
        if(before != null){before.accept(this);}
        setHead();
        setWidth();
        setBody();
        if(after != null){after.accept(this);}
    }

    private void setBody() {
        List<Table.Column> columnList = table.getColumnList();
        List<CellRangeValue> cellRangeValueList = table.getCellRangeValueList();
        if(columnList != null && !columnList.isEmpty()) {
            for (Object o : dataList) {
                Row row = newRow();
                for (Table.Column column : columnList) {
                    Table.Converter converter = column.getConverter();
                    Object v;
                    if(converter != null) {
                        v = converter.getPrintValue(column,o);
                    } else {
                        v = BeanUtil.getProperty(o,column.getField());
                    }
                    Cell cell = row.createCell(column.getIndex());
                    setCellValue(cell,column.getIndex(),column.getFormat(),v);
                }
            }
        }
        if(cellRangeValueList != null && !cellRangeValueList.isEmpty()) {
            for (CellRangeValue cellRangeValue : cellRangeValueList) {
                if(cellRangeValue.isMerge()) {
                    CellRangeAddress cellRangeAddress = new CellRangeAddress(cellRangeValue.getStartRow()
                            , cellRangeValue.getEndRow(), cellRangeValue.getStartColumn(), cellRangeValue.getEndColumn());
                    sheet.addMergedRegion(cellRangeAddress);
                    Row row = sheet.getRow(cellRangeValue.getStartRow());
                    row = row == null ? sheet.createRow(cellRangeValue.getStartRow()) : row;
                    Cell cell = row.createCell(cellRangeValue.getStartColumn());
                    setCellValue(cell,-1,cellRangeValue.getFormat(),cellRangeValue.getValue());
                } else {
                    Row row = sheet.getRow(cellRangeValue.getStartRow());
                    row = row == null ? sheet.createRow(cellRangeValue.getStartRow()) : row;
                    Cell cell = row.createCell(cellRangeValue.getStartColumn());
                    setCellValue(cell,-1,cellRangeValue.getFormat(),cellRangeValue.getValue());
                }
            }
        }
    }

    protected void setCellValue(Cell cell,int index,String format,Object value) {
        if(AssertUtil.isNotEmpty(format)) {
            cell.setCellStyle(cellStyleFactory.create(format));
        }
        if(value == null)
            return;
        Object v = value;
        if(v instanceof Long p && "@".equals(format)) {
            cell.setCellValue(String.valueOf(p));
        } else if(v instanceof Number p) {
            if("@".equals(format)) {
                cell.setCellValue(NumberToTextConverter.toText(p.doubleValue()));
            } else {
                cell.setCellValue(p.doubleValue());
            }
        } else if(v instanceof LocalDateTime p) {
            cell.setCellValue(p);
        } else if(v instanceof LocalDate p) {
            cell.setCellValue(p);
        } else if(v instanceof Calendar p) {
            cell.setCellValue(p);
        } else if (v instanceof Boolean p) {
            cell.setCellValue(p);
        } else if(v instanceof Date p) {
            cell.setCellValue(p);
        } else {
            cell.setCellValue(String.valueOf(v));
        }
    }

    private void setWidth() {
        for (Table.Column column : table.getColumnList()) {
            this.sheet.setColumnWidth(column.getIndex(), column.getWidth() * 268);
        }
    }

    protected Row newRow() {
        Row row = sheet.createRow(rowNum++);
        return row;
    }

    private void setHead() {
        Row row = newRow();
        List<Table.Column> columnList = table.getColumnList();
        for (Table.Column column : columnList) {
            Cell cell = row.createCell(column.getIndex());
            cell.setCellValue(column.getTitle());
        }
    }

    public void setBefore(Consumer<ExcelXlsxDocumentBuilder> before) {
        this.before = before;
    }

    public void setAfter(Consumer<ExcelXlsxDocumentBuilder> after) {
        this.after = after;
    }

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public CellStyleFactory getCellStyleFactory() {
        return cellStyleFactory;
    }

    public void setCellStyleFactory(CellStyleFactory cellStyleFactory) {
        this.cellStyleFactory = cellStyleFactory;
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public Sheet getSheet() {
        return sheet;
    }

    public Table getTable() {
        return table;
    }

    public List<?> getDataList() {
        return dataList;
    }

    public Map<Table.Column, IConverter> getConverterMap() {
        return converterMap;
    }


}
