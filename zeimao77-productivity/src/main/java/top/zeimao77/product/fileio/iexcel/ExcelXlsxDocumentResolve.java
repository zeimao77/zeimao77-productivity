package top.zeimao77.product.fileio.iexcel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import top.zeimao77.product.exception.BaseServiceRunException;
import top.zeimao77.product.model.Orderd;
import top.zeimao77.product.util.BeanUtil;
import top.zeimao77.product.util.ByteArrayCoDesUtil;
import top.zeimao77.product.util.CalendarDateUtil;
import top.zeimao77.product.util.LocalDateTimeUtil;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExcelXlsxDocumentResolve<T> {

    private List<CellFiledTypeResover> resovers;
    private boolean sorted;

    public ExcelXlsxDocumentResolve() {
        resovers = new ArrayList<>(32);
        resovers.add(new CellFiledTypeResover<String>() {
            @Override
            public String resove(Cell cell) {
                switch (cell.getCellType()) {
                    case _NONE:
                        return null;
                    case NUMERIC:
                        return String.valueOf(cell.getNumericCellValue());
                    case STRING:
                        return cell.getStringCellValue();
                    case FORMULA:
                        CellValue evaluate = evaluate(cell);
                        return evaluate.getStringValue();
                    case BLANK:
                        return "";
                    case BOOLEAN:
                        return String.valueOf(cell.getBooleanCellValue());
                    case ERROR:
                        return "0X" + ByteArrayCoDesUtil.hexEncode(cell.getErrorCellValue());
                }
                return cell.getStringCellValue();
            }

            @Override
            public int orderd() {
                return 1000;
            }
        });
        resovers.add(new CellFiledTypeResover<LocalDateTime>() {
            @Override
            public int orderd() {
                return 1100;
            }

            @Override
            public LocalDateTime resove(Cell cell) {
                switch (cell.getCellType()) {
                    case STRING:
                        return LocalDateTimeUtil.parseDateTime(cell.getStringCellValue());
                    case NUMERIC:
                    default:
                        return cell.getLocalDateTimeCellValue();
                }
            }
        });
        resovers.add(new CellFiledTypeResover<Date>() {
            @Override
            public int orderd() {
                return 1200;
            }

            @Override
            public Date resove(Cell cell) {
                switch (cell.getCellType()) {
                    case STRING:
                        return CalendarDateUtil.parseTime(cell.getStringCellValue());
                    case NUMERIC:
                    default:
                        return cell.getDateCellValue();
                }
            }
        });
        resovers.add(new CellFiledTypeResover<Double>() {
            @Override
            public Double resove(Cell cell) {
                switch (cell.getCellType()) {
                    case STRING:
                        return Double.valueOf(cell.getStringCellValue());
                    case NUMERIC:
                        return cell.getNumericCellValue();
                    case FORMULA:
                        CellValue evaluate = evaluate(cell);
                        return evaluate.getNumberValue();
                    case BLANK:
                    case _NONE:
                        return null;
                    case BOOLEAN:
                        return cell.getBooleanCellValue() ? 1D : 0D;
                    case ERROR:
                        return null;
                }
                return Double.valueOf(cell.getNumericCellValue());
            }

            @Override
            public int orderd() {
                return 1300;
            }
        });
        resovers.add(new CellFiledTypeResover<Integer>() {
            @Override
            public int orderd() {
                return 1400;
            }

            @Override
            public Integer resove(Cell cell) {
                switch (cell.getCellType()) {
                    case STRING:
                        return Integer.valueOf(cell.getStringCellValue());
                    case NUMERIC:
                        return Double.valueOf(cell.getNumericCellValue()).intValue();
                    case FORMULA:
                        CellValue evaluate = evaluate(cell);
                        return Integer.valueOf(evaluate.getStringValue());
                    case BLANK:
                    case _NONE:
                        return null;
                    case BOOLEAN:
                        return cell.getBooleanCellValue() ? 1 : 0;
                    case ERROR:
                        return (int)cell.getErrorCellValue();
                }
                return Double.valueOf(cell.getNumericCellValue()).intValue();
            }
        });
        resovers.add(new CellFiledTypeResover<Boolean>() {

            @Override
            public int orderd() {
                return 1500;
            }

            @Override
            public Boolean resove(Cell cell) {
                switch (cell.getCellType()) {
                    case STRING:
                        return Boolean.valueOf(cell.getStringCellValue());
                    case NUMERIC:
                        return cell.getNumericCellValue() == 0D ? false : true;
                    case FORMULA:
                        CellValue evaluate = evaluate(cell);
                        return evaluate.getBooleanValue();
                    case BOOLEAN:
                        return cell.getBooleanCellValue();
                    case BLANK:
                    case _NONE:
                    case ERROR:
                    default:
                        return null;
                }
            }
        });
        resovers.add(new CellFiledTypeResover<BigDecimal>() {

            @Override
            public int orderd() {
                return 1600;
            }

            @Override
            public BigDecimal resove(Cell cell) {
                switch (cell.getCellType()) {
                    case STRING:
                        return new BigDecimal(cell.getStringCellValue());
                    case NUMERIC:
                        return BigDecimal.valueOf(cell.getNumericCellValue());
                    case FORMULA:
                        CellValue evaluate = evaluate(cell);
                        return BigDecimal.valueOf(evaluate.getNumberValue());
                    case BLANK:
                    case _NONE:
                        return null;
                    case BOOLEAN:
                        return cell.getBooleanCellValue() ? BigDecimal.ONE : BigDecimal.ZERO;
                    case ERROR:
                        return null;
                }
                return BigDecimal.valueOf(cell.getNumericCellValue());
            }
        });
        resovers.add(new CellFiledTypeResover<Long>() {
            @Override
            public Long resove(Cell cell) {
                switch (cell.getCellType()) {
                    case STRING:
                        return Long.valueOf(cell.getStringCellValue());
                    case NUMERIC:
                        return Double.valueOf(cell.getNumericCellValue()).longValue();
                    case FORMULA:
                        CellValue evaluate = evaluate(cell);
                        return Long.valueOf(evaluate.getStringValue());
                    case BLANK:
                    case _NONE:
                        return null;
                    case BOOLEAN:
                        return cell.getBooleanCellValue() ? 1L : 0L;
                    case ERROR:
                        return (long)cell.getErrorCellValue();
                }
                return Double.valueOf(cell.getNumericCellValue()).longValue();
            }

            @Override
            public int orderd() {
                return 1700;
            }
        });
        resovers.add(new CellFiledTypeResover<LocalDate>() {
            @Override
            public int orderd() {
                return 1800;
            }

            @Override
            public LocalDate resove(Cell cell) {
                switch (cell.getCellType()) {
                    case STRING:
                        return LocalDateTimeUtil.parseDate(cell.getStringCellValue());
                    case NUMERIC:
                    default:
                        return cell.getLocalDateTimeCellValue().toLocalDate();
                }
            }
        });
        resovers.add(new CellFiledTypeResover<LocalTime>() {
            @Override
            public int orderd() {
                return 1900;
            }

            @Override
            public LocalTime resove(Cell cell) {
                switch (cell.getCellType()) {
                    case STRING:
                        return LocalDateTimeUtil.parseTime(cell.getStringCellValue());
                    case NUMERIC:
                    default:
                        return cell.getLocalDateTimeCellValue().toLocalTime();
                }
            }
        });
        resovers.add(new CellFiledTypeResover<Float>() {
            @Override
            public int orderd() {
                return 2000;
            }

            @Override
            public Float resove(Cell cell) {
                switch (cell.getCellType()) {
                    case STRING:
                        return Float.valueOf(cell.getStringCellValue());
                    case NUMERIC:
                        return Double.valueOf(cell.getNumericCellValue()).floatValue();
                    case FORMULA:
                        CellValue evaluate = evaluate(cell);
                        return Double.valueOf(evaluate.getNumberValue()).floatValue();
                    case BOOLEAN:
                        return cell.getBooleanCellValue() ? 1F : 0F;
                    case BLANK:
                    case _NONE:
                    case ERROR:
                    default:
                        return null;
                }
            }
        });
        resovers.add(new CellFiledTypeResover<Short>() {
            @Override
            public int orderd() {
                return 2100;
            }

            @Override
            public Short resove(Cell cell) {
                switch (cell.getCellType()) {
                    case STRING:
                        return Short.valueOf(cell.getStringCellValue());
                    case NUMERIC:
                        return Double.valueOf(cell.getNumericCellValue()).shortValue();
                    case FORMULA:
                        CellValue evaluate = evaluate(cell);
                        return Double.valueOf(evaluate.getNumberValue()).shortValue();
                    case BOOLEAN:
                        return cell.getBooleanCellValue() ? BigDecimal.ONE.shortValue() : BigDecimal.ZERO.shortValue();
                    case BLANK:
                    case _NONE:
                    case ERROR:
                    default:
                        return null;
                }
            }
        });
        this.sorted = true;
    }

    public CellValue evaluate(Cell cell) {
        XSSFFormulaEvaluator evaluator = new XSSFFormulaEvaluator((XSSFWorkbook) cell.getSheet().getWorkbook());
        CellValue evaluate = evaluator.evaluate(cell);
        return evaluate;
    }

    protected T newObj(Class<T> clazz) {
        try {
            T obj = clazz.getDeclaredConstructor().newInstance();
            return obj;
        } catch (InstantiationException e) {
            throw new BaseServiceRunException("错误",e);
        } catch (IllegalAccessException e) {
            throw new BaseServiceRunException("错误",e);
        } catch (InvocationTargetException e) {
            throw new BaseServiceRunException("错误",e);
        } catch (NoSuchMethodException e) {
            throw new BaseServiceRunException("构造方法未找到",e);
        }
    }

     public void parse(int rowNo, Table.Column column, Cell cell, T t) {
        if(cell == null)
            return;
         Class<?> propertyType = BeanUtil.getPropertyType(t, column.getField());
         Object value = null;
         for (CellFiledTypeResover resover : resovers) {
             if(resover.support(propertyType,cell)) {
                 value = resover.resove(cell);
             }
         }
         BeanUtil.setProperty(t,column.getField(),value);
    }

    protected <T> T resolve(Cell cell,Class<T> clazz) {
        for (int i1 = 0; i1 < resovers.size(); i1++) {
            CellFiledTypeResover filedTypeResover = resovers.get(i1);
            if(filedTypeResover.support(clazz,cell)) {
                T fieldValue = (T) filedTypeResover.resove(cell);
                return fieldValue;
            }
        }
        return null;
    }

    public boolean addFieldTypeResover(CellFiledTypeResover cellFieldTypeResover) {
        this.sorted = false;
        return this.resovers.add(cellFieldTypeResover);
    }

    public ArrayList<T> parse(SXSSFWorkbook workbook, Table table, Class<T> clazz, ArrayList<T> resultList) {
        if(!sorted) {
            synchronized (ExcelXlsxDocumentResolve.class) {
                if(!sorted) {
                    resovers.sort(Orderd::compareTo);
                    sorted = true;
                }
            }
        }
        int rowNum = table.getStartRow();
        List<Table.Column> columnList = table.getColumnList();
        Sheet sheet = workbook.getXSSFWorkbook().getSheetAt(table.getSheetIndex());
        Row row = null;
        Cell cell = null;
        while ((row = sheet.getRow(rowNum)) != null) {
            T t = newObj(clazz);
            for (Table.Column column : columnList) {
                cell = row.getCell(column.getIndex());
                parse(rowNum, column, cell,t);
            }
            resultList.add(t);
            rowNum++;
        }
        return resultList;
    }

}
