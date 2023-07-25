package top.zeimao77.product.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zeimao77.product.main.BaseMain;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

public class AssertUtilTest extends BaseMain {

    private static Logger logger = LoggerFactory.getLogger(AssertUtilTest.class);

    public static void main(String[] args) throws IOException, NoSuchFieldException, IllegalAccessException, InterruptedException {
        BaseMain.showBanner("1.0.1");
        Random random = new Random();
        logger.info( CalendarDateUtil.STANDARDDATETIMEFORMATTER.format(new Date(Long.MAX_VALUE)));
        for (int i = 0; i < 60; i++) {
            new Thread(() -> {
                Date date = new Date(random.nextLong());
                String format = CalendarDateUtil.STANDARDDATETIMEFORMATTER.format(date);
                logger.info(format);
            }).start();

        }

    }

}