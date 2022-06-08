package top.zeimao77.product.jobs;

import com.mysql.cj.jdbc.MysqlDataSource;
import top.zeimao77.product.config.ComponentFactory;
import top.zeimao77.product.mysql.SimpleMysql;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

public class AbcInsertJob extends JobExecTemplateBatch<Abc> {
    public AbcInsertJob(int nMaxJobs) {
        super(nMaxJobs);
    }

    private static DataSource dataSource = ComponentFactory.createDataSource("mysql_top_zeimao77");
    private static ThreadLocal<AbcRepository> repositoryHolder = new ThreadLocal<>();
    private int total = 0;

    @Override
    public void prepare() {
        super.prepare();
    }

    @Override
    protected void moreJob(int page) {
        for (int i = 0; i < 10000; i++) {
            Abc build = Abc.build();
            build.setJobId((long) i);
            addJob(build);
        }
        total += 10000;
        if(total >= 10000) {
            over();
        }
    }

    @Override
    public void successed(List<Abc> jobList) {

    }

    @Override
    public int handle(List<Abc> jobs) {
        AbcRepository repository = repositoryHolder.get();
        if(repository == null) {
            try {
                SimpleMysql mysql = new SimpleMysql(dataSource.getConnection());
                repository = new AbcRepository(mysql);
                repositoryHolder.set(repository);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        repository.batchInsert(jobs);
        return SUCCESSED;
    }

}
