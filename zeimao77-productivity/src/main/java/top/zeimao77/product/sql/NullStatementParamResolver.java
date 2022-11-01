package top.zeimao77.product.sql;

import java.util.Collections;

public class NullStatementParamResolver extends AbstractStatementParamResolver {

    public NullStatementParamResolver(String sqlt, Void params){
        this.sqlt = sqlt;
    }

    @Override
    public void resolve() {
        statementParams = Collections.emptyList();
        this.sql = this.sqlt;
    }

}
