package top.zeimao77.product.sql;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.factory.ComponentFactory;
import top.zeimao77.product.main.BaseMain;
import top.zeimao77.product.model.ImmutablePair;
import top.zeimao77.product.mysql.DemoModel;
import top.zeimao77.product.util.JsonBeanUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class SimpleSqlClientTest extends BaseMain {

    private static final String MYSQL="mysql_top_zeimao77";

    /**
     * 动态SQL查询
     */
    @Test
    void selectByResolver() {
        SimpleSqlClient simpleSqlClient = ComponentFactory.initSimpleSqlClient(MYSQL,null);
        ArrayList<DemoModel> demoList = simpleSqlClient.selectByResolver(new SQL()
                .select("demo_id","demoId")
                .select("demo_name","demoName")
                .select("created_time","createdTime")
                .select("de")
                .from("demo")
                .where(true,SQL.BIND_AND,"demo_id",SQL.COND_QLTE,22321875757563914L)
                .limit(0,30)
            , DemoModel.class);
        for (DemoModel demoModel : demoList) {
            logger.info(demoModel.toString());
        }
    }

    /**
     * 动态更新
     */
    @Test
    void updateByResolver() {
        SimpleSqlClient simpleSqlClient = ComponentFactory.initSimpleSqlClient(MYSQL,null);
        int i = simpleSqlClient.updateByResolver(new SQL()
                .update("demo")
                .set(true, "demo_name", "demo123")
                .where(IWhere.BIND_AND,"demo_id",IWhere.COND_QIS,1)
        );
        logger.info("更新了{}行;",i);
    }

    @Test
    void batchUpdate() {
        SimpleSqlClient simpleSqlClient = ComponentFactory.initSimpleSqlClient(MYSQL,null);
        List<ImmutablePair<String, Integer>> demoList = List.of(new ImmutablePair<>("demo1234", 2));
        int i = simpleSqlClient.batchUpdate(demoList, (o1, o2) -> {
            o1.insert("demo")
                    .set(o2.getLeft() != null, "demo_name", o2.getLeft())
                    .set(o2.getRight() != null, "demo_id", o2.getRight());
        });
        logger.info("更新了{}行",i);
    }

    @Test
    void update() {
        SimpleSqlClient simpleSqlClient = ComponentFactory.initSimpleSqlClient(MYSQL,null);
        int update = simpleSqlClient.update("delete from demo where demo_id = ?", new Object[]{2});
        logger.info("更新了{}行",update);
    }

    @Test
    void selectListString() {
        SimpleSqlClient simpleSqlClient = ComponentFactory.initSimpleSqlClient(MYSQL,null);
        ArrayList<String> strings = simpleSqlClient.selectListString("SELECT demo_name AS result FROM demo LIMIT 0,5", null);
        for (String string : strings) {
            logger.info("string:{}",string);
        }
    }

    @Test
    void selectString() {
        SimpleSqlClient simpleSqlClient = ComponentFactory.initSimpleSqlClient(MYSQL,null);
        String string = simpleSqlClient.selectString("SELECT demo_name AS result FROM demo LIMIT 0,3", null);
        logger.info("string:{}",string);
    }

    @Test
    void selectLong() {
        SimpleSqlClient simpleSqlClient = ComponentFactory.initSimpleSqlClient(MYSQL,null);
        Long along = simpleSqlClient.selectLong("SELECT COUNT(1) AS result FROM demo", null);
        logger.info("总行数:{}",along);
    }

    @Test
    void selectListMap() {
        SimpleSqlClient simpleSqlClient = ComponentFactory.initSimpleSqlClient(MYSQL,null);
        ArrayList<Map<String, Object>> maps = simpleSqlClient.selectListMap("SELECT * FROM demo LIMIT 0,5", null);
        for (Map<String, Object> map : maps) {
            logger.info("JSON:{}", JsonBeanUtil.DEFAULT.toJsonString(map));
        }
    }

    @Test
    void selectListObj() {
        SimpleSqlClient simpleSqlClient = ComponentFactory.initSimpleSqlClient(MYSQL,null);
        ArrayList<DemoModel> demoList = simpleSqlClient.selectListObj("SELECT demo_id AS demoId,demo_name,de,bo AS demoName FROM demo LIMIT 0,10",null, DemoModel.class);
        for (DemoModel demoModel : demoList) {
            logger.info(demoModel.toString());
        }
    }

    @Test
    void selectFirstObj() {
        SimpleSqlClient simpleSqlClient = ComponentFactory.initSimpleSqlClient(MYSQL,null);
        DemoModel demoModel = simpleSqlClient.selectFirstObj("SELECT demo_id AS demoId,demo_name,de,bo AS demoName FROM demo LIMIT 0,1",null, DemoModel.class);
        logger.info(demoModel.toString());
    }

    /**
     * 调用存储过程
     */
    @Test
    void call() {
        SimpleSqlClient simpleSqlClient = ComponentFactory.initSimpleSqlClient(MYSQL,null);
        HashMap<String, Object> param = new HashMap<>();
        param.put("id",1);
        simpleSqlClient.call("CALL delete_demo(#{id,javaType=Integer,jdbcType=INT,mode=IN})",param,null);
    }

}