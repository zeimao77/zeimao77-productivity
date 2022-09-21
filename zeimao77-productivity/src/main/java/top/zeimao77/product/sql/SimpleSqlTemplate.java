package top.zeimao77.product.sql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.zeimao77.product.exception.BaseServiceRunException;
import top.zeimao77.product.factory.BeanFactory;
import top.zeimao77.product.factory.ComponentFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static top.zeimao77.product.exception.ExceptionCodeDefinition.SQLEXCEPTION;

/**
 * SQL客户端的构建工厂
 * 可以通过
 * @see ComponentFactory#initSimpleSqlTemplate(String, BeanFactory)
 * 来创建它
 * 每次调用 openSession 将开启一个新的会话
 * @author zeimao77
 * @since 2.1.1
 */
public class SimpleSqlTemplate {

    private static Logger logger = LogManager.getLogger(SimpleSqlTemplate.class);

    DataSource dataSource;

    protected PreparedStatementSetter preparedStatementSetter = DefaultPreparedStatementSetter.INSTANCE;
    protected ResultSetResolve resultSetResolvel = DefaultResultSetResolve.INSTANCE;

    public SimpleSqlTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 创建一个连接客户端 每次调用都将通过一个新的会话开始;
     * @param level 事务隔离级别
     * Connection.TRANSACTION_READ_UNCOMMITTED  读未提交
     * Connection.TRANSACTION_READ_COMMITTED 读已提交
     * Connection.TRANSACTION_REPEATABLE_READ 可重复读
     * Connection.TRANSACTION_SERIALIZABLE 串行化
     * @param autoCommit 是否自动提交
     * @return 连接
     */
    public SimpleSqlClient openSession(int level, boolean autoCommit,boolean readOnly) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(autoCommit);
            connection.setTransactionIsolation(level);
            connection.setReadOnly(readOnly);
            logger.debug("获取一个新的连接:{}",connection);
        } catch (SQLException e) {
            throw new BaseServiceRunException(SQLEXCEPTION,"SQL错误",e);
        }
        ConnectionTransactionFactory threadExclusiveConnectionFactory = new ConnectionTransactionFactory(connection);
        return new SimpleSqlClient(threadExclusiveConnectionFactory,preparedStatementSetter,resultSetResolvel);
    }

    /**
     * 快速开启一个事务
     * @param transaction 事务方法 如果该方法抛出RuntimeException将会回滚事务
     * @return
     */
    public <T> T execute(Transaction<T> transaction) {
        SimpleSqlClient simpleSqlClient = openSession(Connection.TRANSACTION_READ_COMMITTED,false,false);
        T apply = null;
        try {
            apply = transaction.doInTransaction(simpleSqlClient);
            simpleSqlClient.commit();
        } catch (BaseServiceRunException e) {
            logger.error(String.format("[%s]%s",e.getCode(),e.getMessage()),e);
            simpleSqlClient.rollback();
        } catch (RuntimeException e) {
            logger.error("错误",e);
            simpleSqlClient.rollback();
        } finally {
            simpleSqlClient.close();
        }
        return apply;
    }


    public SimpleSqlClient createClient() {
        DataSourceTransactionFactory factory = new DataSourceTransactionFactory(this.dataSource);
        SimpleSqlClient simpleSqlClient = new SimpleSqlClient(factory,preparedStatementSetter,resultSetResolvel);
        return simpleSqlClient;
    }

    public SimpleSqlClient openSession() {
        return openSession(Connection.TRANSACTION_READ_COMMITTED,false,false);
    }

    @FunctionalInterface
    public interface Transaction<T> {

        T doInTransaction(SimpleSqlClient client);

    }

    public PreparedStatementSetter getPreparedStatementSetter() {
        return preparedStatementSetter;
    }

    public void setPreparedStatementSetter(PreparedStatementSetter preparedStatementSetter) {
        this.preparedStatementSetter = preparedStatementSetter;
    }

    public ResultSetResolve getResultSetResolvel() {
        return resultSetResolvel;
    }

    public void setResultSetResolvel(ResultSetResolve resultSetResolvel) {
        this.resultSetResolvel = resultSetResolvel;
    }
}
