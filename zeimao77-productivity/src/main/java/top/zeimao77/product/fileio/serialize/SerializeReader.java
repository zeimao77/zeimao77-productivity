package top.zeimao77.product.fileio.serialize;

import top.zeimao77.product.exception.BaseServiceRunException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.*;

import static top.zeimao77.product.exception.ExceptionCodeDefinition.WRONG_ACTION;

public class SerializeReader {

    private ByteBuffer byteBuffer;

    public SerializeReader(ByteBuffer byteBuffer){
        this.byteBuffer = byteBuffer;
    }

    public SerializeReader(InputStream is) {
        try {
            byteBuffer = ByteBuffer.wrap(is.readAllBytes());
        } catch (IOException e) {
            throw new BaseServiceRunException(WRONG_ACTION,"读取错误");
        }
    }

    public SerializeReader(byte[] bs) {
        this.byteBuffer = ByteBuffer.wrap(bs);
    }

    public byte[] nextBytes() {
        byte b = byteBuffer.get();
        if(b == Type.NULL.getTypeValue())
            return null;
        else if(b == Type.BYTES.getTypeValue()) {
            int length = byteBuffer.getInt();
            byte[] res = new byte[length];
            byteBuffer.get(res,0,length);
            return res;
        }
        throw new BaseServiceRunException(WRONG_ACTION,"读取错误,非字节数组类型:" + b);
    }

    public Character nextChar() {
        byte b = byteBuffer.get();
        if(b == Type.NULL.getTypeValue())
            return null;
        else if(b == Type.CHAR.getTypeValue()) {
            return byteBuffer.getChar();
        }
        throw new BaseServiceRunException(WRONG_ACTION,"读取错误,非字符类型:" + b);
    }

    public Type nextType() {
        byteBuffer.mark();
        byte b = byteBuffer.get();
        byteBuffer.reset();
        return Type.parse(b);
    }

    public Float nextFloat() {
        byte b = byteBuffer.get();
        if(b == Type.NULL.getTypeValue())
            return null;
        else if(b == Type.FLOAT.getTypeValue()) {
            return byteBuffer.getFloat();
        }
        throw new BaseServiceRunException(WRONG_ACTION,"读取错误,非单精度类型:" + b);
    }

    public Double nextDouble() {
        byte b = byteBuffer.get();
        if(b == Type.NULL.getTypeValue())
            return null;
        else if(b == Type.DOUBLE.getTypeValue()) {
            return byteBuffer.getDouble();
        }
        throw new BaseServiceRunException(WRONG_ACTION,"读取错误,非双精度类型:" + b);
    }

    public String nextString() {
        return nextString(StandardCharsets.UTF_8);
    }

    public String nextString(Charset charset) {
        byte b = byteBuffer.get();
        if(b == Type.NULL.getTypeValue())
            return null;
        else if(b == Type.STRING.getTypeValue()) {
            int anInt = byteBuffer.getInt();
            byte[] bs = new byte[anInt];
            byteBuffer.get(bs,0,anInt);
            return new String(bs,charset);
        }
        throw new BaseServiceRunException(WRONG_ACTION,"读取错误,非字符串类型:" + b);
    }

    public Short nextShort() {
        byte b = byteBuffer.get();
        if(b == Type.NULL.getTypeValue())
            return null;
        else if(b == Type.SHORT.getTypeValue()) {
            return byteBuffer.getShort();
        }
        throw new BaseServiceRunException(WRONG_ACTION,"读取错误,非INT32类型:" + b);
    }

    public Integer nextInt32() {
        byte b = byteBuffer.get();
        if(b == Type.NULL.getTypeValue())
            return null;
        else if(b == Type.INT32.getTypeValue()) {
            return byteBuffer.getInt();
        }
        throw new BaseServiceRunException(WRONG_ACTION,"读取错误,非INT32类型:" + b);
    }

    public Long nextInt64() {
        byte b = byteBuffer.get();
        if(b == Type.NULL.getTypeValue())
            return null;
        else if(b == Type.INT64.getTypeValue()) {
            return byteBuffer.getLong();
        }
        throw new BaseServiceRunException(WRONG_ACTION,"读取错误,非INT64类型:" + b);
    }

    public Boolean nextBool() {
        byte b = byteBuffer.get();
        if(b == Type.NULL.getTypeValue())
            return null;
        else if(b == Type.BOOL.getTypeValue()) {
            byte bool = byteBuffer.get();
            return bool == 0x00 ? false : true;
        }
        throw new BaseServiceRunException(WRONG_ACTION,"读取错误,非BOOL类型:" + b);
    }

