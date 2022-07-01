package top.zeimao77.product.mysql;

import top.zeimao77.product.exception.BaseServiceRunException;
import static top.zeimao77.product.exception.ExceptionCodeDefinition.WRONG_SOURCE;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 查询条件接口
 */
public class BaseSearch {

    /**
     * 默认查询*
     */
    protected String queryColumn = "*";
    /**
     * 表名
     */
    protected String tableName;

    private static final Pattern DEFAULT_PATTERN = Pattern.compile("[0-9A-Za-z_$]*");
    private Pattern pattern = DEFAULT_PATTERN;


    /**
     * @param tableName 表名
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * @return 表名
     */
    public String getTableName() {
        return tableName;
    }

    public String getQueryColumn() {
        return queryColumn;
    }

    protected void setQueryColumn(String queryColumn) {
        this.queryColumn = queryColumn;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    /**
     * 断言字符串是否安全 只允许包含字母、数字、下划线、#$符号
     * @param str 字符串
     */
    public void safe(String str) {
        if(str == null)
            return;
        Matcher matcher = pattern.matcher(str);
        if(!matcher.matches()){
            throw new BaseServiceRunException(WRONG_SOURCE,"SQL不允许包含敏感字符");
        }
    }
}

