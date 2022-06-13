package top.zeimao77.product.redis;

import redis.clients.jedis.commands.ScriptingKeyCommands;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class JedisLockImpl {

    private ScriptingKeyCommands commands;

    public JedisLockImpl(ScriptingKeyCommands commands) {
        this.commands = commands;
    }

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
        int wait = 0;
        while (wait < waitTimeOut) {
            if(tryLock(lockId,value,expire)) {
                return true;
            }
            try {
                TimeUnit.MICROSECONDS.sleep(20);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            wait += 20;
        }
        return false;
    }

    public boolean unLock(String lockId) {
        String unscript = "redis.call('DEL',KEYS[1]);";
        Object eval = this.commands.eval(unscript, List.of(lockId), new ArrayList<>(0));
        return true;
    }


}
