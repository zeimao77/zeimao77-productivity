package top.zeimao77.product.converter;

import java.time.LocalDateTime;

/**
 * @author zeimao77
 * 本次取数据时间与上次刷新时间相差cacheTime秒，触发刷新操作
 */
public abstract class AbstractIntervalRefreshConverter<K> extends AbstractCustomRefreshConverter<K>{

    /**
     * 时间间隔 刷新后会将过期时间设置为当前时间加上该时间 单位：秒
     */
    protected long cacheTime;

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
        super.refreshRule();
        setExpiryTime(LocalDateTime.now().plusSeconds(this.cacheTime));
    }


}
