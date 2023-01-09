package top.zeimao77.product.util;

import top.zeimao77.product.main.BaseMain;

import java.io.*;

import java.util.Arrays;
import java.util.List;

public class AssertUtilTest extends BaseMain {

    public static void main(String[] args) throws IOException, NoSuchFieldException, IllegalAccessException {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18);
        logger.info("{}",CollectionUtil.page(list,4,6));

    }

}