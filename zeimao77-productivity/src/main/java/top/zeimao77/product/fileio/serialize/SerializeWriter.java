package top.zeimao77.product.fileio.serialize;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zeimao77.product.exception.BaseServiceRunException;
import top.zeimao77.product.exception.ExceptionCodeDefinition;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class SerializeWriter {

    private static Logger logger = LoggerFactory.getLogger(SerializeWriter.class);

    private ByteBuffer byteBuffer;

    public SerializeWriter(ByteBuffer byteBuffer) {
        this.byteBuffer = byteBuffer;
    }

    /**
     * 由于自身不支持扩容
     * 容量的分配要特别注意分配充足
     * @param capacity
     */
    public SerializeWriter(int capacity) {
        byteBuffer = ByteBuffer.allocate(capacity);
    }

    public SerializeWriter() {
        this(1024);
    }

    public <T> void writeNext(SerializeUtil.DataSerialize<T> dataSerialize,T t,byte type){
        if(dataSerialize.canWrite(t,type)) {
            dataSerialize.write(byteBuffer,t);
        }
    }

    public void writeBytes(byte[] bs) {
        SerializeUtil.BYTES_DATA_SERIALIZE.write(byteBuffer,bs);
    }

    public void writeChar(Character c) {
        SerializeUtil.CHARACTER_DATA_SERIALIZE.write(byteBuffer,c);
    }

    public void writeShort(Short sho) {
        SerializeUtil.SHORT_DATA_SERIALIZE.write(byteBuffer,sho);
    }

    public void writeInt32(Integer val) {
        SerializeUtil.INTEGER_DATA_SERIALIZE.write(byteBuffer,val);
    }

    public void writeInt64(Long val) {
        SerializeUtil.LONG_DATA_SERIALIZE.write(byteBuffer,val);
    }

    public void writeNull() {
        SerializeUtil.VOID_DATA_SERIALIZER.write(byteBuffer,null);
    }

    public void writeBool(Boolean bool) {
        SerializeUtil.BOOLEAN_DATA_SERIALIZE.write(byteBuffer,bool);
    }

    public void writeTime(LocalTime time) {
        SerializeUtil.LOCAL_TIME_DATA_SERIALIZE.write(byteBuffer,time);
    }

    public void writeDate(LocalDate date) {
        SerializeUtil.LOCAL_DATE_DATA_SERIALIZE.write(byteBuffer,date);
    }

    public void writeDateTime(LocalDateTime dateTime) {
        SerializeUtil.LOCAL_DATE_TIME_DATA_SERIALIZE.write(byteBuffer,dateTime);
    }

    public void writeDouble(Double dou) {
        SerializeUtil.DOUBLE_DATA_SERIALIZE.write(byteBuffer,dou);
    }

    public void writeFloat(Float flo) {
        SerializeUtil.FLOAT_DATA_SERIALIZE.write(byteBuffer,flo);
    }

    public void writeString(String str) {
        SerializeUtil.AUTOSTRING_DATA_SERIALIZE.write(byteBuffer,str);
    }

    public void writeString(String str, Charset charset) {
        SerializeUtil.AutoStringDataSerialize autoStringDataSerialize = new SerializeUtil.AutoStringDataSerialize(charset,1024);
        autoStringDataSerialize.write(byteBuffer,str);
    }

    public void writeMap(Map<String,Object> map) {
        SerializeUtil.MAP_DATA_SERIALIZE.write(byteBuffer,map);
    }

    public void writeList(List<Object> list) {
        SerializeUtil.LIST_DATA_SERIALIZE.write(byteBuffer,list);
    }

    public byte[] array() {
        return Arrays.copyOfRange(byteBuffer.array(), 0, byteBuffer.position());
    }

    public void write(OutputStream os) {
        try {
            os.write(byteBuffer.array(),0,byteBuffer.position());
        } catch (IOException e) {
            throw new BaseServiceRunException(ExceptionCodeDefinition.IOEXCEPTION,"IO错误:"+e.getMessage(),e);
        }
    }

    public int size() {
        return byteBuffer.position();
    }

}
