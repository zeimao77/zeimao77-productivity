package top.zeimao77.product.converter;

import java.util.function.Function;

/**
 * @author zeimao77
 * 转换器接口
 */
public interface IConverter<K> {


    /**
     * 默认的INT类型转换
     */
    Function<Object, Integer> DEFAULT_INTEGER_NAME = o -> o == null ? null : (Integer)o;

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
        return fun.apply(get(key));
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
