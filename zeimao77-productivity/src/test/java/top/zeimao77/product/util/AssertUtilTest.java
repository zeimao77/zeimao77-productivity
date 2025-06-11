package top.zeimao77.product.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zeimao77.product.main.BaseMain;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
        logger.info(StringUtil.subString("",0,20));
        System.out.println("1".compareTo("2"));
        System.out.println(StringUtil.compare(null,"2"));
        System.out.println(LocalDateTimeUtil.toDate(LocalDateTime.now(),LocalDateTimeUtil.SMARTNUMBERDATEFORMATTER));

    }

}