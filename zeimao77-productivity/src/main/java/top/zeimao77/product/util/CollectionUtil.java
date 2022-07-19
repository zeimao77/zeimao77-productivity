package top.zeimao77.product.util;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author zeimao77
 * 集合工具
 */
public class CollectionUtil {

    private CollectionUtil(){}

    /**
     * 求并集
     * @param t1 集合1
     * @param t2 集合2
     * @param <T> 对象类型
     * @return 并集
     */
    public static <T> Set<T> union(Collection<T> t1, Collection<T> t2){
        HashSet set = new HashSet<>(t1);
        set.addAll(t2);
        return set;
    }

    /**
     * 求差集
     * @param t1 集合1
     * @param t2 集合2
     * @param <T> 对象类型
     * @return 差集
     */
    public static <T> Set<T> diff(Collection<T> t1, Collection<T> t2){
        HashSet set = new HashSet<>(t1);
        set.removeAll(t2);
        return set;
    }

    /**
     * 求交集
     * @param t1 集合1
     * @param t2 集合2
     * @param <T> 对象类型
     * @return 交集
     */
    public static <T> Set<T> inter(Collection<T> t1, Collection<T> t2){
        HashSet set = new HashSet<>(t1);
        set.retainAll(t2);
        return set;
    }

    /**
     * 从列表中随机取值
     * @param list 列表
     * @param <T> 集合类型
     * @return 值
     */
    public static <T> T getRandom(List<T> list) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int i = random.nextInt(list.size());
        return list.get(i);
    }

}
