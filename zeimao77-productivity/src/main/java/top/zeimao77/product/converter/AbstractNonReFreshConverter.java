package top.zeimao77.product.converter;

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
        this.refreshRule();
        Object result = defaultName(key);
        if(convRuleMap.containsKey(key)) {
            result = convRuleMap.get(key);
        }
        return result;
    }

    /**
     * 刷新规则
     * 该函数用锁控制了刷新函数只能串行执行
     */
    @Override
    public void refreshRule() {
        if (this.convRuleMap.isEmpty()) {
            lock.lock();
            try {
                if (this.convRuleMap.isEmpty()) {
                    this.refresh();
                }
            }finally {
                lock.unlock();
            }
        }
    }

    /**
     * 刷新规则的具体实现;
     * 在该函数中调用
     * @see AbstractNonReFreshConverter#addConvRule(Object, Object)
     * 来添加规则
     */
    protected abstract void refresh();

}
