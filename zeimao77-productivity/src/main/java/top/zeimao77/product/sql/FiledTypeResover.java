package top.zeimao77.product.sql;

import org.slf4j.Logger;
import top.zeimao77.product.json.Ijson;
import top.zeimao77.product.model.Orderd;
import top.zeimao77.product.util.CalendarDateUtil;
import top.zeimao77.product.util.LocalDateTimeUtil;
import top.zeimao77.product.util.StringOptional;

import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.*;

/**
 * 通过目标类型的字段值解析器接口
 */
public interface FiledTypeResover<T> extends Orderd {

    Logger logger = org.slf4j.LoggerFactory.getLogger(FiledTypeResover.class);

    default Class<T> getTclass() {
        Class<T> tClass = (Class<T>)((ParameterizedType)getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];
        return tClass;
    }

    /**
     * 是否支持该类型解析
     * @param clazz 类型
     * @param obj 解析对象
     * @return 是否支持
     */
    default boolean support(Class<?> clazz,Object obj) {
        if(clazz.equals(getTclass())) {
            return true;
        }
        return false;
    }

    /**
     * @param obj 解析对象
     * @return 解析结果
     */
    T resove(Object obj);

    FiledTypeResover STRINGTYPERESOVER = new FiledTypeResover<String>() {
        @Override
        public String resove(Object obj) {
            return obj == null ? null : obj.toString();
        }

        @Override
        public int orderd() {
            return 1000;
        }
    };
    FiledTypeResover LONGTYPERESOVER = new FiledTypeResover<Long>() {
        @Override
        public Long resove(Object obj) {
            if(obj == null)
                return null;
            if(obj instanceof Integer o) {
                return o.longValue();
            }
            if(obj instanceof Long o) {
                return o;
            }
            if(obj instanceof Timestamp o) {
                return o.getTime();
            }
            if(obj instanceof LocalDateTime o) {
                return o.toEpochSecond(ZoneId.systemDefault().getRules().getOffset(Instant.now()));
            }
            return Long.valueOf(obj.toString());
        }

        @Override
        public int orderd() {
            return 1100;
        }
    };
    FiledTypeResover INTEGTYPERESOVER = new FiledTypeResover<Integer>() {
        @Override
        public Integer resove(Object obj) {
            if(obj == null)
                return null;
            if(obj instanceof Integer o) {
                return o;
            }
            if(obj instanceof Long o) {
                return o.intValue();
            }
            return Integer.valueOf(obj.toString());
        }

        @Override
        public int orderd() {
            return 1200;
        }
    };
    FiledTypeResover DOUBLETYPERESOVER = new FiledTypeResover<Double>() {

        @Override
        public Double resove(Object obj) {
            if(obj == null)
                return null;
            if(obj instanceof Number o)
                return o.doubleValue();
            return Double.valueOf(obj.toString());
        }

        @Override
        public int orderd() {
            return 1300;
        }
    };
    FiledTypeResover BIGDECIMALTYPERESOVER = new FiledTypeResover<BigDecimal>() {
        @Override
        public BigDecimal resove(Object obj) {
            if( obj == null )
                return null;
            if(obj instanceof BigDecimal o)
                return o;
            if(obj instanceof Number o)
                return BigDecimal.valueOf(o.doubleValue());
            return new BigDecimal(obj.toString());
        }

        @Override
        public int orderd() {
            return 1400;
        }
    };
    FiledTypeResover BOOLEANTYPERESOVER = new FiledTypeResover<Boolean>() {

        @Override
        public Boolean resove(Object obj) {
            if( obj == null )
                return null;
            if(obj instanceof Boolean o)
                return o;
            if(obj instanceof Number o)
                return o.intValue() == 0 ? false : true;
            if(obj instanceof String o)
                return Boolean.valueOf(o);
            return null;
        }

        @Override
        public int orderd() {
            return 1500;
        }
    };
    FiledTypeResover DATETYPERESOVER = new FiledTypeResover<java.util.Date>() {
        @Override
        public java.util.Date resove(Object obj) {
            if(obj == null)
                return null;
            if(obj instanceof LocalDateTime o)
                return CalendarDateUtil.fromLocalDateTime(o);
            if(obj instanceof Time o)
                return new Date(o.getTime());
            if(obj instanceof Date o)
                return new Date(o.getTime());
            if(obj instanceof Timestamp o)
                return new Date(o.getTime());
            if(obj instanceof Long o) {
                return Date.from(Instant.ofEpochMilli(o));
            }
            if(obj instanceof String o)
                return CalendarDateUtil.parseDateTime(o);
            logger.error("无法解析的日期时间：{}",obj);
            return null;
        }

        @Override
        public int orderd() {
            return 1600;
        }
    };
    FiledTypeResover LOCALDATETIMETYPERESOVER = new FiledTypeResover<LocalDateTime>() {
        @Override
        public LocalDateTime resove(Object obj) {
            if(obj == null) return null;
            if(obj instanceof LocalDateTime o)
                return o;
            if(obj instanceof LocalDate o) {
                return LocalDateTime.of(o, LocalTime.MIN);
            }
            if(obj instanceof Timestamp o)
                return LocalDateTime.ofInstant(Instant.ofEpochMilli(o.getTime()), ZoneId.systemDefault());
            if(obj instanceof Long o) {
                return LocalDateTime.ofInstant(Instant.ofEpochMilli(o),ZoneId.systemDefault());
            }
            if(obj instanceof String o)
                return LocalDateTime.parse(o, LocalDateTimeUtil.STANDARDDATETIMEFORMATTER);
            logger.error("无法解析的日期时间：{}",obj);
            return null;
        }
        @Override
        public int orderd() {
            return 1700;
        }
    };

