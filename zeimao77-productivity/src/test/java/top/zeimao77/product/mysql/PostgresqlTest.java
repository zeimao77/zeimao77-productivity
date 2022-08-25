package top.zeimao77.product.mysql;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.factory.ComponentFactory;
import top.zeimao77.product.main.BaseMain;
import top.zeimao77.product.sql.*;

import javax.sql.DataSource;


public class PostgresqlTest extends BaseMain {

    @Test
    public void test() {
        DataSource dataSource = ComponentFactory.createDataSource("postgre_local_zeimao77");
        DataSourceTransactionFactory df = new DataSourceTransactionFactory(dataSource);
        SimpleSqlClient simpleSqlClient = new SimpleSqlClient(df, DefaultPreparedStatementSetter.INSTANCE, DefaultResultSetResolve.INSTANCE);
        int i = simpleSqlClient.updateByResolver(new SQL()
                .insert("test")
                        .addValues("id",2)
                .addValues("title",2)
                .addValues("content","hello world333")
                .addValues("is_del",false)
                .endValues()
                .onConflict("test_pkey","id"));
        logger.info("update:{}行",i);

    }


}
