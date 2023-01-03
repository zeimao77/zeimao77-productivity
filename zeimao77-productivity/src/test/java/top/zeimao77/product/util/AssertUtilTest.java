package top.zeimao77.product.util;

import top.zeimao77.product.fileio.serialize.SerializeReader;
import top.zeimao77.product.fileio.serialize.SerializeWriter;
import top.zeimao77.product.main.BaseMain;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AssertUtilTest extends BaseMain {


    public static void main(String[] args) throws IOException {
        SerializeWriter writer = new SerializeWriter();
        HashMap<String, Object> map = new HashMap<>();
        map.put("int",123);

        map.put("arr", Arrays.asList(1,"hello",33.14,true,'c',1.768F,23L, LocalDateTime.now()));
        map.put("hw","hellworld");
        writer.writeMap(map);

        byte[] array = writer.array();
        logger.info("长度:{}",array.length);

        SerializeReader reader = new SerializeReader(array);
        Map<String, Object> stringObjectMap = reader.nextMap();
        stringObjectMap.forEach((o1,o2) -> {
            logger.info("{} : {}",o1,o2);
        });

    }

}