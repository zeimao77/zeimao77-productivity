package top.zeimao77.product.model;

import java.util.HashMap;

public class DoubleKeyHashMap<L,R,V> extends HashMap<ImmutablePair<L,R>,V> {

    private static final long serialVersionUID = 372498820763181265L;

    public DoubleKeyHashMap(){
        super();
    }

    /**
     * @see HashMap#HashMap(int)
     * @param initialCapacity 初始容量
     */
    public DoubleKeyHashMap(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * @param lkey 左键
     * @param rkey 右键
     * @param value 值
     * @return 插入之前键对应的值
     */
    public V put(L lkey,R rkey, V value) {
        ImmutablePair<L,R> key = new ImmutablePair<>(lkey,rkey);
        return put(key,value);
    }

    /**
     * @param lkey 左键
     * @param rkey 右键
     * @return 值
     */
    public V get(L lkey,R rkey) {
        ImmutablePair<L,R> key = new ImmutablePair<>(lkey,rkey);
        return get(key);
    }

    public boolean containsKey(L lkey,R rkey) {
        return containsKey(new ImmutablePair<>(lkey,rkey));
    }

    /**
     * 移除
     * @param lkey key左值
     * @param rkey key右值
     * @param value 如果为null将通过键移除 非null通过键值对移除
     * @return 移除的值
     */
    public V remove(L lkey,R rkey,V value) {
        if(value == null) {
            return remove(new ImmutablePair<>(lkey,rkey));
        } else {
            return remove(new ImmutablePair<>(lkey,rkey),value)?value:null;
        }

    }
}
