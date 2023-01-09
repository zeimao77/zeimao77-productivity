package top.zeimao77.product.util;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.main.BaseMain;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class ParamValidateUtilTest extends BaseMain {


    @Test
    void mapCheck() {
        showBanner();
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        HashMap<String, Object> map = new HashMap<>();
        map.put("param1","");
        // stringObjectHashMap.put("map",null);
        ParamValidateUtil.mapCheck(stringObjectHashMap,"map.param1");

    }
}