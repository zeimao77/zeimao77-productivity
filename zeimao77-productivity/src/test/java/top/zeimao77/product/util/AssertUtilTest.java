package top.zeimao77.product.util;

import sun.misc.Unsafe;
import top.zeimao77.product.fileio.serialize.SerializeUtil;
import top.zeimao77.product.fileio.serialize.SerializeWriter;
import top.zeimao77.product.fileio.serialize.Type;

import top.zeimao77.product.main.BaseMain;
import top.zeimao77.product.mysql.DemoModel;

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

        Field demoId = DemoModel.class.getDeclaredField("demoId");
        demoId.setAccessible(true);


         

        logger.info("字符串序列化长度:{}byte",decimal.toString().length() * 2);
        logger.info("decimal序列化长度:{}byte",1+2+iv.toByteArray().length);
        logger.info("{}",BeanUtil.getProperty(decimal,"intVal"));

        int scale = decimal.scale();
        BigDecimal bigDecimal = new BigDecimal(iv, scale);
        logger.info("{}",bigDecimal);

    }

}