package top.zeimao77.product.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zeimao77.product.util.LocalDateTimeUtil;

import java.time.Instant;
import java.time.ZoneId;

/**
 * @author zeimao77
 * 本次取数据时间与上次刷新时间相差cacheTime秒，触发刷新操作
 */
public abstract class AbstractIntervalRefreshConverter<K> extends AbstractCustomRefreshConverter<K>{

    private static Logger logger = LoggerFactory.getLogger(AbstractIntervalRefreshConverter.class);
    private long cacheTime;

    /**
     * @param cacheTime 缓存时间 单位秒
     */
    public AbstractIntervalRefreshConverter(long cacheTime) {
        this.cacheTime = cacheTime;
    }

    /**
     * 默认缓存时间3600秒
     */
    public AbstractIntervalRefreshConverter(){
        this(0x0E10);
    }


    @Override
    public void refreshRule() {
        refresh();
        setExpiryTime(Instant.ofEpochSecond(Instant.now().getEpochSecond() + this.cacheTime), ZoneId.systemDefault());
        logger.info(String.format("[%s]最新刷新时间：%s", this.getClass().getSimpleName(), LocalDateTimeUtil.nowDateTime()));
    }


}
