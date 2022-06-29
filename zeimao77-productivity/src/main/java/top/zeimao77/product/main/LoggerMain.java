package top.zeimao77.product.main;

import org.apache.logging.log4j.core.config.plugins.util.PluginManager;

public interface LoggerMain {

    static void initLoggerConfig(){
        PluginManager.addPackage("top.zeimao77.product.log4j2");
    }

}
