package top.zeimao77.product.fileio.oexcel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import top.zeimao77.product.converter.AbstractNonReFreshConverter;
import top.zeimao77.product.converter.IConverter;
import top.zeimao77.product.util.AssertUtil;
import top.zeimao77.product.util.BeanUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;

public class ExcelXlsxDocumentBuilder {

    protected Workbook workbook;
    protected Sheet sheet;
    protected int rowNum = 0;
    protected List<?> dataList;

    CellStyleFactory cellStyleFactory = null;

    private Map<Table.Column, IConverter<String>> converterMap;

    protected Table table;
    protected Consumer<ExcelXlsxDocumentBuilder> before;
    protected Consumer<ExcelXlsxDocumentBuilder> after;

    public ExcelXlsxDocumentBuilder(Table table, List<?> dataList) {
        this.table = table;
        this.dataList = dataList;
    }

    protected IConverter<String> converter(Table.Column column) {
        Table.Converter converter = column.getConverter();
        if(converter == null)
            return null;
        if(converterMap == null)
            converterMap = new HashMap<>();
        IConverter<String> stringIConverter = converterMap.get(column);
        if(stringIConverter == null) {
            stringIConverter = new AbstractNonReFreshConverter<String>(converter.getRule()) {
                @Override
                protected void refresh() {
                }
                @Override
                public Object defaultName(String key) {
                    return converter.getDefaultValue();
                }
            };
        }
        return stringIConverter;
    }


    public Workbook build() {
        build(new SXSSFWorkbook());
        return this.workbook;
    }

    public void build(Workbook workbook) {
        this.workbook = workbook;
        this.sheet = workbook.createSheet(table.getTableName());
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
                    IConverter<String> converter = converter(column);
                    Object v = BeanUtil.getProperty(o,column.getField());
                    if(v==null){continue;}
                    if(converter != null) {
                        v = converter.getName(v.toString());
                    }
                    Cell cell = row.createCell(column.getIndex());
                    setCellValue(cell,column.getFormat(),v);
                }
            }
        }
        if(cellRangeValueList != null && !cellRangeValueList.isEmpty()) {
            for (CellRangeValue cellRangeValue : cellRangeValueList) {
                CellRangeAddress cellRangeAddress = new CellRangeAddress(cellRangeValue.getStartRow()
                        , cellRangeValue.getEndRow(), cellRangeValue.getStartColumn(), cellRangeValue.getEndColumn());
                sheet.addMergedRegion(cellRangeAddress);
                Row row = sheet.getRow(cellRangeValue.getStartRow());
                row = row == null ? sheet.createRow(cellRangeValue.getStartRow()) : row;
                Cell cell = row.createCell(cellRangeValue.getStartColumn());
                setCellValue(cell,cellRangeValue.getFormat(),cellRangeValue.getValue());
            }
        }
    }

    private void setCellValue(Cell cell,String format,Object value) {
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
        }else {
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
}