    FiledTypeResover LOCALTIMETYPERESOVER = new FiledTypeResover<LocalTime>() {
        @Override
        public LocalTime resove(Object obj) {
            if(obj == null) return null;
            if(obj instanceof LocalTime o)
                return o;
            if(obj instanceof Time o)
                return o.toLocalTime();
            if(obj instanceof String o)
                return LocalTime.parse(o,LocalDateTimeUtil.STANDARDTIMEFORMATTER);
            logger.error("无法解析的时间：{}",obj);
            return null;
        }

        @Override
        public int orderd() {
            return 1800;
        }
    };

    FiledTypeResover LOCALDATETYPERESOVER = new FiledTypeResover<LocalDate>() {
        @Override
        public LocalDate resove(Object obj) {
            if(obj == null) return  null;
            if(obj instanceof LocalDate o)
                return o;
            if(obj instanceof Date o)
                return o.toLocalDate();
            if(obj instanceof String o)
                return LocalDate.parse(o,LocalDateTimeUtil.STANDARDDATEFORMATTER);
            logger.error("无法解析的日期：{}",obj);
            return null;
        }

        @Override
        public int orderd() {
            return 1900;
        }
    };
    FiledTypeResover FLOATTYPERESOVER = new FiledTypeResover<Float>() {
        @Override
        public Float resove(Object obj) {
            if(obj == null)
                return null;
            if(obj instanceof Float o)
                return o;
            if(obj instanceof Number o)
                return o.floatValue();
            return Float.valueOf(obj.toString());
        }

        @Override
        public int orderd() {
            return 2000;
        }
    };
    FiledTypeResover SHORTTYPERESOVER = new FiledTypeResover<Short>() {
        @Override
        public Short resove(Object obj) {
            if(obj == null)
                return null;
            if(obj instanceof Short o)
                return o;
            if(obj instanceof Number o)
                return o.shortValue();
            return Short.valueOf(obj.toString());
        }

        @Override
        public int orderd() {
            return 2100;
        }
    };
    FiledTypeResover CHARTYPERESOVER = new FiledTypeResover<Character>() {
        @Override
        public Character resove(Object obj) {
            return obj == null ? null : Character.valueOf(obj.toString().charAt(0));
        }

        @Override
        public int orderd() {
            return 2200;
        }
    };
    FiledTypeResover BIGINTTYPERESOVER = new FiledTypeResover<BigInteger>() {

        @Override
        public BigInteger resove(Object obj) {
            if(obj instanceof BigInteger o)
                return o;
            if(obj instanceof Number o) {
                return BigInteger.valueOf(o.longValue());
            }
            return obj == null ? null : new BigInteger(obj.toString());
        }

        @Override
        public int orderd() {
            return 2300;
        }
    };
    FiledTypeResover BYTEBUFFERTYPERESOVER = new FiledTypeResover<ByteBuffer>() {

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
    };
    FiledTypeResover FIELDTYPERESOVER = new FiledTypeResover<StringOptional>() {

        @Override
        public StringOptional resove(Object obj) {
            if(obj == null) return StringOptional.empty();
            return new StringOptional(obj.toString());
        }

        @Override
        public int orderd() {
            return 2500;
        }
    };

    FiledTypeResover IJSONTYPERESOVER = new FiledTypeResover<Ijson>() {
        @Override
        public Ijson resove(Object obj) {
            return Ijson.parse(obj.toString());
        }

        @Override
        public int orderd() {
            return 2600;
        }
    };

    FiledTypeResover OBJECTTYPERESOVER = new FiledTypeResover<Object>() {
        @Override
        public Object resove(Object obj) {
            return obj;
        }

        @Override
        public int orderd() {
            return 5000;
        }
    };
}
