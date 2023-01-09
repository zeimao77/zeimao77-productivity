package top.zeimao77.product.converter;

import redis.clients.jedis.commands.JedisCommands;

public class RedisRuleRepository<K> implements RuleRepository<K>{

    private JedisCommands commands;
    private String ruleKey;

    public RedisRuleRepository(JedisCommands commands, String ruleKey) {
        this.commands = commands;
        this.ruleKey = ruleKey;
    }

    @Override
    public Object put(K key, Object value) {
        return commands.hset(ruleKey,key.toString(),value.toString());
    }

    @Override
    public Object get(K key) {
        String hget = commands.hget(ruleKey, key.toString());
        return hget;
    }

    @Override
    public boolean containsKey(K key) {
        return commands.hexists(ruleKey,key.toString());
    }

    @Override
    public boolean isEmpty() {
        long hlen = commands.hlen(ruleKey);
        return hlen == 0L;
    }

    @Override
    public void clear() {
        commands.del(ruleKey);
    }
}
