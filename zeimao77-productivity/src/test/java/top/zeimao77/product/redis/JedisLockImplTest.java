package top.zeimao77.product.redis;

import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;
import top.zeimao77.product.factory.ComponentFactory;
import top.zeimao77.product.main.BaseMain;
import top.zeimao77.product.util.UuidGenerator;

class JedisLockImplTest extends BaseMain{

    @Test
    void lock() {
        BaseMain.showBanner();
        Jedis jedis = ComponentFactory.initJedis("redis_top_zeimao77",null);
        JedisLockImpl jedisLock = new JedisLockImpl(jedis);
        String generate = UuidGenerator.INSTANCE.generate();
        boolean lock = jedisLock.lock(generate,"1", 2, 100);
        logger.info("lock={}",lock);
        lock = jedisLock.reLook(generate, "1", 2);
        delay_ms(1000);
        logger.info("relock={}",lock);
        lock = jedisLock.unLock(generate, "1");
        logger.info("unlock={}",lock);
        lock = jedisLock.reLook(generate, "1", 2);
        logger.info("relock={}",lock);
    }
}