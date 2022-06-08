package top.zeimao77.product.mysql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractRepository<T,W> {

    private static Logger logger = LogManager.getLogger(AbstractRepository.class);
    protected Reposit repositoryImpl;
    protected String tableName;

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
    protected abstract W insert(SQL sql,T t);

    /**
     * 将要存储或更新的对象转化到SQL对象中
     * @param sql SQL对象
     * @param t 对象
     */
    protected void upsert(SQL sql,T t) {
        insert(sql,t);
        sql.onDuplicateKeyUpdate();
    }
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

    public int insert(T t) {
        SQL sql = new SQL();
        insert(sql,t);
        return repositoryImpl.updateByResolver(sql);
    }

    public T insertAndGet(T t) {
        SQL mysql = new SQL();
        W id = insert(mysql,t);
        return id == null ? null : get(id);
    }

    public int upsert(T t) {
        SQL sql = new SQL();
        upsert(sql,t);
        return repositoryImpl.updateByResolver(sql);
    }

    public int update(T t) {
        SQL sql = new SQL();
        update(sql,t);
        return repositoryImpl.updateByResolver(sql);
    }

    public int delete(W id) {
        SQL sql = new SQL();
        delete(sql,id);
        return repositoryImpl.updateByResolver(sql);
    }

    public T get(W id) {
        SQL mysql = new SQL();
        get(mysql,id);
        ArrayList<T> ts = repositoryImpl.selectByResolver(mysql,getTClass());
        return ts.isEmpty() ? null : ts.get(0);
    }

    public List<T> list(SelectCond selectCond) {
        if(selectCond.isPaging()) {
            SQL sql = new SQL()
                .select("COUNT(1) AS result")
                .from(this.tableName);
            for (SelectCond.SelectCondNode selectCondNode : selectCond.getSearchCondNodeList()) {
                sql.where(selectCondNode.getBind(),selectCondNode.getFieldName(),selectCondNode.getCondition(),selectCondNode.getContent());
            }
            ArrayList<String> strings = repositoryImpl.selectByResolver(sql, String.class);
            Long total = strings.isEmpty() ? 0 : Long.valueOf(strings.get(0));
            selectCond.setTotal(total);
            if(total == 0L) {
                return new ArrayList<>();
            }
        }
        SQL sql = new SQL()
                .select(selectCond.getQueryColumn())
                .from(this.tableName);
        for (SelectCond.SelectCondNode selectCondNode : selectCond.getSearchCondNodeList()) {
            sql.where(selectCondNode.getBind(),selectCondNode.getFieldName(),selectCondNode.getCondition(),selectCondNode.getContent());
        }
        sql.orderBy(selectCond.getOrderBy());
        if(selectCond.isPaging()) {
            selectCond.calcpage();
            sql.limit(selectCond.get_limit(),selectCond.get_offset());
        }
        return repositoryImpl.selectByResolver(sql,getTClass());
    }

    public String getTableName() {
        return this.tableName;
    }
}
