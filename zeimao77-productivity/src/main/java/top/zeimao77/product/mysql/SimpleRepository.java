package top.zeimao77.product.mysql;

import top.zeimao77.product.exception.BaseServiceRunException;
import top.zeimao77.product.sql.*;
import top.zeimao77.product.util.AssertUtil;
import top.zeimao77.product.util.BeanUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import static top.zeimao77.product.exception.ExceptionCodeDefinition.*;

/**
 * @param <T> MODEL
 * @param <W> 主键类型 多主键可以通过记录类提供支持 并提供以对应的主键解析函数:
 * @see SimpleRepository#idParseFunc
 */
public class SimpleRepository<T,W> extends AbstractRepository<T, W> {

    protected List<String> ignoreFieldList;
    protected List<String> primaryKeyFieldList;
    /**
     * 主键解析函数
     * 第一个参数是主键
     * 第二个参数是字段名
     * 返回对应字段的查询条件参数对象
     */
    protected BiFunction<W,String,Object> idParseFunc;

    /**
     * @param columnName 数据库列名
     * @return 是否主键
     */
    protected boolean isPrimaryKey(String columnName) {
        if(primaryKeyFieldList != null && !primaryKeyFieldList.isEmpty()){
            for (String name : primaryKeyFieldList) {
                if(codeNameToDbName(name).equals(columnName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     *
     * @param fieldName 字段名
     * @return 是否主键
     */
    protected boolean isPrimaryKeyField(String fieldName) {
        if(primaryKeyFieldList != null && !primaryKeyFieldList.isEmpty()){
            for (String name : primaryKeyFieldList) {
                if(name.equals(fieldName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param columnName 数据库列名
     * @return 是否被忽略
     */
    protected boolean isIgnore(String columnName) {
        if(ignoreFieldList != null && !ignoreFieldList.isEmpty()){
            for (String name : ignoreFieldList) {
                if(codeNameToDbName(name).equals(columnName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param fieldName Model字段名
     * @return 是否被忽略
     */
    protected boolean isIgnoreField(String fieldName) {
        if(ignoreFieldList != null && !ignoreFieldList.isEmpty()){
            for (String name : ignoreFieldList) {
                if(name.equals(fieldName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public SimpleRepository(Reposit repositoryImpl, String tableName, String... primaryKeyFields) {
        this(repositoryImpl,tableName,primaryKeyFields,null,null);
    }

    public SimpleRepository(Reposit repositoryImpl, String tableName, String[] primaryKeyFields, String[] ignoreFields) {
        this(repositoryImpl,tableName,primaryKeyFields,ignoreFields,null);
    }

    /**
     *
     * @param repositoryImpl SQL执行实现
     * @param tableName 表名
     * @param primaryKeyFields 主键名 支持联合主键 必需
     * @param ignoreFields 忽略的字段名
     * @param idParseFunc 联合主键解析函数 设置联合主键时需要;
     */
    public SimpleRepository(Reposit repositoryImpl, String tableName, String[] primaryKeyFields, String[] ignoreFields,BiFunction<W,String,Object> idParseFunc) {
        super(repositoryImpl, tableName);
        AssertUtil.assertTure(primaryKeyFields != null && primaryKeyFields.length > 0,"主键描述必需");
        this.primaryKeyFieldList = new ArrayList<>();
        for (String primaryKeyField : primaryKeyFields) {
            this.primaryKeyFieldList.add(primaryKeyField);
        }
        if(ignoreFields != null && ignoreFields.length > 0) {
            this.ignoreFieldList = new ArrayList<>();
            for (String ignoreField : ignoreFields) {
                this.ignoreFieldList.add(ignoreField);
            }
        }
        this.idParseFunc = idParseFunc;
    }

    @Override
    protected W insert(SQL sql, T t) {
        Field[] declaredFields = t.getClass().getDeclaredFields();
        sql.insert(tableName);
        for (Field declaredMethod : declaredFields) {
            String name = declaredMethod.getName();
            Object property = BeanUtil.getProperty(t, name);
            StatementParameterInfo info = declaredMethod.getAnnotation(StatementParameterInfo.class);
            int mode = info == null ? 1 : info.mode();
            String valSetPre = info == null ? null : info.valSetPre();
            String valSetPost = info == null ? null : info.valSetPost();
            if(!isIgnoreField(name) && mode == 1) {
                sql.addValues(info == null || property != null,codeNameToDbName(name),valSetPre,valSetPost,property);
            }
        }
        sql.endValues();
        return null;
    }

    @Override
    protected void upsert(SQL sql, T t) {
        insert(sql,t);
        sql.onDuplicateKeyUpdate(o -> !isPrimaryKey(o.getName()));
    }

    @Override
    protected void update(SQL sql, T t) {
        Field[] declaredFields = t.getClass().getDeclaredFields();
        sql.update(tableName);
        List<Field> whereField = new ArrayList<>();
        for (Field declaredMethod : declaredFields) {
            String name = declaredMethod.getName();
            Object property = BeanUtil.getProperty(t, name);
            StatementParameterInfo info = declaredMethod.getAnnotation(StatementParameterInfo.class);
            int mode = info == null ? 1 : info.mode();
            String valSetPre = info == null ? null : info.valSetPre();
            String valSetPost = info == null ? null : info.valSetPost();
            boolean ignoreOrPrimary = false;
            if(isPrimaryKeyField(name)) {
                ignoreOrPrimary = true;
                whereField.add(declaredMethod);
            } else if(isIgnoreField(name) && mode == 1) {
                ignoreOrPrimary = true;
            }
            if(!ignoreOrPrimary) {
                sql.set(info == null || property != null,codeNameToDbName(name),valSetPre,valSetPost,property);
            }
        }
        for (Field declaredMethod : whereField) {
            StatementParameterInfo info = declaredMethod.getAnnotation(StatementParameterInfo.class);
            String valSetPre = info == null ? null : info.valSetPre();
            String valSetPost = info == null ? null : info.valSetPost();
            String name = declaredMethod.getName();
            Object property = BeanUtil.getProperty(t, name);
            sql.where(true,SQL.BIND_AND,codeNameToDbName(name),SQL.COND_QIS,valSetPre,valSetPost,property);
        }
    }

    public Object idParse(W id,String name) {
        return BeanUtil.getProperty(id,name);
    }

    @Override
    protected void delete(SQL sql, W id) {
        sql.delete(tableName);
        if(primaryKeyFieldList.size() == 1) {
            sql.where(SQL.BIND_AND,codeNameToDbName(primaryKeyFieldList.get(0)),SQL.COND_QIS,id);
        } else {
            if(idParseFunc != null) {
                for (String name : primaryKeyFieldList) {
                    sql.where(SQL.BIND_AND,codeNameToDbName(name),SQL.COND_QIS,idParseFunc.apply(id,name));
                }
            } else {
                throw new BaseServiceRunException(CUSTOM | NON_RETRYABLE,"悲剧了,我无法解释该主键");
            }
        }
    }

    @Override
    protected void get(SQL sql, W id) {
        Field[] declaredFields = getTClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            String name = declaredField.getName();
            String columnName = codeNameToDbName(name);
            sql.select(columnName,name);
        }
        sql.from(tableName);
        if(primaryKeyFieldList.size() == 1) {
            sql.where(SQL.BIND_AND,codeNameToDbName(primaryKeyFieldList.get(0)),SQL.COND_QIS,id);
        } else {
            if(idParseFunc != null) {
                for (String name : primaryKeyFieldList) {
                    sql.where(SQL.BIND_AND,codeNameToDbName(name),SQL.COND_QIS,idParseFunc.apply(id,name));
                }
            } else {
                throw new BaseServiceRunException(CUSTOM | NON_RETRYABLE,"悲剧了,我无法解释该主键");
            }
        }
    }

    /**
     * 将字段名转为数据库列名
     * @param name 字段名
     * @return 列名
     */
    public String codeNameToDbName(String name) {
        return name;
    }

    public BiFunction<W, String, Object> getIdParseFunc() {
        return idParseFunc;
    }

    @Override
    public List<T> list(SelectCond selectCond) {
        SQL sql = new SQL();
        if(selectCond.isPaging()) {
            selectCond.calcpage();
            sql.select("COUNT(1)","result");
            sql.from(tableName);
            List<SelectCond.SelectCondNode> searchCondNodeList = selectCond.getSearchCondNodeList();
            for (SelectCond.SelectCondNode selectCondNode : searchCondNodeList) {
                sql.where(selectCondNode.getBind(),codeNameToDbName(selectCondNode.getFieldName()),selectCondNode.getCondition(),selectCondNode.getContent());
            }
            ArrayList<ResultStr> resultStrs = repositoryImpl.selectByResolver(sql, ResultStr.class);
            String result = resultStrs.isEmpty() ? null : resultStrs.get(0).getResult();
            Long total = AssertUtil.isEmpty(result) ? -1L : Long.valueOf(result);
            selectCond.setTotal(total);
            if(total <= 0) {
                return new ArrayList<>();
            }
        }
        sql = new SQL();
        if(selectCond.getQueryFields() == null || selectCond.getQueryFields().isEmpty()) {
            Field[] declaredFields = getTClass().getDeclaredFields();
            for (Field declaredField : declaredFields) {
                String name = declaredField.getName();
                String columnName = codeNameToDbName(name);
                sql.select(columnName,name);
            }
        } else {
            List<String> queryFields = selectCond.getQueryFields();
            for (String queryField : queryFields) {
                String columnName = codeNameToDbName(queryField);
                if(AssertUtil.isNotEmpty(columnName))
                    sql.select(columnName,queryField);
            }
        }
        sql.from(tableName);
        List<SelectCond.SelectCondNode> searchCondNodeList = selectCond.getSearchCondNodeList();
        for (SelectCond.SelectCondNode selectCondNode : searchCondNodeList) {
            sql.where(selectCondNode.getBind(),codeNameToDbName(selectCondNode.getFieldName()),selectCondNode.getCondition(),selectCondNode.getContent());
        }
        List<String> orderBys = selectCond.getOrderBys();
        if(orderBys != null && !orderBys.isEmpty()) {
            String[] orderByArray = new String[orderBys.size()];
            int i = 0;
            for (String orderBy : orderBys) {
                if("DESC".equalsIgnoreCase(orderBy) || "ASC".equalsIgnoreCase(orderBy)) {
                    orderByArray[i] = orderBy;
                } else {
                    orderByArray[i] = codeNameToDbName(orderBy);
                }
                i++;
            }
            sql.orderBy(orderByArray);
        }
        if(selectCond.isPaging()) {
           sql.limit(selectCond.get_limit(),selectCond.get_offset());
        }
        return repositoryImpl.selectByResolver(sql,getTClass());
    }
}
