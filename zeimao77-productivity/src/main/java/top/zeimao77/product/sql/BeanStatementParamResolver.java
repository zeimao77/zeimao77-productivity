package top.zeimao77.product.sql;

import top.zeimao77.product.util.BeanUtil;

/**
 * 对象到JDBC参数的解析器
 */
public class BeanStatementParamResolver extends AbstractStatementParamResolver {

    private Object params;

    public BeanStatementParamResolver(String sqlt, Object params){
        this.sqlt = sqlt;
        this.params = params;
    }

    @Override
    public void resolve() {
        resolve(o -> BeanUtil.getProperty(params,o));
    }

}
