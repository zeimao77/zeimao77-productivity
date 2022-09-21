package top.zeimao77.product.factory;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.pool2.impl.BaseObjectPoolConfig;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Connection;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import top.zeimao77.product.config.LocalContext;
import top.zeimao77.product.email.SimpleEmailSender;
import top.zeimao77.product.redis.JedisBuilder;
import top.zeimao77.product.redis.JedisClusterBuilder;
import top.zeimao77.product.sql.*;
import top.zeimao77.product.util.AssertUtil;
import top.zeimao77.product.util.StreamUtil;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.time.Duration;
import java.util.ArrayList;

/**
 * 辅助初始化部分组件
 * @author zeimao77
 * @since 2.1.1
 */
public class ComponentFactory {

    /**
     * 配置参考:
     * @see ComponentFactory#createDataSource(String)
     * @param prefx 前缀
     * @return SQL客户端工厂对象
     */
    public static SimpleSqlTemplate initSimpleSqlTemplate(String prefx,BeanFactory beanFactory) {
        DataSource dataSource = createDataSource(prefx);
        SimpleSqlTemplate simpleSqlFacroty = new SimpleSqlTemplate(dataSource);
        if(beanFactory != null)
            beanFactory.registerSingleton(prefx,simpleSqlFacroty);
        return simpleSqlFacroty;
    }

    /**
     * 配置参考:
     * @see ComponentFactory#createDataSource(String)
     * @param prefx 前缀
     * @return SQL客户端
     */
    public static SimpleSqlClient initSimpleSqlClient(String prefx,BeanFactory beanFactory) {
        DataSource dataSource = createDataSource(prefx);
        DataSourceTransactionFactory dataSourceTransactionFactory = new DataSourceTransactionFactory(dataSource);
        SimpleSqlClient simpleSqlClient = new SimpleSqlClient(dataSourceTransactionFactory
                , DefaultPreparedStatementSetter.INSTANCE, DefaultResultSetResolve.INSTANCE);
        if(beanFactory != null)
            beanFactory.registerSingleton(prefx,simpleSqlClient);
        return simpleSqlClient;
    }

    /**
     * 配置方法:
     * <pre>
     * ${prefx}_url=jdbc:mysql://${host}:${port}/order?serverTimezone=Asia/Shanghai
     * ${prefx}_username=root
     * ${prefx}_password=*****
     * ${prefx}_maximumPoolSize = 3
     * ${prefx}_maxLifetime= 1800000
     * ${prefx}_keepaliveTime=30000
     * </pre>
     * @param prefx 前缀
     * @return SimpleMysql实例
     */
    public static DataSource createDataSource(String prefx) {
        String url = LocalContext.getString(prefx + "_url");
        String username = LocalContext.getString(prefx + "_username");
        String password = LocalContext.getString(prefx + "_password");
        String driverClassName = LocalContext.getString(prefx + "_driverClassName");
        String maxLifetime = LocalContext.getString(prefx + "_maxLifetime");
        String connectionTestQuery = LocalContext.getString(prefx + "_connectionTestQuery");
        String connectionTimeout = LocalContext.getString(prefx + "_connectionTimeout");
        String poolName = LocalContext.getString(prefx + "_poolName");
        String maximumPoolSize = LocalContext.getString(prefx + "_maximumPoolSize");
        String minimumIdea = LocalContext.getString(prefx + "_minimumIdle");
        String idleTimeout = LocalContext.getString(prefx + "_idleTimeout");
        String keepaliveTime = LocalContext.getString(prefx + "_keepaliveTime");
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        if(AssertUtil.isNotEmpty(driverClassName))
            dataSource.setDriverClassName(driverClassName);
        if(AssertUtil.isNotEmpty(poolName))
            dataSource.setPoolName(poolName);
        // 客户端等待来自池的连接的最大毫秒数，超时将SQLException 最少 250; 默认 30000（30秒）
        if(AssertUtil.isNotEmpty(connectionTimeout))
            dataSource.setConnectionTimeout(Long.valueOf(connectionTimeout));
        // 连接的最大生命周期 最小 30000; 默认值：1800000（30 分钟）
        if(AssertUtil.isNotEmpty(maxLifetime))
            dataSource.setMaxLifetime(Long.valueOf(maxLifetime));
        // 允许达到的连接的最大数量 默认值：10
        if(AssertUtil.isNotEmpty(maximumPoolSize))
            dataSource.setMaximumPoolSize(Integer.valueOf(maximumPoolSize));
        // 维护的最小空闲连接数 默认值 与 maximumPoolSize 相同
        if(AssertUtil.isNotEmpty(minimumIdea))
            dataSource.setMinimumIdle(Integer.valueOf(minimumIdea));
        // 允许连接在池中空闲的最长时间 超时淘汰 0:永不淘汰
        // 允许的最小值为 10000; 默认值：600000（10 分钟）
        if(AssertUtil.isNotEmpty(idleTimeout))
            dataSource.setIdleTimeout(Long.valueOf(idleTimeout));
        // 尝试保持连接活动的频率 最小值为 30000（30 秒）， 默认值：0（禁用）
        if(AssertUtil.isNotEmpty(keepaliveTime))
            dataSource.setKeepaliveTime(Long.valueOf(keepaliveTime));
        // 存在Connection.isValid() API时不建议设置此值
        if(AssertUtil.isNotEmpty(connectionTestQuery))
            dataSource.setConnectionTestQuery(connectionTestQuery);
        return dataSource;
    }

