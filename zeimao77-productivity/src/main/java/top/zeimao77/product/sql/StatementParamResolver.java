package top.zeimao77.product.sql;

import top.zeimao77.product.util.ByteArrayCoDesUtil;
import top.zeimao77.product.util.CalendarDateUtil;
import top.zeimao77.product.util.LocalDateTimeUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.List;

/**
 * SQL参数处理
 */
public interface StatementParamResolver {

    /**
     * 函数引号
     */
    char QUOTATION_MARKS = '\'';

    /**
     * 解析参数，如果必要的话
     */
    void resolve();

    /**
     * 必需实现，JDBC参数列表,如果没有可以返回空列表
     * @return 预编译的参数列表
     */
    List<StatementParameter> getStatementParams();

    /**
     * 必需实现，SQL 它用来创建Statement，需要预编译的参数使用 ? 点位
     * @return 用于创建Statement的SQL语句
     */
    String getSql();

    /**
     * 可以用来导出SQL脚本文件
     * @return 可执行SQL(字符串)
     */
    default String getExecSql(ExecSqlParamSetter paramSetter) {
        StringBuilder execSqlBuilder = null;
        int start = 0;
        int end = 0;
        if(getStatementParams() == null || getStatementParams().isEmpty()) {
            return getSql();
        } else {
            execSqlBuilder = new StringBuilder();
            for (StatementParameter param : getStatementParams()) {
                end = getSql().indexOf("?",end);
                if(end > 0) {
                    execSqlBuilder.append(getSql(),start,end);
                    paramSetter.setParam(execSqlBuilder,param.getIndex(),param.getJavaType(),param.getJdbcType(),param.getValue());
                    start = end+1;
                    end++;
                }
            }
            execSqlBuilder.append(getSql(),start,getSql().length());
        }
        return execSqlBuilder.toString();
    }

    default String getExecSql() {
        return getExecSql(DefaultExecSqlParamSetter.INSTANCE);
    }

    interface ExecSqlParamSetter {
        /**
         * 可执行SQL(字符串)序列化 参数设置
         * @param execSqlBuilder sql拼接容器
         * @param index 参数位置
         * @param javaType 参数对应JAVA类型
         * @param jdbcType 参数对应JDBC类型
         * @see java.sql.Types
         * 缺省的JDBC类型：
         * @see StatementParameter#DEFAULT_JDBCTYPE
         * @param value 参数值
         */
        void setParam(StringBuilder execSqlBuilder, int index, Class<?> javaType, int jdbcType, Object value);
    };

    class DefaultExecSqlParamSetter implements ExecSqlParamSetter{

        public static DefaultExecSqlParamSetter INSTANCE = new DefaultExecSqlParamSetter();

        @Override
        public void setParam(StringBuilder execSqlBuilder, int index, Class<?> javaType, int jdbcType, Object value) {
            if(value == null) {
                execSqlBuilder.append("NULL");
            } else if(String.class.isAssignableFrom(javaType)) {
                String s = value.toString();
                if(s.contains("'")) {
                    execSqlBuilder.append(QUOTATION_MARKS).append(s.replaceAll("'","\'")).append(QUOTATION_MARKS);
                } else {
                    execSqlBuilder.append(QUOTATION_MARKS).append(value).append(QUOTATION_MARKS);
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
    }

}
