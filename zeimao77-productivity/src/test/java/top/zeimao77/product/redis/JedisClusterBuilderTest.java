package top.zeimao77.product.redis;

import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPubSub;
import top.zeimao77.product.main.BaseMain;

class JedisClusterBuilderTest extends BaseMain {


    public static void main(String[] args) throws InterruptedException {
        BaseMain.showBanner();
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
        JedisPubSub jedisPubSub = new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {    //收到消息
                logger.info("receive redis published message, channel {}, message {}", channel, message);
            }

            @Override
            public void onSubscribe(String channel, int subscribedChannels) {    //订阅频道
                logger.info("subscribe redis channel success, channel {}, subscribedChannels {}",
                        channel, subscribedChannels);
            }

            @Override
            public void onUnsubscribe(String channel, int subscribedChannels) {   //取消订阅
                logger.info("unsubscribe redis channel, channel {}, subscribedChannels {}",
                        channel, subscribedChannels);
            }
        };
        Thread t = new Thread(()->{
            cluster.subscribe(jedisPubSub,channel);
        });
        t.start();
        for (int i = 0; i < 10; i++) {
            cluster.publish(channel,"c"+i);
            delay_ms(500);
        }
        jedisPubSub.unsubscribe();
        cluster.close();
    }


}