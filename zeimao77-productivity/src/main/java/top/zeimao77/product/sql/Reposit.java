package top.zeimao77.product.sql;

import top.zeimao77.product.model.ImmutableRow;
import top.zeimao77.product.util.BeanUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * 一个SQL执行器的实现 它将以某种形式把SQL消费掉
 */
public interface Reposit {

    /**
     * 通过Bean或者Map的方式插入数据
     * @param tablename 表名
     * @param table 对象数据
     * @param fun 忽略字段及字段名转换函数扩展
     * @return 修改的行数
     */
    default int insertTable(String tablename, Object table, BiFunction<String,Object, ImmutableRow<Boolean,String,String>> fun) {
        SQL sql = new SQL();
        Field[] declaredFields = table.getClass().getDeclaredFields();
        sql.insert(tablename);
        BiConsumer<String,Object> con = (name,property) -> {
            if(fun != null) {
                ImmutableRow<Boolean, String, String> apply = fun.apply(name, property);
                if(apply != null)
                    sql.addValues(apply.getLeft(),name,apply.getCenter(),apply.getRight(),property);
                else
                    sql.addValues(name,property);
            } else {
                sql.addValues(name,property);
            }
        };
        if(table instanceof Map) {
            Map<String, Object> tableMap = (Map<String, Object>) table;
            for(Iterator<Map.Entry<String, Object>> iterator = tableMap.entrySet().iterator();iterator.hasNext();) {
                Map.Entry<String, Object> next = iterator.next();
                con.accept(next.getKey(),next.getValue());
            }
        } else {
            for (Field declaredField : declaredFields) {
                String name = declaredField.getName();
                Object property = BeanUtil.getProperty(table, name);
                con.accept(name,property);
            }
        }
        sql.endValues();
        return updateByResolver(sql);
    }

    /**
     * @param sql SQL及参数对象封装
     * @param clazz 返回类型
     * @return 查询结果列表
     * @param <T> 返回类型约束
     */
    <T> ArrayList<T> selectByResolver(StatementParamResolver sql, Class<T> clazz);
    int updateByResolver(StatementParamResolver sql);

    /**
     * 批量更新
     * @param list 数据模型
     * @param con 如果将模型组装一个SQL
     * @return 更新行数
     * @param <Z> 数据模型类型
     */
    <Z> int batchUpdate(List<Z> list, BiConsumer<SQL,Z> con);

    /**
     * 执行一个更新SQL
     * @param sql SQL语句
     * @return 更新行数
     */
    int update(String sql);

    /**
     * 执行一个更新SQL
     * @param sqlt SQL模板
     * @param params 参数
     * @return 更新行数
     */
    int update(String sqlt,Object params);

    /**
     * @param sqlt SQL语句 使用#{*}点位符替换 如果参数是数组使用?占位
     * @param param  参数 支持 Map、Bean、数组参数
     * @param clazz 返回类型类定义
     * @param <T> 返回泛型
     * @return 查询结果列表,如果没有数据,它不应该返回null,而应该是new ArrayList()
     */
    <T> ArrayList<T> selectListObj(String sqlt,Object param, Class<T> clazz);

    <T> ArrayList<T> selectListObj(String sql,Class<T> clazz);

    /**
     * 查询一个MAP,示例：
     * <pre>
     * simpleMysql.selectListMap("SELECT a,b,e,g,o,q,s,u,w FROM abc LIMIT 0,10", null);
     * </pre>
     * @param sqlt SQL
     * @param param 参数
     * @return 查询结果
     */
    ArrayList<Map<String,Object>> selectListMap(String sqlt, Object param);

    ArrayList<String> selectListString(String sqlt, Object param);
    default String selectString(String sqlt, Object param) {
        ArrayList<String> strings = selectListString(sqlt, param);
        return strings.isEmpty() ? null : strings.get(0);
    }

    /**
     * 查询一个Long值，调用示例：
     * <pre>
     * simpleMysql.selectLong("SELECT COUNT(1) AS result FROM demo WHERE demo_id > ?", new Object[]{1});
     * </pre>
     * @param sqlt SQL
     * @param param
     * @return 查询结果
     */
    default Long selectLong(String sqlt,Object param) {
        String s = selectString(sqlt, param);
        return s == null ? null : Long.valueOf(s);
    }


}
