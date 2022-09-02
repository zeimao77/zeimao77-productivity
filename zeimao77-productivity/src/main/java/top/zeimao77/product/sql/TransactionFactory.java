package top.zeimao77.product.sql;

import java.sql.Connection;

/**
 * sql连接工厂
 * @author zeimao77
 * @since 2.1.1
 */
public interface TransactionFactory extends AutoCloseable {

    /**
     * @return 获取一个连接
     */
    Connection createContection();

    /**
     * 关闭一个连接
     * @param connection 连接
     */
    void close(Connection connection);

    void commit();
    void rollback();

}
