package com.zeimao77;

import top.zeimao77.product.main.BaseMain;
import top.zeimao77.product.config.LocalContext;


public class App extends BaseMain {
    public static void main( String[] args )
    {
        BaseMain.showBanner("0.0.1");
        logger.info( "Hello World!" );
        logger.info("param:{}", LocalContext.getString("top_zeimao77_test").get());
        for (String arg : args) {
            logger.info(arg);
        }
    }

}
