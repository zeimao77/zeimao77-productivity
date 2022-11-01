package top.zeimao77.product.util;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.main.BaseMain;

import java.util.List;
import java.util.Set;

class CollectionUtilTest extends BaseMain {

    private static List<Integer> l1 = List.of(1,2,3,4,5,6);
    private static List<Integer> l2 = List.of(4,5,6,7,8,9);

    @Test
    void union() {
        Set<Integer> union = CollectionUtil.union(l1, l2);
        String s = JsonBeanUtil.DEFAULT.toJsonString(union);
        logger.info("并集:{}",s);
    }

    @Test
    void diff() {
        Set<Integer> diff = CollectionUtil.diff(l1, l2);
        String s = JsonBeanUtil.DEFAULT.toJsonString(diff);
        logger.info("差集:{}",s);
    }

    @Test
    void inter() {
        Set<Integer> inter = CollectionUtil.inter(l1, l2);
        String s = JsonBeanUtil.DEFAULT.toJsonString(inter);
        logger.info("交集:{}",s);
    }

    @Test
    void getRandom() {
        for (int i = 0; i < 12; i++) {
            Integer random = CollectionUtil.getRandom(l1);
            logger.info("第{}次取值{}",i,random);
        }
    }
}