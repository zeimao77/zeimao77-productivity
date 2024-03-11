package top.zeimao77.product.security;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@JsonSerialize(using = JacksonDesensitizationSerializer.class)
@JacksonAnnotationsInside
public @interface JacksonDesensitization {

    /**
     * type类型
     */
    int MOBILEPHONE = 1;
    int IDENTITYCARD = 2;
    int EMAIL = 3;
    int NAME = 4;
    int TELEPHONE = 5;

    int type();

}
