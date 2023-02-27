package top.zeimao77.product.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import top.zeimao77.product.email.SimpleEmailSender;
import top.zeimao77.product.exception.BaseServiceRunException;
import static top.zeimao77.product.exception.ExceptionCodeDefinition.WRONG_ACTION;

import top.zeimao77.product.sql.SimpleSqlClient;
import top.zeimao77.product.sql.SimpleSqlTemplate;
import top.zeimao77.product.util.AssertUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 简单小型的BEAN工作实现
 */
public class BeanFactory {

    private static Logger logger = LogManager.getLogger(BeanFactory.class);
    /**
     * 默认的BEAN工厂实现
     */
    public static final BeanFactory DEFAULT = new BeanFactory();

    private final Map<String, Object> singletonObjects;
    private final Map<String, Supplier<?>> prototypesFactory;


    public BeanFactory() {
        this(64,16);
    }

    public BeanFactory(int singletonSize,int prototypesSize) {
        singletonObjects = new ConcurrentHashMap<>(singletonSize);
        prototypesFactory = new ConcurrentHashMap<>(prototypesSize);
    }

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
     * 是否已经注册了这个Bean名称
     * @param beanName Bean名称
     * @return 是否已经注册了这个Bean名称
     */
    public boolean hasBean(String beanName) {
        return singletonObjects.containsKey(beanName) || prototypesFactory.containsKey(beanName);
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
        if(requiredType == SimpleSqlClient.class && beanName.startsWith(ComponentFactory.AUTOBEAN_SQLCLIENT)) {
            SimpleSqlClient client = ComponentFactory.initSimpleSqlClient(beanName, null);
            registerSingleton(beanName,client);
            return (T) client;
        }
        if(requiredType == SimpleSqlTemplate.class && beanName.startsWith(ComponentFactory.AUTOBEAN_SQLTEMPLATE)) {
            SimpleSqlTemplate client = ComponentFactory.initSimpleSqlTemplate(beanName, null);
            registerSingleton(beanName,client);
            return (T) client;
        }
        if(requiredType == Jedis.class) {
            return (T) ComponentFactory.initJedis(beanName,this);
        }
        if(requiredType == JedisCluster.class) {
            return (T) ComponentFactory.initJedisCluster(beanName,this);
        }
        if(requiredType == SimpleEmailSender.class) {
            SimpleEmailSender simpleEmailSender = ComponentFactory.initSimpleEmailSender(beanName, this);
            return (T) simpleEmailSender;
        }
        throw new BaseServiceRunException(WRONG_ACTION,"没有这样的BEAN实例:"+beanName);
    }

    /**
     * 通过类型获取一个单例BEAN对象
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
        throw new BaseServiceRunException(WRONG_ACTION,"没有这样的单例BEAN实例:"+requiredType.getName());
    }

    /**
     * 注销一个BEAN
     * @param beanName BEAN名称
     * @param requiredType BEAN类型
     * @param logOff 注销后调用
     * @param <T> BEAN泛型
     */
    public <T> void logOffSingleton(String beanName, Class<T> requiredType, Consumer<T> logOff) {
        T bean = getBean(beanName, requiredType);
        if(logOff == null) {
            logOffSingleton(beanName,requiredType);
        } else {
            logOff.accept(bean);
            this.singletonObjects.remove(beanName);
        }
    }

    public <T> void logOffSingleton(String beanName, Class<T> requiredType) {
        T bean = getBean(beanName, requiredType);
        if(bean instanceof AutoCloseable a) {
            try {
                a.close();
            } catch (Exception e) {
                logger.error("自动关闭错误",e);
            }
        }
        this.singletonObjects.remove(beanName);
    }

    /**
     * 销毁工厂
     * 如果单例Bean实现自 java.lang.AutoCloseable 将自动执行 close() 方法;
     */
    public void destory() {
        this.singletonObjects.forEach((o1,o2) -> {
            if(o2 instanceof AutoCloseable a) {
                try {
                    a.close();
                } catch (Exception e) {
                    logger.error("自动关闭错误",e);
                }
            }
        });
        this.singletonObjects.clear();
    }


}
