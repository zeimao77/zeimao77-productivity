package top.zeimao77.product.sql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.zeimao77.product.exception.BaseServiceRunException;
import top.zeimao77.product.exception.ExceptionCodeDefinition;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static top.zeimao77.product.exception.ExceptionCodeDefinition.SQLEXCEPTION;

public class PrototypeConnectionFactory implements ConnectFacotry{

    private static Logger logger = LogManager.getLogger(PrototypeConnectionFactory.class);

    private DataSource dataSource;

    public PrototypeConnectionFactory(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Connection createContection() {
        try {
            Connection connection = dataSource.getConnection();
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
    public void close() {

    }
}
