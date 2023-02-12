package top.zeimao77.product.sql;

import top.zeimao77.product.exception.BaseServiceRunException;
import top.zeimao77.product.util.AssertUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * 利用它可以简单直观的创建一个SQL
 * @author zeimao77
 * @since 2.1.1
 */
public class SQL implements StatementParamResolver, IWhere {

    private StringBuilder sqlBuilder = new StringBuilder(1024);
    private ArrayList<StatementParameter> statementParams = new ArrayList<>();
    private static final int FLAG_SELECT = 0x01;
    private static final int FLAG_SET = 0x02;
    private static final int FLAG_VALUES = 0x04;
    private static final int FLAG_WHERE = 0x08;
    private static final int FLAG_ON = 0x10;
    private int whereOrSetFlag = 0;
    private int sqlType = 0; // 1 = SELECT ;2 = DELETE ;3 = UPDATE ;4 INSERT ;5 = UPSERT ;
    private int paramIndex = 0;

    private int dbtype;

    public SQL() {
        this(Dbtype.ALL);
    }

    public SQL(int dbtype) {
        this.dbtype = dbtype;
    }

    public SQL select() {
        sqlBuilder.append("SELECT *");
        whereOrSetFlag |= FLAG_SELECT;
        this.sqlType = 1;
        return this;
    }

    public SQL append(String text) {
        sqlBuilder.append(text);
        return this;
    }

    public SQL select(String labelName) {
        if((whereOrSetFlag & FLAG_SELECT) == 0) {
            sqlBuilder.append("SELECT ");
            whereOrSetFlag |= FLAG_SELECT;
        } else {
            sqlBuilder.append(",");
        }
        sqlBuilder.append(labelName);
        return this;
    }

    /**
     * @param columnName 列名
     * @param alias 别名
     * @return this
     */
    public SQL select(String columnName,String alias) {
        if(alias == null || columnName.equals(alias)) {
            return select(columnName);
        } else {
            String labelName = String.format("%s AS \"%s\"",columnName,alias);
            return select(labelName);
        }
    }

    public SQL from(String tableName) {
        sqlBuilder.append(" FROM ").append(tableName);
        return this;
    }

    public SQL from(String tableName,String alias) {
        sqlBuilder.append(" FROM ").append(tableName).append(" AS ").append(alias);
        return this;
    }

    public SQL innerJoin(String tableName,String alias) {
        sqlBuilder.append(" INNER JOIN ").append(tableName).append(" AS ").append(alias);
        whereOrSetFlag &= (~FLAG_ON);
        return this;
    }

    public SQL leftJoin(String tableName,String alias) {
        sqlBuilder.append(" LEFT JOIN ").append(tableName).append(" AS ").append(alias);
        whereOrSetFlag &= (~FLAG_ON);
        return this;
    }

    public SQL rightJoin(String tableName,String alias) {
        sqlBuilder.append(" RIGHT JOIN ").append(tableName).append(" AS ").append(alias);
        whereOrSetFlag &= (~FLAG_ON);
        return this;
    }

    public SQL on(String bind,String columnName,String columnName2) {
        if((whereOrSetFlag & FLAG_ON) == 0) {
            sqlBuilder.append(" ON ");
            whereOrSetFlag |= FLAG_ON;
        } else if(BIND_AND.equals(bind)) {
            sqlBuilder.append(" AND ");
        } else if(BIND_OR.equals(bind)) {
            sqlBuilder.append(" OR ");
        }
        sqlBuilder.append(columnName).append(" = ").append(columnName2);
        return this;
    }

    public SQL where(String bind,String columnName,String cond,Object value) {
        return where(true,bind,columnName,cond,null,null,value);
    }


    public SQL where(boolean expression,String bind,String columnName,String cond,Object value) {
        return where(expression,bind,columnName,cond,null,null,value);
    }

