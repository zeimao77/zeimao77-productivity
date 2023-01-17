package top.zeimao77.product.fileio.iexcel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import top.zeimao77.product.exception.BaseServiceRunException;
import top.zeimao77.product.exception.ExceptionCodeDefinition;
import top.zeimao77.product.model.Orderd;
import top.zeimao77.product.util.*;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class ExcelXlsxDocumentResolve<T> {

    private static Logger logger = LogManager.getLogger(ExcelXlsxDocumentResolve.class);

    private List<CellFiledTypeResover> resovers;
    private boolean sorted;
    private boolean stopOnError = true;

    /**
     * @param stopOnError 是否出错时停止
     */
    public ExcelXlsxDocumentResolve(boolean stopOnError) {
        this.stopOnError = stopOnError;
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
        resovers.add(new CellFiledTypeResover<StringOptional>() {
            @Override
            public int orderd() {
                return 2200;
            }

            @Override
            public StringOptional resove(Cell cell) {
                switch (cell.getCellType()) {
                    case _NONE:
                        return StringOptional.empty();
                    case NUMERIC:
                        return new StringOptional(String.valueOf(cell.getNumericCellValue()));
                    case STRING:
                        return new StringOptional(cell.getStringCellValue());
                    case FORMULA:
                        CellValue evaluate = evaluate(cell);
                        return new StringOptional(evaluate.getStringValue());
                    case BLANK:
                        return new StringOptional("");
                    case BOOLEAN:
                        return new StringOptional(String.valueOf(cell.getBooleanCellValue()));
                    case ERROR:
                        return new StringOptional("0X" + ByteArrayCoDesUtil.hexEncode(cell.getErrorCellValue()));
                }
                return new StringOptional(cell.getStringCellValue());
            }
        });
        this.sorted = true;
    }

    public static CellValue evaluate(Cell cell) {
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

    public void parseCell(int rowNo, Table.Column column, Cell cell, T t) {
        if(cell == null)
            return;
        Class<?> propertyType = BeanUtil.getPropertyType(t, column.getField());
        for (CellFiledTypeResover resover : resovers) {
            if(resover.support(propertyType,cell)) {
                Object value = resover.resove(cell);
                BeanUtil.setProperty(t,column.getField(),value);
             }
        }

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

    @FunctionalInterface
    public interface CellConsumer {
        /**
         * @param rowNo 行号
         * @param column 列
         * @param cell 单元格
         */
        void accept(int rowNo,Table.Column column,Cell cell);
    }

    /**
     *
     * @param workbook 工作簿
     * @param table 表描述
     * @param cellConsumer 单元格处理函数
     */
    public void parse(SXSSFWorkbook workbook, Table table, CellConsumer cellConsumer) {
        if(!sorted) {
            synchronized (this) {
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
            for (Table.Column column : columnList) {
                cell = row.getCell(column.getIndex());
                cellConsumer.accept(rowNum,column,cell);
            }
            rowNum++;
        }
    }

    public T parseRow(Row row,Table table, Class<T> clazz,ArrayList<ErrorMsg> errorMsgList) {
        T t = newObj(clazz);
        List<Table.Column> columnList = table.getColumnList();
        Cell cell;
        int rowNum = table.getStartRow();
        for (Table.Column column : columnList) {
            ErrorMsg errorMsg = null;
            cell = row.getCell(column.getIndex());
            try {
                parseCell(rowNum, column, cell, t);
            } catch (BaseServiceRunException e) {
                errorMsg = new ErrorMsg(rowNum,column,e.getCode(),e.getMessage());
            } catch (IllegalStateException e) {
                errorMsg = new ErrorMsg(rowNum,column, ExceptionCodeDefinition.APPERR,"单元格格式错误");
            }catch (NumberFormatException e) {
                errorMsg = new ErrorMsg(rowNum,column, ExceptionCodeDefinition.APPERR,"数字格式错误");
            } catch (Throwable e) {
                logger.error("单元格解析错误:",e);
                errorMsg = new ErrorMsg(rowNum,column,e.getMessage());
            }
            if(errorMsg != null && errorMsgList != null) {
                errorMsgList.add(errorMsg);
            }
            if(stopOnError && errorMsg != null) {
                throw new BaseServiceRunException(ExceptionCodeDefinition.WRONG_SOURCE | ExceptionCodeDefinition.NON_RETRYABLE ,"出错停止解析");
            }
        }
        return t;
    }

    /**
     * excel解析到model
     * @param workbook 工作簿
     * @param table 表描述
     * @param clazz MODEL类
     * @param resultList 返回列表
     * @param errorMsgList 错误信息列表
     */
    public void parse(SXSSFWorkbook workbook, Table table, Class<T> clazz, ArrayList<T> resultList,ArrayList<ErrorMsg> errorMsgList) {
        if(!sorted) {
            synchronized (this) {
                if(!sorted) {
                    resovers.sort(Orderd::compareTo);
                    sorted = true;
                }
            }
        }
        int rowNum = table.getStartRow();
        Sheet sheet = workbook.getXSSFWorkbook().getSheetAt(table.getSheetIndex());
        Row row;
        while ((row = sheet.getRow(rowNum)) != null) {
            T t = parseRow(row,table,clazz,errorMsgList);
            if(resultList != null)
                resultList.add(t);
            rowNum++;
        }
    }

    /**
     * 解析excel到map
     * @param workbook 工作簿
     * @param table 表描述
     * @param resultList 返回列表
     * @param errorMsgList 错误信息列表
     */
    public void parseMap(SXSSFWorkbook workbook, Table table,ArrayList<Map<String,Object>> resultList,ArrayList<ErrorMsg> errorMsgList) {
        if(!sorted) {
            synchronized (this) {
                if(!sorted) {
                    resovers.sort(Orderd::compareTo);
                    sorted = true;
                }
            }
        }
        int rowNum = table.getStartRow();
        List<Table.Column> columnList = table.getColumnList();
        Sheet sheet = workbook.getXSSFWorkbook().getSheetAt(table.getSheetIndex());
        Row row;
        Cell cell;
        while ((row = sheet.getRow(rowNum)) != null) {
            Map<String,Object> t = new HashMap<>();
            for (Table.Column column : columnList) {
                cell = row.getCell(column.getIndex());
                Object value = null;


                ErrorMsg errorMsg = null;
                cell = row.getCell(column.getIndex());
                try {
                    for (CellFiledTypeResover resover : resovers) {
                        if(resover.support(column.getJavaType(),cell)) {
                            value = resover.resove(cell);
                            break;
                        }
                    }
                } catch (BaseServiceRunException e) {
                    errorMsg = new ErrorMsg(rowNum+1,column,e.getCode(),e.getMessage());
                } catch (IllegalStateException e) {
                    errorMsg = new ErrorMsg(rowNum+1,column, ExceptionCodeDefinition.APPERR,"单元格格式错误");
                }catch (NumberFormatException e) {
                    errorMsg = new ErrorMsg(rowNum+1,column, ExceptionCodeDefinition.APPERR,"数字格式错误");
                } catch (Throwable e) {
                    logger.error("单元格解析错误:",e);
                    errorMsg = new ErrorMsg(rowNum+1,column,e.getMessage());
                }
                if(errorMsg != null && errorMsgList != null) {
                    errorMsgList.add(errorMsg);
                }
                if(stopOnError && errorMsg != null) {
                    throw new BaseServiceRunException(ExceptionCodeDefinition.WRONG_SOURCE | ExceptionCodeDefinition.NON_RETRYABLE ,"出错停止解析");
                }
                t.put(column.getField(),value);
            }
            if(resultList != null)
                resultList.add(t);
            rowNum++;
        }
    }

}
