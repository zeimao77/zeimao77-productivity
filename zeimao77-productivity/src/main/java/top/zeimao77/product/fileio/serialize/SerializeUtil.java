package top.zeimao77.product.fileio.serialize;

import sun.misc.Unsafe;
import top.zeimao77.product.util.ByteArrayCoDesUtil;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.*;

public class SerializeUtil {

    public interface DataSerialize<T> {
        boolean canRead(byte type);
        boolean canWrite(Object t,byte type);
        T read(ByteBuffer byteBuffer);
        void write(ByteBuffer byteBuffer,T t);
    }

    public static byte nextType(ByteBuffer byteBuffer) {
        byteBuffer.mark();
        byte b = byteBuffer.get();
        byteBuffer.reset();
        return b;
    }

    public static class StringDataSerialize implements DataSerialize<String>{

        private Charset charset = StandardCharsets.UTF_8;

        public StringDataSerialize(Charset charset) {
            this.charset = charset;
        }

        public StringDataSerialize() {}

        @Override
        public boolean canRead(byte type) {
            return Type.NULL.getTypeValue() == type || Type.MAP.getTypeValue() == type;
        }

        @Override
        public boolean canWrite(Object t,byte type) {
            return (t instanceof String) && (Type.STRING.getTypeValue() == type);
        }

        @Override
        public String read(ByteBuffer byteBuffer) {
            byte b = byteBuffer.get();
            if(b == Type.STRING.getTypeValue()) {
                int anInt = byteBuffer.getInt();
                byte[] bs = new byte[anInt];
                byteBuffer.get(bs,0,anInt);
                return new String(bs, charset);
            }
            return null;
        }

        @Override
        public void write(ByteBuffer byteBuffer, String val) {
            if(val == null)
                byteBuffer.put(Type.NULL.getTypeValue());
            else {
                byteBuffer.put(Type.STRING.getTypeValue());
                byte[] bytes = val.getBytes(charset);
                byteBuffer.putInt(bytes.length);
                byteBuffer.put(bytes);
            }
        }
    }
    public static class ZipStringDataSerialize implements DataSerialize<String> {

        private Charset byteset;

        public ZipStringDataSerialize(Charset byteset) {
            this.byteset = byteset;
        }

        public ZipStringDataSerialize() {
            this.byteset = StandardCharsets.UTF_8;
        }

        @Override
        public boolean canRead(byte type) {
            return Type.NULL.getTypeValue() == type || Type.ZIPSTRING.getTypeValue() == type;
        }

        @Override
        public boolean canWrite(Object t, byte type) {
            return t instanceof String;
        }

        @Override
        public String read(ByteBuffer byteBuffer) {
            byte b = byteBuffer.get();
            if(b == Type.ZIPSTRING.getTypeValue()) {
                int anInt = byteBuffer.getInt();
                byte[] bs = new byte[anInt];
                byteBuffer.get(bs,0,anInt);
                byte[] zipDecode = ByteArrayCoDesUtil.zipDecode(bs);
                return new String(zipDecode,byteset);
            }
            return null;
        }

        @Override
        public void write(ByteBuffer byteBuffer, String val) {
            if(val == null)
                byteBuffer.put(Type.NULL.getTypeValue());
            else {
                byteBuffer.put(Type.ZIPSTRING.getTypeValue());
                byte[] bytes = val.getBytes(byteset);
                byte[] zipDecode = ByteArrayCoDesUtil.zipDecode(bytes);
                byteBuffer.putInt(zipDecode.length);
                byteBuffer.put(zipDecode);
            }
        }
    }
    public static class AutoStringDataSerialize implements DataSerialize<String> {

        private int zipSize;
        private Charset charset;
        private StringDataSerialize stringDataSerialize;
        private ZipStringDataSerialize zipStringDataSerialize;

        AutoStringDataSerialize(Charset charset,int zipSize) {
            this.charset = charset;
            this.zipSize = zipSize;
        }