    /**
     * @param expression 如果为 false 将不处理该条件
     * @param bind 条件关系
     * @see SQL#BIND_AND
     * @see SQL#BIND_OR
     * @param columnName 列名
     * @param cond 运算符
     * @see SQL#COND_QNIN
     * @param valSetPre 值的函数前缀
     * @param valSetPost 值的函数后缀
     * @param value 值
     * @return this
     */
    public SQL where(boolean expression,String bind,String columnName,String cond,String valSetPre,String valSetPost,Object value) {
        if(!expression) {
            return this;
        }
        if((whereOrSetFlag & FLAG_WHERE) == 0) {
            sqlBuilder.append(" WHERE ");
            whereOrSetFlag |= FLAG_WHERE;
        } else if(BIND_AND.equals(bind)) {
            sqlBuilder.append(" AND ");
        } else if(BIND_OR.equals(bind)) {
            sqlBuilder.append(" OR ");
        }
        sqlBuilder.append(columnName);
        Consumer<Object> con = o -> {
            StatementParameter objectJdbcParam = new StatementParameter<>(++paramIndex, columnName);
            objectJdbcParam.setJavaType(o.getClass());
            objectJdbcParam.setValue(o);
            statementParams.add(objectJdbcParam);
        };
        switch (cond) {
            case COND_QIS -> {
                sqlBuilder.append(" = ");
                if(!AssertUtil.isEmpty(valSetPre))
                    sqlBuilder.append(valSetPre);
                sqlBuilder.append("?");
                if(!AssertUtil.isEmpty(valSetPost))
                    sqlBuilder.append(valSetPost);
                con.accept(value);
            }
            case COND_QGT -> {
                sqlBuilder.append(" > ");
                if(!AssertUtil.isEmpty(valSetPre))
                    sqlBuilder.append(valSetPre);
                sqlBuilder.append("?");
                if(!AssertUtil.isEmpty(valSetPost))
                    sqlBuilder.append(valSetPost);
                con.accept(value);
            }
            case COND_QGTE -> {
                sqlBuilder.append(" >= ");
                if(!AssertUtil.isEmpty(valSetPre))
                    sqlBuilder.append(valSetPre);
                sqlBuilder.append("?");
                if(!AssertUtil.isEmpty(valSetPost))
                    sqlBuilder.append(valSetPost);
                con.accept(value);
            }
            case COND_QLT -> {
                sqlBuilder.append(" < ");
                if(!AssertUtil.isEmpty(valSetPre))
                    sqlBuilder.append(valSetPre);
                sqlBuilder.append("?");
                if(!AssertUtil.isEmpty(valSetPost))
                    sqlBuilder.append(valSetPost);
                con.accept(value);
            }
            case COND_QLTE -> {
                sqlBuilder.append(" <= ");
                if(!AssertUtil.isEmpty(valSetPre))
                    sqlBuilder.append(valSetPre);
                sqlBuilder.append("?");
                if(!AssertUtil.isEmpty(valSetPost))
                    sqlBuilder.append(valSetPost);
                con.accept(value);
            }
            case COND_QLIKE -> {
                sqlBuilder.append(" LIKE CONCAT('%',");
                if(!AssertUtil.isEmpty(valSetPre))
                    sqlBuilder.append(valSetPre);
                sqlBuilder.append("?");
                if(!AssertUtil.isEmpty(valSetPost))
                    sqlBuilder.append(valSetPost);
                sqlBuilder.append(",'%')");
                con.accept(value);
            }
            case COND_QLLIKE -> {
                sqlBuilder.append(" LIKE CONCAT('%',");
                if(!AssertUtil.isEmpty(valSetPre))
                    sqlBuilder.append(valSetPre);
                sqlBuilder.append("?");
                if(!AssertUtil.isEmpty(valSetPost))
                    sqlBuilder.append(valSetPost);
                sqlBuilder.append(")");
                con.accept(value);
            }
            case COND_QRLIKE -> {
                sqlBuilder.append(" LIKE CONCAT(");
                if(!AssertUtil.isEmpty(valSetPre))
                    sqlBuilder.append(valSetPre);
                sqlBuilder.append("?");
                if(!AssertUtil.isEmpty(valSetPost))
                    sqlBuilder.append(valSetPost);
                sqlBuilder.append(",'%')");
                con.accept(value);
            }
            case COND_QREGEXP -> {
                sqlBuilder.append(" REGEXP ");
                if(!AssertUtil.isEmpty(valSetPre))
                    sqlBuilder.append(valSetPre);
                sqlBuilder.append("?");
                if(!AssertUtil.isEmpty(valSetPost))
                    sqlBuilder.append(valSetPost);
                con.accept(value);
            }
            case COND_QNE -> {
                sqlBuilder.append(" <> ");
                if(!AssertUtil.isEmpty(valSetPre))
                    sqlBuilder.append(valSetPre);
                sqlBuilder.append("?");
                if(!AssertUtil.isEmpty(valSetPost))
                    sqlBuilder.append(valSetPost);
                con.accept(value);
            }
            case COND_QIN -> {
                sqlBuilder.append(" IN (");
                if(value.getClass().isArray() || value instanceof Collection) {
                    Object[] values = null;
                    if(value instanceof Collection) {
                        values = ((Collection<?>) value).toArray();
                    } else {
                        values = (Object[]) value;
                    }
                    for (int i = 0; i < values.length; i++) {
                        if(i > 0)
                            sqlBuilder.append(",");
                        if(!AssertUtil.isEmpty(valSetPre))
                            sqlBuilder.append(valSetPre);
                        sqlBuilder.append("?");
                        if(!AssertUtil.isEmpty(valSetPost))
                            sqlBuilder.append(valSetPost);
                        con.accept(values[i]);
                    }
                }
                sqlBuilder.append(")");
            }
            case COND_QNIN -> {
                sqlBuilder.append(" NOT IN (");
                if(value.getClass().isArray() || value instanceof Collection) {
                    Object[] values = null;
                    if(value instanceof Collection) {
                        values = ((Collection<?>) value).toArray();
                    } else {
                        values = (Object[]) value;
                    }
                    for (int i = 0; i < values.length; i++) {
                        if(i > 0)
                            sqlBuilder.append(",");
                        if(!AssertUtil.isEmpty(valSetPre))
                            sqlBuilder.append(valSetPre);
                        sqlBuilder.append("?");
                        if(!AssertUtil.isEmpty(valSetPost))
                            sqlBuilder.append(valSetPost);
                        con.accept(values[i]);
                    }
                }
                sqlBuilder.append(")");
            }
            default -> throw new BaseServiceRunException("不支持的查询运算符");
        }
        return this;
    }

