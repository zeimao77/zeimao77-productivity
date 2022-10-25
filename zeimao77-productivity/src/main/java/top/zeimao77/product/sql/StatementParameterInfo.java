package top.zeimao77.product.sql;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface StatementParameterInfo {

    /**
     * @return 适用的数据库
     */
    int dbtype() default 0x7FFFFFFF;

    /**
     * 参数类型
     * @return 1:入参;2:出参
     */
    int mode() default 1;

    /**
     * @return 语句类型
     */
    int jdbcType() default StatementParameter.DEFAULT_JDBCTYPE;

    /**
     * @return 参数函数前
     */
    String valSetPre();

    /**
     * @return 参数函数后
     */
    String valSetPost();

}
