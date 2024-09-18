package top.zeimao77.product.converter;

import java.util.function.Function;

/**
 * @author zeimao77
 * @since 2.0.10
 * 转换器接口
 */
public interface IConverter<K> {

    int REFRESHFLAG = 0x01;  // 刷新标志
    int REFRESHFLAG_SUCCESS = 0x02;  // 刷新成功标志



    /**
     * 默认的INT类型转换
     */
    Function<Object, Integer> DEFAULT_INTEGER_NAME = o -> o == null ? null : (Integer)o;
    /**
     * 默认的LONG类型转换
     */
    Function<Object,Long> DEFAULT_LONG_NAME = o -> o == null ? null : (Long)o;

    /**
     *
     * @param key key键
     * @return 源值对象
     */
    Object get(K key);

    /**
     * 刷新转换规则
     */
    void refreshRule();

    /**
     * @param key 键
     * @param fun 自定义返回结果
     * @param <T> 返回类型
     * @return 结果
     */
    default <T> T getName(K key, Function<Object,T> fun){
        Object o = get(key);
        if(o == null)
            return null;
        return fun.apply(o);
    }

    /**
     * @param key 键值
     * @return 以字符串方式返回结果
     */
    default String getName(K key){
        return getName(key,Object::toString);
    }

    /**
     * 如果字典不存在，将返回的默认值
     * @param key 健值
     * @return 默认值
     */
    default Object defaultName(K key){
        return "";
    }


}
