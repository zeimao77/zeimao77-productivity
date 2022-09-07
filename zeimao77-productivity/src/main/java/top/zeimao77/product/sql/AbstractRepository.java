package top.zeimao77.product.sql;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractRepository<T,W> implements Repository<T,W> {

    protected Reposit repositoryImpl;
    protected String tableName;
    protected Integer dbType = Dbtype.ALL;

    public Class<T> getTClass()
    {
        Class<T> tClass = (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return tClass;
    }

    /**
     * @param repositoryImpl SQL执行实现
     * @param tableName 表名
     */
    public AbstractRepository(Reposit repositoryImpl, String tableName) {
        this.repositoryImpl = repositoryImpl;
        this.tableName = tableName;
    }

    /**
     * 将要存储的对象转化到SQL对象中
     * @param sql SQL对象
     * @param t 对象
     * @return 主键
     */
    protected abstract W insert(SQL sql, T t);

    /**
     * 将要存储或更新的对象转化到SQL对象中
     * @param sql SQL对象
     * @param t 对象
     */
    protected abstract void upsert(SQL sql,T t);
    protected abstract void update(SQL sql,T t);
    protected abstract void delete(SQL sql,W id);

    /**
     * 将主键条件转化到SQL对象中
     * @param sql SQL对象
     * @param id 主键
     */
    protected abstract void get(SQL sql,W id);

    /**
     * 通过主键批量删除
     * @param ids 主键数组
     * @return 影响行数
     */
    public int batchDelete(W... ids) {
        return repositoryImpl.batchUpdate(List.of(ids),(o1,o2)->this.delete(o1,o2));
    }

    public int batchDelete(List<W> ids) {
        return repositoryImpl.batchUpdate(ids,(o1,o2)->this.delete(o1,o2));
    }

    /**
     * 批量插入对象
     * @param list 对象列表
     * @return 影响行数
     */
    public int batchInsert(List<T> list) {
        return repositoryImpl.batchUpdate(list,(o1,o2)->this.insert(o1,o2));
    }

    public int batchUpdate(List<T> list) {
        return repositoryImpl.batchUpdate(list,(o1,o2)->this.update(o1,o2));
    }

    public int batchUpsert(List<T> list) {
        return repositoryImpl.batchUpdate(list,(o1,o2)->this.upsert(o1,o2));
    }


    protected void beforeInsert(SQL sql,T t) {};
    protected void afterInsert(T t) {};
    @Override
    public int insert(T t) {
        SQL sql = new SQL(dbType);
        insert(sql,t);
        beforeInsert(sql,t);
        int i = repositoryImpl.updateByResolver(sql);
        afterInsert(t);
        return i;
    }

    public T insertAndGet(T t) {
        SQL mysql = new SQL(dbType);
        W id = insert(mysql,t);
        return id == null ? null : get(id);
    }

    protected void beforeUpsert(SQL sql,T t){};
    protected void afterUpsert(T t){};
    public int upsert(T t) {
        SQL sql = new SQL(dbType);
        upsert(sql,t);
        beforeUpsert(sql,t);
        int i = repositoryImpl.updateByResolver(sql);
        afterUpsert(t);
        return i;
    }

    protected void beforeUpdate(SQL sql,T t){};
    protected void afterUpdate(T t){};
    @Override
    public int update(T t) {
        SQL sql = new SQL(dbType);
        update(sql,t);
        beforeUpdate(sql,t);
        int i = repositoryImpl.updateByResolver(sql);
        afterUpdate(t);
        return i;
    }

    protected void beforeDelete(SQL sql,W id){};
    protected void afterDelete(W id){};
    @Override
    public int delete(W id) {
        SQL sql = new SQL(dbType);
        delete(sql,id);
        beforeDelete(sql,id);
        int i = repositoryImpl.updateByResolver(sql);
        afterDelete(id);
        return i;
    }

    @Override
    public T get(W id) {
        SQL mysql = new SQL(dbType);
        get(mysql,id);
        ArrayList<T> ts = repositoryImpl.selectByResolver(mysql,getTClass());
        return ts.isEmpty() ? null : ts.get(0);
    }

    public String getTableName() {
        return this.tableName;
    }

    public Integer getDbType() {
        return dbType;
    }

    public void setDbType(Integer dbType) {
        this.dbType = dbType;
    }
}
