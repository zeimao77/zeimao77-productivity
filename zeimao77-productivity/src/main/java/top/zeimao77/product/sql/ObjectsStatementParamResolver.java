package top.zeimao77.product.sql;

import java.util.ArrayList;
import java.util.List;

public class ObjectsStatementParamResolver implements StatementParamResolver {

    private String sql;
    private Object[] params;
    protected ArrayList<StatementParameter> statementParams;

    public ObjectsStatementParamResolver(String sql, Object[] params) {
        this.sql = sql;
        this.params = params;
    }

    @Override
    public void resolve() {
        this.statementParams = new ArrayList<>(params.length);
        for (int i = 0; i < params.length; i++) {
            StatementParameter jdbcParam = StatementParameter.Builder.create(i+1, String.format("param%d", i+1))
                    .javaType(params[i].getClass()).build();
            jdbcParam.setValue(params[i]);
            statementParams.add(jdbcParam);
        }
    }

    @Override
    public List<StatementParameter> getStatementParams() {
        return this.statementParams;
    }

    @Override
    public String getSql() {
        return this.sql;
    }

}
