package top.zeimao77.product.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zeimao77.product.main.BaseMain;

import java.io.*;
<<<<<<< HEAD

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class AssertUtilTest extends BaseMain {

    public static void main(String[] args) throws IOException, NoSuchFieldException, IllegalAccessException {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18);
        logger.info(LocalDateTime.now().toString());
=======
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;

public class AssertUtilTest extends BaseMain {

    private static Logger logger = LoggerFactory.getLogger(AssertUtilTest.class);

    public static void main(String[] args) throws IOException, NoSuchFieldException, IllegalAccessException, InterruptedException {
        BaseMain.showBanner("1.0.1");
        NumberUtil numberUtil = new NumberUtil(NumberUtil.DECIMAL,false, 4, RoundingMode.HALF_UP);
        logger.info(numberUtil.format(23344432233.23));
        logger.info("2023-08-21 23:59:59".substring(0,10));
        logger.info(UUID.randomUUID().toString());
>>>>>>> main

    }

}