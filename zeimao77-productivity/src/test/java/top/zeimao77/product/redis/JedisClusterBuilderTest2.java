package top.zeimao77.product.redis;

import redis.clients.jedis.JedisCluster;
import top.zeimao77.product.main.BaseMain;

class JedisClusterBuilderTest2 extends BaseMain {

    public static void main(String[] args) {
<<<<<<< HEAD
        BaseMain.showBanner("");
=======
        BaseMain.showBanner("0.0.1");
>>>>>>> main
        JedisCluster cluster = JedisClusterBuilder.create()
                .addNode("192.168.1.105", 6379)
                .addNode("192.168.1.105", 6380)
                .addNode("192.168.1.105", 6381)
                .addNode("192.168.1.105", 6382)
                .addNode("192.168.1.105", 6383)
                .addNode("192.168.1.105", 6384)
                .password("123456")
                .build();
        String channel = "channel";
        cluster.publish(channel,"44444");
        cluster.close();
    }


}