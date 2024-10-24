package top.zeimao77.product.sql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zeimao77.product.exception.BaseServiceRunException;
import top.zeimao77.product.util.StringOptional;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import static top.zeimao77.product.exception.ExceptionCodeDefinition.NOT_SUPPORTED;

public class BeanResultMapResolver<T> implements ResultSetResolver {

    private static Logger logger = LoggerFactory.getLogger(BeanResultMapResolver.class);

    private Class<T> clazz;
    private List<T> list;

    private DefaultFieldTypeResover fieldTypeResover = DefaultFieldTypeResover.DEFAULT;

    public BeanResultMapResolver(Class<T> clazz, List<T> list) {
        this.clazz = clazz;
        this.list = list;
    }

    public BeanResultMapResolver(Class<T> clazz, List<T> list, DefaultFieldTypeResover fieldTypeResover) {
        this.clazz = clazz;
        this.list = list;
        this.fieldTypeResover = fieldTypeResover;
    }

    public <T> T newObj(Class<T> clazz) {
        try {
            T obj = clazz.getDeclaredConstructor().newInstance();
            return obj;
        } catch (InstantiationException e) {
            throw new BaseServiceRunException("错误", e);
        } catch (IllegalAccessException e) {
            throw new BaseServiceRunException("错误", e);
        } catch (InvocationTargetException e) {
            throw new BaseServiceRunException("错误", e);
        } catch (NoSuchMethodException e) {
            throw new BaseServiceRunException(NOT_SUPPORTED, "构造方法未找到", e);
        }
    }

    @Override
    public void resolve(ResultSet rs, ResultSetMetaData rsmd) throws SQLException {
        T obj = newObj(clazz);
        int colCount = rsmd.getColumnCount();
        for (int i = 1; i <= colCount; i++) {
            String columnLabel = rsmd.getColumnLabel(i);
            Object fieldValue = null;
            Object value = rs.getObject(i);
            try {
                Field field = clazz.getDeclaredField(columnLabel);
                // 基本数据类型
                if (value == null) {
                    fieldValue = null;
                } else if (field.getType() == long.class) {
                    fieldValue = value == null ? 0L : Long.valueOf(value.toString()).longValue();
                } else if (field.getType() == int.class) {
                    fieldValue = value == null ? 0 : Integer.valueOf(value.toString()).intValue();
                } else if (field.getType() == float.class) {
                    fieldValue = value == null ? 0F : Float.valueOf(value.toString()).floatValue();
                } else if (field.getType() == double.class) {
                    fieldValue = value == null ? 0D : Double.valueOf(value.toString()).doubleValue();
                } else if (field.getType() == short.class) {
                    fieldValue = value == null ? 0 : Short.valueOf(value.toString()).shortValue();
                } else if (field.getType() == byte.class) {
                    fieldValue = value == null ? 0x00 : Byte.valueOf(value.toString());
                } else if (field.getType() == char.class) {
                    fieldValue = value == null ? 0x00 : value.toString().charAt(0);
                } else if (field.getType() == boolean.class) {
                    fieldValue = value == null ? false : Boolean.valueOf(value.toString()).booleanValue();
                } else {
                    // 非基本数据类型
                    fieldValue = this.fieldTypeResover.resolve(value, field.getType());
                }
                field.setAccessible(true);
                if (fieldValue == null && field.getType() == StringOptional.class)
                    fieldValue = StringOptional.empty();
                field.set(obj, fieldValue);
            } catch (NoSuchFieldException e) {
                logger.debug("字段未找到:{}", columnLabel);
            } catch (IllegalAccessException e) {
                throw new BaseServiceRunException("参数错误", e);
            }
        }
        list.add(obj);
    }

}
