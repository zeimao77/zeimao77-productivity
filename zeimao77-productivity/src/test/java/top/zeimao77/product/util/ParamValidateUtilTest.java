package top.zeimao77.product.util;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zeimao77.product.main.BaseMain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ParamValidateUtilTest extends BaseMain {

    private Logger log = LoggerFactory.getLogger(ParamValidateUtilTest.class);

    @Test
    public void mapRemoveNull() {
        Map<String,Object> map = new HashMap();
        Map<String,Object> m1 = new HashMap<>();
        m1.put("test","1");
        m1.put("test1","");
        m1.put("test2",new ArrayList<>());
        map.put("m1",m1);
        ParamValidateUtil.mapRemoveEmpty(map);
        logger.info(m1);
    }

    @Test
    public void mapCheck() {
        Map<String,Object> map = new HashMap();
        Map<String,Object> m2 = new HashMap<>();
        map.put("abc","2");
        m2.put("test1",new String[]{"1"});
        map.put("m2",m2);
        ParamValidateUtil.mapChecks(map,"m2.test1","abc");
    }

}