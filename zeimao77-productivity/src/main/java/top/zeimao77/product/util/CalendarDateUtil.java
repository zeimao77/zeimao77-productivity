package top.zeimao77.product.util;

import top.zeimao77.product.exception.BaseServiceRunException;

import static top.zeimao77.product.exception.ExceptionCodeDefinition.APPERR;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * java.util.Date 格式化转换工具
 */
public class CalendarDateUtil {

    public static final SimpleDateFormat STANDARDDATETIMEFORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat STANDARDDATEFORMATTER = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat NUMBERDATEFORMATTER = new SimpleDateFormat("yyyyMMdd");
    public static final SimpleDateFormat STANDARDTIMEFORMATTER = new SimpleDateFormat("HH:mm:ss");

    private CalendarDateUtil(){}

    public synchronized static Date parseDateTime(String text) {
        try {
            return STANDARDDATETIMEFORMATTER.parse(text);
        } catch (ParseException e) {
            throw new BaseServiceRunException(APPERR,"日期时间格式化错误",e);
        }
    }

    /**
     * @param text 日期字符串文本
     * @return Date对象
     */
    public synchronized static Date parseDate(String text) {
        try {
            return STANDARDDATEFORMATTER.parse(text);
        } catch (ParseException e) {
            throw new BaseServiceRunException(APPERR,"日期格式化错误",e);
        }
    }

    /**
     * @param text 日间字符串文
     * @return Date对象 缺省日期: 1970-01-01
     */
    public synchronized static Date parseTime(String text) {
        try {
            return STANDARDTIMEFORMATTER.parse(text);
        } catch (ParseException e) {
            throw new BaseServiceRunException(APPERR,"时间格式化错误",e);
        }
    }

    /**
     * @param date 日期对象
     * @return 格式化后的字符串
     */
    public synchronized static String toDateTime(Date date) {
        return date == null ? null : STANDARDDATETIMEFORMATTER.format(date);
    }

    public synchronized static String toDate(Date date) {
        return date == null ? null : STANDARDDATEFORMATTER.format(date);
    }

    public synchronized static String toTime(Date date) {
        return date == null ? null : STANDARDTIMEFORMATTER.format(date);
    }

    public static Date nowDate() {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        return calendar.getTime();
    }

    public static Date fromLocalDateTime(LocalDateTime dateTime) {
        // ZoneId zoneId = ZoneId.of("Asia/Shanghai");
        return fromLocalDateTime(dateTime,ZoneId.systemDefault());
    }

    public static Date fromLocalDateTime(LocalDateTime dateTime, ZoneId zone) {
        return dateTime == null ? null : Date.from((dateTime.atZone(zone).toInstant()));
    }

}
