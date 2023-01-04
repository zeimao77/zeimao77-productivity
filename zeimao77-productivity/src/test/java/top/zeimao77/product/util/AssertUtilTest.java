package top.zeimao77.product.util;

import sun.misc.Unsafe;
import top.zeimao77.product.fileio.serialize.SerializeUtil;
import top.zeimao77.product.fileio.serialize.SerializeWriter;
import top.zeimao77.product.fileio.serialize.Type;

import top.zeimao77.product.main.BaseMain;

import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;

public class AssertUtilTest extends BaseMain {


    public static void main(String[] args) throws IOException, NoSuchFieldException, IllegalAccessException {
        SerializeWriter writer = new SerializeWriter();
        String j = "{\"int_val\":123874,\"dec_val\":\"-1782.3450000000001223450\",\"dou_val\":11.1434,\"fki_val\":3.1415926,\"arr_val\":[1,\"hello\",33.14,true,'c',1.768,23,\"2022-01-04\"],\"hw_val\":\"helloworld\"}";
        logger.info("j:{}",j.length());
        HashMap<String, Object> map = new HashMap<>();
        map.put("int_val",123874);
        map.put("dou_val",11.14D);
        map.put("flo_val",3.1415926F);

        map.put("arr_val", Arrays.asList(1,"hello",33.14,true,'c',1.768F,23L, LocalDateTime.now()));
        map.put("hw_val","hellworld");
        map.put("dec_val","-1782.3450000000001223450");

        writer.writeInt32(123);
        writer.writeDouble(11.1434D);
        writer.writeFloat(3.1415926F);
        writer.writeList(Arrays.asList(1,"hello",33.14,true,'c',1.768F,23L, LocalDateTime.now()));
        writer.writeString("helloworld");
        BigDecimal decimal = new BigDecimal("-1782.3450000000001223450");
        writer.writeNext(SerializeUtil.BIGDECIMAL_DATA_SERIALIZE,decimal, Type.DECIMAL.getTypeValue());

        byte[] array = writer.array();
        logger.info("长度:{}",array.length);

        /**
        SerializeReader reader = new SerializeReader(array);
        Map<String, Object> stringObjectMap = reader.nextMap();
        stringObjectMap.forEach((o1,o2) -> {
            logger.info("{} : {}",o1,o2);
        });
         **/


        logger.info("有效数值长度:{}",decimal.precision());
        logger.info("小数点位置:{}",decimal.scale());

        Field intVal = BigDecimal.class.getDeclaredField("intVal");

        Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafe.setAccessible(true);
        Unsafe unsafe = (Unsafe)theUnsafe.get(null);
        long l = unsafe.objectFieldOffset(intVal);
        BigInteger iv = (BigInteger) unsafe.getObject(decimal, l);
        logger.info("{}",iv.toString());

        /**
        intVal.setAccessible(true);
        BigInteger bigInteger1 = (BigInteger) intVal.get(decimal);
         logger.info("{}",bigInteger1.toString());
         **/

        logger.info("字符串序列化长度:{}byte",decimal.toString().length() * 2);
        logger.info("decimal序列化长度:{}byte",1+2+iv.toByteArray().length);

        int scale = decimal.scale();
        BigDecimal bigDecimal = new BigDecimal(iv, scale);
        logger.info("{}",bigDecimal);

    }

}