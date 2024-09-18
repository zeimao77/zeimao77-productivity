package top.zeimao77.product.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractFixedLenConverter<K> implements IConverter<K> {

    private static Logger logger = LoggerFactory.getLogger(AbstractFixedLenConverter.class);

    protected int capacity;
    protected ConcurrentHashMap<K, CacheData> ruleRepository;

    private AtomicInteger refreshFalg = new AtomicInteger(0);

    /**
     * 最小刷新间隔 单位秒
     */
    protected int minRefresh;

    /**
     * 上次刷新时间
     */
    protected LocalDateTime lastRefresh;

    public abstract Object doGet(K key);

    public AbstractFixedLenConverter(int capacity,int minRefresh) {
        this.capacity = capacity;
        this.minRefresh = minRefresh;
        this.ruleRepository = new ConcurrentHashMap<>(capacity);
    }

    protected synchronized void addConvRule(K key, Object value) {
        this.ruleRepository.put(key,new CacheData(value));
    }

    @Override
    public Object get(K key) {
        Object result = null;
        CacheData cacheData = ruleRepository.get(key);
        if(cacheData != null) {
            cacheData.access();
            return cacheData.getData();
        }
        this.refreshRule();
        result = doGet(key);

        if(refreshFalg.get() == 0 && result != null && this.ruleRepository.size() < capacity) {
            addConvRule(key, result);
        }
        return result;
    }

    @Override
    public synchronized void refreshRule() {
        try {
            if(needRefresh()) {
                refreshFalg.set(1);
                logger.info("正在清理......................");
                doRefreshRule();
            }
        } finally {
            refreshFalg.set(0);
        }
    }

    public void doRefreshRule() {
        long flag,flag2;
        {
            long l = System.currentTimeMillis();
            flag = l - this.minRefresh * 500;
            flag2 = l - this.minRefresh * 2000;
        }
        for(Iterator<Map.Entry<K, CacheData>> ite = this.ruleRepository.entrySet().iterator();ite.hasNext();) {
            Map.Entry<K, CacheData> next = ite.next();
            CacheData cacheData = next.getValue();
            if(cacheData.getAccessdTime() < flag)
                ite.remove();
            if(cacheData.getCacheTime() < flag2)
                ite.remove();
        }
        this.lastRefresh = LocalDateTime.now();
    }

    private boolean needRefresh() {
        if(lastRefresh == null)
            lastRefresh = LocalDateTime.now();
        // 超过2倍的最小刷新时间  则刷新
        LocalDateTime nextRefreshTime = lastRefresh.plusSeconds(minRefresh*2);
        if(nextRefreshTime.isBefore(LocalDateTime.now()))
            return true;
        // 不满 则不用清理
        if(this.ruleRepository.size() < capacity)
            return false;
        // 没有满足最小刷新时间 则不清
        nextRefreshTime = lastRefresh.plusSeconds(minRefresh);
        if(nextRefreshTime.isAfter(LocalDateTime.now()))
            return false;
        return true;
    }

    public static class CacheData {
        private Long accessdTime;
        private Long cacheTime;
        private int count;
        private Object data;

        public CacheData(Object data) {
            this.data = data;
            this.accessdTime = System.currentTimeMillis();
            this.cacheTime = accessdTime;
            this.count = 1;
        }

        public void access() {
            this.accessdTime = System.currentTimeMillis();
            this.count++;
        }

        public Long getCacheTime() {
            return cacheTime;
        }

        public void setCacheTime(Long cacheTime) {
            this.cacheTime = cacheTime;
        }

        public Long getAccessdTime() {
            return accessdTime;
        }

        public void setAccessdTime(Long accessdTime) {
            this.accessdTime = accessdTime;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }
    }

}
