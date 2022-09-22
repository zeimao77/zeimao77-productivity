package top.zeimao77.product.sql;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.exception.BaseServiceRunException;
import top.zeimao77.product.factory.ComponentFactory;
import top.zeimao77.product.main.BaseMain;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class SimpleSqlTemplateTest extends BaseMain {

    private static final String MYSQL="mysql_top_zeimao77";

    SimpleSqlTemplate sqlTemplate = ComponentFactory.initSimpleSqlTemplate(MYSQL,null);


    /**
     * 执行一个事务
     */
    @Test
    void execute() {
        sqlTemplate.execute(o -> {
            o.update("UPDATE demo set demo_name = '123' WHERE demo_id = 22309205494988801");
            throw new BaseServiceRunException("抛出一个异常");
        });
    }

    /**
     * 执行事务
     */
    @Test
    void openSession() {
        SimpleSqlClient simpleSqlClient = sqlTemplate.openSession(Connection.TRANSACTION_READ_COMMITTED,false,false);
        simpleSqlClient.update("UPDATE demo set demo_name = '123' WHERE demo_id = 22309205494988801");
        simpleSqlClient.commit();
        simpleSqlClient.close();
    }
}