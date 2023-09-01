package top.zeimao77.product.redis;

import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;
import top.zeimao77.product.factory.ComponentFactory;
import top.zeimao77.product.main.BaseMain;

import java.util.ArrayList;
import java.util.List;

class SimpleJedisTest extends BaseMain {

    @Test
    void getJedis() {
        Jedis jedis = ComponentFactory.initJedis("redis_top_zeimao77",null);
        String script = """
                if redis.call('EXISTS',KEYS[1]) == 0 then
                    redis.call('SET',KEYS[1],ARGV[1]);
                    redis.call('EXPIRE',KEYS[1],ARGV[2]);
                    return 1;
                else 
                    return 0;
                end
                """;
        Object obj = jedis.eval(script, List.of("ABCDEFGHIJKLMNOPQRSTUVWXYZ666"),List.of("1","10"));
        logger.info("加锁结果：{}",obj);
        String unscript = "redis.call('DEL',KEYS[1]);";
        jedis.eval(unscript,List.of("ABCDEFGHIJKLMNOPQRSTUVWXYZ666"),new ArrayList<>(0));
        obj = jedis.eval(script, List.of("ABCDEFGHIJKLMNOPQRSTUVWXYZ666"),List.of("1","10"));
        logger.info("加锁结果：{}",obj);
    }



}