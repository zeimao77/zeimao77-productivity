package top.zeimao77.product.mysql;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * 一个SQL执行器的实现 它将以某种形式把SQL消费掉
 */
public interface Reposit {

    /**
     * 测试一个连接是否可以使用
     * @return
     */
    boolean testConnection();

    /**
     * @param sql SQL及参数对象封装
     * @param clazz 返回类型
     * @return 查询结果列表
     * @param <T> 返回类型约束
     */
    <T> ArrayList<T> selectByResolver(StatementParamResolver sql, Class<T> clazz);
    int updateByResolver(StatementParamResolver sql);
    <Z> int batchUpdate(List<Z> list, BiConsumer<SQL,Z> con);

}
