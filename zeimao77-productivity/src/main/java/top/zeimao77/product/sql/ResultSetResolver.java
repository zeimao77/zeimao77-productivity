package top.zeimao77.product.sql;

import top.zeimao77.product.exception.BaseServiceRunException;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import static top.zeimao77.product.exception.ExceptionCodeDefinition.SQLEXCEPTION;

public interface ResultSetResolver {

    default void resolve(ResultSet rs) {
        ResultSetMetaData rsmd = null;
        try {
            rsmd = rs.getMetaData();
            while(rs.next()){
                resolve(rs,rsmd);
            }
        } catch (SQLException e) {
            throw new BaseServiceRunException(SQLEXCEPTION,"SQL异常",e);
        }
    }

    void resolve(ResultSet rs, ResultSetMetaData rsmd) throws SQLException;

}
