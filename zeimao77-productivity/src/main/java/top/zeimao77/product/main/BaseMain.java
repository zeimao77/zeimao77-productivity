package top.zeimao77.product.main;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import top.zeimao77.product.config.LocalContext;
import top.zeimao77.product.config.MyConfigurationFactory;
import top.zeimao77.product.exception.BaseServiceRunException;

import java.io.*;
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
        initLogger();
        initLocalContext();
    }

    private static void initLocalContext(){
        String localContextFile = getPropertyOrEnv("local.context.file",null);
        String localContextActive = getPropertyOrEnv("local.context.active",null);
        String contextFile = null;
        if(localContextFile != null && localContextFile.endsWith(".properties")) {
            contextFile = localContextFile;
        } else if(localContextActive != null) {
            contextFile = System.getProperty("user.dir") + File.separator + "localcontext-"+localContextActive + ".properties";
        } else {
            contextFile = System.getProperty("user.dir") + File.separator + "localcontext.properties";
        }
        File file = new File(contextFile);
        if(file.exists() && file.canRead()) {
            try(FileInputStream fileInputStream = new FileInputStream(file)) {
                logger.debug("尝试加载配置文件:{}",contextFile);
                LocalContext.putByProperties(fileInputStream);
            } catch (FileNotFoundException e) {
                throw new BaseServiceRunException("文件不存在",e);
            } catch (IOException e) {
                logger.error("IO错误",e);
            }
            return;
        }
        {
            InputStream resourceAsStream = null;
            if(localContextActive == null) {
                resourceAsStream = BaseMain.class.getClassLoader().getResourceAsStream("localcontext.properties");
                if(resourceAsStream != null)
                    logger.debug("尝试加载配置文件:localcontext.properties");
            } else {
                resourceAsStream = BaseMain.class.getClassLoader().getResourceAsStream("localcontext-"+localContextActive + ".properties");
                if(resourceAsStream != null)
                    logger.debug("尝试加载配置文件:localcontext-{}.properties",localContextActive);
            }
            if(resourceAsStream != null) {
                LocalContext.putByProperties(resourceAsStream);
                try {
                    resourceAsStream.close();
                } catch (IOException e) {
                    throw new BaseServiceRunException("IO错误",e);
                } finally {
                    try {
                        resourceAsStream.close();
                    } catch (IOException e) {
                        logger.error("IO错误",e);
                    }
                }
                return;
            }
        }
    }

    private static void initLogger(){
        String levelName = getPropertyOrEnv("log.level","DEBUG");
        String logfile = getPropertyOrEnv("log.file",null);
        String rolling = getPropertyOrEnv("log.rolling", "FALSE");
        int rolloverStrategyMax = Integer.valueOf(getPropertyOrEnv("log.rolloverStrategyMax", "10"));
        boolean rollingLog = "FALSE".equals(rolling) ? false : true;
        Level level = Level.valueOf(levelName);
        ConfigurationFactory.setConfigurationFactory(new MyConfigurationFactory(level,logfile,rollingLog,rolloverStrategyMax));
        logger = LogManager.getLogger(BaseMain.class);
    }

    /**
     * 从命令参数或者环境变量获取参数
     * @param key 键
     * @param defaultValue 默认值 如果找不到将返回该值
     * @return 结果
     */
    public static String getPropertyOrEnv(String key,String defaultValue) {
        String value = System.getProperty(key);
        if(value == null){
            value = System.getenv(key);
        }
        return value == null ? defaultValue : value;
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
    public static void showBanner() {
        logger.info("           /\\_/\\");
        logger.info("     _____/ o o \\");
        logger.info("   /~_____  =-= /");
        logger.info("  (__zm77__)_m_m)");
    }

}
