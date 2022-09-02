package top.zeimao77.product.sql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.zeimao77.product.exception.BaseServiceRunException;
import top.zeimao77.product.exception.ExceptionCodeDefinition;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static top.zeimao77.product.exception.ExceptionCodeDefinition.SQLEXCEPTION;

/**
 * 每次执行SQL都是一个单独事务,初始化示例:

 */
public class DataSourceTransactionFactory implements TransactionFactory {

    private static Logger logger = LogManager.getLogger(DataSourceTransactionFactory.class);

    private DataSource dataSource;

    public DataSourceTransactionFactory(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Connection createContection() {
        try {
            Connection connection = dataSource.getConnection();
            connection.setAutoCommit(true);
            logger.debug("获取一个新的连接:{}",connection);
            return connection;
        } catch (SQLException e) {
            throw new BaseServiceRunException(ExceptionCodeDefinition.SQLEXCEPTION,"SQL异常",e);
        }
    }

    @Override
    public void close(Connection connection) {
        logger.debug("关闭一个连接：{}",connection);
        try {
            if(connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new BaseServiceRunException(SQLEXCEPTION,"SQL错误",e);
        }
    }

    @Override
    public void commit() {}

    @Override
    public void rollback() {}

    @Override
    public void close() {}
}
