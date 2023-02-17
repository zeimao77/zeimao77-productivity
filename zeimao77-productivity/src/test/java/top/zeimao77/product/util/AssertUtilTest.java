package top.zeimao77.product.util;


import top.zeimao77.product.main.BaseMain;

import java.io.*;

import java.util.*;
import java.util.concurrent.CountDownLatch;

public class AssertUtilTest extends BaseMain {

    public static void main(String[] args) throws IOException, NoSuchFieldException, IllegalAccessException, InterruptedException {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18);
        logger.info("{}",CollectionUtil.page(list,4,6));
        IdentityHashMap map = new IdentityHashMap();
        map.put(Long.valueOf(266),"abcdefg");
        map.put(Long.valueOf(266),"aabbccde");
        logger.info(map.get(Long.valueOf(266)));

        HashMap map1 = new HashMap<>();
        map1.put(Long.valueOf(266),"abcdefg");
        map1.put(Long.valueOf(266),"aabbccde");
        logger.info(map1.get(Long.valueOf(266)));

        Process generatePassword = Runtime.getRuntime().exec("id-generator");

        OutputStream outputStream = generatePassword.getOutputStream();
        PrintWriter printWriter = new PrintWriter(outputStream);
        InputStream inputStream = generatePassword.getInputStream();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        new Thread(() -> {
            String line;
            Scanner scanner = new Scanner(inputStream);
            while ((line = scanner.nextLine()) != null) {
                    System.out.println(line);
            }
        }).start();
        outputStream.write('\r');
        outputStream.write('\n');
        outputStream.flush();

        generatePassword.waitFor();


        printWriter.close();
        inputStream.close();

    }

}