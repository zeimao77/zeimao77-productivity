package top.zeimao77.product.converter;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@JsonSerialize(using = JacksonConverterSerializer.class)
@JacksonAnnotationsInside
public @interface JacksonConvertion {

      /**
       * 转换器Bean名字
       * 如果找不到转换器Bean 将不会有任何动作;
       * @return
       */
      String converterBean();

      /**
       * simple 简单格式
       * full [key]value格式
       * @return
       */
      String format() default "simple";

}
