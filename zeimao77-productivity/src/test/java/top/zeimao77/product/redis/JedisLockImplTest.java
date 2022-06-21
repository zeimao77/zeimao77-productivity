package top.zeimao77.product.redis;

import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;
import top.zeimao77.product.config.ComponentFactory;
import top.zeimao77.product.main.BaseMain;
import top.zeimao77.product.util.UuidGenerator;

class JedisLockImplTest extends BaseMain{

    @Test
    void lock() {
        BaseMain.showBanner();
        Jedis jedis = ComponentFactory.createJedis("redis_top_zeimao77");
        JedisLockImpl jedisLock = new JedisLockImpl(jedis);
        String generate = UuidGenerator.INSTANCE.generate();
        boolean lock = jedisLock.lock(generate,"1", 2, 100);
        logger.info("LOCK(0):{}",lock);
        for (int i = 1; i < 12; i++) {
            delay_ms(1000);
            lock = jedisLock.lock(generate,"1", 2, 100);
            logger.info("LOCK({}):{}",i,lock);
            boolean b = jedisLock.unLock(generate, "2");
            logger.info("UNLOCK:{}",b);
        }

    }
}