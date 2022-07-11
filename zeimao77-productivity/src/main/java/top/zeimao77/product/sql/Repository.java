package top.zeimao77.product.sql;

import java.util.List;

public interface Repository<T,W> {

    int insert(T t);
    int delete(W id);
    int update(T t);
    T get(W id);
    List<T> list(SelectCond selectCond);

}
