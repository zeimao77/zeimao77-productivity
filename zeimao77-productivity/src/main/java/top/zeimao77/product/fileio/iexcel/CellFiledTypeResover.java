package top.zeimao77.product.fileio.iexcel;

import org.apache.poi.ss.usermodel.Cell;
import top.zeimao77.product.model.Orderd;
import top.zeimao77.product.util.AssertUtil;

import java.lang.reflect.ParameterizedType;

public interface CellFiledTypeResover<T> extends Orderd {

    default Class<T> getTclass() {
        Class<T> tClass = (Class<T>)((ParameterizedType)getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];
        return tClass;
    }

    default boolean support(Class<?> clazz, Cell cell) {
        AssertUtil.notNull(clazz,"excel解析类型必须指定;");
        return clazz.equals(getTclass());
    }

    T resove(Cell cell);
}
