package top.zeimao77.product.sql;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.factory.BeanFactory;
import top.zeimao77.product.factory.ComponentFactory;
import top.zeimao77.product.main.BaseMain;
import top.zeimao77.product.model.ImmutablePair;
import top.zeimao77.product.mysql.DemoModel;
import top.zeimao77.product.mysql.SimpleRepository;
import top.zeimao77.product.util.AssertUtil;
import top.zeimao77.product.util.JsonBeanUtil;
import top.zeimao77.product.util.StreamUtil;
import top.zeimao77.product.util.WordUtil;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

class SimpleSqlClientTest extends BaseMain {

    private static final String MYSQL="mysql_top_zeimao77";

    @Test
    void test() {
        // 取客户端
        SimpleSqlClient simpleSqlClient = BeanFactory.DEFAULT.getBean(ComponentFactory.AUTOBEAN_SQLCLIENT+MYSQL,SimpleSqlClient.class);

        // 静态SQL 用法1
        simpleSqlClient.update("DELETE FROM demo WHERE demo_id = ?",new Object[]{22309205499183107L});
        simpleSqlClient.update("insert into demo(demo_id,demo_name,ch,bo,de) values ('22309205499183107','demo0',2,2,32.456)", null);
        simpleSqlClient.update("UPDATE demo SET demo_name = ? WHERE demo_id = ?"
                ,new Object[]{"demo1",22309205499183107L});
        String demoName = simpleSqlClient.selectString("SELECT demo_name AS result FROM demo WHERE demo_id = '22309205499183107'", null);
        logger.info(demoName);
        // 静态SQL 用法2
        HashMap<Object, Object> param = new HashMap<>();
        param.put("demoId",22309205499183107L);
        param.put("demoName","demo2");
        simpleSqlClient.update("UPDATE demo SET demo_name = #{demoName} WHERE demo_id = ${demoId}",param);
        // 静态SQL 用法
        DemoModel demoModel = new DemoModel();
        demoModel.setDemoId(22309205499183107L);
        demoModel.setDemoName("demo3");
        simpleSqlClient.update("UPDATE demo SET demo_name = #{demoName} WHERE demo_id = ${demoId}",demoModel);
        demoName = simpleSqlClient.selectString("SELECT demo_name AS result FROM demo WHERE demo_id = '22309205499183107'", null);
        logger.info(demoName);
        // 其它更多方法参考实现即可 并不复杂
        // 动态SQL
        SQL sql = new SQL()
                .update("demo")
                .set(AssertUtil.isNotEmpty(demoModel.getCh()),"ch",demoModel.getCh())
                .set(AssertUtil.isNotEmpty(demoModel.getDemoName()),"demo_name",demoModel.getDemoName())
                .where(SQL.BIND_AND,"demo_id",SQL.COND_QIS,demoModel.getDemoId());
        // 输出一下可执行SQL  这样我们可以保存到文件
        // 执行动态SQL 输出SQL
        OnlyPrintReposit onlyPrintReposit = new OnlyPrintReposit(new PrintWriter(System.out));
        onlyPrintReposit.updateByResolver(sql);
        onlyPrintReposit.close();
        simpleSqlClient.updateByResolver(sql);

        // 不写SQL的动态SQL 支持增删改查、upsert操作
        // 需要注意的是null值也将设置 除非加了 StatementParameterInfo 注解会忽略空值
        SimpleRepository<DemoModel, Long> demoModelLongSimpleRepository =
                new SimpleRepository<DemoModel, Long>(simpleSqlClient,"demo","demoId") {
                    @Override
                    public String codeNameToDbName(String name) {
                        return WordUtil.humpToLine(name);
                    }
                };
        demoModelLongSimpleRepository.update(demoModel);
        SelectCond selectCond = SelectCond.select()
                .page(1, 4)
                .where(SQL.BIND_AND, "demo_id", SQL.COND_QGTE, 22309205499183107L);
        List<DemoModel> list = demoModelLongSimpleRepository.list(selectCond);

        logger.info("总行数:{}",selectCond.getTotal());
        for (int i = 0; i < list.size(); i++) {
            logger.info("第{}行:{}",i,list.get(i).getDemoName());
        }

        // 事务
        SimpleSqlTemplate simpleSqlTemplate = ComponentFactory.initSimpleSqlTemplate(MYSQL, BeanFactory.DEFAULT);
        simpleSqlTemplate.execute(o -> {
            o.update("UPDATE demo SET demo_name = 'demo4' WHERE demo_id = 22309205499183107");
            o.update("UPDATE demo SET demo_namea = 'demo4' WHERE demo_id = 22309205499183107");
            return null;
        });
        SimpleSqlClient client = simpleSqlTemplate.openSession();
        client.commit();
        client.close();
        demoName = simpleSqlClient.selectString("SELECT demo_name AS result FROM demo WHERE demo_id = '22309205499183107'", null);
        logger.info(demoName);
        BeanFactory.DEFAULT.destory();

    }


    @Test
    void select() {
        SimpleSqlClient simpleSqlClient = ComponentFactory.initSimpleSqlClient(MYSQL,null);
        ArrayList<ResultStr> select = simpleSqlClient.select("SELECT demo_name AS result FROM demo WHERE demo_id <= ?", o -> {
            try {
                o.setLong(1, 22309205499183107L);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, DefaultResultSetResolve.INSTANCE, ResultStr.class);
        select.forEach(o -> logger.info(o.getResult()));
    }



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

    @Test
    void jsonTest() {
        SimpleSqlClient simpleSqlClient = ComponentFactory.initSimpleSqlClient(MYSQL,null);
        simpleSqlClient.updateByResolver(new SQL().update("json_test")
                .jsonSet(true,"text","$.abc",666)
        );
        ArrayList<ResultStr> objects = simpleSqlClient.selectByResolver(new SQL()
                .selectJsonVal("text", "$.abc", "result")
                .from("json_test"), ResultStr.class);
        for (ResultStr object : objects) {
            logger.info(object.getResult());
        }

    }

    @Test
    void insertTable() {
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("abc",123);
        stringObjectHashMap.put("bbc", LocalDateTime.now());
        OnlyPrintReposit onlyPrintReposit = new OnlyPrintReposit(new PrintWriter(System.out),true);
        onlyPrintReposit.insertTable("test",stringObjectHashMap,null);
        DemoModel demoModel = DemoModel.DemoFactory.create();
        onlyPrintReposit.insertTable("demo",demoModel,null);

    }
}