    /**
     * 配置方法：
     * <pre>
     * ${prefx}_host[0]=${host0}:${port0}
     * ${prefx}_host[1]=${host1}:${port1}
     * ${prefx}_password=******
     * </pre>
     * @param prefx 前缀
     * @return JedisCluster实例
     */
    public static JedisCluster initJedisCluster(String prefx,BeanFactory beanFactory) {
        ArrayList<String> hps = LocalContext.get(prefx+"_host[%d]",6);
        String passoword = LocalContext.getString(prefx+"_passoword");
        String maxIdle = LocalContext.getString(prefx + "_maxIdle");
        String minIdle = LocalContext.getString(prefx + "_minIdle");
        String maxTotal = LocalContext.getString(prefx + "_maxTotal");
        String maxWait = LocalContext.getString(prefx + "_maxWait");
        Boolean testOnBorrow = LocalContext.getBoolean(prefx + "_testOnBorrow");
        Boolean testOnReturn = LocalContext.getBoolean(prefx + "_testOnReturn");
        Boolean testOnCreate = LocalContext.getBoolean(prefx + "_testOnCreate");
        String timeBetweenEvictionRuns = LocalContext.getString(prefx + "_timeBetweenEvictionRuns");
        String numTestsPerEvictionRun = LocalContext.getString(prefx + "_numTestsPerEvictionRun");
        GenericObjectPoolConfig<Connection> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxIdle(AssertUtil.isNotEmpty(maxIdle)?Integer.valueOf(maxIdle).intValue()
                :GenericObjectPoolConfig.DEFAULT_MAX_IDLE);
        poolConfig.setMinIdle(AssertUtil.isNotEmpty(minIdle)?Integer.valueOf(minIdle).intValue()
                :GenericObjectPoolConfig.DEFAULT_MIN_IDLE);
        poolConfig.setMaxTotal(AssertUtil.isNotEmpty(maxTotal)?Integer.valueOf(maxTotal).intValue()
                :GenericObjectPoolConfig.DEFAULT_MAX_TOTAL);
        poolConfig.setMaxWait(AssertUtil.isNotEmpty(maxWait)?Duration.ofMillis(Long.valueOf(maxWait))
                :GenericObjectPoolConfig.DEFAULT_MAX_WAIT);
        poolConfig.setTimeBetweenEvictionRuns(AssertUtil.isNotEmpty(timeBetweenEvictionRuns)
                ?Duration.ofMillis(Long.valueOf(timeBetweenEvictionRuns))
                :GenericObjectPoolConfig.DEFAULT_TIME_BETWEEN_EVICTION_RUNS);
        poolConfig.setNumTestsPerEvictionRun(AssertUtil.isNotEmpty(numTestsPerEvictionRun)
                ?Integer.valueOf(numTestsPerEvictionRun)
                :GenericObjectPoolConfig.DEFAULT_NUM_TESTS_PER_EVICTION_RUN);
        poolConfig.setTestOnBorrow(testOnBorrow != null ? testOnBorrow
                :GenericObjectPoolConfig.DEFAULT_TEST_ON_BORROW);
        poolConfig.setTestOnReturn(testOnReturn != null?testOnReturn
                :GenericObjectPoolConfig.DEFAULT_TEST_ON_RETURN);
        poolConfig.setTestOnCreate(testOnCreate != null?testOnCreate
                :GenericObjectPoolConfig.DEFAULT_TEST_ON_CREATE);
        poolConfig.setMinEvictableIdleTime(Duration.ofSeconds(30));
        poolConfig.setSoftMinEvictableIdleTime(BaseObjectPoolConfig.DEFAULT_SOFT_MIN_EVICTABLE_IDLE_DURATION);
        poolConfig.setEvictionPolicyClassName(BaseObjectPoolConfig.DEFAULT_EVICTION_POLICY_CLASS_NAME);

        JedisClusterBuilder jedisClusterBuilder = JedisClusterBuilder.create();
        for (String hp : hps) {
            jedisClusterBuilder.addNode(HostAndPort.from(hp));
        }
        jedisClusterBuilder.password(passoword);
        JedisCluster build = jedisClusterBuilder.build(poolConfig);
        if(beanFactory != null)
            beanFactory.registerSingleton(prefx,build);
        return build;
    }

