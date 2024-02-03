package com.zeimao77;

import top.zeimao77.product.main.BaseMain;

/**
 * Hello world!
 *
 */
public class App extends BaseMain {
    public static void main( String[] args )
    {
        BaseMain.showBanner("0.0.1");
        logger.info( "Hello World!" );
        for (String arg : args) {
            logger.info(arg);
        }
    }

}
