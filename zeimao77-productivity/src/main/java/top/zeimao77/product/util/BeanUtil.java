package top.zeimao77.product.util;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.zeimao77.product.exception.BaseServiceRunException;

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
            throw new BaseServiceRunException("错误",e);
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
            throw new BaseServiceRunException("错误",e);
        }
    }

    /**
     * @param bean BEAN对象
     * @param name 属性名
     * @return 属性对类的类型
     */
    public static Class<?> getPropertyType(final Object bean, final String name) {
        try {
            return PropertyUtils.getPropertyType(bean,name);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new BaseServiceRunException("获取属性类型错误",e);
        }
    }

    /**
     * 设置属性的值
     * @param bean BEAN对象
     * @param name 属性名
     * @param value 值
     */
    public static void setProperty(final Object bean, final String name, final Object value) {
        try {
            PropertyUtils.setProperty(bean,name,value);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new BaseServiceRunException("设置属性错误",e);
        }
    }

    /**
     * 获取属性的值
     * @param bean BEAN对象
     * @param name 属性名
     * @return 属性值
     */
    public static Object getProperty(final Object bean, final String name) {
        try {
            return PropertyUtils.getProperty(bean,name);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new BaseServiceRunException("获取属性错误",e);
        }
    }

}
