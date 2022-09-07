package top.zeimao77.product.sql;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface StatementParameterInfo {

    int dbtype() default 0x7FFFFFFF;
    int mode() default 1;
    int jdbcType() default StatementParameter.DEFAULT_JDBCTYPE;
    String valSetPre();
    String valSetPost();

}
