package top.zeimao77.product.sql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zeimao77.product.exception.BaseServiceRunException;
import top.zeimao77.product.util.AssertUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import static top.zeimao77.product.exception.ExceptionCodeDefinition.*;

/**
 * SQL客户端
 * 我们区分为两种使用场景：<br>
 * 1. 每次执行SQL都是一个单独事务
 * 可以通过 DataSourceTransactionFactory 来实例化它
 * 示例：
 * <pre>
 * DataSourceTransactionFactory f = new DataSourceTransactionFactory(dataSource);
 * new SimpleSqlClient(f,DefaultPreparedStatementSetter.INSTANCE,DefaultResultSetResolve.INSTANCE);
 * </pre>
 *
 * 2. 开启一个事务，直到关闭它
 * <pre>
 * ConnectionTransactionFactory f = new ConnectionTransactionFactory(connection);
 * new SimpleSqlClient(f,DefaultPreparedStatementSetter.INSTANCE,DefaultResultSetResolve.INSTANCE);
 * </pre>
 *
 * 简单的初始化方法:
 * <pre>
 * SimpleSqlTemplate simpleSqlTemplate= ComponentFactory.initSimpleSqlTemplate("mysql_top_zeimao77");
 * SimpleSqlClient simpleMysql = simpleSqlTemplate.openSession();
 * simpleMysql.close();
 * </pre>
 * @author zeimao77
 * @since 2.1.1
 */
public class SimpleSqlClient implements Reposit,AutoCloseable {

    private static Logger logger = LoggerFactory.getLogger(SimpleSqlClient.class);

    protected int queryTimeout = 30;

    protected TransactionFactory connectFacotry;

    public static SimpleSqlClient create(TransactionFactory connectFacotry) {
        return new SimpleSqlClient(connectFacotry);
    }

    /**
     *
     * @param connectFacotry Connection持有
    */
    public SimpleSqlClient(TransactionFactory connectFacotry) {
        this.connectFacotry = connectFacotry;
    }

    /**
     * 执行一个更新语句
     * @param sql SQL语句
     * @return 影响行数
     */
    @Override
    public int updateByResolver(StatementParamResolver sql) {
        sql.resolve();
        List<StatementParameter> statementParams = sql.getStatementParams();
        DefaultPreparedStatementSetter defaultPreparedStatementSetter = new DefaultPreparedStatementSetter(statementParams);
        return update(sql.getSql(),defaultPreparedStatementSetter);
    }

    public <Z> void batchUpdate(String sqlt,Consumer<PreparedStatement> statementParamSetter) {
        PreparedStatement preparedStatement = null;
        Connection connection = createContection();
        try {
            preparedStatement = connection.prepareStatement(sqlt);
            statementParamSetter.accept(preparedStatement);
        } catch (SQLException e) {
            throw new BaseServiceRunException(IOEXCEPTION,"IO错误",e);
        }  finally {
            close(connection);
        }
    }

    /**
     * 批量更新
     * @param list 数据源
     * @param con SQL构造器
     * @param <Z> 数据源类型
     * @return 影响行数
     */
    @Override
    public <Z> int batchUpdate(List<Z> list, BiConsumer<SQL, Z> con) {
        if(list == null || list.isEmpty())
            return 0;
        long start = System.currentTimeMillis();
        PreparedStatement preparedStatement = null;
        String lastSql = null;
        int result = 0;
        Connection connection = createContection();
        try {
            for (Z z : list) {
                SQL sql = new SQL();
                con.accept(sql,z);
                if(!sql.getSql().equals(lastSql) || preparedStatement == null) {
                    lastSql = sql.getSql();
                    logger.debug("Prepared SQL:{}",lastSql);
                    if(preparedStatement != null) {
                        int[] ints = preparedStatement.executeBatch();
                        for (int anInt : ints) {
                            result += anInt;
                        }
                    }
                    preparedStatement = connection.prepareStatement(sql.getSql());
                }
                ArrayList<StatementParameter> statementParamterList = sql.getStatementParams();
                new DefaultPreparedStatementSetter(statementParamterList).setParam(preparedStatement);
                preparedStatement.addBatch();
            }
            int[] ints = preparedStatement.executeBatch();
            for (int anInt : ints) {
                result += anInt;
            }
        } catch (SQLException e) {
            throw new BaseServiceRunException(IOEXCEPTION,"IO错误",e);
        } finally {
            close(connection);
        }
        logger.debug("SQL执行耗时：{}ms",(System.currentTimeMillis() - start));
        return result;
    }

    /**
     * 执行一个更新SQL,示例：
     * <pre>
     * simpleMysql.update("truncate table demo");
     * </pre>
     * @param sql
     * @return
     */
    @Override
    public int update(String sql) {
        return update(sql,null);
    }

