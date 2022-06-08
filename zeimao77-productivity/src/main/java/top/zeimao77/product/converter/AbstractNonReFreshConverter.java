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
    private ReentrantLock lock = new ReentrantLock();

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
        if (this.convRuleMap.isEmpty()) {
            lock.lock();
            if (this.convRuleMap.isEmpty()) {
                this.refreshRule();
            }
            lock.unlock();
        }
        Object result = defaultName(key);
        if(convRuleMap.containsKey(key)) {
            result = convRuleMap.get(key);
        }
        return result;
    }

    @Override
    public void refreshRule() {
        this.refresh();
    }

    protected abstract void refresh();

}
