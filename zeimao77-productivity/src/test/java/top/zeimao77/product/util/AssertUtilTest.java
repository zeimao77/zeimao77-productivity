package top.zeimao77.product.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zeimao77.product.main.BaseMain;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AssertUtilTest extends BaseMain {

    private static Logger logger = LoggerFactory.getLogger(AssertUtilTest.class);

    public static void main(String[] args) throws IOException, NoSuchFieldException, IllegalAccessException, InterruptedException {
        BaseMain.showBanner("1.0.1");
        String s = "omni:paymentmethodref:005:3318";
        Pattern pattern = Pattern.compile("omni:paymentmethodref:(\\S+):(\\S+)");
        Matcher matcher = pattern.matcher(s);
        if(matcher.matches()) {
            String erpCode = matcher.group(1);
            String payCode = matcher.group(2);
            logger.info("{},{}",erpCode,payCode);
        }

    }

}