        @Override
        public boolean canRead(byte type) {
            return Type.NULL.getTypeValue() == type || Type.STRING.getTypeValue() == type
                    || Type.ZIPSTRING.getTypeValue() == type;
        }

        @Override
        public boolean canWrite(Object t, byte type) {
            return t instanceof String;
        }

        @Override
        public String read(ByteBuffer byteBuffer) {
            byte b = nextType(byteBuffer);
            if (b == Type.STRING.getTypeValue()) {
                if(stringDataSerialize == null)
                    stringDataSerialize = new StringDataSerialize(charset);
                return stringDataSerialize.read(byteBuffer);
            }
            else {
                if(zipStringDataSerialize == null)
                    zipStringDataSerialize = new ZipStringDataSerialize(charset);
                return zipStringDataSerialize.read(byteBuffer);
            }
        }

        @Override
        public void write(ByteBuffer byteBuffer, String s) {
            if(zipSize == 0 || s.length() < zipSize) {
                if(stringDataSerialize == null)
                    stringDataSerialize = new StringDataSerialize(charset);
                stringDataSerialize.write(byteBuffer,s);
            } else {
                if(zipStringDataSerialize == null)
                    zipStringDataSerialize = new ZipStringDataSerialize(charset);
                zipStringDataSerialize.write(byteBuffer,s);
            }
        }
    }
    public static class MapDataSerialize implements DataSerialize<Map<String,Object>> {

        DataSerialize<Object> serialize;

        public MapDataSerialize(DataSerialize<Object> serialize) {
            this.serialize = serialize;
        }

        public MapDataSerialize() {
            serialize = BASE_DATA_SERIALIZE;
        }

        @Override
        public boolean canRead(byte type) {
            return Type.NULL.getTypeValue() == type || Type.MAP.getTypeValue() == type;
        }

        @Override
        public boolean canWrite(Object t,byte type) {
            return t instanceof Map;
        }

        @Override
        public Map<String, Object> read(ByteBuffer byteBuffer) {
            byte b = byteBuffer.get();
            if(b == Type.MAP.getTypeValue()) {
                int size = byteBuffer.getInt();
                HashMap<String, Object> res = new HashMap<>(size);
                for (int i = 0; i < size; i++) {
                    String key = AUTOSTRING_DATA_SERIALIZE.read(byteBuffer);
                    Object read = BASE_DATA_SERIALIZE.read(byteBuffer);
                    res.put(key,read);
                }
                return res;
            }
            return null;
        }

        @Override
        public void write(ByteBuffer byteBuffer, Map<String,Object> val) {
            if(val == null)
                byteBuffer.put(Type.NULL.getTypeValue());
            else {
                byteBuffer.put(Type.MAP.getTypeValue());
                byteBuffer.putInt(val.size());
                for (String key : val.keySet()) {
                    AUTOSTRING_DATA_SERIALIZE.write(byteBuffer,key);
                    Object o = val.get(key);
                    BASE_DATA_SERIALIZE.write(byteBuffer,o);
                }
            }
        }
    }
    public static class ListDataSerialize implements DataSerialize<List<Object>> {

        DataSerialize<Object> serialize;

        public ListDataSerialize(DataSerialize<Object> serialize) {
            this.serialize = serialize;
        }

        public ListDataSerialize() {
            this.serialize = BASE_DATA_SERIALIZE;
        }

        @Override
        public boolean canRead(byte type) {
            return Type.NULL.getTypeValue() == type || Type.ARRAY.getTypeValue() == type;
        }

        @Override
        public boolean canWrite(Object t, byte type) {
            return t instanceof List;
        }

        @Override
        public List<Object> read(ByteBuffer byteBuffer) {
            byte b = byteBuffer.get();
            if(b == Type.ARRAY.getTypeValue()) {
                int size = byteBuffer.getInt();
                ArrayList<Object> res = new ArrayList<>(size);
                for (int i = 0; i < size; i++) {
                    res.add(serialize.read(byteBuffer));
                }
                return res;
            }
            return null;
        }

