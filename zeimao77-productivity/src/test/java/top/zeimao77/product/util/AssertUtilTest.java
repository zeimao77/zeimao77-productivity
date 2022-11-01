package top.zeimao77.product.util;

import top.zeimao77.product.main.BaseMain;

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AssertUtilTest extends BaseMain {


    public static void main(String[] args) {
        byte[] bs = "helloworld".getBytes(StandardCharsets.UTF_8);
        int i = 0;
        byte result = 0x00;
        for (byte b : bs) {
            i = (result + b) & 0x07;
            result ^= ((b >> (8 - i) | (b << i)));
        }
        logger.info("{}",result & 0x7F);

    }

}