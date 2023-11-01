package com.zeimao77;

import top.zeimao77.product.config.LocalContext;
import top.zeimao77.product.main.BaseMain;
import top.zeimao77.product.util.CalendarDateUtil;
import top.zeimao77.product.util.LocalDateTimeUtil;
import top.zeimao77.product.util.LongIdGenerator;
import top.zeimao77.product.util.UuidGenerator;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

public class Main extends BaseMain {

    public static void main(String[] args) throws UnsupportedEncodingException {
        BaseMain.showBanner("0.0.1");

        logger.info("param:{}", LocalContext.getString("top_zeimao77_test").get());
        logger.trace("{}",LongIdGenerator.INSTANCE.generate());
        logger.debug("{}",LongIdGenerator.INSTANCE.generate());
        logger.info("{}",LongIdGenerator.INSTANCE.generate());
        logger.warn("{}",LongIdGenerator.INSTANCE.generate());
        logger.error("{}",LongIdGenerator.INSTANCE.generate());
        logger.info(Long.toHexString(System.currentTimeMillis()));
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(1698802091254L), ZoneId.systemDefault());
        logger.info(LocalDateTimeUtil.toDateTime(localDateTime));
        logger.info( ZoneId.of("+07:00").getId());
        logger.info(CalendarDateUtil.toDate(new Date()));

    }


}