    @Override
    public int update(String sqlt,Object params) {
        return updateByResolver(new DefaultStatementParamResolver(sqlt, params));
    }

    public int update(String sql,PreparedStatementSetter statementParamSetter) {
        Connection connection = createContection();
        try {
            logger.debug("Prepared SQL:{}",sql);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            if(statementParamSetter != null) {
                statementParamSetter.setParam(preparedStatement);
            }
            long start = System.currentTimeMillis();
            int update = preparedStatement.executeUpdate();
            logger.debug("SQL执行耗时：{}ms",(System.currentTimeMillis() - start));
            return update;
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new BaseServiceRunException(SQLICVEXCEPTION,"数据完整性约束错误",e);
        }catch (SQLException e) {
            throw new BaseServiceRunException(SQLEXCEPTION,"SQL错误",e);
        } finally {
            close(connection);
        }
    }

    public <T> ArrayList<T> select(String sql, Class<T> clazz) {
        return select(sql,null,clazz);
    }

    /**
     * 执行一个查询SQL
     * @param sql SQL 点位使用 ? 指定
     * @param statementParamSetter 参数设置器
     * @param clazz 返回类对象
     * @param <T> 返回泛型
     * @return 查询结果集
     */
    public <T> ArrayList<T> select(String sql, PreparedStatementSetter statementParamSetter
            , Class<T> clazz){
        ArrayList<T> list = new ArrayList<>();
        BeanResultMapResolver<T> tBeanResultMapResolver = new BeanResultMapResolver<>(clazz, list);
        select(sql,statementParamSetter,tBeanResultMapResolver);
        return list;
    }

    public void select(String sql, PreparedStatementSetter statementParamSetter,ResultSetResolver resolver) {
        Connection contection = createContection();
        try{
            logger.debug("Prepared SQL:{}",sql);
            PreparedStatement preparedStatement = contection.prepareStatement(sql);
            if(statementParamSetter != null) {
                statementParamSetter.setParam(preparedStatement);
            }
            preparedStatement.setQueryTimeout(queryTimeout);
            long start = System.currentTimeMillis();
            ResultSet resultSet = preparedStatement.executeQuery();
            logger.debug("SQL执行耗时：{}ms",(System.currentTimeMillis() - start));
            resolver.resolve(resultSet);
        } catch (SQLException e) {
            throw new BaseServiceRunException(SQLEXCEPTION,"SQL错误",e);
        } finally {
            close(contection);
        }
    }

    /**
     * 注意: 返回值需要使用 result 作为别名
     * @param sqlt SQL语句 使用#{*}点位符替换 如果参数是数组使用?占位
     * @param param  参数 支持 Map、Bean、数组参数
     * @return 查询结果
     */
    public ArrayList<String> selectListString(String sqlt, Object param) {
        List<ResultStr> objects = selectByResolver(new DefaultStatementParamResolver(sqlt, param),ResultStr.class);
        ArrayList<String> collect = objects.stream().map(ResultStr::getResult)
                .collect(ArrayList::new,ArrayList::add,ArrayList::addAll);
        return collect;
    }

    @Override
    public ArrayList<Map<String, Object>> selectListMap(String sqlt, Object param) {
        DefaultStatementParamResolver defaultStatementParamResolver = new DefaultStatementParamResolver(sqlt, param);
        defaultStatementParamResolver.resolve();
        List<StatementParameter> statementParams = defaultStatementParamResolver.getStatementParams();
        DefaultPreparedStatementSetter statementSetter = new DefaultPreparedStatementSetter(statementParams);
        return selectListMap(defaultStatementParamResolver.getSql(),statementSetter);
    }

    public ArrayList<Map<String,Object>> selectListMap(String sql, PreparedStatementSetter preparedStatementSetter) {
        ArrayList<Map<String,Object>> list = new ArrayList<>();
        Connection contection = createContection();
        try{
            logger.debug("Prepared SQL:{}",sql);
            PreparedStatement preparedStatement = contection.prepareStatement(sql);
            preparedStatementSetter.setParam(preparedStatement);
            preparedStatement.setQueryTimeout(queryTimeout);
            long start = System.currentTimeMillis();
            ResultSet resultSet = preparedStatement.executeQuery();
            logger.debug("SQL执行耗时：{}ms",(System.currentTimeMillis() - start));
            new HashMapResultSetResolver(list).resolve(resultSet);
        } catch (SQLException e) {
            throw new BaseServiceRunException(SQLEXCEPTION,"SQL错误",e);
        } finally {
            close(contection);
        }
        return list;
    }


