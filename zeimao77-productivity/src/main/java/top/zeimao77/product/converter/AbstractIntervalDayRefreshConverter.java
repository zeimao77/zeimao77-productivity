package top.zeimao77.product.converter;

import java.time.*;

/**
 * @author zeimao77
 * 隔天刷新，以cacheTime为界，如果在此时间点之后取数据时没有刷新过则刷新
 */
public abstract class AbstractIntervalDayRefreshConverter<K> extends AbstractCustomRefreshConverter<K>{

    /**
     * 刷新时间界点 如果在此时间点之后取数据时没有刷新过则刷新
     */
    protected LocalTime cacheTime;

    /**
     * 如果在当天cacheTime之后来调用转换，没有刷新过则刷新
     * @param cacheTime 时间界限标记
     */
    public AbstractIntervalDayRefreshConverter(LocalTime cacheTime) {
        this.cacheTime = cacheTime;
    }

    /**
     * 缺省时间5点整
     */
    public AbstractIntervalDayRefreshConverter(){
        this.cacheTime = LocalTime.of(5,0,0);
    }

    @Override
    protected void refreshExpiryTime() {
        setExpiryTime(LocalDateTime.of(LocalDate.now().plusDays(1),this.cacheTime));
    }
}
