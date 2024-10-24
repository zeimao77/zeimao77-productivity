package top.zeimao77.product.sql;

import top.zeimao77.product.util.ByteArrayCoDesUtil;
import top.zeimao77.product.util.CalendarDateUtil;
import top.zeimao77.product.util.LocalDateTimeUtil;
import top.zeimao77.product.util.StringOptional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

public class DefaultExecSqlSetter implements PreparedStatementSetter<StringBuilder> {

    char QUOTATION_MARKS = '\'';

    @Override
    public void setParam(StringBuilder execSqlBuilder, int index, Class javaType, int jdbcType, Object value) {
        if(value == null) {
            execSqlBuilder.append("NULL");
        } else if(String.class.isAssignableFrom(javaType)) {
            String s = value.toString();
            if(s.contains("'")) {
                execSqlBuilder.append(QUOTATION_MARKS).append(s.replaceAll("'","\\\\'")).append(QUOTATION_MARKS);
            } else {
                execSqlBuilder.append(QUOTATION_MARKS).append(value).append(QUOTATION_MARKS);
            }
        } else if(StringOptional.class.isAssignableFrom(javaType)) {
            StringOptional s = (StringOptional) value;
            if(s.isBlank()) {
                execSqlBuilder.append("NULL");
            } else if(s.get().contains("'")) {
                execSqlBuilder.append(QUOTATION_MARKS).append(s.get().replaceAll("'","\\\\'")).append(QUOTATION_MARKS);
            } else {
                execSqlBuilder.append(QUOTATION_MARKS).append(s.get()).append(QUOTATION_MARKS);
            }
        } else if(Long.class.isAssignableFrom(javaType)){
            execSqlBuilder.append(value);
        } else if(Integer.class.isAssignableFrom(javaType)){
            execSqlBuilder.append(value);
        } else if(Short.class.isAssignableFrom(javaType)){
            execSqlBuilder.append(value);
        } else if(Float.class.isAssignableFrom(javaType)){
            execSqlBuilder.append(value);
        } else if(Double.class.isAssignableFrom(javaType)){
            execSqlBuilder.append(value);
        } else if(BigInteger.class.isAssignableFrom(javaType)){
            execSqlBuilder.append(value);
        } else if(BigDecimal.class.isAssignableFrom(javaType)){
            execSqlBuilder.append(value);
        } else if(LocalDateTime.class.isAssignableFrom(javaType)) {
            execSqlBuilder.append(QUOTATION_MARKS).append(LocalDateTimeUtil.toDateTime((TemporalAccessor) value)).append(QUOTATION_MARKS);
        } else if(LocalDate.class.isAssignableFrom(javaType)) {
            execSqlBuilder.append(QUOTATION_MARKS).append(LocalDateTimeUtil.toDate((TemporalAccessor) value)).append(QUOTATION_MARKS);
        } else if(LocalTime.class.isAssignableFrom(javaType)) {
            execSqlBuilder.append(QUOTATION_MARKS).append(LocalDateTimeUtil.toTime((TemporalAccessor) value)).append(QUOTATION_MARKS);
        } else if(ByteBuffer.class.isAssignableFrom(javaType)){
            execSqlBuilder.append("UNHEX('").append(ByteArrayCoDesUtil.hexEncode(((ByteBuffer) value).array())).append("')");
        } else if(Boolean.class.isAssignableFrom(javaType)) {
            execSqlBuilder.append(((Boolean) value).booleanValue()?1:0);
        } else if(Character.class.isAssignableFrom(javaType)) {
            execSqlBuilder.append(QUOTATION_MARKS).append(value).append(QUOTATION_MARKS);
        } else if(java.util.Date.class.isAssignableFrom(javaType) && jdbcType == Types.TIMESTAMP){
            execSqlBuilder.append(new Timestamp(((java.util.Date) value).getTime()));
        } else if(java.util.Date.class.isAssignableFrom(javaType) && jdbcType == Types.DATE){
            execSqlBuilder.append(QUOTATION_MARKS).append(CalendarDateUtil.toDate((Date)value)).append(QUOTATION_MARKS);
        } else if(java.util.Date.class.isAssignableFrom(javaType) && jdbcType == Types.TIME){
            execSqlBuilder.append(QUOTATION_MARKS).append(CalendarDateUtil.toTime((Date)value)).append(QUOTATION_MARKS);
        } else if(java.util.Date.class.isAssignableFrom(javaType) && (jdbcType == StatementParameter.DEFAULT_JDBCTYPE || jdbcType == Types.VARCHAR)) {
            execSqlBuilder.append(QUOTATION_MARKS).append(CalendarDateUtil.toDateTime(((Date) value))).append(QUOTATION_MARKS);
        } else {
            execSqlBuilder.append(QUOTATION_MARKS).append(value).append(QUOTATION_MARKS);
        }
    }

    @Override
    public void setParam(StringBuilder statement) throws SQLException {

    }
}
