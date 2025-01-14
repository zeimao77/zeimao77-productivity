package top.zeimao77.product.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractFixedLenConverter<K> implements IConverter<K> {

    private static Logger logger = LoggerFactory.getLogger(AbstractFixedLenConverter.class);

    protected int capacity;
    protected ConcurrentHashMap<K, CacheData> ruleRepository;

    private AtomicInteger refreshFlag = new AtomicInteger(0);

    /**
     * 最小刷新间隔 单位秒
     */
    protected int minRefresh;

    /**
     * 上次刷新时间
     */
    protected long lastRefreshTimeMillis;

    public abstract Object doGet(K key);

    public AbstractFixedLenConverter(int capacity,int minRefresh) {
        this.capacity = capacity;
        this.minRefresh = minRefresh;
        this.ruleRepository = new ConcurrentHashMap<>(capacity);
    }

    protected void addConvRule(K key, Object value) {
        this.ruleRepository.put(key,new CacheData(value));
    }

    @Override
    public Object get(K key) {
        Object result = null;
        this.refreshRule(false);
        CacheData cacheData = ruleRepository.get(key);
        if(cacheData != null) {
            cacheData.access();
            return cacheData.getData();
        }
        result = doGet(key);
        if(result != null && this.ruleRepository.size() < capacity) {
            addConvRule(key, result);
        }
        return result;
    }

    @Override
    public void refreshRule(boolean force) {
        if(needRefresh() && refreshFlag.compareAndSet(0,1)) {
            try {
                doRefreshRule();
            } finally {
                refreshFlag.set(0);
            }
        }
    }

    public void doRefreshRule() {
        long flag,flag2;
        {
            long l = System.currentTimeMillis();
            flag = l - this.minRefresh * 600;
            flag2 = l - this.minRefresh * 2000;
        }
        for(Iterator<Map.Entry<K, CacheData>> ite = this.ruleRepository.entrySet().iterator();ite.hasNext();) {
            Map.Entry<K, CacheData> next = ite.next();
            CacheData cacheData = next.getValue();
            if(cacheData.getAccessdTime() < flag || cacheData.getCacheTime() < flag2)
                ite.remove();
        }
        this.lastRefreshTimeMillis = System.currentTimeMillis();
    }

    private boolean needRefresh() {
        if (lastRefreshTimeMillis == 0L)
            this.lastRefreshTimeMillis = System.currentTimeMillis();
        // 超过最小刷新时间  则刷新
        long nextRefreshTimeMillis = this.lastRefreshTimeMillis + this.minRefresh * 1000;
        return nextRefreshTimeMillis < System.currentTimeMillis()
                || this.ruleRepository.size() >= capacity;
    }

    public static class CacheData {
        private long accessdTime;
        private long cacheTime;
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

        public long getAccessdTime() {
            return accessdTime;
        }

        public void setAccessdTime(long accessdTime) {
            this.accessdTime = accessdTime;
        }

        public long getCacheTime() {
            return cacheTime;
        }

        public int getCount() {
            return count;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }
    }

}