    /**
     * 配置方法
     * <pre>
     * ${prefx}_host=${host}:${port}
     * ${prefx}_password=******
     * </pre>
     * @param prefx 前缀
     * @return Jedis实例
     */
    public static Jedis initJedis(String prefx,BeanFactory beanFactory) {
        String host = LocalContext.getString(prefx + "_host");
        String password = LocalContext.getString(prefx + "_password");
        Jedis build = JedisBuilder.create().host(HostAndPort.from(host))
                .password(password)
                .build();
        if(build != null)
            beanFactory.registerSingleton(prefx,build);
        return build;
    }

    /**
     * 配置方法:
     * <pre>
     * ${prefx}_smtpHost=smtp.163.com
     * ${prefx}_from=******@163.com
     * ${prefx}_username=******@163.com
     * ${prefx}_password=******
     * </pre>
     * @param prefx 前缀
     * @return SimpleEmailSender实例
     */
    public static SimpleEmailSender initSimpleEmailSender(String prefx,BeanFactory beanFactory) {
        String smtpHost = LocalContext.getString(prefx+"_smtpHost");
        String from = LocalContext.getString(prefx+"_from");
        String username = LocalContext.getString(prefx + "_username");
        String password = LocalContext.getString(prefx + "_password");
        SimpleEmailSender simpleEmailSender = new SimpleEmailSender(smtpHost, from);
        simpleEmailSender.authenticator(username,password);
        if(beanFactory != null)
            beanFactory.registerSingleton(prefx,beanFactory);
        return simpleEmailSender;
    }

    /**
     * 默认提供一个输出流路径配置的变量名
     */
    public static final String APP_DEFAULT_PRINTWRITER_PATH = "app_default_printwriter_path";

    public static PrintWriter createPrintWriter(String key) {
        PrintWriter printStream = StreamUtil.printWriter(LocalContext.getString(key));
        return printStream;
    }

}
