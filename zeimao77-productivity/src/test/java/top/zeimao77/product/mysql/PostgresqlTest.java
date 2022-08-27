package top.zeimao77.product.mysql;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.factory.ComponentFactory;
import top.zeimao77.product.main.BaseMain;
import top.zeimao77.product.postgresql.SimpleRepository;
import top.zeimao77.product.sql.*;
import top.zeimao77.product.util.WordUtil;

import javax.sql.DataSource;
import java.time.LocalDateTime;


public class PostgresqlTest extends BaseMain {

    @Test
    public void test() {
        DataSource dataSource = ComponentFactory.createDataSource("postgre_local_zeimao77");
        DataSourceTransactionFactory df = new DataSourceTransactionFactory(dataSource);
        SimpleSqlClient simpleSqlClient = new SimpleSqlClient(df, DefaultPreparedStatementSetter.INSTANCE, DefaultResultSetResolve.INSTANCE);
        TestRepository testRepository = new TestRepository(simpleSqlClient);
        TestModel test = new TestModel();
        test.setId(1L);
        test.setCreatedTime(LocalDateTime.now());
        test.setTitle("hello");
        int upsert = testRepository.upsert(test);
        logger.info("upsert:{}",upsert);

    }

    public static class TestRepository extends SimpleRepository<TestModel,Long> {

        public TestRepository(Reposit repositoryImpl) {
            super(repositoryImpl, "test", "id");
        }

        @Override
        public String codeNameToDbName(String name) {
            return WordUtil.humpToLine(name);
        }
    }

    public static class TestModel {
        private Long id;
        private String title;
        @StatementParameterInfo(valSetPre = "to_timestamp(",valSetPost = ",'YYYY-MM-DD HH24:MI:SS')")
        private LocalDateTime createdTime;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public LocalDateTime getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(LocalDateTime createdTime) {
            this.createdTime = createdTime;
        }
    }


}
