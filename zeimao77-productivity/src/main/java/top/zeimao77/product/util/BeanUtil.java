package top.zeimao77.product.util;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import top.zeimao77.product.exception.BaseServiceRunException;
import top.zeimao77.product.json.Ijson;

import static top.zeimao77.product.exception.ExceptionCodeDefinition.WRONG_SOURCE;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * BEAN 工具
 */
public class BeanUtil {

    private BeanUtil(){}

    /**
     * 将一个Bean转成Map
     * @param obj bean对象
     * @return Map
     */
    public static Map<String,Object> beanMap(Object obj) {
        try {
            Map<String, Object> describe = PropertyUtils.describe(obj);
            return describe;
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new BaseServiceRunException(WRONG_SOURCE,"错误",e);
        }
    }

    /**
     * 将Map转Bean
     * @param map map
     * @param clazz Bean类对象
     * @param <T> 转换的目标Bean类型
     * @return bean对象
     */
    public static <T> T bean(Map<String,Object> map,Class<T> clazz) {
        try {
            T bean = clazz.getConstructor().newInstance();
            BeanUtils.populate(bean,map);
            return bean;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new BaseServiceRunException(WRONG_SOURCE,"错误",e);
        }
    }

    /**
     * @param bean BEAN对象
     * @param name 属性名
     * @return 属性对类的类型
     */
    public static Class<?> getPropertyType(final Object bean, final String name) {
        if(bean instanceof Map) {
            return ((Map)bean).get(name).getClass();
        } else if (bean instanceof Ijson ijson) {
            return ijson.getProperty(name).getClass();
        }
        try {
            Field field = bean.getClass().getDeclaredField(name);
            return field.getType();
        } catch (NoSuchFieldException e) {
        }
        return null;
    }

    /**
     * 设置属性的值
     * @param bean BEAN对象
     * @param name 属性名
     * @param value 值
     */
    public static void setProperty(final Object bean, final String name, final Object value) {
        if(bean instanceof Map map) {
            map.put(name,value);
        }
        try {
            Field field = bean.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(bean,value);
        } catch (NoSuchFieldException e) {
        } catch (IllegalAccessException e) {
        }
    }

    /**
     * 获取属性的值
     * @param bean BEAN对象
     * @param name 属性名
     * @return 属性值
     */
    public static Object getProperty(final Object bean, final String name) {
        if(bean instanceof Map) {
            return ((Map)bean).get(name);
        } else if (bean instanceof Ijson ijson) {
            return ijson.getProperty(name);
        }
        try {
            Field field = bean.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.get(bean);
        } catch (NoSuchFieldException e) {
        } catch (IllegalAccessException e) {
        }
        return null;
    }

}
