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
    public int update(String sqlt,Object params);

    <T> ArrayList<T> selectListObj(String sqlt,Object param, Class<T> clazz);
    <T> ArrayList<T> selectListObj(String sql,Class<T> clazz);

    ArrayList<Map<String,Object>> selectListMap(String sqlt, Object param);

}
