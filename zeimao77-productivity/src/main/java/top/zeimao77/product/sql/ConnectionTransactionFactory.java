package top.zeimao77.product.sql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.zeimao77.product.exception.BaseServiceRunException;

import java.sql.Connection;
import java.sql.SQLException;

import static top.zeimao77.product.exception.ExceptionCodeDefinition.SQLEXCEPTION;

/**
 * 保存一个连接 直到它关闭
 */
public class ConnectionTransactionFactory implements TransactionFactory {

    private static Logger logger = LogManager.getLogger(ConnectionTransactionFactory.class);

    private Connection connection;

    public ConnectionTransactionFactory(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void close() {
        if(connection == null)
            return;
        logger.debug("关闭一个连接：{}",connection);
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error("关闭连接出错",e);
        }
    }

    public synchronized void commit() {
        if(connection != null) {
            try {
                connection.commit();
            } catch (SQLException e) {
                throw new BaseServiceRunException(SQLEXCEPTION, "SQL错误", e);
            }
        }
    }

    public synchronized void rollback() {
        if(connection != null) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                throw new BaseServiceRunException(SQLEXCEPTION, "SQL错误", e);
            }
        }
    }

    @Override
    public Connection createContection() {
        return this.connection;
    }

    @Override
    public void close(Connection connection) {
        logger.debug("忽略关闭连接:{}",connection);
    }


}
