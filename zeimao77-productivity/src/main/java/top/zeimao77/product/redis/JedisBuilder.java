package top.zeimao77.product.redis;

import redis.clients.jedis.*;
import top.zeimao77.product.util.AssertUtil;

public class JedisBuilder{

    private String user,password;
    private HostAndPort hostAndPort;
    private int db = 0;

    private JedisBuilder(){}

    public static JedisBuilder create() {
        return new JedisBuilder();
    }

    public JedisBuilder host(String host,int port) {
        this.hostAndPort = new HostAndPort(host,port);
        return this;
    }

    public JedisBuilder host(HostAndPort hostAndPort) {
        this.hostAndPort = hostAndPort;
        return this;
    }

    public JedisBuilder password(String password) {
        this.password = password;
        return this;
    }

    public JedisBuilder user(String user) {
        this.user = user;
        return this;
    }

    public JedisBuilder db(int db) {
        this.db = db;
        return this;
    }

    public Jedis build() {
        Jedis jedis = new Jedis(hostAndPort);
        if(AssertUtil.isEmpty(user) && AssertUtil.isNotEmpty(password)) {
            jedis.auth(password);
        }
        if(AssertUtil.isNotEmpty(user) && AssertUtil.isNotEmpty(password)) {
            jedis.auth(user,password);
        }
        jedis.select(db);
        return jedis;
    }
}
