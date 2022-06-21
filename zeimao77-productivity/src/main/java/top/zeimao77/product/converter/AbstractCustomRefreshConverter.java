package top.zeimao77.product.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zeimao77.product.util.LocalDateTimeUtil;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 自定义过期时间
 * @param <K>  Key类型
 */
public abstract class AbstractCustomRefreshConverter<K> implements IConverter<K>{

    private static Logger logger = LoggerFactory.getLogger(AbstractCustomRefreshConverter.class);
    /**
     * 过期时间
     */
    protected LocalDateTime expiryTime = LocalDateTime.of(2000,1,1,0,0,0);
    /**
     * 规则刷新锁，防止并发刷新规则;
     */
    protected ReentrantLock lock = new ReentrantLock();
    /**
     * 规则缓存
     */
    protected HashMap<K,Object> convRuleMap = new HashMap<>();

    /**
     * 设置过期时间
     * @param expiryTime 过期时间
     */
    protected void setExpiryTime(LocalDateTime expiryTime) {
        this.expiryTime = expiryTime;
        logger.debug(String.format("[%s]设置了过期时间：%s", this.getClass().getSimpleName(), LocalDateTimeUtil.toDateTime(this.expiryTime)));
    }

    /**
     * 设置过期时间
     * @param instant 时间戳 （UTC时区）
     * @param zone 时区 缺省值：ZoneId.systemDefault()
     */
    protected void setExpiryTime(Instant instant, ZoneId zone) {
        setExpiryTime(LocalDateTime.ofInstant(instant,zone == null ? ZoneId.systemDefault():zone));
    }

    /**
     * 添加转换规则
     * @param key 键
     * @param value 值
     */
    protected void addConvRule(K key, Object value) {
        this.convRuleMap.put(key,value);
    }

    /**
     * 如果规则为空或者过期将调用刷新 过期参数跳转:
     * @see AbstractCustomRefreshConverter#refreshRule()
     * @param key key键
     * @return 值
     */
    @Override
    public Object get(K key) {
        refreshRule();
        Object resultValue = defaultName(key);
        if (this.convRuleMap.containsKey(key)) {
            resultValue = this.convRuleMap.get(key);
        }
        return resultValue;
    }

    /**
     * 规则刷新函数
     */
    @Override
    public void refreshRule() {
        long between = ChronoUnit.SECONDS.between(LocalDateTime.now(), expiryTime);
        if(convRuleMap.isEmpty() || between <= 0) {
            lock.lock();
            between = ChronoUnit.SECONDS.between(LocalDateTime.now(), expiryTime);
            if(convRuleMap.isEmpty() || between <= 0) {
                refresh();
            }
            lock.unlock();
        }
    }

    /**
     * 子类实现规则刷新
     */
    protected abstract void refresh();

    public LocalDateTime getExpiryTime() {
        return expiryTime;
    }
}
