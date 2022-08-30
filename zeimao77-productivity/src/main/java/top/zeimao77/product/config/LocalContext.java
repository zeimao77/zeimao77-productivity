package top.zeimao77.product.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.zeimao77.product.exception.BaseServiceRunException;
import top.zeimao77.product.exception.ExceptionCodeDefinition;
import top.zeimao77.product.util.AssertUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

/**
 * @author zeimao77
 * 本地静态存储上下文
 */
public class LocalContext {

    private static Logger logger = LogManager.getLogger(LocalContext.class);

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

    public static Boolean getBoolean(String key) {
        Object o = get(key);
        if(o != null) {
            if("TRUE".equalsIgnoreCase(o.toString())
                    || "Y".equalsIgnoreCase(o.toString())
                    || "YES".equals(o.toString())
                    || "1".equals(o.toString())) {
                return true;
            }
            if("FALSE".equalsIgnoreCase(o.toString())
                    || "N".equalsIgnoreCase(o.toString())
                    || "NO".equalsIgnoreCase(o.toString())
                    || "0".equalsIgnoreCase(o.toString())) {
                return false;
            }
        }
        return null;
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
            }
        }
        return resultList;
    }

    public static void putByProperties(InputStream is) {
        Properties properties = new Properties();
        try {
            properties.load(is);
        } catch (IOException e) {
            throw new BaseServiceRunException(ExceptionCodeDefinition.IOEXCEPTION,"IO错误");
        }
        for (Object o : properties.keySet()) {
            String k = o.toString();
            Object v = properties.get(o);
            LocalContext.put(k,v);
        }
    }


}
