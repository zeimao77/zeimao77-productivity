package top.zeimao77.product.sql;

import java.util.List;

/**
 * SQL参数处理
 */
public interface StatementParamResolver {

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
    default String getExecSql() {
        StringBuilder execSqlBuilder = null;
        int start = 0;
        int end = 0;
        if(getStatementParams() == null || getStatementParams().isEmpty()) {
            return getSql();
        }
        execSqlBuilder = new StringBuilder();
        for (StatementParameter param : getStatementParams()) {
            end = getSql().indexOf("?",end);
            if(end > 0) {
                execSqlBuilder.append(getSql(),start,end);
                new DefaultExecSqlSetter().setParam(execSqlBuilder,param.getIndex(),param.getJavaType(),param.getJdbcType(),param.getValue());
                start = end+1;
                end++;
            }
        }
        execSqlBuilder.append(getSql(),start,getSql().length());
        return execSqlBuilder.toString();
    }





}
