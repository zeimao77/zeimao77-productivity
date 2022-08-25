package top.zeimao77.product.postgresql;

import top.zeimao77.product.sql.AbstractSimpleRepository;
import top.zeimao77.product.sql.Reposit;
import top.zeimao77.product.sql.SQL;
import top.zeimao77.product.util.BeanUtil;

import java.util.function.BiFunction;

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
        sql.appent(" ON CONFLICT (");
        for (int i = 0; i < primaryKeyFieldList.size(); i++) {
            if(i > 0) {
                sql.appent(",");
            }
            sql.appent(codeNameToDbName(primaryKeyFieldList.get(i)));
        }
        sql.appent(") DO UPDATE SET ");
        sql.onDuplicateKeyUpdate(o -> !isPrimaryKey(o.getName()));
    }

    public Object idParse(W id,String name) {
        return BeanUtil.getProperty(id,name);
    }


}
