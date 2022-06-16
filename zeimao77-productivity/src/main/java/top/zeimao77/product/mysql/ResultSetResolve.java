package top.zeimao77.product.mysql;

import com.mysql.cj.protocol.Resultset;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ResultSetResolve {

    /**
     * 将ResultSet对象转一个JAVA对象
     * @param rs ResultSet对象
     * @param clazz 目标对象类型
     * @param list 转换后的对象存放容器
     * @param <T> 对象类型
     */
    <T> void populate(ResultSet rs , Class<T> clazz, List<T> list);

    void populateMap(ResultSet rs,List<Map<String,Object>> list);

    /**
     * 将一个数据库对象转JAVA对象，在存储过程出参时使用
     * @param value 数据库对象
     * @param clazz 目标对象类型
     * @return 结果
     * @param <T> 对象类型
     */
    <T> T resolve(Object value,Class<T> clazz);

}
