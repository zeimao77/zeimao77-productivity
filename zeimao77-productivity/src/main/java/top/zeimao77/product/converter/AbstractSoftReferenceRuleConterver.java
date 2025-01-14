package top.zeimao77.product.converter;

import top.zeimao77.product.exception.ExceptionCodeDefinition;
import top.zeimao77.product.util.AssertUtil;

import java.lang.ref.SoftReference;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 这是一个在内存不够时会优先被回收的转换器
 * 在使用前请先调用lock() 锁住 这样在使用期间不会被回收
 * 在使用完成之后调用unlock() 这样在内存不够时将优先回收它
 * 它比较适合保存比较大的缓存数据
 * @param <K>
 * @since 2.2.1
 */
public abstract class AbstractSoftReferenceRuleConterver<K> implements IConverter<K> {

    private MemoryRuleRepository<K> memoryRuleRepository;
    private SoftReference<MemoryRuleRepository<K>> softReferenceMemoryRuleRepository = new SoftReference<>(null);
    private AtomicInteger refrenceCount = new AtomicInteger(0);
    protected ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    protected LocalDateTime expiryTime = LocalDateTime.of(2000,1,1,0,0,0);
    protected int refreshFalg = 0;


    public void lock() {
        if(memoryRuleRepository == null)
            memoryRuleRepository = softReferenceMemoryRuleRepository.get();
        if(memoryRuleRepository == null) {
            try {
                lock.writeLock().lock();
                memoryRuleRepository = softReferenceMemoryRuleRepository.get();
                if(memoryRuleRepository == null) {
                    memoryRuleRepository = new MemoryRuleRepository<K>();
                    softReferenceMemoryRuleRepository = new SoftReference<>(memoryRuleRepository);
                    refreshFalg = 0;
                }
            } finally {
                lock.writeLock().unlock();
            }
        }
        refrenceCount.addAndGet(1);
    }

    public void unLock() {
        int refCount = refrenceCount.decrementAndGet();
        if(refCount == 0)
            memoryRuleRepository = null;
    }

    protected abstract void refresh();

    protected void addConvRule(K key, Object value) {
        this.memoryRuleRepository.put(key,value);
    }

    public void clear() {
        this.memoryRuleRepository.clear();
    }

    @Override
    public void refreshRule(boolean force) {
        AssertUtil.assertTrue(this.refrenceCount.get() > 0, ExceptionCodeDefinition.WRONG_ACTION,"缓存处于软引用状态，请先上锁");
        if(force || needRefresh()) {
            lock.writeLock().lock();
            try {
                if(force || needRefresh())
                    clear();
                    refreshFalg = 0;
                    refresh();
                    refreshFalg |= REFRESHFLAG;
                    refreshExpiryTime();
            } finally {
                lock.writeLock().unlock();
            }
        }
    }

    protected abstract void refreshExpiryTime();

    protected boolean needRefresh() {
        long between = ChronoUnit.SECONDS.between(LocalDateTime.now(), expiryTime);
        return (refreshFalg & REFRESHFLAG) == 0 || between < 0;
    }


    @Override
    public Object get(K key) {
        refreshRule(false);
        try {
            lock.readLock().lock();
            Object resultValue = this.memoryRuleRepository.get(key);
            return resultValue == null ? defaultName(key) : resultValue;
        }finally {
            lock.readLock().unlock();
        }
    }

}
