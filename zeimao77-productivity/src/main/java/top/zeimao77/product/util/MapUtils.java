package top.zeimao77.product.util;

import java.util.Map;

public class MapUtils {

    public static void mapCopy(Map<String,?> source,Map<String,Object> target,String... keys) {
        for (String key : keys) {
            if(source.containsKey(key))
                target.put(key,source.get(key));
        }
    }

}