    @Override
    public <T> ArrayList<T> selectByResolver(StatementParamResolver sql, Class<T> clazz) {
        sql.resolve();
        List<StatementParameter> statementParams = sql.getStatementParams();
        DefaultPreparedStatementSetter preparedStatementSetter = new DefaultPreparedStatementSetter(statementParams);
        return select(sql.getSql(),preparedStatementSetter,clazz);
    }

    public void selectByResolver(StatementParamResolver sql, ResultSetResolver resolver) {
        sql.resolve();
        List<StatementParameter> statementParams = sql.getStatementParams();
        DefaultPreparedStatementSetter statementSetter = new DefaultPreparedStatementSetter(statementParams);
        select(sql.getSql(),statementSetter,resolver);
    }

    @Override
    public <T> ArrayList<T> selectListObj(String sqlt,Object param, Class<T> clazz) {
        return selectByResolver(new DefaultStatementParamResolver(sqlt, param),clazz);
    }

    @Override
    public <T> ArrayList<T> selectListObj(String sql, Class<T> clazz) {
        return select(sql,null,clazz);
    }

    public <T> T selectFirstObj(String sqlt,Object param,Class<T> clazz) {
        ArrayList<T> select = selectListObj(sqlt,param,clazz);
        return select.isEmpty() ? null : select.get(0);
    }

    /**
     * 调用存储过程
     * 示例：
     * <pre>
     * call("call clean_table_student(#{resultCode,javaType=Integer,jdbcType=INT,mode=OUT}
     *          ,#{message,javaType=String,MODE=OUT,jdbcType=VARCHAR});",param);
     * </pre>
     * @param sqlt
     * @param param
     * @param clazz
     * @param <T>
     */
    public <T> ArrayList<T> call(String sqlt, Map<String,Object> param, Class<T> clazz) {
        AssertUtil.notNull(param,"参数 param 必需");
        ArrayList<T> resultList = new ArrayList<>();
        BeanResultMapResolver beanResultMapResolver = new BeanResultMapResolver<>(clazz,resultList);
        DefaultStatementParamResolver resolver = new DefaultStatementParamResolver(sqlt,param);
        resolver.resolve();
        String sql = resolver.getSql();
        List<StatementParameter> statementParams = resolver.getStatementParams();
        DefaultPreparedStatementSetter defaultPreparedStatementSetter = new DefaultPreparedStatementSetter(statementParams);
        ArrayList<StatementParameter> outParams = new ArrayList<>();
        Connection contection = createContection();
        try {
            logger.debug("Prepared SQL:{}",sql);
            CallableStatement callableStatement = contection.prepareCall(sql);
            callableStatement.setQueryTimeout(queryTimeout);
            for (StatementParameter statementParam : statementParams) {
                if(statementParam.getMode() == 1) {
                    defaultPreparedStatementSetter.setParam(callableStatement,statementParam.getIndex(),statementParam.getJavaType()
                            ,statementParam.getJdbcType(),statementParam.getValue());
                } else if(statementParam.getMode() == 2) {
                    callableStatement.registerOutParameter(statementParam.getIndex(),statementParam.getJdbcType());
                    outParams.add(statementParam);
                }
            }
            long start = System.currentTimeMillis();
            callableStatement.execute();
            logger.debug("SQL执行耗时：{}ms",(System.currentTimeMillis() - start));
            ResultSet resultSet = callableStatement.getResultSet();
            if(resultSet != null && clazz != null && clazz != Void.class) {
                beanResultMapResolver.resolve(resultSet);
            }
            for (StatementParameter outParam : outParams) {
                Object object = callableStatement.getObject(outParam.getIndex());
                Object out = DefaultFieldTypeResover.DEFAULT.resolve(object, outParam.getJavaType());
                param.put(outParam.getName(),out);
            }
        } catch (SQLException e) {
            throw new BaseServiceRunException(IOEXCEPTION,"SQL错误",e);
        }finally {
            close(contection);
        }
        return resultList;
    }

    public Connection createContection() {
        return this.connectFacotry.createContection();
    }

    public void close(Connection connection) {
        this.connectFacotry.close(connection);
    }

    @Override
    public void close() {
        try {
            this.connectFacotry.close();
        } catch (Exception e) {
            logger.error("close错误",e);
        }
    }

    public void commit() {
        this.connectFacotry.commit();
    }

    public void rollback() {
        this.connectFacotry.rollback();
    }

    public int getQueryTimeout() {
        return queryTimeout;
    }

    public void setQueryTimeout(int queryTimeout) {
        this.queryTimeout = queryTimeout;
    }

    public TransactionFactory getConnectFacotry() {
        return connectFacotry;
    }

    public void setConnectFacotry(TransactionFactory connectFacotry) {
        this.connectFacotry = connectFacotry;
    }
}
