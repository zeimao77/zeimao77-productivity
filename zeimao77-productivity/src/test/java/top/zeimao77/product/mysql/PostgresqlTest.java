package top.zeimao77.product.mysql;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.factory.BeanFactory;
import top.zeimao77.product.factory.ComponentFactory;
import top.zeimao77.product.main.BaseMain;
import top.zeimao77.product.postgresql.SimpleRepository;
import top.zeimao77.product.sql.*;
import top.zeimao77.product.util.LongIdGenerator;
import top.zeimao77.product.util.UuidGenerator;
import top.zeimao77.product.util.WordUtil;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.ArrayList;


public class PostgresqlTest extends BaseMain {

    @Test
    public void test() {
        BeanFactory.DEFAULT.registerPrototypesFactory("test",() -> {
            TestModel testModel = new TestModel();
            testModel.setId(LongIdGenerator.INSTANCE.generate());
            testModel.setTitle(UuidGenerator.INSTANCE.generate());
            testModel.setContent(testModel.getTitle());
            return testModel;
        });
        DataSource dataSource = ComponentFactory.createDataSource("postgre_local_zeimao77",null);
        DataSourceTransactionFactory df = new DataSourceTransactionFactory(dataSource);
        SimpleSqlClient simpleSqlClient = new SimpleSqlClient(df);
        TestRepository testRepository = new TestRepository(simpleSqlClient);
        ArrayList<TestModel> list = new ArrayList();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 200; i++) {

            TestModel testModel = BeanFactory.DEFAULT.getBean("test",TestModel.class);
            list.add(testModel);
            // testRepository.insert(testModel);
        }
        testRepository.batchInsert(list);
        long end  = System.currentTimeMillis();
        logger.info("耗时:{}ms",(end-start));

        Long aLong = simpleSqlClient.selectLong("SELECT COUNT(1) AS result FROM test", null);
        logger.info("aLong:{}",aLong);
        ArrayList<TestModel> test = simpleSqlClient.selectByResolver(new SQL().select("id").select("title").select("content").select("created_time","createdTime").from("test").limit(0, 20), TestModel.class);
        for (TestModel testModel : test) {
            logger.info(testModel.toString());
        }
    }

    public static class TestRepository extends SimpleRepository<TestModel,Long> {

        public TestRepository(Reposit repositoryImpl) {
            super(repositoryImpl, "test", new String[]{"id"});
        }

        @Override
        public String codeNameToDbName(String name) {
            return WordUtil.humpToLine(name);
        }
    }

    public static class TestModel {
        private Long id;
        private String title;
        private String content;
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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        @Override
        public String toString() {
            return "TestModel{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", content='" + content + '\'' +
                    ", createdTime=" + createdTime +
                    '}';
        }
    }


}
