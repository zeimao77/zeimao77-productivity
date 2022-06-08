package top.zeimao77.product.util;

import java.lang.reflect.Field;

public class ClassUtil {

    private ClassUtil(){}

    public static Field getField(Class<?> clazz,String fieldName) {
        for (Field declaredField : clazz.getDeclaredFields()) {
            if(fieldName.equals(declaredField.getName())) {
                return declaredField;
            }
        }
        return null;
    }

}