    public LocalTime nextTime() {
        byte b = byteBuffer.get();
        if(b == Type.NULL.getTypeValue())
            return null;
        else if(b == Type.TIME.getTypeValue()) {
            int anInt = byteBuffer.getInt();
            LocalTime localTime = LocalTime.ofSecondOfDay(anInt);
            return localTime;
        }
        throw new BaseServiceRunException(WRONG_ACTION,"读取错误,非时间类型:" + b);
    }

    public LocalDate nextDate() {
        byte b = byteBuffer.get();
        if(b == Type.NULL.getTypeValue())
            return null;
        else if(b == Type.DATE.getTypeValue()) {
            long aLong = byteBuffer.getLong();
            LocalDate localDate = LocalDate.ofEpochDay(aLong);
            return localDate;
        }
        throw new BaseServiceRunException(WRONG_ACTION,"读取错误,非日期类型:" + b);
    }

    public LocalDateTime nextDateTime() {
        byte b = byteBuffer.get();
        if(b == Type.NULL.getTypeValue())
            return null;
        else if(b == Type.DATETIME.getTypeValue()) {
            long aLong = byteBuffer.getLong();
            LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(aLong, 0, ZoneOffset.UTC);
            return localDateTime;
        }
        throw new BaseServiceRunException(WRONG_ACTION,"读取错误,非日期时间类型:" + b);
    }

    private void readList(int size,Collection<Object> coll) {
        for (int i = 0; i < size; i++) {
            switch (nextType()) {
                case NULL -> coll.add(null);
                case STRING -> coll.add(nextString());
                case BOOL -> coll.add(nextBool());
                case INT32 -> coll.add(nextInt32());
                case INT64 -> coll.add(nextInt64());
                case DOUBLE -> coll.add(nextDouble());
                case SHORT -> coll.add(nextShort());
                case FLOAT -> coll.add(nextFloat());
                case CHAR -> coll.add(nextChar());
                case DATE -> coll.add(nextDate());
                case TIME -> coll.add(nextTime());
                case DATETIME -> coll.add(nextDateTime());
                case ARRAY -> coll.add(nextList());
                case SET -> coll.add(nextSet());
                case BYTES -> coll.add(nextBytes());
                case MAP -> coll.add(nextMap());
            }
        }
    }

    public HashSet<Object> nextSet() {
        byte b = byteBuffer.get();
        if(b == Type.NULL.getTypeValue())
            return null;
        else if(b == Type.ARRAY.getTypeValue() || b == Type.SET.getTypeValue()) {
            int size = byteBuffer.getInt();
            HashSet<Object> res = new HashSet<>(size);
            readList(size,res);
            return res;
        }
        throw new BaseServiceRunException(WRONG_ACTION,"读取错误,非日期时间类型:" + b);
    }

    public List<Object> nextList() {
        byte b = byteBuffer.get();
        if(b == Type.NULL.getTypeValue())
            return null;
        else if(b == Type.ARRAY.getTypeValue() || b == Type.SET.getTypeValue()) {
            int size = byteBuffer.getInt();
            ArrayList<Object> res = new ArrayList<>(size);
            readList(size,res);
            return res;
        }
        throw new BaseServiceRunException(WRONG_ACTION,"读取错误,非日期时间类型:" + b);
    }

    public Map<String,Object> nextMap() {
        byte b = byteBuffer.get();
        if(b == Type.NULL.getTypeValue())
            return null;
        else if(b == Type.MAP.getTypeValue()) {
            int size = byteBuffer.getInt();
            HashMap<String, Object> res = new HashMap<>(size);
            for (int i = 0; i < size; i++) {
                String key = nextString();
                Type type = nextType();
                switch (type) {
                    case STRING -> res.put(key,nextString());
                    case BOOL -> res.put(key,nextBool());
                    case INT32 -> res.put(key,nextInt32());
                    case INT64 -> res.put(key,nextInt64());
                    case DOUBLE -> res.put(key,nextDouble());
                    case SHORT -> res.put(key,nextShort());
                    case FLOAT -> res.put(key,nextFloat());
                    case CHAR -> res.put(key,nextChar());
                    case DATE -> res.put(key,nextDate());
                    case TIME -> res.put(key,nextTime());
                    case DATETIME -> res.put(key,nextDateTime());
                    case ARRAY -> res.put(key,nextList());
                    case SET -> res.put(key,nextSet());
                    case BYTES -> res.put(key,nextBytes());
                    case MAP -> res.put(key,nextMap());
                }
            }
            return res;
        }
        throw new BaseServiceRunException(WRONG_ACTION,"读取错误,非对象数据类型:" + b);
    }



}