    public SQL insert(String tableName) {
        sqlType = 4;
        sqlBuilder.append("INSERT INTO ").append(tableName);
        return this;
    }

    public SQL update(String tableName) {
        sqlType = 3;
        sqlBuilder.append("UPDATE ").append(tableName);
        return this;
    }

    public SQL delete(String tableName) {
        sqlType = 2;
        sqlBuilder.append("DELETE FROM ").append(tableName);
        return this;
    }

    public SQL limit(int limit,int offset) {
        sqlBuilder.append(String.format(" LIMIT %d OFFSET %d",offset,limit));
        return this;
    }

    public SQL orderBy(String... orderBy) {
        if(orderBy == null || orderBy.length == 0) {
            return this;
        }
        sqlBuilder.append(" ORDER BY ");
        for (int i = 0; i < orderBy.length; i++) {
            if(i == 0) {
                sqlBuilder.append(orderBy[i]);
            } else if("DESC".equalsIgnoreCase(orderBy[i]) || "ASC".equalsIgnoreCase(orderBy[i])) {
                sqlBuilder.append(" ").append(orderBy[i].toUpperCase());
            } else {
                sqlBuilder.append(" ,").append(orderBy[i]);
            }
        }
        return this;
    }

    public SQL groupby(String... groupBy) {
        if(groupBy == null || groupBy.length == 0) {
            return this;
        }
        sqlBuilder.append(" GROUP BY");
        for (int i = 0; i < groupBy.length; i++) {
            if(i == 0) {
                sqlBuilder.append(groupBy[i]);
            } else {
                sqlBuilder.append(" ,").append(groupBy[i]);
            }
        }
        return this;
    }

    public SQL addValues(String columnName,Object value) {
        return addValues(true,columnName,value);
    }

    /**
     * 可以通过一个表达式来设置SQL 动态SQL实现
     * @param expression 表达式 如果通过将设置值到SQL
     * @param columnName 列名
     * @param value 值
     * @return this
     */
    public SQL addValues(boolean expression,String columnName,Object value) {
        return addValues(expression,columnName,null,null,value);
    }

    public SQL addValues(boolean expression,String columnName,String valSetPre,String valSetPost,Object value) {
        if(expression) {
            if((whereOrSetFlag & FLAG_VALUES) == 0) {
                sqlBuilder.append("(");
                whereOrSetFlag |= FLAG_VALUES;
            } else {
                sqlBuilder.append(",");
            }
            sqlBuilder.append(columnName);
            addJdbcParam(columnName,valSetPre,valSetPost,value);
        }
        return this;
    }

    /**
     * 配合
     * @see SQL#addValues(boolean, String, Object)
     * 将SET参数添加完之后调用以设置VALUES
     * @return this
     */
    public SQL endValues() {
        return endValue(paramIndex);
    }

    public SQL endValue(int c) {
        sqlBuilder.append(") VALUES (");
        for (int i = 0; i < paramIndex; i++) {
            StatementParameter statementParameter = statementParams.get(i);
            if(i > 0)
                sqlBuilder.append(",");
            if(AssertUtil.isNotEmpty(statementParameter.getValSetPre()))
                sqlBuilder.append(statementParameter.getValSetPre());
            sqlBuilder.append("?");
            if(AssertUtil.isNotEmpty(statementParameter.getValSetPost()))
                sqlBuilder.append(statementParameter.getValSetPost());

        }
        sqlBuilder.append(")");
        return this;
    }

