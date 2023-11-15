package top.zeimao77.product.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * 一个SQL执行器的实现 它将以某种形式把SQL消费掉
 */
public interface Reposit {

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
