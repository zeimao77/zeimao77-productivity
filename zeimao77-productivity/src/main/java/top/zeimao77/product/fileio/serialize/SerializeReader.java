package top.zeimao77.product.fileio.serialize;

import top.zeimao77.product.exception.BaseServiceRunException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static top.zeimao77.product.exception.ExceptionCodeDefinition.WRONG_ACTION;

public class SerializeReader {

    private ByteBuffer byteBuffer;

    public SerializeReader(ByteBuffer byteBuffer){
        this.byteBuffer = byteBuffer;
    }

    public SerializeReader() {}

    public void load(InputStream is) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buff = new byte[8192];
            int len;
            while((len = is.read(buff,0,8192)) != -1) {
                byteArrayOutputStream.write(buff,0,len);
            }
            buff = null;
            byteBuffer = ByteBuffer.wrap(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            throw new BaseServiceRunException(WRONG_ACTION,"读取错误",e);
        }
    }

    public SerializeReader(byte[] bs) {
        this.byteBuffer = ByteBuffer.wrap(bs);
    }

    public <T> T readNext(SerializeUtil.DataSerialize<T> dataSerialize) {
        byte type = SerializeUtil.nextType(byteBuffer);
        if(dataSerialize.canRead(type))
            return dataSerialize.read(byteBuffer);
        throw new BaseServiceRunException(WRONG_ACTION,"读取类型错误:" + type);
    }

    public byte[] nextBytes() {
        return readNext(SerializeUtil.BYTES_DATA_SERIALIZE);
    }

    public Character nextChar() {
        return readNext(SerializeUtil.CHARACTER_DATA_SERIALIZE);
    }

    public Float nextFloat() {
        return readNext(SerializeUtil.FLOAT_DATA_SERIALIZE);
    }

    public Double nextDouble() {
        return readNext(SerializeUtil.DOUBLE_DATA_SERIALIZE);
    }

    public String nextString() {
        return readNext(SerializeUtil.AUTOSTRING_DATA_SERIALIZE);
    }

    public String nextString(Charset charset) {
        SerializeUtil.AutoStringDataSerialize autoStringDataSerialize =
                new SerializeUtil.AutoStringDataSerialize(StandardCharsets.UTF_8, 1024);
        return readNext(autoStringDataSerialize);
    }

    public Short nextShort() {
        return readNext(SerializeUtil.SHORT_DATA_SERIALIZE);
    }

    public Integer nextInt32() {
        return readNext(SerializeUtil.INTEGER_DATA_SERIALIZE);
    }

    public Long nextInt64() {
        return readNext(SerializeUtil.LONG_DATA_SERIALIZE);
    }

    public Boolean nextBool() {
        return readNext(SerializeUtil.BOOLEAN_DATA_SERIALIZE);
    }

    public LocalTime nextTime() {
        return readNext(SerializeUtil.LOCAL_TIME_DATA_SERIALIZE);
    }

    public LocalDate nextDate() {
        return readNext(SerializeUtil.LOCAL_DATE_DATA_SERIALIZE);
    }

    public LocalDateTime nextDateTime() {
        return readNext(SerializeUtil.LOCAL_DATE_TIME_DATA_SERIALIZE);
    }

    public Set<Object> nextSet() {
        return readNext(SerializeUtil.SET_DATA_SERIALIZE);
    }

    public List<Object> nextList() {
        return readNext(SerializeUtil.LIST_DATA_SERIALIZE);
    }

    public Map<String,Object> nextMap() {
        return readNext(SerializeUtil.MAP_DATA_SERIALIZE);
    }

}
