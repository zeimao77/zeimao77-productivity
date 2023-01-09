package top.zeimao77.product.converter;

import java.util.HashMap;

public class MemoryRuleRepository<K> implements RuleRepository<K> {

    private HashMap<K,Object> rule;

    public MemoryRuleRepository() {
        this.rule = new HashMap<>();
    }

    public MemoryRuleRepository(HashMap<K, Object> rule) {
        this.rule = rule;
    }

    @Override
    public Object put(K key, Object value) {
        return this.rule.put(key,value);
    }

    @Override
    public Object get(K key) {
        return this.rule.get(key);
    }

    @Override
    public boolean containsKey(K key) {
        return this.rule.containsKey(key);
    }

    @Override
    public boolean isEmpty() {
        return this.rule.isEmpty();
    }

    @Override
    public void clear() {
        this.rule.clear();
    }


    public HashMap<K, Object> getRule() {
        return rule;
    }

    public void setRule(HashMap<K, Object> rule) {
        this.rule = rule;
    }

}
