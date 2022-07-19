package top.zeimao77.product.factory;

import top.zeimao77.product.exception.BaseServiceRunException;
import top.zeimao77.product.exception.ExceptionCodeDefinition;
import top.zeimao77.product.util.AssertUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 简单小型的BEAN工作实现
 */
public class BeanFactory {

    /**
     * 默认的BEAN工厂实现
     */
    public static final BeanFactory DEFAULT = new BeanFactory();

    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(32);
    private final Map<String, Supplier<?>> prototypesFactory = new ConcurrentHashMap<>(16);

    /**
     * 注册一个原型的BEAN提供者
     * @param beanName BEAN名称
     * @param supplier 生产者
     */
    public void registerPrototypesFactory(String beanName,Supplier<?> supplier) {
        synchronized (this.prototypesFactory) {
            Supplier<?> oldObject = prototypesFactory.get(beanName);
            if (oldObject != null) {
                throw new IllegalStateException("不可以注册单例BEAN 因为该BEAN名称已经被定义;");
            }
            this.prototypesFactory.put(beanName,supplier);
        }
    }

    /**
     * 注册一个单例BEAN
     * @param beanName BEAN名称
     * @param singletonObject BEAN
     */
    public void registerSingleton(String beanName, Object singletonObject) {
        AssertUtil.notNull(beanName, "BEAN名称不能为空");
        AssertUtil.notNull(singletonObject, "单例BEAN不能为空");
        synchronized (this.singletonObjects) {
            Object oldObject = this.singletonObjects.get(beanName);
            if (oldObject != null) {
                throw new IllegalStateException("不可以注册单例BEAN 因为该BEAN名称已经被定义;");
            }
           this.singletonObjects.put(beanName,singletonObject);
        }
    }

    /**
     * 获取一个BEAN对象 优先单例获取
     * @param beanName BEAN名称
     * @param requiredType BEAN类型
     * @param <T> BEAN泛型
     * @return BEAN对象
     */
    public <T> T getBean(String beanName, Class<T> requiredType) {
        AssertUtil.notNull(beanName, "BEAN名称不能为空");
        AssertUtil.notNull(requiredType, "单例类型不能为空");
        T t = (T) singletonObjects.get(beanName);
        if(t != null)
            return t;
        Supplier<?> supplier = prototypesFactory.get(beanName);
        if(supplier != null)
            return (T)supplier.get();
        throw new BaseServiceRunException(ExceptionCodeDefinition.WRONG_ACTION,"没有这样的BEAN实例");
    }

    /**
     * 对通类型获取一个单例BEAN对象
     * @param requiredType BEAN类型
     * @param <T> BEAN泛型
     * @return BEAN对象
     */
    public <T> T getBean(Class<T> requiredType) {
        for (Object value : singletonObjects.values()) {
            if(requiredType.isAssignableFrom(value.getClass())) {
                return (T)value;
            }
        }
        throw new BaseServiceRunException(ExceptionCodeDefinition.WRONG_ACTION,"没有这样的单例BEAN实例");
    }

    /**
     * 注销一个BEAN
     * @param beanName BEAN名称
     * @param requiredType BEAN类型
     * @param logOff 注册后置调用
     * @param <T> BEAN泛型
     */
    public <T> void logOffSingleton(String beanName, Class<T> requiredType, Consumer<T> logOff) {
        T bean = getBean(beanName, requiredType);
        if(logOff != null)
            logOff.accept(bean);
    }

}
