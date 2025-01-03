package top.zeimao77.product.sql;

import java.sql.*;
/**
 * 可以为 PreparedStatement 设置参数
 */
public interface PreparedStatementSetter<T> {

    /**
     * 为Statement设置参数
     * @param index 参数位置
     * @param javaType 参数对应JAVA类型
     * @param jdbcType 参数对应JDBC类型
     * @see java.sql.Types
     * 缺省的JDBC类型：
     * @see StatementParameter#DEFAULT_JDBCTYPE
     * @param value 参数值
     */
    void setParam(T statement, int index, Class<?> javaType, int jdbcType, Object value);

    void setParam(T statement) throws SQLException;

}
