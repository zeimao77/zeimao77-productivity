package top.zeimao77.product.factory;

import com.zaxxer.hikari.HikariDataSource;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import top.zeimao77.product.config.LocalContext;
import top.zeimao77.product.email.SimpleEmailSender;
import top.zeimao77.product.redis.JedisBuilder;
import top.zeimao77.product.redis.JedisClusterBuilder;
import top.zeimao77.product.sql.SimpleSqlFacroty;
import top.zeimao77.product.util.AssertUtil;
import top.zeimao77.product.util.StreamUtil;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ComponentFactory {

    public static SimpleSqlFacroty initSimpleSqlFacroty(String prefx) {
        DataSource dataSource = createDataSource(prefx);
        SimpleSqlFacroty simpleSqlFacroty = new SimpleSqlFacroty(dataSource);
        BeanFactory.DEFAULT.registerSingleton(prefx,simpleSqlFacroty);
        return simpleSqlFacroty;
    }


    /**
     * 配置方法
     * ${prefx}_url=...
     * ${prefx}_username=...
     * ${prefx}_password=...
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
     * 配置方法
     * ${prefx}_host[0]=192.168.0.100:6379
     * ${prefx}_host[1]=192.168.0.101:6379
     * ${prefx}_password=...
     * @param prefx 前缀
     * @return JedisCluster实例
     */
    public static JedisCluster createJedisCluster(String prefx) {
        ArrayList<String> hps = LocalContext.get(prefx+"_host[%d]",6);
        String passoword = LocalContext.getString(prefx+"_passoword");
        JedisClusterBuilder jedisClusterBuilder = JedisClusterBuilder.create();
        for (String hp : hps) {
            jedisClusterBuilder.addNode(HostAndPort.from(hp));
        }
        jedisClusterBuilder.password(passoword);
        JedisCluster build = jedisClusterBuilder.build();
        return build;
    }

    /**
     * 配置方法
     * ${prefx}_host=192.168.0.101:6379
     * ${prefx}_password=...
     * @param prefx 前缀
     * @return Jedis实例
     */
    public static Jedis createJedis(String prefx) {
        String host = LocalContext.getString(prefx + "_host");
        String password = LocalContext.getString(prefx + "_password");
        Jedis build = JedisBuilder.create().host(HostAndPort.from(host))
                .password(password)
                .build();
        return build;
    }

    /**
     * 配置方法
     * ${prefx}_smtpHost=smtp.163.com
     * ${prefx}_from=***
     * ${prefx}_username=***
     * ${prefx}_password=***
     * @param prefx 前缀
     * @return SimpleEmailSender实例
     */
    public static SimpleEmailSender createSimpleEmailSender(String prefx) {
        String smtpHost = LocalContext.getString(prefx+"_smtpHost");
        String from = LocalContext.getString(prefx+"_from");
        String username = LocalContext.getString(prefx + "_username");
        String password = LocalContext.getString(prefx + "_password");
        SimpleEmailSender simpleEmailSender = new SimpleEmailSender(smtpHost, from);
        simpleEmailSender.authenticator(username,password);
        return simpleEmailSender;
    }

    public static final String APP_DEFAULT_PRINTWRITER_PATH = "app_default_printwriter_path";

    public static PrintWriter createPrintWriter(String key) {
        PrintWriter printStream = StreamUtil.printWriter(LocalContext.getString(key));
        return printStream;
    }

}
