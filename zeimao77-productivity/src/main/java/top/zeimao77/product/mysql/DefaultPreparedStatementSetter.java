package top.zeimao77.product.mysql;

import top.zeimao77.product.util.CalendarDateUtil;
import top.zeimao77.product.util.LocalDateTimeUtil;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DefaultPreparedStatementSetter implements PreparedStatementSetter {

    public static DefaultPreparedStatementSetter INSTANCE = new DefaultPreparedStatementSetter();

    @Override
    public void setParam(PreparedStatement preparedStatement, int index, Class<?> javaType, int jdbcType, Object value) {
        try {
            if(value == null) {
                preparedStatement.setNull(index, Types.NULL);
            } else if(String.class.isAssignableFrom(javaType)) {
                preparedStatement.setString(index, (String) value);
            } else if(Integer.class.isAssignableFrom(javaType)) {
                preparedStatement.setInt(index, ((Integer) value));
            } else if(Long.class.isAssignableFrom(javaType)) {
                preparedStatement.setLong(index,(Long)value);
            } else if(Double.class.isAssignableFrom(javaType)) {
                preparedStatement.setDouble(index,(Double)value);
            } else if(Short.class.isAssignableFrom(javaType)) {
                preparedStatement.setShort(index,(Short)value);
            } else if(Float.class.isAssignableFrom(javaType)) {
                preparedStatement.setFloat(index,(Float)value);
            } else if(BigDecimal.class.isAssignableFrom(javaType)) {
                preparedStatement.setBigDecimal(index, (BigDecimal) value);
            } else if(Boolean.class.isAssignableFrom(javaType)) {
                preparedStatement.setBoolean(index,(Boolean)value);
            } else if(LocalDateTime.class.isAssignableFrom(javaType) && (jdbcType == StatementParameter.DEFAULT_JDBCTYPE || jdbcType == Types.VARCHAR)) {
                preparedStatement.setString(index, LocalDateTimeUtil.toDateTime(((LocalDateTime) value)));
            } else if(LocalDateTime.class.isAssignableFrom(javaType) && (jdbcType == Types.TIMESTAMP)) {
                preparedStatement.setTimestamp(index, Timestamp.valueOf(((LocalDateTime) value)));
            } else if(LocalDate.class.isAssignableFrom(javaType) && (jdbcType == StatementParameter.DEFAULT_JDBCTYPE || jdbcType == Types.VARCHAR)) {
                preparedStatement.setString(index, LocalDateTimeUtil.toDate(((LocalDate) value)));
            } else if(LocalDate.class.isAssignableFrom(javaType) && (jdbcType == Types.DATE)) {
                preparedStatement.setDate(index, Date.valueOf(((LocalDate) value)));
            } else if(LocalTime.class.isAssignableFrom(javaType) && (jdbcType == StatementParameter.DEFAULT_JDBCTYPE || jdbcType == Types.VARCHAR)) {
                preparedStatement.setString(index, LocalDateTimeUtil.toTime(((LocalTime) value)));
            } else if(LocalTime.class.isAssignableFrom(javaType) && (jdbcType == Types.TIME)) {
                preparedStatement.setTime(index, Time.valueOf(((LocalTime) value)));
            } else if(java.util.Date.class.isAssignableFrom(javaType) && jdbcType == Types.TIMESTAMP){
                preparedStatement.setTimestamp(index,new Timestamp(((java.util.Date) value).getTime()));
            } else if(java.util.Date.class.isAssignableFrom(javaType) && jdbcType == Types.DATE){
                preparedStatement.setDate(index,new Date(((java.util.Date) value).getTime()));
            } else if(java.util.Date.class.isAssignableFrom(javaType) && jdbcType == Types.TIME){
                preparedStatement.setTime(index,new Time(((java.util.Date) value).getTime()));
            } else if(java.util.Date.class.isAssignableFrom(javaType) && (jdbcType == StatementParameter.DEFAULT_JDBCTYPE || jdbcType == Types.VARCHAR)) {
                preparedStatement.setString(index, CalendarDateUtil.toDateTime((java.util.Date) value));
            } else if(ByteBuffer.class.isAssignableFrom(javaType)) {
                preparedStatement.setBytes(index, ((ByteBuffer) value).array());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
