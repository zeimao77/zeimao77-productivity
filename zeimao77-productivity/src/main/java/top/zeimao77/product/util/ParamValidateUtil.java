package top.zeimao77.product.util;

import top.zeimao77.product.exception.BaseServiceRunException;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static top.zeimao77.product.exception.ExceptionCodeDefinition.APPERR;

public class ParamValidateUtil {

    /**
     * 移除Map参数中的空参数
     * @param map Map
     */
    public static void mapRemoveEmpty(Map<String, Object> map) {
        if(map == null) {
            return;
        }
        Set<String> keys = map.keySet();
        Iterator ite = keys.iterator();
        while(ite.hasNext()) {
            String key = (String)ite.next();
            Object obj = map.get(key);
            if (obj instanceof Map) {
                Map o = ((Map) obj);
                mapRemoveEmpty(o);
                if (o.isEmpty()) {
                    ite.remove();
                }
            } else if(obj instanceof Collection) {
                Collection o = ((Collection) obj);
                for (Iterator iterator = o.iterator();iterator.hasNext();) {
                    Object n = iterator.next();
                    if(n instanceof Map) {
                        Map m = ((Map)n);
                        mapRemoveEmpty(m);
                    } else if(AssertUtil.isEmpty(obj)) {
                        iterator.remove();
                    }
                }
                if(o.isEmpty()) {
                    ite.remove();
                }
            } else if (AssertUtil.isEmpty(obj)) {
                ite.remove();
            }
        }
    }

    public static void mapChecks(Map<String, Object> map,String... checks){
        for (String check : checks) {
            mapCheck(map,check);
        }
    }

    static Pattern paramCheckPattern = Pattern.compile("(\\S+?)\\.(.*)");
    /**
     * 检查map参数
     * @param map 需要检查的map
     * @param check 检查的字段，支持点来层次处理
     */
    public static void mapCheck(Map<String, Object> map,String check){
        AssertUtil.notEmpty(map,"参数空");
        Matcher matcher = paramCheckPattern.matcher(check);
        if(matcher.matches()) {
            String c1 = matcher.group(1);
            String c2 = matcher.group(2);
            Object o = map.get(c1);
            AssertUtil.assertTrue(!AssertUtil.isEmpty(o),String.format("参数 %s 空或是无效参数,该参数必需",c1));
            if(o instanceof Map){
                Map m = ((Map) o);
                AssertUtil.assertTrue(!m.isEmpty(),APPERR,"参数 %s 空或是无效参数,该参数必需",c1);
                mapCheck(m,c2);
            } else if(o instanceof Collection ) {
                Collection o1 = ((Collection) o);
                for (Object o2 : o1) {
                    if(o2 instanceof Map)
                        mapCheck(((Map) o2),c2);
                    else
                        throw new BaseServiceRunException(APPERR,"参数 " + c1 + " 检查错误,不支持的类型");
                }
            } else if(o == null) {
                throw new BaseServiceRunException(APPERR,"参数 " + c1 + " 空或是无效参数,该参数必需");
            } else {
                throw new BaseServiceRunException(APPERR,"参数 " + c1 + " 检查错误,不支持的类型");
            }
        } else {
            Object o = map.get(check);
            if(o instanceof Map) {
                Map m = (Map)o;
                AssertUtil.assertTrue(!m.isEmpty(),APPERR,"参数 %s 空或是无效参数,该参数必需",check);
            } else if(o instanceof Collection) {
                Collection c = (Collection) o;
                AssertUtil.assertTrue(!c.isEmpty(), APPERR, "参数 %s 空或是无效参数,该参数必需", check);
            } else {
                AssertUtil.assertTrue(!AssertUtil.isEmpty(o), "参数 %s 空或是无效参数,该参数必需", check);
            }
        }
    }

    // map拷贝
    public static <T extends Map> void mapCopy(final T source,T target,String... fields){
        for (String field : fields) {
            target.put(field,source.get(field));
        }
    }

}
