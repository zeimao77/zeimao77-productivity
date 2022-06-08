package com.zeimao77;

import top.zeimao77.product.main.BaseMain;
import top.zeimao77.product.util.LongIdGenerator;

public class Main extends BaseMain {

    public static void main(String[] args) {
        BaseMain.showBanner();
        logger.trace(LongIdGenerator.INSTANCE.generate());
        logger.debug(LongIdGenerator.INSTANCE.generate());
        logger.info(LongIdGenerator.INSTANCE.generate());
        logger.warn(LongIdGenerator.INSTANCE.generate());
        logger.error(LongIdGenerator.INSTANCE.generate());
        logger.fatal(LongIdGenerator.INSTANCE.generate());
    }
}
