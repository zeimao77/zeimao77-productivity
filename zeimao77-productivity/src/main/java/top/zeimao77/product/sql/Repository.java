package top.zeimao77.product.sql;

import java.util.List;

/**
 * dao接口 提供普适的单行新增、修改、删除、查询功能
 * @param <T> 技术化对象
 * @param <W> 主键
 * @author zeimao77
 * @since 2.1.1
 */
public interface Repository<T,W> {

    int insert(T t);
    int delete(W id);
    int update(T t);
    T get(W id);
    List<T> list(SelectCond selectCond);

}
