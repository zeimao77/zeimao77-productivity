package top.zeimao77.product.mysql;

import top.zeimao77.product.model.Orderd;

import java.lang.reflect.ParameterizedType;

/**
 * 通过目标类型的字段值解析器接口
 */
public interface FiledTypeResover<T> extends Orderd {

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
}
