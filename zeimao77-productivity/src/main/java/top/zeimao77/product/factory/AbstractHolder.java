package top.zeimao77.product.factory;

import java.util.HashMap;

public abstract class AbstractHolder<K,V> {

    public HashMap<K,V> store = new HashMap<>();

    public abstract V create(K k);

    public V get(K k) {
        if(!store.containsKey(k)) {
            synchronized (this) {
                if(!store.containsKey(k)) {
                    V v = create(k);
                    store.put(k, v);
                }
            }
        }
        return store.get(k);
    }

}
