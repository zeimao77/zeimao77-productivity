package top.zeimao77.product.sql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.zeimao77.product.exception.BaseServiceRunException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static top.zeimao77.product.exception.ExceptionCodeDefinition.SQLEXCEPTION;

public class SimpleSqlFacroty {

    private static Logger logger = LogManager.getLogger(SimpleSqlFacroty.class);

    DataSource dataSource;

    public SimpleSqlFacroty(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * @param level 事务隔离级别
     * Connection.TRANSACTION_READ_UNCOMMITTED  读未提交
     * Connection.TRANSACTION_READ_COMMITTED 读已提交
     * Connection.TRANSACTION_REPEATABLE_READ 可重复读
     * Connection.TRANSACTION_SERIALIZABLE 串行化
     * @param autoCommit 是否自动提交
     * @return 连接
     */
    public SimpleSqlClient openSession(int level, boolean autoCommit) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(autoCommit);
            connection.setTransactionIsolation(level);
            logger.debug("获取一个新的连接:{}",connection);
        } catch (SQLException e) {
            throw new BaseServiceRunException(SQLEXCEPTION,"SQL错误",e);
        }
        ConnectionTransactionFactory threadExclusiveConnectionFactory = new ConnectionTransactionFactory(connection);
        return new SimpleSqlClient(threadExclusiveConnectionFactory,DefaultPreparedStatementSetter.INSTANCE,DefaultResultSetResolve.INSTANCE);
    }

    public SimpleSqlClient openSession() {
        return openSession(Connection.TRANSACTION_READ_COMMITTED,true);
    }


}
