package top.zeimao77.product.factory;

import com.mysql.cj.jdbc.MysqlDataSource;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import top.zeimao77.product.config.LocalContext;
import top.zeimao77.product.email.SimpleEmailSender;
import top.zeimao77.product.mysql.SimpleMysql;
import top.zeimao77.product.redis.JedisBuilder;
import top.zeimao77.product.redis.JedisClusterBuilder;
import top.zeimao77.product.util.StreamUtil;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ComponentFactory {

    /**
     * 配置方法
     * ${prefx}_url=...
     * ${prefx}_username=...
     * ${prefx}_password=...
     * @param prefx 前缀
     * @return SimpleMysql实例
     */
    public static SimpleMysql createSimpleMysql(String prefx) {
        String url = LocalContext.getString(prefx+"_url");
        String username = LocalContext.getString(prefx+"_username");
        String password = LocalContext.getString(prefx+"_password");
        SimpleMysql build = SimpleMysql.Builder.create8()
                .url(url)
                .username(username)
                .password(password)
                .build();
        return build;
    }

    public static DataSource createDataSource(String prefx) {
        String url = LocalContext.getString(prefx+"_url");
        String username = LocalContext.getString(prefx+"_username");
        String password = LocalContext.getString(prefx+"_password");
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUrl(url);
        dataSource.setUser(username);
        dataSource.setPassword(password);
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

    public static PrintWriter createPrintWriter(String key) {
        PrintWriter printStream = StreamUtil.printWriter(LocalContext.getString(key));
        return printStream;
    }

    public static PrintWriter createPrintWriter() {
        return createPrintWriter("app_default_printwriter_path");
    }

}
