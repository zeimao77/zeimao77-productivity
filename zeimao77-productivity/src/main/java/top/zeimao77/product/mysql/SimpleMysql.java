package top.zeimao77.product.mysql;

import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;
import top.zeimao77.product.sql.*;

public class SimpleMysql extends AbstractSqlClient {

    private static Logger logger = LogManager.getLogger(SimpleMysql.class);
    private String testSql = "SELECT USER() AS result";

    public SimpleMysql(ConnectFacotry connectFacotry) {
        super(connectFacotry,DefaultPreparedStatementSetter.INSTANCE,DefaultResultSetResolve.INSTANCE);
    }

    public SimpleMysql(ConnectFacotry connectFacotry, PreparedStatementSetter preparedStatementSetter, ResultSetResolve resultSetResolvel) {
        super(connectFacotry,preparedStatementSetter,resultSetResolvel);
    }

    @Override
    public boolean testConnection() {
        try {
            String s = this.selectString(testSql, null);
            logger.info("test connection 结果:{}",s);
            return true;
        } catch (RuntimeException e) {
            logger.error("测试连接错误",e);
        }
        return false;
    }

}
