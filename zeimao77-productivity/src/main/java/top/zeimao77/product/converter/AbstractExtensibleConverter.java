package top.zeimao77.product.converter;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class AbstractExtensibleConverter<K> implements IConverter<K> {

    protected ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    protected int refreshFalg = 0;

    @Override
    public void refreshRule(boolean force) {
        if(force || needRefresh()) {
            clear();
            refreshFalg = 0;
            this.refresh();
            refreshFalg |= REFRESHFLAG;
        }
    }

    @Override
    public Object get(K key) {
        refreshRule(false);
        Object result = defaultName(key);
        try {
            lock.readLock().lock();
            result = this.doGet(key);
        } finally {
            lock.readLock().unlock();
        }
        return result;
    }

    public abstract Object doGet(K key);

    public void clear() {}

    protected boolean needRefresh() {
        return (refreshFalg & REFRESHFLAG) == 0;
    }

    protected abstract void refresh();

    protected abstract void addConvRule(K key, Object value);


}