    /**
     * MYSQL upsert操作
     * @return
     */
    public SQL onDuplicateKeyUpdate() {
        this.sqlType = 5;
        sqlBuilder.append(" ON DUPLICATE KEY UPDATE ");
        return onDuplicateKeyUpdate(o -> true);
    }

    /**
     * postgreSQL 啥事也不做
     * @return
     */
    public SQL onConflictDoNothing(String keyName) {
        sqlBuilder.append(" ON CONFLICT ON CONSTRAINT ");
        sqlBuilder.append(keyName);
        sqlBuilder.append(" DO NOTHING");
        return this;
    }


    /**
     * postgreSQL upsert
     * @param columnName
     * @return
     */
    public SQL onConflict(String columnName) {
        this.sqlType = 5;
        sqlBuilder.append(" ON CONFLICT (");
        sqlBuilder.append(columnName);
        sqlBuilder.append(") DO UPDATE SET ");
        return onDuplicateKeyUpdate(o -> {
            if (o.getName().equals(columnName)) {
                return false;
            }
            return true;
        });
    }

    public SQL onConflict(String keyName, List<String> columnNameList) {
        this.sqlType = 5;
        sqlBuilder.append(" ON CONFLICT ON CONSTRAINT ");
        sqlBuilder.append(keyName);
        sqlBuilder.append(" DO UPDATE SET ");
        return onDuplicateKeyUpdate(o -> {
            if(columnNameList == null || columnNameList.size() == 0)
                return true;
            for (String column : columnNameList) {
                if(o.getName().equals(column)){
                    return false;
                }
            }
            return true;
        });
    }

    public SQL onConflict(String keyName,String... columnNames) {
        return onConflict(keyName,List.of(columnNames));
    }


    /**
     * @param predicate UPDATE语句字段过滤 如果返回false该字段将被忽略
     * @return this
     */
    public SQL onDuplicateKeyUpdate(Predicate<StatementParameter> predicate) {

        int l = statementParams.size();
        boolean firstUpdate = true;
        for (int i = 0; i < l; i++){
            StatementParameter jdbcParameter = statementParams.get(i);
            if(predicate.test(jdbcParameter)) {
                if(!firstUpdate)
                    sqlBuilder.append(",");
                sqlBuilder.append(jdbcParameter.getName());
                sqlBuilder.append(" = ");
                if(AssertUtil.isNotEmpty(jdbcParameter.getValSetPre()))
                    sqlBuilder.append(jdbcParameter.getValSetPre());
                sqlBuilder.append("?");
                if(AssertUtil.isNotEmpty(jdbcParameter.getValSetPost()))
                    sqlBuilder.append(jdbcParameter.getValSetPost());
                addJdbcParam(jdbcParameter.getName(),jdbcParameter.getValSetPre(),jdbcParameter.getValSetPost(), jdbcParameter.getValue());
                firstUpdate = false;
            }
        }
        return this;
    }

    public SQL set(boolean expression,String columnName,Object value) {
        return set(expression,columnName,null,null,value);
    }

    public SQL set(String columnName,Object value) {
        return set(true,columnName,value);
    }

    public SQL set(boolean expression,String columnName,String valSetPre,String valSetPost,Object value) {
        if(!expression)
            return this;
        if((whereOrSetFlag & FLAG_SET) == 0) {
            sqlBuilder.append(" SET ");
            whereOrSetFlag |= FLAG_SET;
        } else {
            sqlBuilder.append(",");
        }
        sqlBuilder.append(columnName).append(" = ");
        if(AssertUtil.isNotEmpty(valSetPre))
            sqlBuilder.append(valSetPre);
        sqlBuilder.append("?");
        if(AssertUtil.isNotEmpty(valSetPost))
            sqlBuilder.append(valSetPost);
        addJdbcParam(columnName,valSetPre,valSetPost,value);
        return this;
    }

    protected boolean addJdbcParam(String columnName,String valSetPre,String valSetPost,Object value) {
        StatementParameter objectJdbcParam = new StatementParameter<>(++paramIndex, columnName);
        objectJdbcParam.setJavaType(value == null ? null : value.getClass());
        objectJdbcParam.setValue(value);
        objectJdbcParam.setValSetPre(valSetPre);
        objectJdbcParam.setValSetPost(valSetPost);
        return statementParams.add(objectJdbcParam);
    }

    @Override
    public String getSql() {
        return sqlBuilder.toString();
    }

    @Override
    public void resolve() {}

    @Override
    public ArrayList<StatementParameter> getStatementParams() {
        return statementParams;
    }

    public int getSqlType() {
        return sqlType;
    }

    public int getDbtype() {
        return dbtype;
    }

    public void setDbtype(int dbtype) {
        this.dbtype = dbtype;
    }
}
