package top.zeimao77.product.sql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zeimao77.product.exception.BaseServiceRunException;
import static top.zeimao77.product.exception.ExceptionCodeDefinition.*;

import top.zeimao77.product.json.Ijson;
import top.zeimao77.product.model.Orderd;
import top.zeimao77.product.util.CalendarDateUtil;
import top.zeimao77.product.util.LocalDateTimeUtil;
import top.zeimao77.product.util.StringOptional;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.sql.*;
import java.time.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ResultSet结果集解析器
 */
public class DefaultResultSetResolve implements ResultSetResolve {

    private static Logger logger = LoggerFactory.getLogger(DefaultResultSetResolve.class);
    public static final DefaultResultSetResolve INSTANCE = new DefaultResultSetResolve();
    private List<FiledTypeResover> resovers;
    private boolean sorted;

    public boolean addFieldTypeResover(FiledTypeResover filedTypeResover) {
        this.sorted = false;
        return this.resovers.add(filedTypeResover);
    }

    public DefaultResultSetResolve() {
        resovers = new ArrayList<>(32);
        resovers.add(FiledTypeResover.STRINGTYPERESOVER);
        resovers.add(FiledTypeResover.LONGTYPERESOVER);
        resovers.add(FiledTypeResover.INTEGTYPERESOVER);
        resovers.add(FiledTypeResover.DOUBLETYPERESOVER);
        resovers.add(FiledTypeResover.BIGDECIMALTYPERESOVER);
        resovers.add(FiledTypeResover.BOOLEANTYPERESOVER);
        resovers.add(FiledTypeResover.DATETYPERESOVER);
        resovers.add(FiledTypeResover.LOCALDATETIMETYPERESOVER);
        resovers.add(FiledTypeResover.LOCALTIMETYPERESOVER);
        resovers.add(FiledTypeResover.LOCALDATETYPERESOVER);
        resovers.add(FiledTypeResover.FLOATTYPERESOVER);
        resovers.add(FiledTypeResover.SHORTTYPERESOVER);
        resovers.add(FiledTypeResover.CHARTYPERESOVER);
        resovers.add(FiledTypeResover.BIGINTTYPERESOVER);
        resovers.add(FiledTypeResover.BYTEBUFFERTYPERESOVER);
        resovers.add(FiledTypeResover.FIELDTYPERESOVER);
        resovers.add(FiledTypeResover.IJSONTYPERESOVER);
        resovers.add(FiledTypeResover.OBJECTTYPERESOVER);
        sorted = true;
    }

    public <T> T newObj(Class<T> clazz) {
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
            throw new BaseServiceRunException(NOT_SUPPORTED,"构造方法未找到",e);
        }
    }

    public <T> T mapRow(ResultSet rs, int rowNo, Class<T> clazz,ResultSetMetaData rsmd) throws SQLException {
        T obj = newObj(clazz);
        int colCount = rsmd.getColumnCount();
        for(int i = 1;i<=colCount;i++){
            String columnLabel = rsmd.getColumnLabel(i);
            Object fieldValue = null;
            if(!sorted) {
                synchronized (this) {
                    if(!sorted) {
                        resovers.sort(Orderd::compareTo);
                        sorted = true;
                    }
                }
            }
            Object value = rs.getObject(i);
            try{
                Field field = clazz.getDeclaredField(columnLabel);
                // 基本数据类型
                if(value == null) {
                    fieldValue = null;
                } else if(field.getType() == long.class) {
                    fieldValue = value == null ? 0L : Long.valueOf(value.toString()).longValue();
                } else if(field.getType() == int.class) {
                    fieldValue = value == null ? 0 : Integer.valueOf(value.toString()).intValue();
                } else if(field.getType() == float.class) {
                    fieldValue = value == null ? 0F : Float.valueOf(value.toString()).floatValue();
                } else if(field.getType() == double.class) {
                    fieldValue = value == null ? 0D : Double.valueOf(value.toString()).doubleValue();
                } else if(field.getType() == short.class) {
                    fieldValue = value == null ? 0 : Short.valueOf(value.toString()).shortValue();
                } else if(field.getType() == byte.class) {
                    fieldValue = value == null ? 0x00 : Byte.valueOf(value.toString());
                } else if(field.getType() == char.class) {
                    fieldValue = value == null ? 0x00 : value.toString().charAt(0);
                } else if(field.getType() == boolean.class) {
                    fieldValue = value == null ? false : Boolean.valueOf(value.toString()).booleanValue();
                } else {
                    // 非基本数据类型
                    fieldValue = resolve(value,field.getType());
                }
                field.setAccessible(true);
                if(fieldValue == null && field.getType() == StringOptional.class)
                    fieldValue = StringOptional.empty();
                field.set(obj,fieldValue);
            } catch (NoSuchFieldException e) {
                logger.debug("字段未找到:{}",columnLabel);
            } catch (IllegalAccessException e) {
                throw new BaseServiceRunException("参数错误",e);
            }
        }
        return obj;
    }

    /**
     * 解析一个ResultSet
     * @param rs ResultSet对象
     * @param clazz 目标Class对象
     * @param list 容器
     * @param <T>
     */
    @Override
    public <T> void populate(ResultSet rs , Class<T> clazz, List<T> list) {
        ResultSetMetaData rsmd = null;
        try {
            rsmd = rs.getMetaData();
            while(rs.next()){
                T obj = mapRow(rs,rs.getRow(),clazz,rsmd);
                list.add(obj);
            }
        } catch (SQLException e) {
            throw new BaseServiceRunException(SQLEXCEPTION,"SQL异常",e);
        }
    }


    public void populateMap(ResultSet rs , List<Map<String,Object>> list) {
        ResultSetMetaData rsmd = null;
        try {
            rsmd = rs.getMetaData();
            int colCount = rsmd.getColumnCount();
            while(rs.next()){
                Map<String,Object> t = new HashMap<>();
                for(int i = 1;i<=colCount;i++){
                    String columnLabel = rsmd.getColumnLabel(i);
                    Object fieldValue = null;
                    if(!sorted) {
                        synchronized (this) {
                            if(!sorted) {
                                resovers.sort(Orderd::compareTo);
                                sorted = true;
                            }
                        }
                    }
                    Object value = rs.getObject(i);
                    if(value == null) {
                        fieldValue = null;
                    } else {
                        int columnType = rsmd.getColumnType(i);
                        switch (columnType) {
                            case Types.TIMESTAMP -> fieldValue = resolve(value,LocalDateTime.class);
                            case Types.DATE -> fieldValue = resolve(value,LocalDate.class);
                            case Types.TIME -> fieldValue = resolve(value,LocalTime.class);
                            case Types.BINARY,Types.VARBINARY,Types.LONGVARBINARY -> fieldValue = resolve(value,ByteBuffer.class);
                            default -> fieldValue = rs.getObject(i);
                        }
                    }
                    t.put(columnLabel,fieldValue);
                }
                list.add(t);
            }
        } catch (SQLException e) {
            throw new BaseServiceRunException(SQLEXCEPTION,"SQL异常",e);
        }
    }

    /**
     * 解析转换一个具体的字段
     * @param value 值
     * @param clazz 需要解析转换的目标Class对象
     * @param <T>
     * @return
     */
    public <T> T resolve(Object value,Class<T> clazz) {
        for (int i1 = 0; i1 < resovers.size(); i1++) {
            FiledTypeResover filedTypeResover = resovers.get(i1);
            if(filedTypeResover.support(clazz,value)) {
                T fieldValue = (T) filedTypeResover.resove(value);
                return fieldValue;
            }
        }
        return null;
    }

}
