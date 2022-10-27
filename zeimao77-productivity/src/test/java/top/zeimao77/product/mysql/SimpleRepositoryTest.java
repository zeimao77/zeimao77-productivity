package top.zeimao77.product.mysql;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import top.zeimao77.product.main.BaseMain;
import top.zeimao77.product.oracle.mysql.SimpleRepository;
import top.zeimao77.product.sql.Dbtype;
import top.zeimao77.product.sql.OnlyPrintReposit;
import top.zeimao77.product.sql.Reposit;
import top.zeimao77.product.sql.SelectCond;
import top.zeimao77.product.util.LongIdGenerator;
import top.zeimao77.product.util.WordUtil;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

class SimpleRepositoryTest extends BaseMain {

    public static class DemoRepository extends SimpleRepository<DemoModel,Long> {

        public DemoRepository(Reposit repositoryImpl) {
            super(repositoryImpl, "demo", new String[]{"demoId"}, null, null);
            setDbType(Dbtype.ORACLE);
        }

        @Override
        public String codeNameToDbName(String name) {
            return WordUtil.humpToLine(name);
        }
    }


    public OnlyPrintReposit onlyPrintReposit = new OnlyPrintReposit(new PrintWriter(System.out));

    @AfterEach
    public void afterAll() {
        onlyPrintReposit.close();
    }

    @Test
    public void batchInsert() {
        DemoRepository demoRepository = new DemoRepository(onlyPrintReposit);
        ArrayList<DemoModel> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(DemoModel.DemoFactory.create());
        }
        int i = demoRepository.batchInsert(list);
        logger.info("插入了{}行",i);
    }

    @Test
    public void delete() {
        DemoRepository demoRepository = new DemoRepository(onlyPrintReposit);
        int delete = demoRepository.delete(22309205499183114L);
        logger.info("删除{}行",delete);
    }

    @Test
    public void update() {
        DemoRepository demoRepository = new DemoRepository(onlyPrintReposit);
        DemoModel demoModel = demoRepository.get(22309205499183119L);
        demoModel.setCh(66);
        demoRepository.update(demoModel);
    }

    @Test
    public void upsert() {
        Long id = LongIdGenerator.INSTANCE.generate();
        DemoRepository demoRepository = new DemoRepository(onlyPrintReposit);
        DemoModel demoModel = DemoModel.DemoFactory.create();
        demoModel.setDemoId(id);
        int insert = demoRepository.insert(demoModel);
        logger.info("影响{}行",insert);
        demoModel = DemoModel.DemoFactory.create();
        demoModel.setDemoId(id);
        int upsert = demoRepository.upsert(demoModel);
        logger.info("影响{}行",upsert);
    }

    @Test
    public void list() {
        DemoRepository demoRepository = new DemoRepository(onlyPrintReposit);
        List<DemoModel> demo = demoRepository.list(SelectCond.select().from("demo").is("createDate","2022").orderBy("createDate","desc","de","asc"));

    }


}