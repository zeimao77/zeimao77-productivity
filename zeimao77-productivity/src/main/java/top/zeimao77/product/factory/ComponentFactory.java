package top.zeimao77.product.factory;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.pool2.impl.BaseObjectPoolConfig;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Connection;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import top.zeimao77.product.config.LocalContext;
import top.zeimao77.product.dingding.DingDingRobotClient;
import top.zeimao77.product.email.SimpleEmailSender;
import top.zeimao77.product.exception.BaseServiceRunException;
import top.zeimao77.product.redis.JedisBuilder;
import top.zeimao77.product.redis.JedisClusterBuilder;
import top.zeimao77.product.sql.*;
import top.zeimao77.product.util.AssertUtil;
import top.zeimao77.product.util.StreamUtil;
import top.zeimao77.product.util.StringOptional;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Optional;

import static top.zeimao77.product.exception.ExceptionCodeDefinition.WRONG_ACTION;

/**
 * 辅助初始化部分组件
 * @author zeimao77
 * @since 2.1.1
 */
public class ComponentFactory {

    public static final String AUTOBEAN_DATASOURCE_PREFIX = "$_datasource_";
    public static final String AUTOBEAN_SQLTEMPLATE = "$_sqltemplate_";
    public static final String AUTOBEAN_SQLCLIENT = "$_sqlclient_";

    /**
     * 配置参考:
     * @see ComponentFactory#createDataSource(String, BeanFactory)
     * @param prefx 前缀
     * @return SQL客户端工厂对象
     */
    public static SimpleSqlTemplate initSimpleSqlTemplate(String prefx,BeanFactory beanFactory) {
        if(beanFactory != null && beanFactory.hasBean(prefx))
            throw new BaseServiceRunException(WRONG_ACTION,prefx+" Bean已经被定义！");
        DataSource dataSource = createDataSource(prefx,beanFactory);
        SimpleSqlTemplate simpleSqlFacroty = new SimpleSqlTemplate(dataSource);
        if(beanFactory != null)
            beanFactory.registerSingleton(AUTOBEAN_SQLTEMPLATE + prefx,simpleSqlFacroty);
        return simpleSqlFacroty;
    }

