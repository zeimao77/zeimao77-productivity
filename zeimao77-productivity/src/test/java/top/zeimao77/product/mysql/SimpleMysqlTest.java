package top.zeimao77.product.mysql;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.factory.ComponentFactory;
import top.zeimao77.product.main.BaseMain;
import top.zeimao77.product.sql.SQL;
import top.zeimao77.product.sql.SimpleSqlClient;
import top.zeimao77.product.sql.SimpleSqlTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class SimpleMysqlTest extends BaseMain {

    /**
     * 表
     * CREATE TABLE `demo` (
     *   `demo_id` bigint NOT NULL,
     *   `demo_name` varchar(32) DEFAULT NULL,
     *   `created_time` datetime DEFAULT NULL,
     *   `ch` tinyint DEFAULT NULL,
     *   `bo` tinyint(1) DEFAULT '0',
     *   `de` decimal(10,4) DEFAULT '0.0000',
     *   PRIMARY KEY (`demo_id`)
     * ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
     *
     */
    static SimpleSqlClient simpleMysql;

    static {
        SimpleSqlTemplate simpleSqlFacroty= ComponentFactory.initSimpleSqlFacroty("mysql_top_zeimao77");
        simpleMysql = simpleSqlFacroty.openSession();
    }

    @Test
    void batchUpdate() {
        int truncate_table_demo = simpleMysql.update("truncate table demo");
        logger.info("truncate_table_demo={}",truncate_table_demo);
        ArrayList<DemoModel> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(DemoModel.DemoFactory.create());
        }
        int i = simpleMysql.batchUpdate(list, (o1, o2) -> {
            o1.insert("demo")
                    .addValues("demo_id", o2.getDemoId())
                    .addValues("demo_name", o2.getDemoName())
                    .addValues("created_time", o2.getCreatedTime())
                    .addValues("ch", o2.getCh())
                    .addValues("bo", o2.getBo())
                    .addValues("de", o2.getDe())
                    .endValues();
        });
        logger.info("插入{}行;",i);
    }

    @Test
    void selectLong() {
        Long aLong = simpleMysql.selectLong("SELECT COUNT(1) AS result FROM demo WHERE demo_id > ?", new Object[]{1});
        logger.info("aLong={}",aLong);
        Map<String,Object> mapParam = new HashMap<>();
        mapParam.put("demoIdStart",3);
        mapParam.put("demoIdEnd",300);
        Long bLong = simpleMysql.selectLong("SELECT COUNT(1) AS result FROM demo WHERE demo_id > #{demoIdStart} AND demo_id < #{demoIdEnd}", mapParam);
        logger.info("bLong={}",bLong);
    }

    @Test
    void selectListObj() {
        ArrayList<DemoModel> demoModels = simpleMysql.selectListObj("SELECT demo_id AS demoId,demo_name AS demoName FROM demo WHERE 1 = 1", DemoModel.class);
        demoModels.forEach(logger::info);
    }

    @Test
    void selectByResolver() {
        ArrayList<DemoModel> demoModels = simpleMysql.selectByResolver(new SQL()
                .select("demo_id", "demoId")
                .select("demo_name", "demoName")
                .select("created_time", "createdTime")
                .select("ch")
                .select("bo")
                .from("demo")
                .where(SQL.BIND_AND, "demo_id", SQL.COND_QIS, 1)
                .limit(0,10)
                , DemoModel.class);
        demoModels.forEach(logger::info);
    }

    @Test
    void update() {
        DemoModel demoModel = simpleMysql.selectFirstObj("SELECT demo_id AS demoId FROM demo WHERE bo = ? LIMIT 0,1", new Object[]{true}, DemoModel.class);
        logger.info(demoModel);
        demoModel.setCh(100);
        int i = simpleMysql.updateByResolver(new SQL()
                .update("demo")
                .set("ch", demoModel.getCh())
                .where(SQL.BIND_AND, "demo_id", SQL.COND_QIS, demoModel.getDemoId())
        );
        logger.info("更新{}行",i);
        demoModel.setCh(66);
        int update = simpleMysql.update("UPDATE demo SET ch = #{ch} WHERE demo_id = #{demoId}", demoModel);
        logger.info("更新{}行",update);
    }

    @Test
    void selectListMap() {
        ArrayList<Map<String, Object>> maps = simpleMysql.selectListMap("SELECT a,b,e,g,o,q,s,u,w FROM abc LIMIT 0,10", null);
        logger.info(maps.size());
    }

}