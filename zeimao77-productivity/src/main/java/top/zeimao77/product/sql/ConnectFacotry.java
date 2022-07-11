package top.zeimao77.product.sql;

import java.sql.Connection;

public interface ConnectFacotry extends AutoCloseable {

    Connection createContection();

    void close(Connection connection);

}
