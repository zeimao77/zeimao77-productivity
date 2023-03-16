package top.zeimao77.product.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zeimao77.product.main.BaseMain;

import java.io.*;

import java.util.*;

public class AssertUtilTest extends BaseMain {

    private static Logger logger = LoggerFactory.getLogger(AssertUtilTest.class);

    public static void main(String[] args) throws IOException, NoSuchFieldException, IllegalAccessException, InterruptedException {
        BaseMain.showBanner("1.0.1");
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18);
        logger.info("{}",CollectionUtil.page(list,4,6));
        IdentityHashMap map = new IdentityHashMap();
        map.put(Long.valueOf(266),"abcdefg");
        map.put(Long.valueOf(266),"aabbccde");
        logger.info("{}",map.get(Long.valueOf(266)));

        HashMap map1 = new HashMap<>();
        map1.put(Long.valueOf(266),"abcdefg");
        map1.put(Long.valueOf(266),"aabbccde");
        logger.info("{}",map1.get(Long.valueOf(266)));

        String KEY = "PN9NefS9QeqVM7upuwZ4TL3uCnsyZgYkDBcroXZSw3g=";
        byte[] bytes = ByteArrayCoDesUtil.base64Decode(KEY);
        for (byte aByte : bytes) {
            System.out.print(aByte+",");
        }

        for (int i = 0; i < 10; i++) {

            logger.info(UuidGenerator.INSTANCE.generate());
        }

    }

}