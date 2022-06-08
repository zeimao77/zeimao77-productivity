package top.zeimao77.product.config;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Order;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.LayoutComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.RootLoggerComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.apache.logging.log4j.core.config.plugins.Plugin;

import java.net.URI;

/**
 * @author zeimao77
 * 如果需要使用此文件配置，需要在Log加载之前手动加载
 * ConfigurationFactory.setConfigurationFactory(new MyConfigurationFactory(...));
 * 它仅仅可以定义日志级别以及日志文件位置，文件以及标准输出流将使用相同的日志级别
 */
@Plugin(name = "MyConfigurationFactory", category = ConfigurationFactory.CATEGORY)
@Order(50)
public class MyConfigurationFactory extends ConfigurationFactory {

    private Level rootLevel;
    private String logfile;

    /**
     * 配置Log4j2
     * @param rootLevel 日志级别
     */
    public MyConfigurationFactory(Level rootLevel) {
        this.rootLevel = rootLevel;
    }

    /**
     * 配置Log4j2
     * @param rootLevel 日志级别
     * @param logfile 日志文件
     */
    public MyConfigurationFactory(Level rootLevel,String logfile) {
        this.rootLevel = rootLevel;
        this.logfile = logfile;
    }

    /**
     * 配置标准输出日志
     * @param builder --
     * @param layoutComponentBuilder --
     * @return --
     */
    private AppenderComponentBuilder console(ConfigurationBuilder<BuiltConfiguration> builder,LayoutComponentBuilder layoutComponentBuilder){
        AppenderComponentBuilder consoleComponentBuilder = builder.newAppender("stdout", "CONSOLE").
                addAttribute("target", ConsoleAppender.Target.SYSTEM_OUT);
        consoleComponentBuilder.add(layoutComponentBuilder);
        return consoleComponentBuilder;
    }

    /**
     * 配置文件日志
     * @param builder --
     * @param layoutComponentBuilder --
     * @return --
     */
    public AppenderComponentBuilder file(ConfigurationBuilder<BuiltConfiguration> builder,LayoutComponentBuilder layoutComponentBuilder){
        AppenderComponentBuilder fileComponentBuilder = builder.newAppender("file", "FILE")
                .addAttribute("fileName", logfile);
        fileComponentBuilder.add(layoutComponentBuilder);
        return fileComponentBuilder;
    }

    private Configuration createConfiguration(final String name, ConfigurationBuilder<BuiltConfiguration> builder) {
        builder.setConfigurationName(name);
        LayoutComponentBuilder layoutComponentBuilder = builder.newLayout("PatternLayout").
                addAttribute("pattern", "%d [%t] %-5level: %msg%n%throwable");
        builder.add(console(builder,layoutComponentBuilder));
        RootLoggerComponentBuilder rootLoggerComponentBuilder = builder.newRootLogger(rootLevel);
        rootLoggerComponentBuilder.add(builder.newAppenderRef("stdout"));
        if(logfile != null) {
            builder.add(file(builder,layoutComponentBuilder));
            rootLoggerComponentBuilder.add(builder.newAppenderRef("file"));
        }
        builder.add(rootLoggerComponentBuilder);
        return builder.build();
    }

    @Override
    protected String[] getSupportedTypes() {
        return new String[] {"*"};
    }

    @Override
    public Configuration getConfiguration(final LoggerContext loggerContext, final ConfigurationSource source) {
        return getConfiguration(loggerContext, source.toString(), null);
    }

    @Override
    public Configuration getConfiguration(final LoggerContext loggerContext, final String name, final URI configLocation) {
        ConfigurationBuilder<BuiltConfiguration> builder = newConfigurationBuilder();
        return createConfiguration(name, builder);
    }

}
