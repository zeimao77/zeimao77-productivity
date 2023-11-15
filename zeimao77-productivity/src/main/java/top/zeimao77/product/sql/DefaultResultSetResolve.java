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
        resovers.add(new FiledTypeResover<String>() {
            @Override
            public String resove(Object obj) {
                return obj == null ? null : obj.toString();
            }

            @Override
            public int orderd() {
                return 1000;
            }
        });
        resovers.add(new FiledTypeResover<Long>() {
            @Override
            public Long resove(Object obj) {
                if(obj == null)
                    return null;
                if(obj instanceof Integer) {
                    Integer o = ((Integer) obj);
                    return o.longValue();
                }
                if(obj instanceof Long) {
                    Long o = ((Long) obj);
                    return o;
                }
                if(obj instanceof Timestamp) {
                    Timestamp o = ((Timestamp) obj);
                    return o.getTime();
                }
                if(obj instanceof LocalDateTime) {
                    LocalDateTime o = ((LocalDateTime) obj);
                    return o.toEpochSecond(ZoneId.systemDefault().getRules().getOffset(Instant.now()));
                }
                return Long.valueOf(obj.toString());
            }

            @Override
            public int orderd() {
                return 1100;
            }
        });
        resovers.add(new FiledTypeResover<Integer>() {
            @Override
            public Integer resove(Object obj) {
                if(obj == null)
                    return null;
                if(obj instanceof Integer) {
                    Integer o = ((Integer) obj);
                    return o;
                }
                if(obj instanceof Long) {
                    Long o = ((Long) obj);
                    return o.intValue();
                }
                return Integer.valueOf(obj.toString());
            }

            @Override
            public int orderd() {
                return 1200;
            }
        });
        resovers.add(new FiledTypeResover<Double>() {

            @Override
            public Double resove(Object obj) {
                if(obj == null)
                    return null;
                if(obj instanceof Number) {
                    Number o = ((Number) obj);
                    return o.doubleValue();
                }
                return Double.valueOf(obj.toString());
            }

            @Override
            public int orderd() {
                return 1300;
            }
        });
        resovers.add(new FiledTypeResover<BigDecimal>() {
            @Override
            public BigDecimal resove(Object obj) {
                if( obj == null )
                    return null;
                if(obj instanceof BigDecimal) {
                    return ((BigDecimal) obj);
                }
                if(obj instanceof Number) {
                    Number o = (Number) obj;
                    return BigDecimal.valueOf(o.doubleValue());
                }
                return new BigDecimal(obj.toString());
            }

            @Override
            public int orderd() {
                return 1400;
            }
        });
        resovers.add(new FiledTypeResover<Boolean>() {

            @Override
            public Boolean resove(Object obj) {
                if( obj == null )
                    return null;
                if(obj instanceof Boolean)
                    return (Boolean) obj;
                if(obj instanceof Number) {
                    Number o = ((Number) obj);
                    return o.intValue() == 0 ? false : true;
                }
                if(obj instanceof String){
                    String o = ((String) obj);
                    return Boolean.valueOf(o);
                }
                logger.error("无法解析的布尔值：{}",obj);
                return null;
            }

            @Override
            public int orderd() {
                return 1500;
            }
        });
        resovers.add(new FiledTypeResover<java.util.Date>() {
            @Override
            public java.util.Date resove(Object obj) {
                if(obj == null)
                    return null;
                if(obj instanceof LocalDateTime) {
                    LocalDateTime o = ((LocalDateTime) obj);
                    return CalendarDateUtil.fromLocalDateTime(o);
                }
                if(obj instanceof Time) {
                    Time o = ((Time) obj);
                    return new Date(o.getTime());
                }
                if(obj instanceof Date) {
                    Date o = ((Date)obj);
                    return new Date(o.getTime());
                }
                if(obj instanceof Timestamp) {
                    Timestamp o = ((Timestamp) obj);
                    return new Date(o.getTime());
                }
                if(obj instanceof Long) {
                    Long o = ((Long) obj);
                    return Date.from(Instant.ofEpochMilli(o));
                }
                if(obj instanceof String) {
                    String o = ((String) obj);
                    return CalendarDateUtil.parseDateTime(o);
                }
                logger.error("无法解析的日期时间：{}",obj);
                return null;
            }

            @Override
            public int orderd() {
                return 1600;
            }
        });
        resovers.add(new FiledTypeResover<LocalDateTime>() {
            @Override
            public LocalDateTime resove(Object obj) {
                if(obj == null) return null;
                if(obj instanceof LocalDateTime)
                    return (LocalDateTime) obj;
                if(obj instanceof LocalDate) {
                    LocalDate o = (LocalDate) obj;
                    return LocalDateTime.of(o,LocalTime.MIN);
                }
                if(obj instanceof Timestamp) {
                    Timestamp o = ((Timestamp) obj);
                    return LocalDateTime.ofInstant(Instant.ofEpochMilli(o.getTime()), ZoneId.systemDefault());
                }
                if(obj instanceof Long) {
                    Long o = ((Long) obj);
                    return LocalDateTime.ofInstant(Instant.ofEpochMilli(o),ZoneId.systemDefault());
                }
                if(obj instanceof String) {
                    String o = ((String) obj);
                    return LocalDateTime.parse(o,LocalDateTimeUtil.STANDARDDATETIMEFORMATTER);
                }
                logger.error("无法解析的日期时间：{}",obj);
                return null;
            }
            @Override
            public int orderd() {
                return 1700;
            }
        });
        resovers.add(new FiledTypeResover<LocalTime>() {
            @Override
            public LocalTime resove(Object obj) {
                if(obj == null) return null;
                if(obj instanceof LocalTime)
                    return ((LocalTime) obj);
                if(obj instanceof Time) {
                    Time o = ((Time) obj);
                    return o.toLocalTime();
                }
                if(obj instanceof String) {
                    String o = ((String) obj);
                    return LocalTime.parse(o,LocalDateTimeUtil.STANDARDTIMEFORMATTER);
                }
                logger.error("无法解析的时间：{}",obj);
                return null;
            }

            @Override
            public int orderd() {
                return 1800;
            }
        });
        resovers.add(new FiledTypeResover<LocalDate>() {
            @Override
            public LocalDate resove(Object obj) {
                if(obj == null) return  null;
                if(obj instanceof LocalDate)
                    return (LocalDate) obj;
                if(obj instanceof Date) {
                    Date o = ((Date) obj);
                    return o.toLocalDate();
                }
                if(obj instanceof String) {
                    String o = ((String) obj);
                    return LocalDate.parse(o,LocalDateTimeUtil.STANDARDDATEFORMATTER);
                }
                logger.error("无法解析的日期：{}",obj);
                return null;
            }

            @Override
            public int orderd() {
                return 1900;
            }
        });
        resovers.add(new FiledTypeResover<Float>() {
            @Override
            public Float resove(Object obj) {
                if(obj == null)
                    return null;
                if(obj instanceof Float) {
                    Float o = ((Float) obj);
                    return o;
                }
                if(obj instanceof Number) {
                    Number o = (Number) obj;
                    return o.floatValue();
                }
                return Float.valueOf(obj.toString());
            }

            @Override
            public int orderd() {
                return 2000;
            }
        });
        resovers.add(new FiledTypeResover<Short>() {
            @Override
            public Short resove(Object obj) {
                if(obj == null)
                    return null;
                if(obj instanceof Short) {
                    Short o = ((Short) obj);
                    return o;
                }
                if(obj instanceof Number) {
                    return ((Number)obj).shortValue();
                }
                return Short.valueOf(obj.toString());
            }

            @Override
            public int orderd() {
                return 2100;
            }
        });
        resovers.add(new FiledTypeResover<Character>() {
            @Override
            public Character resove(Object obj) {
                return obj == null ? null : Character.valueOf(obj.toString().charAt(0));
            }

            @Override
            public int orderd() {
                return 2200;
            }
        });
        resovers.add(new FiledTypeResover<BigInteger>() {

            @Override
            public BigInteger resove(Object obj) {
                if(obj instanceof BigInteger) {
                    BigInteger o = ((BigInteger) obj);
                    return o;
                }
                if(obj instanceof Number) {
                    return BigInteger.valueOf(((Number)obj).longValue());
                }
                return obj == null ? null : new BigInteger(obj.toString());
            }

            @Override
            public int orderd() {
                return 2300;
            }
        });
        resovers.add(new FiledTypeResover<ByteBuffer>() {

            @Override
            public ByteBuffer resove(Object obj) {
                if(obj == null) return null;
                if(obj.getClass().isArray() && obj.getClass().getComponentType() == byte.class) {
                    return ByteBuffer.wrap((byte[])obj);
                }
                return null;
            }

            @Override
            public int orderd() {
                return 2400;
            }
        });
        resovers.add(new FiledTypeResover<StringOptional>() {

            @Override
            public StringOptional resove(Object obj) {
                if(obj == null) return StringOptional.empty();
                return new StringOptional(obj.toString());
            }

            @Override
            public int orderd() {
                return 2500;
            }
        });
        resovers.add(new FiledTypeResover<Ijson>() {
            @Override
            public Ijson resove(Object obj) {
                return Ijson.parse(obj.toString());
            }

            @Override
            public int orderd() {
                return 2600;
            }
        });
        resovers.add(new FiledTypeResover<Object>() {
            @Override
            public Object resove(Object obj) {
                return obj;
            }

            @Override
            public int orderd() {
                return 5000;
            }
        });
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

    @Override
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
                            case Types.TIMESTAMP:
                                fieldValue = resolve(value,LocalDateTime.class);
                                break;
                            case Types.DATE:
                                fieldValue = resolve(value,LocalDate.class);
                                break;
                            case Types.TIME:
                                fieldValue = resolve(value,LocalTime.class);
                                break;
                            case Types.BINARY:
                            case Types.VARBINARY:
                            case Types.LONGVARBINARY:
                                fieldValue = resolve(value,ByteBuffer.class);
                                break;
                            default :
                                fieldValue = rs.getObject(i);
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