    /**
     * 配置参考:
     * @see ComponentFactory#createDataSource(String, BeanFactory)
     * @param prefx 前缀
     * @return SQL客户端
     */
    public static SimpleSqlClient initSimpleSqlClient(String prefx,BeanFactory beanFactory) {
        if(beanFactory != null && beanFactory.hasBean(prefx))
            throw new BaseServiceRunException(WRONG_ACTION,prefx+" Bean已经被定义！");
        DataSource dataSource = createDataSource(prefx,beanFactory);
        DataSourceTransactionFactory dataSourceTransactionFactory = new DataSourceTransactionFactory(dataSource);
        SimpleSqlClient simpleSqlClient = new SimpleSqlClient(dataSourceTransactionFactory
                , DefaultPreparedStatementSetter.INSTANCE, DefaultResultSetResolve.INSTANCE);
        if(beanFactory != null)
            beanFactory.registerSingleton(AUTOBEAN_SQLCLIENT + prefx,simpleSqlClient);
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
     * @param beanFactory Bean工厂
     * @return SimpleMysql实例
     */
    public static DataSource createDataSource(String prefx,BeanFactory beanFactory) {
        String datasourceBeanName = null;
        if(beanFactory != null) {
            datasourceBeanName = AUTOBEAN_DATASOURCE_PREFIX+prefx;
            if(beanFactory.hasBean(datasourceBeanName))
                return beanFactory.getBean(datasourceBeanName,DataSource.class);
        }
        StringOptional url = LocalContext.getString(prefx + "_url");
        url.ifBlackThrow(prefx+"_url");
        StringOptional username = LocalContext.getString(prefx + "_username");
        username.ifBlackThrow(prefx+"_username");
        StringOptional password = LocalContext.getString(prefx + "_password");
        password.ifBlackThrow(prefx+"_password");

        StringOptional driverClassName = LocalContext.getString(prefx + "_driverClassName");
        StringOptional maxLifetime = LocalContext.getString(prefx + "_maxLifetime");
        StringOptional connectionTestQuery = LocalContext.getString(prefx + "_connectionTestQuery");
        StringOptional connectionTimeout = LocalContext.getString(prefx + "_connectionTimeout");
        StringOptional poolName = LocalContext.getString(prefx + "_poolName");
        StringOptional maximumPoolSize = LocalContext.getString(prefx + "_maximumPoolSize");
        StringOptional minimumIdea = LocalContext.getString(prefx + "_minimumIdle");
        StringOptional idleTimeout = LocalContext.getString(prefx + "_idleTimeout");
        StringOptional keepaliveTime = LocalContext.getString(prefx + "_keepaliveTime");
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(url.get());
        dataSource.setUsername(username.get());
        dataSource.setPassword(password.get());
        driverClassName.ifNotBlack(dataSource::setDriverClassName);
        poolName.ifNotBlack(dataSource::setPoolName);
        // 客户端等待来自池的连接的最大毫秒数，超时将SQLException 最少 250; 默认 30000（30秒）
        connectionTimeout.ifNotBlack(o -> dataSource.setConnectionTimeout(Long.valueOf(o)));
        // 连接的最大生命周期 最小 30000; 默认值：1800000（30 分钟）
        maxLifetime.ifNotBlack(o -> dataSource.setMaxLifetime(Long.valueOf(o)));
        // 允许达到的连接的最大数量 默认值：10
        maximumPoolSize.ifNotBlack(o -> dataSource.setMaximumPoolSize(Integer.valueOf(o)));
        // 维护的最小空闲连接数 默认值 与 maximumPoolSize 相同
        minimumIdea.ifNotBlack(o -> dataSource.setMinimumIdle(Integer.valueOf(o)));
        // 允许连接在池中空闲的最长时间 超时淘汰 0:永不淘汰
        // 允许的最小值为 10000; 默认值：600000（10 分钟）
        idleTimeout.ifNotBlack(o -> dataSource.setIdleTimeout(Long.valueOf(o)));
        // 尝试保持连接活动的频率 最小值为 30000（30 秒）， 默认值：0（禁用）
        keepaliveTime.ifNotBlack(o -> dataSource.setKeepaliveTime(Long.valueOf(o)));
        // 存在Connection.isValid() API时不建议设置此值
        connectionTestQuery.ifNotBlack(dataSource::setConnectionTestQuery);
        if(beanFactory != null)
            beanFactory.registerSingleton(datasourceBeanName,dataSource);
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
        ArrayList<String> hps = LocalContext.get(prefx+"_host[%d]",32);
        AssertUtil.assertTrue(!hps.isEmpty(),prefx + "_host[]参数必需;");
        StringOptional passoword = LocalContext.getString(prefx+"_passoword");
        Optional<Integer> maxIdle = LocalContext.getInteger(prefx + "_maxIdle");
        Optional<Integer> minIdle = LocalContext.getInteger(prefx + "_minIdle");
        Optional<Integer> maxTotal = LocalContext.getInteger(prefx + "_maxTotal");
        Optional<Duration> maxWait = LocalContext.getDuration(prefx + "_maxWait", ChronoUnit.MILLIS);
        Optional<Boolean> testOnBorrow = LocalContext.getBoolean(prefx + "_testOnBorrow");
        Optional<Boolean> testOnReturn = LocalContext.getBoolean(prefx + "_testOnReturn");
        Optional<Boolean> testOnCreate = LocalContext.getBoolean(prefx + "_testOnCreate");
        Optional<Duration> timeBetweenEvictionRuns = LocalContext.getDuration(prefx + "_timeBetweenEvictionRuns",ChronoUnit.MILLIS);
        Optional<Integer> numTestsPerEvictionRun = LocalContext.getInteger(prefx+"_numTestsPerEvictionRun");
        GenericObjectPoolConfig<Connection> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxIdle(maxIdle.orElseGet(() -> GenericObjectPoolConfig.DEFAULT_MAX_IDLE));
        poolConfig.setMinIdle(minIdle.orElseGet(() -> GenericObjectPoolConfig.DEFAULT_MIN_IDLE));
        poolConfig.setMaxTotal(maxTotal.orElseGet(() -> GenericObjectPoolConfig.DEFAULT_MAX_TOTAL));
        poolConfig.setMaxWait(maxWait.orElseGet(() -> GenericObjectPoolConfig.DEFAULT_MAX_WAIT));
        poolConfig.setTimeBetweenEvictionRuns(timeBetweenEvictionRuns.orElseGet(() -> GenericObjectPoolConfig.DEFAULT_TIME_BETWEEN_EVICTION_RUNS));
        poolConfig.setNumTestsPerEvictionRun(numTestsPerEvictionRun.orElseGet(() -> GenericObjectPoolConfig.DEFAULT_NUM_TESTS_PER_EVICTION_RUN));
        poolConfig.setTestOnBorrow(testOnBorrow.orElseGet(() -> GenericObjectPoolConfig.DEFAULT_TEST_ON_BORROW));
        poolConfig.setTestOnReturn(testOnReturn.orElseGet(() -> GenericObjectPoolConfig.DEFAULT_TEST_ON_RETURN));
        poolConfig.setTestOnCreate(testOnCreate.orElseGet(() -> GenericObjectPoolConfig.DEFAULT_TEST_ON_CREATE));
        poolConfig.setMinEvictableIdleTime(Duration.ofSeconds(30));
        poolConfig.setSoftMinEvictableIdleTime(BaseObjectPoolConfig.DEFAULT_SOFT_MIN_EVICTABLE_IDLE_DURATION);
        poolConfig.setEvictionPolicyClassName(BaseObjectPoolConfig.DEFAULT_EVICTION_POLICY_CLASS_NAME);

        JedisClusterBuilder jedisClusterBuilder = JedisClusterBuilder.create();
        for (String hp : hps) {
            jedisClusterBuilder.addNode(HostAndPort.from(hp));
        }
        jedisClusterBuilder.password(passoword.get());
        JedisCluster cluster = jedisClusterBuilder.build(poolConfig);
        if(beanFactory != null)
            beanFactory.registerSingleton(prefx,cluster);
        return cluster;
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
        StringOptional host = LocalContext.getString(prefx + "_host");
        host.ifBlackThrow(prefx + "_host");
        StringOptional password = LocalContext.getString(prefx + "_password");
        Jedis jedis = JedisBuilder.create().host(HostAndPort.from(host.get()))
                .password(password.get())
                .build();
        if(jedis != null)
            beanFactory.registerSingleton(prefx,jedis);
        return jedis;
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
        StringOptional smtpHost = LocalContext.getString(prefx+"_smtpHost");
        smtpHost.ifBlackThrow(prefx+"_smtpHost");
        StringOptional from = LocalContext.getString(prefx+"_from");
        smtpHost.ifBlackThrow(prefx+"_from");
        StringOptional username = LocalContext.getString(prefx + "_username");
        smtpHost.ifBlackThrow(prefx+"_username");
        StringOptional password = LocalContext.getString(prefx + "_password");
        smtpHost.ifBlackThrow(prefx+"_password");
        SimpleEmailSender simpleEmailSender = new SimpleEmailSender(smtpHost.get(), from.get());
        simpleEmailSender.authenticator(username.get(),password.get());
        if(beanFactory != null)
            beanFactory.registerSingleton(prefx,beanFactory);
        return simpleEmailSender;
    }

    /**
     * 配置方法:
     * <pre>
     * ${prefx}_webhook=https://oapi.dingtalk.com/robot/send
     * ${prefx}_token=******
     * ${prefx}_secret=******
     * </pre>
     * @param prefx 前缀
     * @return DingDingRobotClient实例
     */
    public static DingDingRobotClient initDingDingRobotClient(String prefx,BeanFactory beanFactory) {
        StringOptional webhook = LocalContext.getString(prefx+"_webhook");
        webhook.ifBlackThrow("webhook");
        StringOptional token = LocalContext.getString(prefx+"_token");
        StringOptional secret = LocalContext.getString(prefx+"_secret");
        secret.ifBlackThrow("secret");
        DingDingRobotClient client = null;
        if(token.isBlack())
            client = new DingDingRobotClient(webhook.get(),secret.get());
        else
            client = new DingDingRobotClient(webhook.get(),token.get(),secret.get());
        if(beanFactory != null)
            beanFactory.registerSingleton(prefx,beanFactory);
        return client;

    }

    /**
     * 默认提供一个输出流路径配置的变量名
     */
    public static final String APP_DEFAULT_PRINTWRITER_PATH = "app_default_printwriter_path";

    public static PrintWriter createPrintWriter(String key) {
        StringOptional writerPath = LocalContext.getString(key);
        writerPath.ifBlackThrow(key);
        PrintWriter printStream = StreamUtil.printWriter(writerPath.get());
        return printStream;
    }

}
