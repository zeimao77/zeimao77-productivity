package com.zeimao77;

import top.zeimao77.product.cmd.ProgressBar;
import top.zeimao77.product.config.LocalContext;
import top.zeimao77.product.jobs.TokenBucket;
import top.zeimao77.product.main.BaseMain;

import top.zeimao77.product.util.LongIdGenerator;

import java.io.UnsupportedEncodingException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.TimeUnit;

public class Main extends BaseMain {

    public static void main(String[] args) throws UnsupportedEncodingException {
        BaseMain.showBanner("0.0.1");
        logger.info(String.format("%d===",null));

        logger.info("param:{}", LocalContext.getString("top_zeimao77_test").get());
        logger.trace("{}",LongIdGenerator.INSTANCE.generate());
        logger.debug("{}",LongIdGenerator.INSTANCE.generate());
        logger.info("{}",LongIdGenerator.INSTANCE.generate());
        logger.warn("{}",LongIdGenerator.INSTANCE.generate());
        logger.error("{}",LongIdGenerator.INSTANCE.generate());
        logger.info(Long.toHexString(System.currentTimeMillis()));
        System.out.println();
        ProgressBar progressBar = new ProgressBar("Progress",100,80,TimeUnit.MILLISECONDS);
        progressBar.start();
        TokenBucket.SleetStrategy sleetStrategy = new TokenBucket.SleetStrategy(10, TimeUnit.MILLISECONDS);
        for (int i = 0; i < 100; i++) {
            progressBar.addCur(1);
            sleetStrategy.sleep();
        }
        System.out.println(Integer.MAX_VALUE);
        logger.info(BigDecimal.valueOf(2.3426).setScale(3, RoundingMode.HALF_UP).toString());


    }


}
