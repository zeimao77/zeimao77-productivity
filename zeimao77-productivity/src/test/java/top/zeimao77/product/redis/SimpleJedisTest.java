package top.zeimao77.product.redis;

import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;
import top.zeimao77.product.config.ComponentFactory;
import top.zeimao77.product.main.BaseMain;

class SimpleJedisTest extends BaseMain {

    @Test
    void getJedis() {
        BaseMain.showBanner();
        Jedis jedis = ComponentFactory.createJedis("redis_top_zeimao77");
        String pass = jedis.get("pass");
        logger.info(pass);
    }



}