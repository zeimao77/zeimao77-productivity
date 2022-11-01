package top.zeimao77.product.mysql;

import top.zeimao77.product.sql.*;
import top.zeimao77.product.util.AssertUtil;

import java.util.*;

public class MapRepository extends AbstractRepository<Map<String,Object>,Map<String,Object>> {

    protected List<String> ignoreFieldList;
    protected List<String> primaryKeyFieldList;

    public MapRepository(Reposit repositoryImpl, String tableName) {
        super(repositoryImpl, tableName);
    }

    public MapRepository(Reposit repositoryImpl, String tableName, String[] primaryKeyFields, String[] ignoreFields) {
        super(repositoryImpl, tableName);
        AssertUtil.assertTrue(primaryKeyFields != null && primaryKeyFields.length > 0,"主键描述必需");
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
    }


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

    public String codeNameToDbName(String name) {
        return name;
    }


    @Override
    protected Map insert(SQL sql, Map<String,Object> map) {
        sql.insert(tableName);
        for (Map.Entry<String, Object> stringObjectEntry : map.entrySet()) {
            String name = stringObjectEntry.getKey();
            if(!isIgnoreField(name)) {
                Object property  = stringObjectEntry.getValue();
                sql.addValues(true,codeNameToDbName(name),null,null,property);
            }
        }
        sql.endValues();
        return map;
    }

    @Override
    protected void upsert(SQL sql, Map<String,Object> map) {
        insert(sql,map);
        sql.append(" ON DUPLICATE KEY UPDATE ");
        sql.onDuplicateKeyUpdate(o -> !isPrimaryKey(o.getName()));
    }

    @Override
    protected void update(SQL sql, Map<String,Object> map) {
        sql.update(tableName);
        List<String> whereField = new ArrayList<>();
        for (Map.Entry<String, Object> stringObjectEntry : map.entrySet()) {
            String name = stringObjectEntry.getKey();
            if(isPrimaryKeyField(name)) {
                whereField.add(name);
                continue;
            }
            if(!isIgnoreField(name)) {
                Object property  = stringObjectEntry.getValue();
                sql.set(true,codeNameToDbName(name),null,null,property);
            }
        }
        for (String name : whereField) {
            Object property = map.get(name);
            sql.where(SQL.BIND_AND,codeNameToDbName(name),SQL.COND_QIS,property);
        }
    }

    @Override
    protected void delete(SQL sql, Map<String,Object> id) {
        sql.delete(tableName);
        for (String name : primaryKeyFieldList) {
            Object property = id.get(name);
            sql.where(SQL.BIND_AND,codeNameToDbName(name),SQL.COND_QIS,property);
        }
    }

    @Override
    protected void get(SQL sql, Map<String,Object> id) {
        sql.select(tableName);
        for (String name : primaryKeyFieldList) {
            Object property = id.get(name);
            sql.where(SQL.BIND_AND,codeNameToDbName(name),SQL.COND_QIS,property);
        }
    }

    @Override
    public List<Map<String,Object>> list(SelectCond selectCond) {
        return Collections.EMPTY_LIST;
    }
}
