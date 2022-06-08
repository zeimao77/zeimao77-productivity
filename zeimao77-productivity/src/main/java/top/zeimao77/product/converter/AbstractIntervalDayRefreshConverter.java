package top.zeimao77.product.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zeimao77.product.util.LocalDateTimeUtil;

import java.time.*;

/**
 * @author zeimao77
 * 隔天刷新，以cacheTime为界，如果在此时间之后取数据时没有刷新过则刷新
 */
public abstract class AbstractIntervalDayRefreshConverter<K> extends AbstractCustomRefreshConverter<K>{

    private LocalTime cacheTime;
    private static Logger logger = LoggerFactory.getLogger(AbstractIntervalDayRefreshConverter.class);

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
    public void refreshRule() {
        refresh();
        setExpiryTime(LocalDateTime.of(LocalDate.now().plusDays(1),this.cacheTime));
        logger.info(String.format("[%s]最新刷新时间：%s", this.getClass().getSimpleName(), LocalDateTimeUtil.nowDateTime()));
    }

}
