package top.zeimao77.product.converter;

import top.zeimao77.product.exception.ExceptionCodeDefinition;
import top.zeimao77.product.util.AssertUtil;

import java.lang.ref.SoftReference;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 这是一个在内存不够时会优先被回收的转换器
 * 在使用前请先调用lock() 锁住 这样在使用期间不会被回收
 * 在使用完成之后调用unlock() 这样在内存不够时将优先回收它
 * 它比较适合保存比较大的缓存数据
 * @param <K>
 * @since 2.2.1
 */
public abstract class AbstractSoftReferenceRuleConterver<K> implements IConverter<K>{

    private MemoryRuleRepository<K> memoryRuleRepository;
    private SoftReference<MemoryRuleRepository<K>> softReferenceMemoryRuleRepository = new SoftReference<>(null);
    private AtomicInteger refrenceCount = new AtomicInteger(0);
    protected ReentrantLock lock = new ReentrantLock();

    public synchronized void lock() {
        if(memoryRuleRepository == null)
            memoryRuleRepository = softReferenceMemoryRuleRepository.get();
        if(memoryRuleRepository == null) {
            memoryRuleRepository = new MemoryRuleRepository<K>();
            softReferenceMemoryRuleRepository = new SoftReference<>(memoryRuleRepository);
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

    @Override
    public void refreshRule() {
        AssertUtil.assertTrue(this.refrenceCount.get() > 0, ExceptionCodeDefinition.WRONG_ACTION,"缓存处于软引用状态，请先上锁");
        if(memoryRuleRepository.isEmpty()) {
            lock.lock();
            try {
                if(memoryRuleRepository.isEmpty())
                    refresh();
            } finally {
                lock.unlock();
            }
        }
    }

    @Override
    public Object get(K key) {
        refreshRule();
        Object resultValue = this.memoryRuleRepository.get(key);
        return resultValue == null ? defaultName(key) : resultValue;
    }

}
