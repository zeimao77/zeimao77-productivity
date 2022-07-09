package top.zeimao77.product.mysql;

import top.zeimao77.product.exception.BaseServiceRunException;
import top.zeimao77.product.util.LongBitMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class SQL implements StatementParamResolver {

    public static final String BIND_AND = "AND";
    public static final String BIND_OR = "OR";
    public static final String COND_QIN = "$in";
    public static final String COND_QNIN = "$nin";
    public static final String COND_QIS = "$is";
    public static final String COND_QNE = "$ne";
    public static final String COND_QLIKE = "$like";
    public static final String COND_QLLIKE = "$llike";  // 左边添加百分号
    public static final String COND_QRLIKE = "$rlike";  // 右边添加百分号
    public static final String COND_QGT = "$gt";
    public static final String COND_QGTE = "$gte";
    public static final String COND_QLT = "$lt";
    public static final String COND_QLTE = "$lte";
    public static final String COND_QREGEXP = "$regexp";
    private StringBuilder sqlBuilder = new StringBuilder(1024);
    ArrayList<StatementParameter> statementParams = new ArrayList<>();
    private static final int FLAG_SELECT = 0x01;
    private static final int FLAG_SET = 0x02;
    private static final int FLAG_VALUES = 0x04;
    private static final int FLAG_WHERE = 0x08;
    private static final int FLAG_ON = 0x10;
    private int whereOrSetFlag = 0;
    private int sqlType = 0; // 1 = SELECT ;2 = DELETE ;3 = UPDATE ;4 INSERT
    private int paramIndex = 0;

    public SQL select() {
        sqlBuilder.append("SELECT *");
        whereOrSetFlag |= FLAG_SELECT;
        this.sqlType = 1;
        return this;
    }

    public SQL appent(String text) {
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
        whereOrSetFlag &= 0xFFFFFFF7;
        return this;
    }

    public SQL leftJoin(String tableName,String alias) {
        sqlBuilder.append(" LEFT JOIN ").append(tableName).append(" AS ").append(alias);
        whereOrSetFlag &= 0xFFFFFFF7;
        return this;
    }

    public SQL rightJoin(String tableName,String alias) {
        sqlBuilder.append(" RIGHT JOIN ").append(tableName).append(" AS ").append(alias);
        whereOrSetFlag &= 0xFFFFFFF7;
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

    public SQL where(boolean expression,String bind,String columnName,String cond,Object value) {
        if(expression) {
            return where(bind,columnName,cond,value);
        }
        return this;
    }

    /**
     * @param bind 条件关系
     * @see SQL#BIND_AND
     * @see SQL#BIND_OR
     * @param columnName 列名
     * @param cond 运算符
     * @see SQL#COND_QNIN
     * @param value 值
     * @return this
     */
    public SQL where(String bind,String columnName,String cond,Object value) {
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
                sqlBuilder.append(" = ?");
                con.accept(value);
            }
            case COND_QGT -> {
                sqlBuilder.append(" > ?");
                con.accept(value);
            }
            case COND_QGTE -> {
                sqlBuilder.append(" >= ?");
                con.accept(value);
            }
            case COND_QLT -> {
                sqlBuilder.append(" < ?");
                con.accept(value);
            }
            case COND_QLTE -> {
                sqlBuilder.append(" <= ?");
                con.accept(value);
            }
            case COND_QLIKE -> {
                sqlBuilder.append(" LIKE CONCAT('%',?,'%'");
                con.accept(value);
            }
            case COND_QLLIKE -> {
                sqlBuilder.append(" LIKE CONCAT('%',?)");
                con.accept(value);
            }
            case COND_QRLIKE -> {
                sqlBuilder.append(" LIKE CONCAT(?,'%')");
                con.accept(value);
            }
            case COND_QREGEXP -> {
                sqlBuilder.append(" REGEXP ?");
                con.accept(value);
            }
            case COND_QNE -> {
                sqlBuilder.append(" <> ?");
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
                        if(i == 0) {
                            sqlBuilder.append("?");
                        } else {
                            sqlBuilder.append(",?");
                        }
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
                        if(i == 0) {
                            sqlBuilder.append("?");
                        } else {
                            sqlBuilder.append(",?");
                        }
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
        sqlBuilder.append(String.format(" LIMIT %d,%d",limit,offset));
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
                sqlBuilder.append(" ").append(orderBy[i]);
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
        if(expression) {
            if((whereOrSetFlag & FLAG_VALUES) == 0) {
                sqlBuilder.append("(");
                whereOrSetFlag |= FLAG_VALUES;
            } else {
                sqlBuilder.append(",");
            }
            sqlBuilder.append(columnName);
            addJdbcParam(columnName,value);
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
        sqlBuilder.append(") VALUES (?");
        for (int i = 1; i < c; i++) {
            sqlBuilder.append(",?");
        }
        sqlBuilder.append(")");
        return this;
    }

    public SQL onDuplicateKeyUpdate() {
        return onDuplicateKeyUpdate(o -> true);
    }

    /**
     * @param predicate UPDATE语句字段过滤 如果返回false该字段将被忽略
     * @return this
     */
    public SQL onDuplicateKeyUpdate(Predicate<StatementParameter> predicate) {
        sqlBuilder.append(" ON DUPLICATE KEY UPDATE ");
        int l = statementParams.size();
        boolean firstUpdate = true;
        for (int i = 0; i < l; i++){
            StatementParameter jdbcParameter = statementParams.get(i);
            if(predicate.test(jdbcParameter)) {
                if(firstUpdate) {
                    sqlBuilder.append(String.format("%s = ?", jdbcParameter.getName()));
                    addJdbcParam(jdbcParameter.getName(), jdbcParameter.getValue());
                    firstUpdate = false;
                } else {
                    sqlBuilder.append(String.format(" ,%s = ?", jdbcParameter.getName()));
                    addJdbcParam(jdbcParameter.getName(), jdbcParameter.getValue());
                }
            }
        }
        return this;
    }

    public SQL set(boolean expression,String columnName,Object value) {
        if(expression) {
            return set(columnName,value);
        }
        return this;
    }

    public SQL set(String columnName,Object value) {
        if((whereOrSetFlag & FLAG_SET) == 0) {
            sqlBuilder.append(" SET ").append(columnName).append(" = ?");
            whereOrSetFlag |= FLAG_SET;
        } else {
            sqlBuilder.append(",").append(columnName).append(" = ?");
        }
        addJdbcParam(columnName,value);
        return this;
    }

    protected boolean addJdbcParam(String columnName,Object value) {
        StatementParameter objectJdbcParam = new StatementParameter<>(++paramIndex, columnName);
        objectJdbcParam.setJavaType(value == null ? null : value.getClass());
        objectJdbcParam.setValue(value);
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
}
