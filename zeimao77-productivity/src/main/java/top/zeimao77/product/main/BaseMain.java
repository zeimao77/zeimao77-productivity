package top.zeimao77.product.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.util.PluginManager;
import top.zeimao77.product.config.LocalContext;
import top.zeimao77.product.exception.BaseServiceRunException;

import static top.zeimao77.product.exception.ExceptionCodeDefinition.WRONG_SOURCE;
import static top.zeimao77.product.exception.ExceptionCodeDefinition.IOEXCEPTION;

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
        initLoggerConfig();
        logger = LogManager.getLogger(BaseMain.class);
        initLocalContext();
    }

    /**
     * 初始化LocalContext环境
     * 支持通过环境变量或执行参数配置它
     * local.context.file 指定一个配置文件路径
     * 默认从classpath下查找 localcontext.properties;
     */
    private static void initLocalContext(){
        String localContextFile = getPropertyOrEnv("local.context.file",null);
        String localContextActive = getPropertyOrEnv("local.context.active",null);
        String contextFile;
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
                throw new BaseServiceRunException(WRONG_SOURCE,"文件不存在",e);
            } catch (IOException e) {
                throw new BaseServiceRunException(IOEXCEPTION,"IO错误",e);
            }
            return;
        }
        {
            InputStream resourceAsStream;
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
                    throw new BaseServiceRunException(IOEXCEPTION,"IO错误",e);
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

    /**
     * 初始化log4j2默认的日志配置
     * 支持通过环境变量或执行参数配置它
     * log.level 用于配置日志级别
     * log.file 日志路径将日志持久化到文件
     * log.rolling true/false 开启滚动日志，默认保存10个文件
     * log.rolloverStrategyMax  保存历史日志文件数量
     */
    private static void initLoggerConfig(){
        PluginManager.addPackage("top.zeimao77.product.log4j2");
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
    public static void showBanner(String version) {
        logger.info("           /\\_/\\");
        logger.info("     _____/ o o \\");
        logger.info("   /~_____  =-= /");
        logger.info("  (__zm77__)_m_m)");
        if(version != null)
            logger.info("  app-version:{}",version);
    }

}
