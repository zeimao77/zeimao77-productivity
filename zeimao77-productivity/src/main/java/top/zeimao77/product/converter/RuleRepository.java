package top.zeimao77.product.converter;

public interface RuleRepository<K> {

    Object put(K key, Object value);

    Object get(K key);

    boolean containsKey(K key);

    boolean isEmpty();

    void clear();
}
