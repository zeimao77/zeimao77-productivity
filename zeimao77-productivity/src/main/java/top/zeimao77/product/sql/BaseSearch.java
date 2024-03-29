package top.zeimao77.product.sql;

import top.zeimao77.product.exception.BaseServiceRunException;
import static top.zeimao77.product.exception.ExceptionCodeDefinition.WRONG_SOURCE;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 查询条件接口
 */
public class BaseSearch {

    /**
     * 默认查询*
     */
    protected List<String> queryFields = null;
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
        safe(tableName);
        this.tableName = tableName;
    }

    /**
     * @return 表名
     */
    public String getTableName() {
        return tableName;
    }

    public List<String> getQueryFields() {
        return queryFields;
    }

    public void setQueryFields(List<String> queryFields) {
        for (String queryField : queryFields) {
            safe(queryField);
        }
        this.queryFields = queryFields;
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

