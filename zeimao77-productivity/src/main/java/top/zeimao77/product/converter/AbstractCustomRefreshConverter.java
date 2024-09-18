package top.zeimao77.product.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zeimao77.product.util.LocalDateTimeUtil;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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
    protected ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    /**
     * 未刷新过标志 刷新后置false
     */
    protected int refreshFalg = 0;
    /**
     * 规则缓存
     */
    protected RuleRepository<K> ruleRepository = new MemoryRuleRepository<>();

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
        this.ruleRepository.put(key,value);
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
        try {
            lock.readLock().lock();
            Object resultValue = this.ruleRepository.get(key);
            return resultValue == null ? defaultName(key) : resultValue;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * 规则刷新函数
     */
    @Override
    public void refreshRule() {
        if(needRefresh()) {
            lock.writeLock().lock();
            try {
                if(needRefresh()) {
                    clear();
                    refreshFalg = 0;
                    refresh();
                    refreshFalg |= REFRESHFLAG;
                    refreshExpiryTime();
                }
            } finally {
                lock.writeLock().unlock();
            }
        }
    }

    protected boolean needRefresh() {
        long between = ChronoUnit.SECONDS.between(LocalDateTime.now(), expiryTime);
        return (refreshFalg & REFRESHFLAG) == 0 || between < 0;
    }

    /**
     * 刷新结束时更新过期时间
     */
    protected abstract void refreshExpiryTime();

    /**
     * 刷新规则的具体实现;
     * 在该函数中调用
     * 来添加规则
     */
    protected abstract void refresh();

    /**
     * @return 过期时间
     */
    public LocalDateTime getExpiryTime() {
        return expiryTime;
    }

    public void clear() {
        this.ruleRepository.clear();
    }

    public RuleRepository<K> getRuleRepository() {
        return ruleRepository;
    }

    public void setRuleRepository(RuleRepository<K> ruleRepository) {
        this.ruleRepository = ruleRepository;
    }
}
