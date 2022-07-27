package top.zeimao77.product.redis;

import org.apache.commons.pool2.impl.BaseObjectPoolConfig;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Connection;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.time.Duration;
import java.util.HashSet;

public class JedisClusterBuilder {

    private HashSet<HostAndPort> nodes = new HashSet<>();
    private String user,passowrd;

    private JedisClusterBuilder(){}

    public static JedisClusterBuilder create() {
        return new JedisClusterBuilder();
    }

    public JedisClusterBuilder addNode(String host,int port) {
        nodes.add(new HostAndPort(host,port));
        return this;
    }

    public JedisClusterBuilder addNode(HostAndPort hostAndPort) {
        nodes.add(hostAndPort);
        return this;
    }

    public JedisClusterBuilder user(String user) {
        this.user = user;
        return this;
    }

    public JedisClusterBuilder password(String password) {
        this.passowrd = password;
        return this;
    }

    public JedisCluster build(GenericObjectPoolConfig<Connection> poolConfig) {
        JedisCluster cluster = new JedisCluster(nodes
                ,JedisCluster.DEFAULT_TIMEOUT
                ,JedisCluster.DEFAULT_TIMEOUT
                ,JedisCluster.DEFAULT_MAX_ATTEMPTS
                ,user
                ,passowrd
                ,null
                ,poolConfig);
        return cluster;
    }

    public JedisCluster build() {
        GenericObjectPoolConfig<Connection> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxIdle(GenericObjectPoolConfig.DEFAULT_MAX_IDLE);   // 最大空闲的连接数
        poolConfig.setMinIdle(GenericObjectPoolConfig.DEFAULT_MIN_IDLE);    // 最少空闲的连接数
        poolConfig.setMaxTotal(GenericObjectPoolConfig.DEFAULT_MAX_TOTAL);    // 最大连接数
        poolConfig.setBlockWhenExhausted(BaseObjectPoolConfig.DEFAULT_BLOCK_WHEN_EXHAUSTED);  // 获取连接允许阻塞
        poolConfig.setMaxWait(Duration.ofSeconds(60));    // 获取连接允许阻塞的最大时长
        poolConfig.setTestOnReturn(BaseObjectPoolConfig.DEFAULT_TEST_ON_RETURN);    // 归还连接时检查链接有效性
        poolConfig.setTestOnBorrow(true);    // 获取连接时检查链接有效性
        poolConfig.setTestOnCreate(BaseObjectPoolConfig.DEFAULT_TEST_ON_CREATE);    // 创建连接时检查链接有效性
        poolConfig.setTimeBetweenEvictionRuns(Duration.ofSeconds(60));  // 空闲连接检测的周期
        poolConfig.setNumTestsPerEvictionRun(BaseObjectPoolConfig.DEFAULT_NUM_TESTS_PER_EVICTION_RUN); // 空闲连接检测每次运行时检测的空闲对象的数量
        // 连接空闲时间大于minEvictableIdleTimeMillis时清理空闲
        // minEvictableIdleDuration 与 softMinEvictableIdleDuration 满足其一即行清理
        poolConfig.setMinEvictableIdleTime(Duration.ofSeconds(30));
        // 连接空闲时间大于softMinEvictableIdleTimeMillis并且当前连接池的空闲连接数大于最小空闲连接数minIdle是清理空闲
        poolConfig.setSoftMinEvictableIdleTime(BaseObjectPoolConfig.DEFAULT_SOFT_MIN_EVICTABLE_IDLE_DURATION);
        poolConfig.setEvictionPolicyClassName(BaseObjectPoolConfig.DEFAULT_EVICTION_POLICY_CLASS_NAME);  // 驱逐策略
        JedisCluster cluster = new JedisCluster(nodes
                ,JedisCluster.DEFAULT_TIMEOUT   // 建立连接超时时间
                ,JedisCluster.DEFAULT_TIMEOUT   // 读取数据超时时间
                ,JedisCluster.DEFAULT_MAX_ATTEMPTS  // 最大重试次数
                ,user
                ,passowrd
                ,null
                ,poolConfig);
        return cluster;
    }

}
