package top.zeimao77.product.mysql;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.zeimao77.product.exception.BaseServiceRunException;
import top.zeimao77.product.util.AssertUtil;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * 通过该工具使用MYSQL 注意
 * @see SimpleMysql#newConnection 属性设置
 */
public class SimpleMysql implements AutoCloseable, Reposit {

    private static Logger logger = LogManager.getLogger(SimpleMysql.class);
    private int queryTimeout = 10;

    private DataSource dataSource;
    private Connection connection;
    /**
     * 如果为false将由本实例来管理连接，这样该类将不在是线程安全的，连接将所有线程共享 缺省
     * @see SimpleMysql#connection
     * 如果为true本实例不会管理连接，这样该类线程安全，每次执行将创建新的连接 这样做提供了对外部实现事务连接管理器的支持
     */
    private boolean newConnection = false;
    private ResultSetResolve resultSetResolvel;

    private PreparedStatementSetter jdbcParamSetter;

    private String testSql = "SELECT USER() AS result";

    public SimpleMysql(DataSource dataSource) {
        this(dataSource, DefaultPreparedStatementSetter.INSTANCE,DefaultResultSetResolve.INSTANCE);
    }

    /**
     *
     * @param dataSource 数据源
     * @param resultSetResolvel 结果集解析器
     */
    public SimpleMysql(DataSource dataSource, PreparedStatementSetter jdbcParamSetter, ResultSetResolve resultSetResolvel) {
        this.dataSource = dataSource;
        this.jdbcParamSetter = jdbcParamSetter;
        this.resultSetResolvel = resultSetResolvel;
    }

    public SimpleMysql(Connection connection){
        this(connection, DefaultPreparedStatementSetter.INSTANCE, DefaultResultSetResolve.INSTANCE);
    }

    /**
     *
     * @param connection 连接
     * @param resultSetResolve 结果集解析器
     */
    public SimpleMysql(Connection connection, PreparedStatementSetter jdbcParamSetter, ResultSetResolve resultSetResolve) {
        this.connection = connection;
        this.jdbcParamSetter = jdbcParamSetter;
        this.resultSetResolvel = resultSetResolve;
    }

    @Override
    public boolean testConnection() {
        try {
            String s = this.selectString(testSql, null);
            logger.info("test connection 结果:{}",s);
        } catch (RuntimeException e) {
            logger.error("SQL错误",e);
        }
        return false;
    }

    protected Connection createContection() {
        try {
            if(!this.newConnection && this.connection != null && !connection.isClosed()) {
                return this.connection;
            }
            logger.info("旧的连接不可用，尝试创建一个新的连接...");
            if(this.connection != null && !this.connection.isClosed()) {
                this.connection.close();
            }
            this.connection = null;
            if(this.newConnection) {
                return this.dataSource.getConnection();
            } else {
                this.connection = this.dataSource.getConnection();
            }
        } catch (SQLException e) {
            throw new BaseServiceRunException("获取连接错误",e);
        }
        return this.connection;
    }

    private void checkNewConnection() {
        if(this.newConnection)
            throw new BaseServiceRunException("不支持的操作,原因:newConnection=true");
    }

    public void autoCommit(boolean autoCommlit) {
        checkNewConnection();
        try {
            createContection().setAutoCommit(autoCommlit);
        } catch (SQLException e) {
            throw new BaseServiceRunException("SQL错误",e);
        }
    }

    public void commit() {
        checkNewConnection();
        try {
            createContection().commit();
        } catch (SQLException e) {
            throw new BaseServiceRunException("SQL错误",e);
        }
    }

    public void close() {
        checkNewConnection();
        try {
            createContection().close();
        } catch (SQLException e) {
            throw new BaseServiceRunException("SQL错误",e);
        }
    }