        @Override
        public void write(ByteBuffer byteBuffer, List<Object> val) {
            if(val == null)
                byteBuffer.put(Type.NULL.getTypeValue());
            else {
                byteBuffer.put(Type.ARRAY.getTypeValue());
                byteBuffer.putInt(val.size());
                for (Object o : val) {
                    serialize.write(byteBuffer,o);
                }
            }
        }
    }
    public static class SetDataSerialize implements DataSerialize<Set<Object>> {

        DataSerialize<Object> serialize;

        public SetDataSerialize(DataSerialize<Object> serialize) {
            this.serialize = serialize;
        }

        public SetDataSerialize() {
            this.serialize = BASE_DATA_SERIALIZE;
        }

        @Override
        public boolean canRead(byte type) {
            return Type.NULL.getTypeValue() == type || Type.ARRAY.getTypeValue() == type;
        }

        @Override
        public boolean canWrite(Object t, byte type) {
            return t instanceof List;
        }

        @Override
        public Set<Object> read(ByteBuffer byteBuffer) {
            byte b = byteBuffer.get();
            if(b == Type.ARRAY.getTypeValue() || b == Type.SET.getTypeValue()) {
                int size = byteBuffer.getInt();
                HashSet<Object> res = new HashSet<>(size);
                for (int i = 0; i < size; i++) {
                    res.add(serialize.read(byteBuffer));
                }
                return res;
            }
            return null;
        }

        @Override
        public void write(ByteBuffer byteBuffer, Set<Object> val) {
            if(val == null)
                byteBuffer.put(Type.NULL.getTypeValue());
            else {
                byteBuffer.put(Type.SET.getTypeValue());
                byteBuffer.putInt(val.size());
                for (Object o : val) {
                    serialize.write(byteBuffer,o);
                }
            }
        }
    }
    public static class BaseDataSerializeComponent implements DataSerialize<Object> {

        @Override
        public boolean canRead(byte type) {
            return true;
        }

        @Override
        public boolean canWrite(Object t, byte type) {
            return true;
        }

        @Override
        public Object read(ByteBuffer byteBuffer) {
            byte b = nextType(byteBuffer);
            switch (b) {
                case 0x01:
                    return VOID_DATA_SERIALIZER.read(byteBuffer);
                case 0x02:
                    return BYTES_DATA_SERIALIZE.read(byteBuffer);
                case 0x03:
                    return CHARACTER_DATA_SERIALIZE.read(byteBuffer);
                case 0x04:
                    return SHORT_DATA_SERIALIZE.read(byteBuffer);
                case 0x05:
                    return BOOLEAN_DATA_SERIALIZE.read(byteBuffer);
                case 0x06:
                    return INTEGER_DATA_SERIALIZE.read(byteBuffer);
                case 0x07:
                    return FLOAT_DATA_SERIALIZE.read(byteBuffer);
                case 0x08:
                    return LONG_DATA_SERIALIZE.read(byteBuffer);
                case 0x09:
                    return DOUBLE_DATA_SERIALIZE.read(byteBuffer);
                case 0x0A:
                case 0x0E:
                    return AUTOSTRING_DATA_SERIALIZE.read(byteBuffer);
                case 0x0B:
                    return LOCAL_TIME_DATA_SERIALIZE.read(byteBuffer);
                case 0x0C:
                    return LOCAL_DATE_DATA_SERIALIZE.read(byteBuffer);
                case 0x0D:
                    return LOCAL_DATE_TIME_DATA_SERIALIZE.read(byteBuffer);
                case 0x11:
                    return LIST_DATA_SERIALIZE.read(byteBuffer);
                case 0x012:
                    return SET_DATA_SERIALIZE.read(byteBuffer);
                case 0x13:
                    return MAP_DATA_SERIALIZE.read(byteBuffer);
            }
            return null;
        }

