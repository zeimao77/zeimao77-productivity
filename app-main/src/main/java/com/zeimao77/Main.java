package com.zeimao77;

import top.zeimao77.product.main.BaseMain;
import top.zeimao77.product.util.LongIdGenerator;

public class Main extends BaseMain {

    public static void main(String[] args) {
        BaseMain.showBanner();

        new Thread(() -> {
            while (true) {
                delay_ms(2000);
                logger.info(LongIdGenerator.INSTANCE.generate());
            }
        }).start();
        while(true) {
            delay_ms(2000);
            logger.info(Long.toHexString(System.currentTimeMillis()));
        }
    }



}
