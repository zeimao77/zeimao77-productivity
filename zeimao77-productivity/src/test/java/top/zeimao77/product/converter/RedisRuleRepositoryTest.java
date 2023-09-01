package top.zeimao77.product.converter;

import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;
import top.zeimao77.product.factory.BeanFactory;
import top.zeimao77.product.factory.ComponentFactory;
import top.zeimao77.product.main.BaseMain;

import static org.junit.jupiter.api.Assertions.*;

class RedisRuleRepositoryTest extends BaseMain {

    public static final String REDIS = "redis_zeimao77_top";

    @Test
    void put() {
        Jedis jedis = ComponentFactory.initJedis(REDIS, BeanFactory.DEFAULT);
        RedisRuleRepository<String> stringRedisRuleRepository = new RedisRuleRepository<String>(jedis,"rule");
        stringRedisRuleRepository.put("r1","abcdefg");
        Object r1 = stringRedisRuleRepository.get("r1");
        stringRedisRuleRepository.clear();

    }
}