        @Override
        public void write(ByteBuffer byteBuffer, Object o) {
            if(o == null)
                VOID_DATA_SERIALIZER.write(byteBuffer,null);
            else if(BYTES_DATA_SERIALIZE.canWrite(byteBuffer,Type.BYTES.getTypeValue()))
                BYTES_DATA_SERIALIZE.write(byteBuffer,(byte[])o);
            else if(o instanceof Character t)
                CHARACTER_DATA_SERIALIZE.write(byteBuffer,t);
            else if(o instanceof Short t)
                SHORT_DATA_SERIALIZE.write(byteBuffer,t);
            else if(o instanceof Boolean t)
                BOOLEAN_DATA_SERIALIZE.write(byteBuffer,t);
            else if(o instanceof Integer t)
                INTEGER_DATA_SERIALIZE.write(byteBuffer,t);
            else if(o instanceof Float t)
                FLOAT_DATA_SERIALIZE.write(byteBuffer,t);
            else if(o instanceof Long t)
                LONG_DATA_SERIALIZE.write(byteBuffer,t);
            else if(o instanceof Double t)
                DOUBLE_DATA_SERIALIZE.write(byteBuffer,t);
            else if(o instanceof String t)
                AUTOSTRING_DATA_SERIALIZE.write(byteBuffer,t);
            else if(o instanceof LocalTime t)
                LOCAL_TIME_DATA_SERIALIZE.write(byteBuffer,t);
            else if(o instanceof LocalDate t)
                LOCAL_DATE_DATA_SERIALIZE.write(byteBuffer,t);
            else if(o instanceof LocalDateTime t)
                LOCAL_DATE_TIME_DATA_SERIALIZE.write(byteBuffer,t);
            else if(o instanceof List t)
                LIST_DATA_SERIALIZE.write(byteBuffer,t);
            else if(o instanceof Set t)
                SET_DATA_SERIALIZE.write(byteBuffer,t);
            else if(o instanceof Map t)
                MAP_DATA_SERIALIZE.write(byteBuffer,t);
        }
    }

