package top.zeimao77.product.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;

public class LocalDateTimeUtil {

    private LocalDateTimeUtil(){}

    public static final DateTimeFormatter STANDARDTIMESTAMPFORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    public static final DateTimeFormatter STANDARDDATETIMEFORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter STANDARDDATEFORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter NUMBERDATEFORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static final DateTimeFormatter STANDARDTIMEFORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static LocalDateTime parseDateTime(String text) {
        return LocalDateTime.parse(text, STANDARDDATETIMEFORMATTER);
    }

    public static LocalDate parseDate(String text) {
        return LocalDate.parse(text, STANDARDDATEFORMATTER);
    }

    public static LocalTime parseTime(String text) {
        return LocalTime.parse(text, STANDARDTIMEFORMATTER);
    }

    public static String toDateTime(TemporalAccessor dateTime) {
        return dateTime != null ? STANDARDDATETIMEFORMATTER.format(dateTime) : null;
    }

    public static String toDateTime(TemporalAccessor dateTime,DateTimeFormatter formatter) {
        return dateTime != null ? formatter.format(dateTime) : null;
    }

    public static String toDate(TemporalAccessor dateTime) {
        return dateTime != null ? STANDARDDATEFORMATTER.format(dateTime) : null;
    }

    public static String toTime(TemporalAccessor dateTime) {
        return dateTime != null ? STANDARDTIMEFORMATTER.format(dateTime) : null;
    }

    public static String toDate(TemporalAccessor dateTime,DateTimeFormatter formatter) {
        return dateTime != null ? formatter.format(dateTime) : null;
    }

    public static String nowDateTime() {
        return toDateTime(LocalDateTime.now());
    }

    public static String nowDate() {
        return toDate(LocalDate.now());
    }

    public static long betweenSecond(LocalDateTime s,LocalDateTime e) {
        return ChronoUnit.SECONDS.between(s, e);
    }

    public static long monthDays(LocalDate month) {
        LocalDate s = LocalDate.of(month.getYear(),month.getMonth(),1);
        LocalDate e = s.plusMonths(1);
        return ChronoUnit.DAYS.between(s,e);
    }

}
