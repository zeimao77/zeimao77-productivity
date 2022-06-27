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

@Plugin(name = "MyConfigurationFactory", category = ConfigurationFactory.CATEGORY)
@Order(2)
public class ConfigurationFacotry77 extends ConfigurationFactory {

    Level rootLevel;
    String logfile;
    boolean rolling;
    int rolloverStrategyMax;

    public ConfigurationFacotry77(){
        String logLevel = getPropertyOrEnv("log.level","DEBUG");
        this.rootLevel = Level.valueOf(logLevel);
        logfile = getPropertyOrEnv("log.file",null);
        String logRolling = getPropertyOrEnv("log.rolling", "FALSE");
        this.rolling = "TRUE".equalsIgnoreCase(logRolling) || "1".equals(logRolling) ? true : false;
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
        return new String[]{".77"};
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
    private AppenderComponentBuilder console(ConfigurationBuilder<BuiltConfiguration> builder, LayoutComponentBuilder layoutComponentBuilder){
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

    public AppenderComponentBuilder rollingFile(ConfigurationBuilder<BuiltConfiguration> builder,LayoutComponentBuilder layoutComponentBuilder){
        ComponentBuilder triggeringPolicy = builder.newComponent("Policies")
                .addComponent(builder.newComponent("CronTriggeringPolicy").addAttribute("schedule", "0 0 0 * * ?"))
                // .addComponent(builder.newComponent("TimeBasedTriggeringPolicy").addAttribute("interval",3))
                .addComponent(builder.newComponent("SizeBasedTriggeringPolicy").addAttribute("size", "500MB"));

        ComponentBuilder rolloverStrategy = builder.newComponent("DefaultRolloverStrategy")
                .addAttribute("max",rolloverStrategyMax);

        AppenderComponentBuilder fileComponentBuilder = builder.newAppender("rollingFile", "RollingFile")
                .addAttribute("fileName", logfile)
                .addAttribute("filePattern", logfile + "-%d{MM-dd-yy}-%i.log.gz")
                .addComponent(triggeringPolicy)
                .addComponent(rolloverStrategy);
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
        if(!rolling && logfile != null) {
            builder.add(file(builder,layoutComponentBuilder));
            rootLoggerComponentBuilder.add(builder.newAppenderRef("file"));
        }
        if(rolling && logfile != null) {
            builder.add(rollingFile(builder,layoutComponentBuilder));
            rootLoggerComponentBuilder.add(builder.newAppenderRef("rollingFile"));
        }
        builder.add(rootLoggerComponentBuilder);
        return builder.build();
    }


}