    public static final BaseDataSerializeComponent BASE_DATA_SERIALIZE = new BaseDataSerializeComponent();
    public static final DataSerialize<String> AUTOSTRING_DATA_SERIALIZE = new AutoStringDataSerialize(StandardCharsets.UTF_8,1024);
    public static final DataSerialize<Integer> INTEGER_DATA_SERIALIZE = new DataSerialize<>() {
        @Override
        public boolean canRead(byte type) {
            return Type.NULL.getTypeValue() == type || Type.INT32.getTypeValue() == type;
        }

        @Override
        public boolean canWrite(Object t,byte type) {
            return t instanceof Integer;
        }

        @Override
        public Integer read(ByteBuffer byteBuffer) {
            byte b = byteBuffer.get();
            if (b == Type.INT32.getTypeValue())
                return byteBuffer.getInt();
            return null;
        }

        @Override
        public void write(ByteBuffer byteBuffer, Integer val) {
            if(val == null)
                byteBuffer.put(Type.NULL.getTypeValue());
            else {
                byteBuffer.put(Type.INT32.getTypeValue());
                byteBuffer.putInt(val);
            }
        }
    };
    public static final DataSerialize<Long> LONG_DATA_SERIALIZE = new DataSerialize<>() {
        @Override
        public boolean canRead(byte type) {
            return Type.NULL.getTypeValue() == type || Type.INT64.getTypeValue() == type;
        }

        @Override
        public boolean canWrite(Object t,byte type) {
            return t instanceof Long;
        }

        @Override
        public Long read(ByteBuffer byteBuffer) {
            byte b = byteBuffer.get();
            if(b == Type.INT64.getTypeValue())
                return byteBuffer.getLong();
            return null;
        }

        @Override
        public void write(ByteBuffer byteBuffer, Long val) {
            if(val == null)
                byteBuffer.put(Type.NULL.getTypeValue());
            else {
                byteBuffer.put(Type.INT64.getTypeValue());
                byteBuffer.putLong(val);
            }
        }
    };
    public static final DataSerialize<Boolean> BOOLEAN_DATA_SERIALIZE = new DataSerialize<>() {
        @Override
        public boolean canRead(byte type) {
            return Type.NULL.getTypeValue() == type || Type.BOOL.getTypeValue() == type;
        }

        @Override
        public boolean canWrite(Object t,byte type) {
            return t instanceof Long;
        }

        @Override
        public Boolean read(ByteBuffer byteBuffer) {
            byte b = byteBuffer.get();
            if(b == Type.BOOL.getTypeValue()) {
                byte bool = byteBuffer.get();
                return bool == 0x00 ? false : true;
            }
            return null;
        }

        @Override
        public void write(ByteBuffer byteBuffer, Boolean val) {
            if(val == null)
                byteBuffer.put(Type.NULL.getTypeValue());
            else {
                byteBuffer.put(Type.BOOL.getTypeValue());
                byteBuffer.put(val ? (byte) 0x01 : (byte) 0x00);
            }
        }
    };
    public static final DataSerialize<Double> DOUBLE_DATA_SERIALIZE = new DataSerialize<>() {
        @Override
        public boolean canRead(byte type) {
            return Type.NULL.getTypeValue() == type || Type.DOUBLE.getTypeValue() == type;
        }

        @Override
        public boolean canWrite(Object t,byte type) {
            return t instanceof Double;
        }

        @Override
        public Double read(ByteBuffer byteBuffer) {
            byte b = byteBuffer.get();
            if(b == Type.DOUBLE.getTypeValue())
                return byteBuffer.getDouble();
            return null;
        }

        @Override
        public void write(ByteBuffer byteBuffer, Double val) {
            if(val == null)
                byteBuffer.put(Type.NULL.getTypeValue());
            else {
                byteBuffer.put(Type.DOUBLE.getTypeValue());
                byteBuffer.putDouble(val);
            }
        }
    };
    public static final DataSerialize<LocalTime> LOCAL_TIME_DATA_SERIALIZE = new DataSerialize<>() {
        @Override
        public boolean canRead(byte type) {
            return Type.NULL.getTypeValue() == type || Type.TIME.getTypeValue() == type;
        }

        @Override
        public boolean canWrite(Object t,byte type) {
            return t instanceof LocalTime;
        }

        @Override
        public LocalTime read(ByteBuffer byteBuffer) {
            byte b = byteBuffer.get();
            if(b == Type.TIME.getTypeValue()) {
                int anInt = byteBuffer.getInt();
                LocalTime localTime = LocalTime.ofSecondOfDay(anInt);
                return localTime;
            }
            return null;
        }

        @Override
        public void write(ByteBuffer byteBuffer, LocalTime val) {
            if(val == null)
                byteBuffer.put(Type.NULL.getTypeValue());
            else {
                byteBuffer.put(Type.TIME.getTypeValue());
                int i = val.toSecondOfDay();
                byteBuffer.putInt(i);
            }
        }
    };
    public static final DataSerialize<LocalDate> LOCAL_DATE_DATA_SERIALIZE = new DataSerialize<>() {
        @Override
        public boolean canRead(byte type) {
            return Type.NULL.getTypeValue() == type || Type.DATE.getTypeValue() == type;
        }

        @Override
        public boolean canWrite(Object t,byte type) {
            return t instanceof LocalDate;
        }

        @Override
        public LocalDate read(ByteBuffer byteBuffer) {
            byte b = byteBuffer.get();
            if(b == Type.DATE.getTypeValue()) {
                long aLong = byteBuffer.getLong();
                LocalDate localDate = LocalDate.ofEpochDay(aLong);
                return localDate;
            }
            return null;
        }

        @Override
        public void write(ByteBuffer byteBuffer, LocalDate val) {
            if(val == null)
                byteBuffer.put(Type.NULL.getTypeValue());
            else {
                byteBuffer.put(Type.DATE.getTypeValue());
                long i = val.toEpochDay();
                byteBuffer.putLong(i);
            }
        }
    };
    public static final DataSerialize<Short> SHORT_DATA_SERIALIZE = new DataSerialize<>() {
        @Override
        public boolean canRead(byte type) {
            return Type.NULL.getTypeValue() == type || Type.SHORT.getTypeValue() == type;
        }

        @Override
        public boolean canWrite(Object t,byte type) {
            return t instanceof Short;
        }

        @Override
        public Short read(ByteBuffer byteBuffer) {
            byte b = byteBuffer.get();
            if(b == Type.SHORT.getTypeValue())
                return byteBuffer.getShort();
            return null;
        }

        @Override
        public void write(ByteBuffer byteBuffer, Short val) {
            if(val == null)
                byteBuffer.put(Type.NULL.getTypeValue());
            else {
                byteBuffer.put(Type.SHORT.getTypeValue());
                byteBuffer.putShort(val);
            }
        }
    };
    public static final DataSerialize<Float> FLOAT_DATA_SERIALIZE = new DataSerialize<>() {
        @Override
        public boolean canRead(byte type) {
            return Type.NULL.getTypeValue() == type || Type.FLOAT.getTypeValue() == type;
        }

        @Override
        public boolean canWrite(Object t,byte type) {
            return t instanceof Float;
        }

        @Override
        public Float read(ByteBuffer byteBuffer) {
            byte b = byteBuffer.get();
            if(b == Type.FLOAT.getTypeValue()) {
                return byteBuffer.getFloat();
            }
            return null;
        }

        @Override
        public void write(ByteBuffer byteBuffer, Float val) {
            if(val == null)
                byteBuffer.put(Type.NULL.getTypeValue());
            else {
                byteBuffer.put(Type.FLOAT.getTypeValue());
                byteBuffer.putFloat(val);
            }
        }
    };
    public static final DataSerialize<LocalDateTime> LOCAL_DATE_TIME_DATA_SERIALIZE = new DataSerialize<>() {
        @Override
        public boolean canRead(byte type) {
            return Type.NULL.getTypeValue() == type || Type.DATETIME.getTypeValue() == type;
        }

        @Override
        public boolean canWrite(Object t,byte type) {
            return t instanceof LocalDateTime;
        }

        @Override
        public LocalDateTime read(ByteBuffer byteBuffer) {
            byte b = byteBuffer.get();
            if(b == Type.DATETIME.getTypeValue()) {
                long aLong = byteBuffer.getLong();
                LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(aLong, 0, ZoneOffset.UTC);
                return localDateTime;
            }
            return null;
        }

        @Override
        public void write(ByteBuffer byteBuffer, LocalDateTime val) {
            if(val == null)
                byteBuffer.put(Type.NULL.getTypeValue());
            else {
                byteBuffer.put(Type.DATETIME.getTypeValue());
                long l = val.toEpochSecond(ZoneOffset.UTC);
                byteBuffer.putLong(l);
            }
        }
    };
    public static final DataSerialize<Character> CHARACTER_DATA_SERIALIZE = new DataSerialize<>() {
        @Override
        public boolean canRead(byte type) {
            return Type.NULL.getTypeValue() == type || Type.CHAR.getTypeValue() == type;
        }

        @Override
        public boolean canWrite(Object t,byte type) {
            return t instanceof Charset;
        }

        @Override
        public Character read(ByteBuffer byteBuffer) {
            byte b = byteBuffer.get();
            if(b == Type.CHAR.getTypeValue()) {
                return byteBuffer.getChar();
            }
            return null;
        }

        @Override
        public void write(ByteBuffer byteBuffer, Character val) {
            if(val == null)
                byteBuffer.put(Type.NULL.getTypeValue());
            else {
                byteBuffer.put(Type.CHAR.getTypeValue());
                byteBuffer.putChar(val);
            }
        }
    };
    public static final DataSerialize<byte[]> BYTES_DATA_SERIALIZE = new DataSerialize<>() {
        @Override
        public boolean canRead(byte type) {
            return Type.NULL.getTypeValue() == type || Type.BYTES.getTypeValue() == type;
        }

        @Override
        public boolean canWrite(Object t, byte type) {
            return t.getClass().isArray();
        }

        @Override
        public byte[] read(ByteBuffer byteBuffer) {
            byte b = byteBuffer.get();
            if(b == Type.BYTES.getTypeValue()) {
                int length = byteBuffer.getInt();
                byte[] res = new byte[length];
                byteBuffer.get(res,0,length);
                return res;
            }
            return null;
        }

        @Override
        public void write(ByteBuffer byteBuffer, byte[] val) {
            if(val == null)
                byteBuffer.put(Type.NULL.getTypeValue());
            else {
                byteBuffer.put(Type.BYTES.getTypeValue());
                byteBuffer.putInt(val.length);
                byteBuffer.put(val,0,val.length);
            }
        }
    };
    public static final DataSerialize<Void> VOID_DATA_SERIALIZER = new DataSerialize<>() {

        @Override
        public boolean canRead(byte type) {
            return Type.NULL.getTypeValue() == type;
        }

        @Override
        public boolean canWrite(Object t, byte type) {
            return t == null;
        }

        @Override
        public Void read(ByteBuffer byteBuffer) {
            byteBuffer.get();
            return null;
        }

        @Override
        public void write(ByteBuffer byteBuffer, Void unused) {
            byteBuffer.put(Type.NULL.getTypeValue());
        }
    };
    public static final DataSerialize<Set<Object>> SET_DATA_SERIALIZE = new SetDataSerialize();
    public static final DataSerialize<List<Object>> LIST_DATA_SERIALIZE = new ListDataSerialize();
    public static final DataSerialize<Map<String,Object>> MAP_DATA_SERIALIZE = new MapDataSerialize();
    public static final DataSerialize<BigDecimal> BIGDECIMAL_DATA_SERIALIZE = new DataSerialize<>() {
        @Override
        public boolean canRead(byte type) {
            return Type.NULL.getTypeValue() == type || Type.DECIMAL.getTypeValue() == type;
        }

        @Override
        public boolean canWrite(Object t, byte type) {
            return t instanceof BigDecimal;
        }

        @Override
        public BigDecimal read(ByteBuffer byteBuffer) {
            byte b = byteBuffer.get();
            if(Type.DECIMAL.getTypeValue() == b) {
                short scale = byteBuffer.getShort();
                short length = byteBuffer.getShort();
                byte[] bs = new byte[length];
                byteBuffer.get(bs,0,length);
                BigInteger intVal = new BigInteger(bs);
                BigDecimal bigDecimal = new BigDecimal(intVal, scale);
                return bigDecimal;
            }
            return null;
        }

        @Override
        public void write(ByteBuffer byteBuffer, BigDecimal val) {
            if(val == null) {
                byteBuffer.put(Type.NULL.getTypeValue());
                return;
            }
            short scale = (short) val.scale();
            byteBuffer.put(Type.DECIMAL.getTypeValue());
            byteBuffer.putShort(scale);
            try {
                Field intVal = BigDecimal.class.getDeclaredField("intVal");
                Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
                theUnsafe.setAccessible(true);
                Unsafe unsafe = (Unsafe)theUnsafe.get(null);
                long l = unsafe.objectFieldOffset(intVal);
                BigInteger iv = (BigInteger) unsafe.getObject(val, l);
                byte[] bs = iv.toByteArray();
                short length = (short) bs.length;
                byteBuffer.putShort(length);
                byteBuffer.put(bs);
            } catch (NoSuchFieldException e) {}
            catch (IllegalAccessException e) {}
        }
    };


}
