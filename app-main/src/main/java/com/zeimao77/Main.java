package com.zeimao77;
import top.zeimao77.product.main.BaseMain;
import top.zeimao77.product.util.ByteArrayCoDesUtil;
import top.zeimao77.product.util.LocalDateTimeUtil;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.regex.Pattern;

public class Main extends BaseMain {

    public static void main(String[] args) throws UnsupportedEncodingException {
        long l = System.currentTimeMillis();
        logger.info("{}",l);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(l), ZoneId.systemDefault());
        logger.info(LocalDateTimeUtil.toDateTime(localDateTime));
        logger.info(ByteArrayCoDesUtil.hexEncode(" ".getBytes("UTF-8")));
        logger.info(ByteArrayCoDesUtil.hexEncode(" ".trim().getBytes("UTF-8")));
        logger.info(ByteArrayCoDesUtil.hexEncode("　".trim().getBytes("UTF-8")));
        logger.info(ByteArrayCoDesUtil.hexEncode(String.valueOf('\u3000').getBytes("UTF-8")));
    }



}
