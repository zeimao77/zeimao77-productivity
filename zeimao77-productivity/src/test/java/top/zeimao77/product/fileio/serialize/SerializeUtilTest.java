package top.zeimao77.product.fileio.serialize;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.email.SimpleEmailSenderTest;
import top.zeimao77.product.main.BaseMain;
import top.zeimao77.product.util.JsonBeanUtil;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class SerializeUtilTest extends BaseMain {

    /**
     * 自定义的序列化相对于JSON序列化 可以大大节省空间与解析效率;但需要编写部分代码;
     * 相对于JSON序列化，压缩空间主要来源于:
     * 1. 字段名
     * 2. 对数字、布尔、小数、日期时间等都有不错的压缩效果
     * 3. 对于长的字符串，可以进行压缩
     * 通常情况下，该序列化后的结果只有JSON的 1/2 - 2/3 压缩效果比较明显;
     */
    @Test
    public void serialize() {
        Map<String,Object> param = new HashMap<>();
        param.put("int_val",32768);
        param.put("dou_val",3.1415926D);
        param.put("lon_val",1583377212355130008L);
        param.put("dec_val", new BigDecimal("123.141592671621111"));
        param.put("bol_val",true);
        param.put("nul_val",null);
        param.put("sho_val",32767);
        param.put("now_val",LocalDateTime.now());
        param.put("str_val","fdmode:3,popzkfd:0.0,fdresult:null,modifytype:null,end_date:2021-12-31,sta_date:2020-12-09,end_time:23:59,sta_time:00:00");
        param.put("arr", Arrays.asList(1234,34.57D,true));
        String s = JsonBeanUtil.DEFAULT.toJsonString(param);

        logger.info("JSON序列化后的长度:{}",s.getBytes(StandardCharsets.UTF_8).length);   //332
        SerializeWriter writer = new SerializeWriter();
        writer.writeInt32(32768);
        writer.writeDouble(3.1415926D);
        writer.writeInt64(1583377212355130008L);
        writer.writeNext(SerializeUtil.BIGDECIMAL_DATA_SERIALIZE,new BigDecimal("123.141592671621111"),Type.DECIMAL.getTypeValue());
        writer.writeBool(true);
        writer.writeNull();
        writer.writeShort((short) 32767);
        writer.writeList(Arrays.asList(1234,34.57D,true));
        writer.writeNext(SerializeUtil.LOCAL_DATE_TIME_DATA_SERIALIZE,LocalDateTime.now(),Type.DATETIME.getTypeValue());
        writer.writeString("fdmode:3,popzkfd:0.0,fdresult:null,modifytype:null,end_date:2021-12-31,sta_date:2020-12-09,end_time:23:59,sta_time:00:00");
        logger.info("自定义序列化后的长度:{}",writer.size());     // 197

        SerializeReader reader = new SerializeReader(writer.array());
        logger.info("读取数据:{}",reader.nextInt32());
        logger.info("读取数据:{}",reader.nextDouble());
        logger.info("读取数据:{}",reader.nextInt64());
        logger.info("读取数据:{}",reader.readNext(SerializeUtil.BIGDECIMAL_DATA_SERIALIZE));
        logger.info("读取数据:{}",reader.nextBool());
        logger.info("读取数据:{}",reader.nextString());
        logger.info("读取数据:{}",reader.nextShort());
        logger.info("读取数据:{}",reader.nextList());
        logger.info("读取数据:{}",reader.readNext(SerializeUtil.LOCAL_DATE_TIME_DATA_SERIALIZE));
        logger.info("读取数据:{}",reader.nextString());

    }

    /**
     * 字符串压缩效果对比
     */
    @Test
    public void testZipString() {
        String str = SimpleEmailSenderTest.MAIL_BODY;
        SerializeUtil.StringDataSerialize stringDataSerialize = new SerializeUtil.StringDataSerialize(StandardCharsets.UTF_8);
        ByteBuffer byteBuffer = ByteBuffer.allocate(4096);
        stringDataSerialize.write(byteBuffer,str);
        logger.info("一般序列化长字符串长度:{}",byteBuffer.position());   // 1679
        SerializeUtil.ZipStringDataSerialize zipStringDataSerialize = new SerializeUtil.ZipStringDataSerialize(StandardCharsets.UTF_8);
        byteBuffer = ByteBuffer.allocate(4096);
        zipStringDataSerialize.write(byteBuffer,str);
        logger.info("压缩序列化长字符串长度:{}",byteBuffer.position());   // 1052
    }




}