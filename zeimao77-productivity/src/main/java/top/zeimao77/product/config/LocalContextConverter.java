package top.zeimao77.product.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zeimao77.product.converter.AbstractNonReFreshConverter;
import top.zeimao77.product.exception.BaseServiceRunException;
import java.io.*;
import java.util.Properties;

import static top.zeimao77.product.exception.ExceptionCodeDefinition.IOEXCEPTION;
import static top.zeimao77.product.exception.ExceptionCodeDefinition.WRONG_SOURCE;

public class LocalContextConverter extends AbstractNonReFreshConverter<String> {

    String localContextFile;
    String localContextActive;

    private static Logger logger = LoggerFactory.getLogger(LocalContextConverter.class);

    public LocalContextConverter(String localContextFile, String localContextActive) {
        this.localContextFile = localContextFile;
        this.localContextActive = localContextActive;
    }

    @Override
    protected void refresh() {
        String localContextFile = LocalContext.getPropertyOrEnv("local.context.file",null);
        String localContextActive = LocalContext.getPropertyOrEnv("local.context.active",null);
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
                putByProperties(fileInputStream);
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
                resourceAsStream = LocalContext.class.getClassLoader().getResourceAsStream("localcontext.properties");
                if(resourceAsStream != null)
                    logger.debug("尝试加载配置文件:localcontext.properties");
            } else {
                resourceAsStream = LocalContext.class.getClassLoader().getResourceAsStream("localcontext-"+localContextActive + ".properties");
                if(resourceAsStream != null)
                    logger.debug("尝试加载配置文件:localcontext-{}.properties",localContextActive);
            }
            if(resourceAsStream != null) {
                putByProperties(resourceAsStream);
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

    private void putByProperties(InputStream is) {
        Properties properties = new Properties();
        try {
            properties.load(is);
        } catch (IOException e) {
            throw new BaseServiceRunException(IOEXCEPTION,"IO错误");
        }
        for (Object o : properties.keySet()) {
            String k = o.toString();
            Object v = properties.get(o);
            addConvRule(k,v);
        }
    }

}
