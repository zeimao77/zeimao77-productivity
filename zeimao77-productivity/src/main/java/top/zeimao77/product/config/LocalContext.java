package top.zeimao77.product.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zeimao77.product.exception.BaseServiceRunException;

import top.zeimao77.product.util.AssertUtil;
import top.zeimao77.product.util.BoolUtil;
import top.zeimao77.product.util.StringOptional;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Optional;

/**
 * @author zeimao77
 * 本地静态存储上下文
 */
public class LocalContext {

    private static Logger logger = LoggerFactory.getLogger(LocalContext.class);
    private static final LocalContextConverter LOCAL_CONTEXT;

    static {
        String localContextFile = getPropertyOrEnv("local.context.file",null);
        String localContextActive = getPropertyOrEnv("local.context.active",null);
        LOCAL_CONTEXT = new LocalContextConverter(localContextFile,localContextActive);
    }

    public static Object get(String key) {
        return LOCAL_CONTEXT.get(key);
    }

    public static Optional<Object> getObject(String key) {
        Object o = get(key);
        return o == null ? Optional.empty() : Optional.of(o);
    }

    public static StringOptional getString(String key) {
        Object o = get(key);
        return o == null ? StringOptional.empty() : new StringOptional(o.toString());
    }

    public static Optional<Long> getLong(String key) {
        Object o = get(key);
        if (o == null)
            return Optional.empty();
        if(o instanceof Long) {
            Long t = (Long) o;
            return Optional.of(t);
        }
        return AssertUtil.isBlank(o.toString()) ? Optional.empty() : Optional.of(Long.valueOf(o.toString()));
    }

    public static Optional<Integer> getInteger(String key) {
        Object o = get(key);
        if(o == null)
            return Optional.empty();
        if(o instanceof Integer) {
            Integer t = (Integer) o;
            return Optional.of(t);
        }
        return AssertUtil.isBlank(o.toString()) ? Optional.empty() : Optional.of(Integer.valueOf(o.toString()));
    }

    public static Optional<Double> getDouble(String key) {
        Object o = get(key);
        if(o == null)
            return Optional.empty();
        if(o instanceof Double) {
            Double t = (Double) o;
            return Optional.of(t);
        }
        return AssertUtil.isBlank(o.toString()) ? Optional.empty() : Optional.of(Double.valueOf(o.toString()));
    }

    public static Optional<Boolean> getBoolean(String key) {
        Object o = get(key);
        if(o == null)
            return Optional.empty();
        try {
            Boolean result = BoolUtil.parseBool(o.toString());
            return Optional.of(result);
        }catch (BaseServiceRunException e) {
            logger.error(e.getMessage(),e);
        }
        return Optional.empty();
    }

    public static Optional<Duration> getDuration(String key, TemporalUnit unit) {
        Optional<Long> aLong = getLong(key);
        return !aLong.isPresent() ? Optional.empty() : Optional.of(Duration.of(aLong.get(),unit));
    }

    public static ArrayList<String> get(String keyFromat,int len) {
        ArrayList<String> resultList = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            String key = String.format(keyFromat,i);
            Object value = get(key);
            if(AssertUtil.isNotEmpty(value)) {
                resultList.add(value.toString());
            } else {
                break;
            }
        }
        return resultList;
    }


    /**
     * 从命令参数或者环境变量获取参数
     * @param key 键
     * @param defaultValue 默认值 如果找不到将返回该值
     * @return 结果
     */
    public static String getPropertyOrEnv(String key,String defaultValue) {
        String value = System.getProperty(key);
        if(value == null)
            value = System.getenv(key);
        return value == null ? defaultValue : value;
    }

}
