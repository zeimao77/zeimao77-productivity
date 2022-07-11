package top.zeimao77.product.sql;

import java.util.Map;

/**
 * 可以解析MAP参数
 */
public class MapStatementParamResolver extends AbstractStatementParamResolver {

    private Map<?,?> params;

    public MapStatementParamResolver(String sqlt, Map<?,?> params){
        this.sqlt = sqlt;
        this.params = params;
    }

    @Override
    public void resolve() {
        resolve(o -> params.get(o));
    }
}
