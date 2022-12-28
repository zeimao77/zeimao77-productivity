package top.zeimao77.product.util;

import top.zeimao77.product.main.BaseMain;

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AssertUtilTest extends BaseMain {


    public static void main(String[] args) {
        String str = "";
        logger.info("{}",str != null && str.length() > 10
                ? str.substring(0,10):str);
        logger.info("{}",StringUtil.cut(str,10,StringUtil.EMPTY_SUFFIX));

    }

}