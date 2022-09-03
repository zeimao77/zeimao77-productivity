package top.zeimao77.product.mysql;

import top.zeimao77.product.sql.*;
import top.zeimao77.product.util.BeanUtil;

import java.util.function.BiFunction;

/**
 * @param <T> MODEL
 * @param <W> 主键类型 多主键可以通过记录类提供支持 并提供以对应的主键解析函数:
 * @see SimpleRepository#idParseFunc
 */
public class SimpleRepository<T,W> extends AbstractSimpleRepository<T, W> {

    public SimpleRepository(Reposit repositoryImpl, String tableName, String... primaryKeyFields) {
        super(repositoryImpl, tableName, primaryKeyFields);
    }

    public SimpleRepository(Reposit repositoryImpl, String tableName, String[] primaryKeyFields, String[] ignoreFields) {
        super(repositoryImpl, tableName, primaryKeyFields, ignoreFields);
    }

    public SimpleRepository(Reposit repositoryImpl, String tableName, String[] primaryKeyFields, String[] ignoreFields, BiFunction<W, String, Object> idParseFunc) {
        super(repositoryImpl, tableName, primaryKeyFields, ignoreFields, idParseFunc);
    }

    @Override
    protected void upsert(SQL sql, T t) {
        insert(sql,t);
        sql.append(" ON DUPLICATE KEY ");
        sql.onDuplicateKeyUpdate(o -> !isPrimaryKey(o.getName()));
    }

    public Object idParse(W id,String name) {
        return BeanUtil.getProperty(id,name);
    }

}
