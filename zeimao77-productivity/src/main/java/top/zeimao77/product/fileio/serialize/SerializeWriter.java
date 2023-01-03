package top.zeimao77.product.fileio.serialize;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.*;

public class SerializeWriter {

    private static Logger logger = LogManager.getLogger(SerializeWriter.class);

    private ByteBuffer byteBuffer;

    public SerializeWriter(ByteBuffer byteBuffer) {
        this.byteBuffer = byteBuffer;
    }

    public SerializeWriter() {
        byteBuffer = ByteBuffer.allocate(512);
    }

    public void write(Short s){ writeShort(s);}
    public void write(Boolean b) { writeBool(b);}
    public void write(Integer i) { writeInt32(i);}
    public void write(Float f) { writeFloat(f);}
    public void write(Long i) { writeInt64(i);}
    public void write(Double d) { writeDouble(d);}
    public void write(String str) { writeString(str);}
    public void write(LocalDate d) { writeDate(d);}
    public void write(LocalTime t) { writeTime(t);}
    public void write(LocalDateTime d) { writeDateTime( d);}
    public void write(Map<String,Object> m) {}
    public void write(Set<Object> s) {}
    public void write(List<Object> l) {}

    public void writeObject(Object obj) {
        if(obj == null)
            writeNull();
        else if(obj instanceof String t)
            writeString(t);
        else if(obj instanceof Integer t)
            writeInt32(t);
        else if(obj instanceof Long t)
            writeInt64(t);
        else if(obj instanceof Double t)
            writeDouble(t);
        else if(obj instanceof Boolean t)
            writeBool(t);
        else if(obj instanceof LocalDateTime t)
            writeDateTime(t);
        else if(obj instanceof LocalDate t)
            writeDate(t);
        else if(obj instanceof LocalTime t)
            writeTime(t);
        else if(obj instanceof Character t)
            writeChar(t);
        else if(obj instanceof Float t)
            writeFloat(t);
        else if(obj instanceof Collection t) {
            writeList(t);
        } else if(obj instanceof Map t) {
            writeMap(t);
        }
    }

    public void writeBytes(byte[] bs) {
        if(bs == null)
            byteBuffer.put(Type.NULL.getTypeValue());
        else {
            byteBuffer.put(Type.BYTES.getTypeValue());
            byteBuffer.putInt(bs.length);
            byteBuffer.put(bs,0,bs.length);
        }
    }

    /**
     * 自定义数据类型;
     * @param type 数据类型
     * @param bs 数据
     */
    public void writeBytes(byte type,byte[] bs) {
        if(bs == null)
            byteBuffer.put(Type.NULL.getTypeValue());
        else {
            byteBuffer.put(type);
            byteBuffer.putInt(bs.length);
            byteBuffer.put(bs,0,bs.length);
        }
    }

    public void writeChar(Character c) {
        if(c == null)
            byteBuffer.put(Type.NULL.getTypeValue());
        else {
            byteBuffer.put(Type.CHAR.getTypeValue());
            byteBuffer.putChar(c);
        }
    }

    public void writeShort(Short sho) {
        if(sho == null)
            byteBuffer.put(Type.NULL.getTypeValue());
        else {
            byteBuffer.put(Type.SHORT.getTypeValue());
            byteBuffer.putShort(sho);
        }
    }

    public void writeInt32(Integer val) {
        if(val == null)
            byteBuffer.put(Type.NULL.getTypeValue());
        else {
            byteBuffer.put(Type.INT32.getTypeValue());
            byteBuffer.putInt(val);
        }
    }

    public void writeInt64(Long val) {
        if(val == null)
            byteBuffer.put(Type.NULL.getTypeValue());
        else {
            byteBuffer.put(Type.INT64.getTypeValue());
            byteBuffer.putLong(val);
        }
    }

    public void writeNull() {
        byteBuffer.put(Type.NULL.getTypeValue());
    }

    public void writeBool(Boolean bool) {
        if(bool == null)
            byteBuffer.put(Type.NULL.getTypeValue());
        else {
            byteBuffer.put(Type.BOOL.getTypeValue());
            byteBuffer.put(bool ? (byte) 0x01 : (byte) 0x00);
        }
    }

    public void writeTime(LocalTime time) {
        if(time == null)
            byteBuffer.put(Type.NULL.getTypeValue());
        else {
            byteBuffer.put(Type.TIME.getTypeValue());
            int i = time.toSecondOfDay();
            byteBuffer.putInt(i);
        }
    }

    public void writeDate(LocalDate date) {
        if(date == null)
            byteBuffer.put(Type.NULL.getTypeValue());
        else {
            byteBuffer.put(Type.DATE.getTypeValue());
            long i = date.toEpochDay();
            byteBuffer.putLong(i);
        }
    }

    public void writeDateTime(LocalDateTime dateTime) {
        if(dateTime == null)
            byteBuffer.put(Type.NULL.getTypeValue());
        else {
            byteBuffer.put(Type.DATETIME.getTypeValue());
            LocalDateTime now = LocalDateTime.now();
            long l = now.toEpochSecond(ZoneOffset.UTC);
            byteBuffer.putLong(l);
        }
    }

    public void writeDouble(Double dou) {
        if(dou == null)
            byteBuffer.put(Type.NULL.getTypeValue());
        else {
            byteBuffer.put(Type.DOUBLE.getTypeValue());
            byteBuffer.putDouble(dou);
        }
    }

    public void writeFloat(Float flo) {
        if(flo == null)
            byteBuffer.put(Type.NULL.getTypeValue());
        else {
            byteBuffer.put(Type.FLOAT.getTypeValue());
            byteBuffer.putFloat(flo);
        }
    }

    public void writeString(String str) {
        writeString(str,StandardCharsets.UTF_8);
    }

    public void writeString(String str, Charset charset) {
        if(str == null)
            byteBuffer.put(Type.NULL.getTypeValue());
        else {
            byteBuffer.put(Type.STRING.getTypeValue());
            byte[] bytes = str.getBytes(charset);
            byteBuffer.putInt(bytes.length);
            byteBuffer.put(bytes);
        }
    }

    public void writeMap(Map<String,Object> map) {
        if(map == null)
            byteBuffer.put(Type.NULL.getTypeValue());
        else {
            byteBuffer.put(Type.MAP.getTypeValue());
            byteBuffer.putInt(map.size());
            for (String s : map.keySet()) {
                writeString(s);
                Object o = map.get(s);
                writeObject(o);
            }
        }
    }

    public void writeList(Collection<Object> list) {
        if(list == null)
            byteBuffer.put(Type.NULL.getTypeValue());
        else {
            if(list instanceof Set)
                byteBuffer.put(Type.SET.getTypeValue());
            else
                byteBuffer.put(Type.ARRAY.getTypeValue());
            byteBuffer.putInt(list.size());
            for (Object o : list) {
                writeObject(o);
            }
        }
    }

    public byte[] array() {
        return Arrays.copyOfRange(byteBuffer.array(), 0, byteBuffer.position());
    }


}
