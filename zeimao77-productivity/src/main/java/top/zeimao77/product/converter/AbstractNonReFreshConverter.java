package top.zeimao77.product.converter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zeimao77
 * 没有刷新规则的转换器
 * @param <K> 健值类型
 */
public abstract class AbstractNonReFreshConverter<K> implements IConverter<K> {

    /**
     * 转换关系MAP
     */
    protected Map<K,Object> convRuleMap;
    protected int refreshFalg = 0;
    protected ReentrantLock lock = new ReentrantLock();

    /**
     * 公开构造
     */
    public AbstractNonReFreshConverter(){
        this.convRuleMap = new HashMap<>();
    }

    /**
     * @param convRuleMap 转换规则
     */
    public AbstractNonReFreshConverter(Map<K,Object> convRuleMap){
        this.convRuleMap = convRuleMap;
    }

    /**
     * 添加转换规则
     * @param key 键
     * @param value 值
     */
    protected void addConvRule(K key, Object value) {
        this.convRuleMap.put(key,value);
    }

    @Override
    public Object get(K key) {
        refreshRule(false);
        Object resultValue = this.convRuleMap.get(key);
        return resultValue == null ? defaultName(key) : resultValue;
    }

    /**
     * 刷新规则
     * 该函数用锁控制了刷新函数只能串行执行
     */
    @Override
    public void refreshRule(boolean force) {
        if (force || needRefresh()) {
            lock.lock();
            try {
                if (force || needRefresh()) {
                    this.refresh();
                    refreshFalg |= REFRESHFLAG;
                }
            }finally {
                lock.unlock();
            }
        }
    }

    protected boolean needRefresh() {
        return (refreshFalg & REFRESHFLAG) == 0;
    }


    /**
     * 刷新规则的具体实现;
     * 在该函数中调用
     * @see AbstractNonReFreshConverter#addConvRule(Object, Object)
     * 来添加规则
     */
    protected abstract void refresh();

}
