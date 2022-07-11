package top.zeimao77.product.sql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.zeimao77.product.exception.BaseServiceRunException;
import top.zeimao77.product.exception.ExceptionCodeDefinition;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static top.zeimao77.product.exception.ExceptionCodeDefinition.SQLEXCEPTION;

public class ThreadExclusiveConnectionFactory implements ConnectFacotry {

    private static Logger logger = LogManager.getLogger(ThreadExclusiveConnectionFactory.class);
    private static final ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<>();

    private Connection connection;

    public ThreadExclusiveConnectionFactory(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void close() {
        if(connection == null)
            return;
        connectionThreadLocal.remove();
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


    public static class Builder {

        private DataSource dataSource;

        public Builder(DataSource dataSource) {
            this.dataSource = dataSource;
        }

        public ThreadExclusiveConnectionFactory build(boolean autoCommit) {
            Connection conn = connectionThreadLocal.get();
            if (conn == null) {
                try {
                    conn = dataSource.getConnection();
                    conn.setAutoCommit(autoCommit);
                    logger.debug("获取一个新的连接:{}",conn);
                } catch (SQLException e) {
                    throw new BaseServiceRunException(ExceptionCodeDefinition.SQLEXCEPTION, "获取连接错误", e);
                }
                connectionThreadLocal.set(conn);
            }
            return new ThreadExclusiveConnectionFactory(conn);
        }
    }


}
