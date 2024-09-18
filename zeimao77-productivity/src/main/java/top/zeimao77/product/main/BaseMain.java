package top.zeimao77.product.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * main函数入口继承它，可以实现环境和日志配置的初始化
 */
public class BaseMain {

    /**
     * 日志工具
     */
    protected static Logger logger;

    static {
        logger = LoggerFactory.getLogger(BaseMain.class);
    }

    /**
     * 线程睡眠
     * @param timeout 时间毫秒
     */
    public static void delay_ms(int timeout) {
        try {
            TimeUnit.MILLISECONDS.sleep(timeout);
        } catch (InterruptedException e) {
            logger.error("线程中断退出阻塞",e);
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 打印一个Banner
     */
    public static void showBanner(String version) {
        logger.info("           /\\_/\\");
        logger.info("     _____/ o o \\");
        logger.info("   /~_____  =-= /");
        logger.info("  (__zm77__)_m_m)");
        if(version != null)
            logger.info("  app-version:{}",version);
    }

}
