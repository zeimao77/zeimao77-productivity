package top.zeimao77.product.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zeimao77.product.exception.BaseServiceRunException;
import static top.zeimao77.product.exception.ExceptionCodeDefinition.IOEXCEPTION;
import top.zeimao77.product.util.AssertUtil;
import top.zeimao77.product.util.BoolUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

/**
 * @author zeimao77
 * 本地静态存储上下文
 */
public class LocalContext {

    private static Logger logger = LoggerFactory.getLogger(LocalContext.class);

    private static final HashMap<String,Object> context = new HashMap<>();

    public static Object put(String key,Object value) {
        return context.put(key,value);
    }

    public static Object get(String key) {
        return context.get(key);
    }

    public static String getString(String key) {
        Object o = get(key);
        return o != null ? o.toString() : null;
    }

    public static Long getLong(String key) {
        Object o = get(key);
        return AssertUtil.isBlack(o.toString()) ? null : Long.valueOf(o.toString());
    }

    public static Integer getInteger(String key) {
        Object o = get(key);
        return AssertUtil.isBlack(o.toString()) ? null : Integer.valueOf(o.toString());
    }

    public static Double getDouble(String key) {
        Object o = get(key);
        return AssertUtil.isBlack(o.toString()) ? null : Double.valueOf(o.toString());
    }

    public static Boolean getBoolean(String key) {
        Object o = get(key);
        Boolean result = null;
        try {
            result = BoolUtil.parseBool(o.toString());
        }catch (BaseServiceRunException e) {
            logger.error(e.getMessage(),e);
        }
        return result;
    }

    public static Object remove(String key) {
        return context.remove(key);
    }

    public static void show() {
        int i = 1;
        for (String s : context.keySet()) {
            logger.info("CONTEXT({}):{}:{}",i,s,context.get(s));
            i++;
        }
    }

    public static ArrayList<String> get(String keyFromat,int len) {
        ArrayList<String> resultList = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            String key = String.format(keyFromat,i);
            Object value = LocalContext.get(key);
            if(AssertUtil.isNotEmpty(value)) {
                resultList.add(value.toString());
            } else {
                break;
            }
        }
        return resultList;
    }

    public static void putByProperties(InputStream is) {
        Properties properties = new Properties();
        try {
            properties.load(is);
        } catch (IOException e) {
            throw new BaseServiceRunException(IOEXCEPTION,"IO错误");
        }
        for (Object o : properties.keySet()) {
            String k = o.toString();
            Object v = properties.get(o);
            LocalContext.put(k,v);
        }
    }


}