    void close(Connection connection) {
        if(!this.newConnection) {
            return;
        }
        try {
            if(connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new BaseServiceRunException("SQL错误",e);
        }
    }

    public void rollback() {
        checkNewConnection();
        try {
            createContection().rollback();
        } catch (SQLException e) {
            throw new BaseServiceRunException("SQL错误",e);
        }
    }

    public void rollback(Savepoint savepoint) {
        checkNewConnection();
        try {
            createContection().rollback(savepoint);
        } catch (SQLException e) {
            throw new BaseServiceRunException("SQL错误",e);
        }
    }

    public Savepoint setSavepoint() {
        checkNewConnection();
        try {
            return createContection().setSavepoint();
        } catch (SQLException e) {
            throw new BaseServiceRunException("SQL错误",e);
        }
    }

    public Savepoint setSavepoint(String name) {
        checkNewConnection();
        try {
            return createContection().setSavepoint(name);
        } catch (SQLException e) {
            throw new BaseServiceRunException("SQL错误",e);
        }
    }

    /**
     * Connection.TRANSACTION_NONE  // 不支持事务
     * Connection.TRANSACTION_READ_UNCOMMITTED  // 读未提交
     * Connection.TRANSACTION_READ_COMMITTED  // 读已提交
     * Connection.TRANSACTION_REPEATABLE_READ  // 可重复读
     * Connection.TRANSACTION_SERIALIZABLE // 串行化
     * @param level 事务隔离级别
     */
    public void setTransactionIsolation(int level) {
        checkNewConnection();
        try {
            createContection().setTransactionIsolation(level);
        } catch (SQLException e) {
            throw new BaseServiceRunException("SQL错误",e);
        }
    }

    /**
     * 查询一个字符串 出参必须以result列名来指定
     * @param sqlt sql模板，参考mybatis
     * @param param sql参数
     * @return
     */
    public String selectString(String sqlt, Object param) {
        ArrayList<String> strings = selectListString(sqlt, param);
        return strings.isEmpty() ? null : strings.get(0);
    }

    public Long selectLong(String sqlt,Object param) {
        String s = selectString(sqlt, param);
        return s == null ? null : Long.valueOf(s);
    }

    /**
     * 查询返回一个对象
     * @param sqlt sql模板，参考mybatis
     * @param param sql参数
     * @param clazz 返回类型对应的Class对象
     * @param <T> 指定返回的类型
     * @return
     */
    public <T> T selectFirstObj(String sqlt,Object param,Class<T> clazz) {
        List<T> ts = selectListObj(sqlt, param, clazz);
        return ts.isEmpty() ? null : ts.get(0);
    }

    @Override
    public int updateByResolver(StatementParamResolver sql) {
        return update(sql.getSql(), sql.getStatementParams());
    }

    public int update(String sql) {
        return update(sql,new ArrayList<>(0));
    }

    /**
     * 执行一个更新SQL语句
     * @param sql SQL 占位符使用： ?
     * @param statementParams statement参数列表
     * @return
     */
    public int update(String sql,ArrayList<StatementParameter> statementParams) {
        Connection connection = createContection();
        try {
            logger.debug("Prepared SQL:{}",sql);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (StatementParameter statementParam : statementParams) {
                Object o = statementParam.getValue();
                setParam(preparedStatement,statementParam.getIndex(),statementParam.getJavaType(),statementParam.getJdbcType(),o);
            }
            long start = System.currentTimeMillis();
            int update = preparedStatement.executeUpdate();
            logger.debug("SQL执行耗时：{}ms",(System.currentTimeMillis() - start));
            return update;
        } catch (SQLException e) {
            throw new BaseServiceRunException("SQL错误",e);
        } finally {
            close(connection);
        }
    }

    /**
     * 执行一个更新SQL
     * @param sqlt SQL模板，参考mybatis
     * @param param SQL参数
     * @return 影响的行数
     */
    public int update(String sqlt,Object param) {
        DefaultStatementParamResolver resolver = new DefaultStatementParamResolver(sqlt, param);
        resolver.resolve();
        String sql = resolver.getSql();
        ArrayList<StatementParameter> statementParams = resolver.getStatementParams();
        return update(sql,statementParams);
    }

    public <T> ArrayList<T> call(String sqlt, Map<String,Object> param, Class<T> clazz) {
        return call(sqlt,param,this.resultSetResolvel,clazz);
    }

    /**
     * 调用存储过程
     * 示例：call("call clean_table_student(#{resultCode,javaType=Integer,jdbcType=INT,mode=OUT}
     *          ,#{message,javaType=String,MODE=OUT,jdbcType=VARCHAR});",param);
     * @param sqlt
     * @param param
     */
    public <T> ArrayList<T> call(String sqlt, Map<String,Object> param, ResultSetResolve resolve, Class<T> clazz) {
        AssertUtil.notNull(param,"参数 param 必需");
        ArrayList<T> resultList = new ArrayList<>();
        DefaultStatementParamResolver resolver = new DefaultStatementParamResolver(sqlt,param);
        resolver.resolve();
        String sql = resolver.getSql();
        ArrayList<StatementParameter> statementParams = resolver.getStatementParams();
        ArrayList<StatementParameter> outParams = new ArrayList<>();
        Connection contection = createContection();
        try {
            logger.debug("Prepared SQL:{}",sql);
            CallableStatement callableStatement = contection.prepareCall(sql);
            callableStatement.setQueryTimeout(queryTimeout);
            for (StatementParameter statementParam : statementParams) {
                if(statementParam.getMode() == 1) {
                    setParam(callableStatement,statementParam.getIndex(),statementParam.getJavaType(),statementParam.getJdbcType(),statementParam.getValue());
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
                resolve.populate(resultSet,clazz,resultList);
            }
            for (StatementParameter outParam : outParams) {
                Object object = callableStatement.getObject(outParam.getIndex());
                Object out = resolve.resolve(object, outParam.getJavaType());
                param.put(outParam.getName(),out);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            close(contection);
        }
        return resultList;
    }

    /**
     * 为preparedStatement对象设置参数
     * @param preparedStatement
     * @param index 参数位置
     * @param javaType JAVA类型
     * @param jdbcType MYSQL数据库类型
     * @param value 值
     */
    private void setParam(PreparedStatement preparedStatement,int index,Class<?> javaType,int jdbcType,Object value) {
        logger.debug("param({}):{}",index,value);
        this.jdbcParamSetter.setParam(preparedStatement,index,javaType,jdbcType,value);
    }

    @Override
    public <T> ArrayList<T> selectByResolver(StatementParamResolver sql, Class<T> clazz) {
        if(clazz == String.class) {
            List<ResultStr> objects = selectListObj(sql.getSql(), sql.getStatementParams(), ResultStr.class);
            ArrayList<String> collect = objects.stream().map(ResultStr::getResult)
                    .collect(ArrayList::new,ArrayList::add,ArrayList::addAll);
            return (ArrayList<T>) collect;
        }
        return select(sql.getSql(),sql.getStatementParams(),this.resultSetResolvel,clazz);
    }

    public <T> ArrayList<T> selectListObj(String sql,Class<T> clazz) {
        return select(sql,new ArrayList(0),this.resultSetResolvel,clazz);
    }

    /**
     * 查询对象列表
     * @param sql SQL 占位符使用： ?
     * @param statementParams statement参数列表
     * @param clazz 返回类型对应的Class对象
     * @param <T> 指定返回的类型
     * @return
     */
    public <T> ArrayList<T> select(String sql, ArrayList<StatementParameter> statementParams, ResultSetResolve resolve, Class<T> clazz) {
        ArrayList<T> list = new ArrayList<>();
        Connection contection = createContection();
        try{
            logger.debug("Prepared SQL:{}",sql);
            PreparedStatement preparedStatement = contection.prepareStatement(sql);
            for (StatementParameter statementParam : statementParams) {
                Object o = statementParam.getValue();
                setParam(preparedStatement,statementParam.getIndex(),statementParam.getJavaType(),statementParam.getJdbcType(),o);
            }
            preparedStatement.setQueryTimeout(queryTimeout);
            long start = System.currentTimeMillis();
            ResultSet resultSet = preparedStatement.executeQuery();
            logger.debug("SQL执行耗时：{}ms",(System.currentTimeMillis() - start));
            resolve.populate(resultSet, clazz,list);
        } catch (SQLException e) {
            throw new BaseServiceRunException("SQL错误",e);
        } finally {
            close(contection);
        }
        return list;
    }

    /**
     * @param sqlt SQL语句 使用#{*}点位符替换 如果参数是数组使用?占位
     * @param param 参数 支持 Map、Bean、数组参数
     * @param clazz 返回类型
     * @param <T> 返回类型约束
     * @return 查询结果
     */
    public <T> ArrayList<T> selectListObj(String sqlt, Object param, Class<T> clazz) {
        if(param == null)
            param = new HashMap<>(0);
        DefaultStatementParamResolver resolver = new DefaultStatementParamResolver(sqlt,param);
        resolver.resolve();
        String sql = resolver.getSql();
        ArrayList<StatementParameter> statementParams = resolver.getStatementParams();
        return select(sql,statementParams, this.resultSetResolvel,clazz);
    }

    /**
     * 注意: 返回值需要使用 result 作为别名
     * @param sqlt SQL语句 使用#{*}点位符替换 如果参数是数组使用?占位
     * @param param  参数 支持 Map、Bean、数组参数
     * @return 查询结果
     */
    public ArrayList<String> selectListString(String sqlt, Object param) {
        List<ResultStr> objects = param == null ? selectListObj(sqlt,ResultStr.class)
                : selectListObj(sqlt, param, ResultStr.class);
        ArrayList<String> collect = objects.stream().map(ResultStr::getResult)
                .collect(ArrayList::new,ArrayList::add,ArrayList::addAll);
        return collect;
    }

    public Connection getConnection() {
        return connection;
    }

    public int getQueryTimeout() {
        return queryTimeout;
    }

    public void setQueryTimeout(int queryTimeout) {
        this.queryTimeout = queryTimeout;
    }

    public String getTestSql() {
        return testSql;
    }

    public boolean isNewConnection() {
        return newConnection;
    }

    /**
     * @param newConnection
     */
    private void setNewConnection(boolean newConnection) {
        this.newConnection = newConnection;
        if(this.newConnection && this.connection != null) {
            try {
                connection.close();
                this.connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * SQL返回返回一个字符串，并以result为别名
     * 例如：SELECT USER() AS result
     * @param testSql 测试连接的SQL语句
     */
    public void setTestSql(String testSql) {
        this.testSql = testSql;
    }

    /**
     * @param list 对象列表
     * @param con SQL处理策略
     * @param <Z> 对象类型
     * @return 非影响的行数
     * @see java.sql.Statement#executeBatch() 求和
     */
    @Override
    public <Z> int batchUpdate(List<Z> list,BiConsumer<SQL,Z> con) {
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
                ArrayList<StatementParameter> params = sql.getStatementParams();
                for (StatementParameter param : params) {
                    setParam(preparedStatement,param.getIndex(),param.getJavaType(),param.getJdbcType(),param.getValue());
                }
                preparedStatement.addBatch();
            }
            int[] ints = preparedStatement.executeBatch();
            for (int anInt : ints) {
                result += anInt;
            }
        } catch (SQLException e) {
            throw new BaseServiceRunException("IO错误",e);
        } finally {
            close(connection);
        }
        logger.debug("SQL执行耗时：{}ms",(System.currentTimeMillis() - start));
        return result;
    }

    public static class Builder{

        private String driverClassName,url,username,password;
        private boolean newConnection = false;

        public Builder(String driverClassName){
            this.driverClassName = driverClassName;
        }

        public static Builder create5() {
            Builder builder = new Builder("com.mysql.jdbc.Driver");
            return builder;
        }

        public static Builder create8(){
            Builder builder = new Builder("com.mysql.cj.jdbc.Driver");
            return builder;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder newConnection() {
            this.newConnection = true;
            return this;
        }

        public Builder db(String host, Integer port, String db) {
            this.url = String.format("jdbc:mysql://%s:%d/%s?serverTimezone=Asia/Shanghai",host,port,db);
            return this;
        }

        public SimpleMysql build() {
            MysqlDataSource dataSource = new MysqlDataSource();
            dataSource.setUrl(url);
            dataSource.setUser(username);
            dataSource.setPassword(password);
            SimpleMysql simpleMysql = new SimpleMysql(dataSource);
            simpleMysql.setNewConnection(this.newConnection);
            return simpleMysql;
        }
    }

    public static class ResultStr {
        private String result;

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }
    }
}
