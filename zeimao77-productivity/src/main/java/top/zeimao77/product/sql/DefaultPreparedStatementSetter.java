package top.zeimao77.product.sql;

import java.sql.*;
import java.util.List;

public class DefaultPreparedStatementSetter implements PreparedStatementSetter {

    private List<StatementParameter> statementParams;

    public DefaultPreparedStatementSetter(List<StatementParameter> statementParams) {
        this.statementParams = statementParams;
    }

    @Override
    public void setParam(PreparedStatement preparedStatement) {
        for (StatementParameter statementParam : statementParams) {
            setParam(preparedStatement,statementParam.getIndex(),statementParam.getJavaType(),statementParam.getJdbcType()
                    ,statementParam.getValue());
        }
    }

}
