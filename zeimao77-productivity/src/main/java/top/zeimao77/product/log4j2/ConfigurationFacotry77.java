package top.zeimao77.product.log4j2;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Order;
import org.apache.logging.log4j.core.config.builder.api.*;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import top.zeimao77.product.util.BoolUtil;

/**
 * 提供一个可以直接使用的默认的日志配置插件以覆盖日志框架提供的默认配置
 * 如果对该配置不满意 可以提供一个log4j2.xml文件以供日志环境初始化;
 * @author zeimao77
 * @since 2.0.9
 */
@Plugin(name = "MyConfigurationFactory", category = ConfigurationFactory.CATEGORY)
@Order(3)
public class ConfigurationFacotry77 extends ConfigurationFactory {

    Level rootLevel;
    String logfile;
    boolean rolling;
    boolean stdoutEnable;
    int rolloverStrategyMax;

    public ConfigurationFacotry77(){
        String logLevel = getPropertyOrEnv("log.level","DEBUG");
        this.rootLevel = Level.valueOf(logLevel);
        logfile = getPropertyOrEnv("log.file",null);
        String logRolling = getPropertyOrEnv("log.rolling", "FALSE");
        this.rolling = BoolUtil.parseBool(logRolling,false);
        String aTrue = getPropertyOrEnv("log.stdout.enable", "TRUE");
        this.stdoutEnable = BoolUtil.parseBool(aTrue,true);
        rolloverStrategyMax = Integer.valueOf(getPropertyOrEnv("log.rolloverStrategyMax", "10"));
    }

    private static String getPropertyOrEnv(String key,String defaultValue) {
        String value = System.getProperty(key);
        if(value == null){
            value = System.getenv(key);
        }
        return value == null ? defaultValue : value;
    }

    @Override
    protected String[] getSupportedTypes() {
        return new String[]{".77.conf"};
    }

    @Override
    public Configuration getConfiguration(LoggerContext loggerContext, ConfigurationSource source) {
        ConfigurationBuilder<BuiltConfiguration> builder = newConfigurationBuilder();
        return createConfiguration(loggerContext.getName(), builder);
    }

    /**
     * 配置标准输出日志
     * @param builder --
     * @param layoutComponentBuilder --
     * @return --
     */
    private AppenderComponentBuilder console(ConfigurationBuilder<BuiltConfiguration> builder
            , LayoutComponentBuilder layoutComponentBuilder,RootLoggerComponentBuilder rootLoggerComponentBuilder){
        AppenderComponentBuilder consoleComponentBuilder = builder.newAppender("stdout", "CONSOLE").
                addAttribute("target", ConsoleAppender.Target.SYSTEM_OUT);
        consoleComponentBuilder.add(layoutComponentBuilder);
        rootLoggerComponentBuilder.add(builder.newAppenderRef("stdout"));
        return consoleComponentBuilder;
    }

    /**
     * 配置文件日志
     * @param builder --
     * @param layoutComponentBuilder --
     * @return --
     */
    public AppenderComponentBuilder file(ConfigurationBuilder<BuiltConfiguration> builder
            ,LayoutComponentBuilder layoutComponentBuilder,RootLoggerComponentBuilder rootLoggerComponentBuilder){
        AppenderComponentBuilder fileComponentBuilder = builder.newAppender("file", "FILE")
                .addAttribute("fileName", logfile);
        fileComponentBuilder.add(layoutComponentBuilder);
        rootLoggerComponentBuilder.add(builder.newAppenderRef("file"));
        return fileComponentBuilder;
    }

    /**
     * 滚动文件日志
     * @param builder --
     * @param layoutComponentBuilder --
     * @return --
     */
    public AppenderComponentBuilder rollingFile(ConfigurationBuilder<BuiltConfiguration> builder
            ,LayoutComponentBuilder layoutComponentBuilder,RootLoggerComponentBuilder rootLoggerComponentBuilder){
        ComponentBuilder triggeringPolicy = builder.newComponent("Policies")
                .addComponent(builder.newComponent("CronTriggeringPolicy").addAttribute("schedule", "0 0 0 * * ?"))
                // .addComponent(builder.newComponent("TimeBasedTriggeringPolicy").addAttribute("interval",3))  // 每3秒时间间隔滚动日志
                .addComponent(builder.newComponent("SizeBasedTriggeringPolicy").addAttribute("size", "500MB"));

        ComponentBuilder rolloverStrategy = builder.newComponent("DefaultRolloverStrategy")
                .addAttribute("max",rolloverStrategyMax);

        AppenderComponentBuilder fileComponentBuilder = builder.newAppender("rollingFile", "RollingFile")
                .addAttribute("fileName", logfile)
                .addAttribute("filePattern", logfile + "-%d{MM-dd-yy}-%i.log.gz")
                .addComponent(triggeringPolicy)
                .addComponent(rolloverStrategy);
        fileComponentBuilder.add(layoutComponentBuilder);
        rootLoggerComponentBuilder.add(builder.newAppenderRef("rollingFile"));
        return fileComponentBuilder;
    }

    private Configuration createConfiguration(final String name, ConfigurationBuilder<BuiltConfiguration> builder) {
        builder.setConfigurationName(name);
        LayoutComponentBuilder layoutComponentBuilder = builder.newLayout("PatternLayout")
                .addAttribute("charset","UTF-8")
                .addAttribute("pattern", "%d [%t] %-5level (%c{1}:%L): %msg%n%throwable");
        RootLoggerComponentBuilder rootLoggerComponentBuilder = builder.newRootLogger(rootLevel);
        if(stdoutEnable) {
            builder.add(console(builder,layoutComponentBuilder,rootLoggerComponentBuilder));
        }
        if(!rolling && logfile != null) {
            builder.add(file(builder,layoutComponentBuilder,rootLoggerComponentBuilder));
        }
        if(rolling && logfile != null) {
            builder.add(rollingFile(builder,layoutComponentBuilder,rootLoggerComponentBuilder));
        }
        builder.add(rootLoggerComponentBuilder);
        return builder.build();
    }


}
