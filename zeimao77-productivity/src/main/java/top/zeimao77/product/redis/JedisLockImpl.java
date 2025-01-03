package top.zeimao77.product.redis;

import redis.clients.jedis.commands.ScriptingKeyCommands;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class JedisLockImpl {

    private ScriptingKeyCommands commands;

    public JedisLockImpl(ScriptingKeyCommands commands) {
        this.commands = commands;
    }

    /**
     * 尝试加锁 不阻塞
     * @param lockId 锁KEY
     * @param value 锁钥
     * @param expire 过期时间 单位:秒
     * @return 是否加锁成功
     */
    public boolean tryLock(String lockId,String value,int expire) {
        String script = """
                if redis.call('EXISTS',KEYS[1]) == 0 then
                    redis.call('SET',KEYS[1],ARGV[1]);
                    redis.call('EXPIRE',KEYS[1],ARGV[2]);
                    return 1;
                else 
                    return 0;
                end
                """;
        Object obj = this.commands.eval(script, List.of(lockId),List.of(value,String.valueOf(expire)));
        return "1".equals(obj.toString());
    }

    public boolean lock(String lockId,String value,int expire,int waitTimeOut) {
        return lock(lockId,value,expire,20,waitTimeOut);
    }


    /**
     * 加锁
     * @param lockId 锁KEY
     * @param value 锁钥
     * @param expire 过期时间 单位:秒
     * @param sleep 尝试周期 单位：毫秒
     * @param waitTimeOut 等待时间 单位：毫秒
     * @return 是否加锁成功
     */
    public boolean lock(String lockId,String value,int expire,int sleep,int waitTimeOut) {
        int wait = 0;
        while (wait < waitTimeOut) {
            if(tryLock(lockId,value,expire)) {
                return true;
            }
            try {
                TimeUnit.MICROSECONDS.sleep(sleep);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
            wait += sleep;
        }
        return false;
    }


    /**
     * 为锁延时
     * @param lockId 锁KEY
     * @param value 锁值
     * @param expire 延时时间 单位:秒
     * @return
     */
    public boolean reLook(String lockId,String value,int expire) {
        String script = """
                if redis.call('GET',KEYS[1]) == ARGV[1] then
                  redis.call('EXPIRE',KEYS[1],ARGV[2]);
                  return 1;
                else 
                  return 0;
                end
                """;
        Object eval = this.commands.eval(script, List.of(lockId), List.of(value,String.valueOf(expire)));
        return "1".equals(eval.toString());
    }

    /**
     * 解锁
     * @param lockId 锁KEY
     * @return true
     */
    public boolean unLock(String lockId) {
        String unscript = "redis.call('DEL',KEYS[1]);";
        Object eval = this.commands.eval(unscript, List.of(lockId), Collections.emptyList());
        return true;
    }

    /**
     * 解锁
     * @param lockId 锁KEY
     * @param value 值
     * @return 如果锁不存在或者解锁成功返回true
     */
    public boolean unLock(String lockId,String value) {
        String script = """
                if redis.call('GET',KEYS[1]) == ARGV[1] then
                  redis.call('DEL',KEYS[1]);
                  return 1;
                elseif redis.call('EXISTS',KEYS[1]) == 0 then
                  return 1;
                else
                  return 0;
                end
                """;
        Object obj = this.commands.eval(script, List.of(lockId),List.of(value));
        return "1".equals(obj.toString());
    }


}
