package top.zeimao77.product.sql;

import top.zeimao77.product.util.AssertUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * JDBC参数解析器
 */
public abstract class AbstractStatementParamResolver implements StatementParamResolver {

    /**
     * sqlt 源SQL
     * sql 解析后的SQL 预处理的值使用 ? 点位;
     */
    protected String sqlt,sql;
    /**
     * 存储JDBC参数列表
     */
    protected List<StatementParameter> statementParams;
    private static final Pattern PATTERN = Pattern.compile("[$|#]\\{([,=\\w]+)\\}");

    /**
     * 解析的基础方法
     * @param fun JDBC参数值的解析策略函数
     */
    public void resolve(Function<String,Object> fun) {
        statementParams = new ArrayList<>(16);
        Matcher matcher = PATTERN.matcher(sqlt);
        int paramIndex = 0,flag = 0;
        if(!matcher.find()) {
            sql = sqlt;
            return;
        }
        StringBuilder sqlBuilder = new StringBuilder(sqlt.length());
        do {
            String group = matcher.group(1);
            sqlBuilder.append(sqlt, flag, matcher.start());
            flag = matcher.end();
            if(sqlt.charAt(matcher.start())== '$') {
                Object o = fun.apply(group);
                AssertUtil.notEmpty(o,String.format("参数 %s 必需",group));
                sqlBuilder.append(o.toString());
            } else {
                sqlBuilder.append("?");
                StatementParameter build = StatementParameter.Builder.create(group).index(++paramIndex).build();
                if(build.getMode() == 1) {
                    Object o = fun.apply(build.getName());
                    build.setValue(o);
                    if (build.getJavaType() == null && o != null) {
                        build.setJavaType(o.getClass());
                    }
                }
                statementParams.add(build);
            }
        } while (matcher.find());
        sqlBuilder.append(sqlt,flag,sqlt.length());
        sql = sqlBuilder.toString();
    }

    @Override
    public List<StatementParameter> getStatementParams() {
        return statementParams;
    }

    @Override
    public String getSql() {
        return sql;
    }

}
