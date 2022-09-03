package top.zeimao77.product.sql;

import java.util.List;
import java.util.Map;

/**
 * 默认的参数解析
 * 支持数组参数、MAP参数、对象参数
 */
public class DefaultStatementParamResolver implements StatementParamResolver {

    public StatementParamResolver resolver;

    public DefaultStatementParamResolver(String sqlt, Object params) {
        if(params == null) {
            this.resolver = new NullStatementParamResolver(sqlt,null);
        } else if(params instanceof Map o) {
            this.resolver = new MapStatementParamResolver(sqlt,o);
        } else if(params.getClass().isArray()) {
            this.resolver = new ObjectsStatementParamResolver(sqlt,(Object[])params);
        } else {
            this.resolver = new BeanStatementParamResolver(sqlt,params);
        }
    }

    @Override
    public void resolve() {
        this.resolver.resolve();
    }

    @Override
    public List<StatementParameter> getStatementParams() {
        return this.resolver.getStatementParams();
    }

    @Override
    public String getSql() {
        return this.resolver.getSql();
    }

}
