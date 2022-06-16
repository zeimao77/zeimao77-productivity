package top.zeimao77.product.mysql;

import java.util.ArrayList;

public class NullStatementParamResolver extends AbstractStatementParamResolver{

    public NullStatementParamResolver(String sqlt, Void params){
        this.sqlt = sqlt;
    }


    @Override
    public void resolve() {
        statementParams = new ArrayList<>(0);
        this.sql = this.sqlt;
    }
}
