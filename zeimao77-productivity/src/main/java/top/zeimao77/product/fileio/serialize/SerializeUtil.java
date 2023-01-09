package top.zeimao77.product.fileio.serialize;

import sun.misc.Unsafe;
import top.zeimao77.product.exception.BaseServiceRunException;
import top.zeimao77.product.exception.ExceptionCodeDefinition;
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

import static top.zeimao77.product.fileio.serialize.Type.*;

public class SerializeUtil {

    public interface DataSerialize<T> {
        boolean canRead(byte type);
        boolean canWrite(Object t,byte type);
        T read(ByteBuffer byteBuffer);
        void write(ByteBuffer byteBuffer,T t);
    }

    public static byte nextType(ByteBuffer byteBuffer) {
        if(byteBuffer.position() >= byteBuffer.limit())
            throw new BaseServiceRunException(ExceptionCodeDefinition.NOT_SUPPORTED,"没有更多可读取数据;");
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
            return NULL.getTypeValue() == type || MAP.getTypeValue() == type;
        }

        @Override
        public boolean canWrite(Object t,byte type) {
            return (t instanceof String) && (STRING.getTypeValue() == type);
        }

        @Override
        public String read(ByteBuffer byteBuffer) {
            byte b = byteBuffer.get();
            if(b == STRING.getTypeValue()) {
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
                byteBuffer.put(NULL.getTypeValue());
            else {
                byteBuffer.put(STRING.getTypeValue());
                byte[] bytes = val.getBytes(charset);
                byteBuffer.putInt(bytes.length);
                byteBuffer.put(bytes);
            }
        }
    }
    public static class ZipStringDataSerialize implements DataSerialize<String> {

        private Charset charset;

        public ZipStringDataSerialize(Charset charset) {
            this.charset = charset;
        }

        public ZipStringDataSerialize() {
            this.charset = StandardCharsets.UTF_8;
        }

        @Override
        public boolean canRead(byte type) {
            return NULL.getTypeValue() == type || ZIPSTRING.getTypeValue() == type;
        }

        @Override
        public boolean canWrite(Object t, byte type) {
            return t instanceof String;
        }

        @Override
        public String read(ByteBuffer byteBuffer) {
            byte b = byteBuffer.get();
            if(b == ZIPSTRING.getTypeValue()) {
                int anInt = byteBuffer.getInt();
                byte[] bs = new byte[anInt];
                byteBuffer.get(bs,0,anInt);
                byte[] zipDecode = ByteArrayCoDesUtil.zipDecode(bs);
                return new String(zipDecode, charset);
            }
            return null;
        }

        @Override
        public void write(ByteBuffer byteBuffer, String val) {
            if(val == null)
                byteBuffer.put(NULL.getTypeValue());
            else {
                byteBuffer.put(ZIPSTRING.getTypeValue());
                byte[] bytes = val.getBytes(charset);
                byte[] zipDecode = ByteArrayCoDesUtil.zipEncode(bytes);
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
            return NULL.getTypeValue() == type || STRING.getTypeValue() == type
                    || ZIPSTRING.getTypeValue() == type;
        }

        @Override
        public boolean canWrite(Object t, byte type) {
            return t instanceof String;
        }

        @Override
        public String read(ByteBuffer byteBuffer) {
            byte b = nextType(byteBuffer);
            if (b == STRING.getTypeValue()) {
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
            return NULL.getTypeValue() == type || MAP.getTypeValue() == type;
        }

        @Override
        public boolean canWrite(Object t,byte type) {
            return t instanceof Map;
        }

        @Override
        public Map<String, Object> read(ByteBuffer byteBuffer) {
            byte b = byteBuffer.get();
            if(b == MAP.getTypeValue()) {
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
                byteBuffer.put(NULL.getTypeValue());
            else {
                byteBuffer.put(MAP.getTypeValue());
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
            return NULL.getTypeValue() == type || ARRAY.getTypeValue() == type;
        }

        @Override
        public boolean canWrite(Object t, byte type) {
            return t instanceof List;
        }

        @Override
        public List<Object> read(ByteBuffer byteBuffer) {
            byte b = byteBuffer.get();
            if(b == ARRAY.getTypeValue()) {
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
                byteBuffer.put(NULL.getTypeValue());
            else {
                byteBuffer.put(ARRAY.getTypeValue());
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
            return NULL.getTypeValue() == type || ARRAY.getTypeValue() == type;
        }

        @Override
        public boolean canWrite(Object t, byte type) {
            return t instanceof List;
        }

        @Override
        public Set<Object> read(ByteBuffer byteBuffer) {
            byte b = byteBuffer.get();
            if(b == ARRAY.getTypeValue() || b == SET.getTypeValue()) {
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
                byteBuffer.put(NULL.getTypeValue());
            else {
                byteBuffer.put(SET.getTypeValue());
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
            Type type = parse(b);
            switch (type) {
                case NULL:
                    return VOID_DATA_SERIALIZER.read(byteBuffer);
                case BYTES:
                    return BYTES_DATA_SERIALIZE.read(byteBuffer);
                case CHAR:
                    return CHARACTER_DATA_SERIALIZE.read(byteBuffer);
                case SHORT:
                    return SHORT_DATA_SERIALIZE.read(byteBuffer);
                case BOOL:
                    return BOOLEAN_DATA_SERIALIZE.read(byteBuffer);
                case INT32:
                    return INTEGER_DATA_SERIALIZE.read(byteBuffer);
                case FLOAT:
                    return FLOAT_DATA_SERIALIZE.read(byteBuffer);
                case INT64:
                    return LONG_DATA_SERIALIZE.read(byteBuffer);
                case DOUBLE:
                    return DOUBLE_DATA_SERIALIZE.read(byteBuffer);
                case STRING:
                case ZIPSTRING:
                    return AUTOSTRING_DATA_SERIALIZE.read(byteBuffer);
                case TIME:
                    return LOCAL_TIME_DATA_SERIALIZE.read(byteBuffer);
                case DATE:
                    return LOCAL_DATE_DATA_SERIALIZE.read(byteBuffer);
                case DATETIME:
                    return LOCAL_DATE_TIME_DATA_SERIALIZE.read(byteBuffer);
                case ARRAY:
                    return LIST_DATA_SERIALIZE.read(byteBuffer);
                case SET:
                    return SET_DATA_SERIALIZE.read(byteBuffer);
                case MAP:
                    return MAP_DATA_SERIALIZE.read(byteBuffer);
            }
            return null;
        }

        @Override
        public void write(ByteBuffer byteBuffer, Object o) {
            if(o == null)
                VOID_DATA_SERIALIZER.write(byteBuffer,null);
            else if(BYTES_DATA_SERIALIZE.canWrite(byteBuffer, BYTES.getTypeValue()))
                BYTES_DATA_SERIALIZE.write(byteBuffer,(byte[])o);
            else if(o instanceof Character) {
                Character t = (Character) o;
                CHARACTER_DATA_SERIALIZE.write(byteBuffer,t);
            } else if(o instanceof Short) {
                Short t = (Short) o;
                SHORT_DATA_SERIALIZE.write(byteBuffer,t);
            } else if(o instanceof Boolean) {
                Boolean t = (Boolean) o;
                BOOLEAN_DATA_SERIALIZE.write(byteBuffer, t);
            } else if(o instanceof Integer) {
                Integer t = (Integer) o;
                INTEGER_DATA_SERIALIZE.write(byteBuffer, t);
            } else if(o instanceof Float) {
                Float t = (Float) o;
                FLOAT_DATA_SERIALIZE.write(byteBuffer, t);
            } else if(o instanceof Long) {
                Long t = (Long) o;
                LONG_DATA_SERIALIZE.write(byteBuffer, t);
            } else if(o instanceof Double) {
                Double t = (Double) o;
                DOUBLE_DATA_SERIALIZE.write(byteBuffer, t);
            } else if(o instanceof String) {
                String t = (String) o;
                AUTOSTRING_DATA_SERIALIZE.write(byteBuffer, t);
            } else if(o instanceof LocalTime) {
                LocalTime t = (LocalTime) o;
                LOCAL_TIME_DATA_SERIALIZE.write(byteBuffer, t);
            } else if(o instanceof LocalDate) {
                LocalDate t = (LocalDate) o;
                LOCAL_DATE_DATA_SERIALIZE.write(byteBuffer, t);
            } else if(o instanceof LocalDateTime) {
                LocalDateTime t = (LocalDateTime) o;
                LOCAL_DATE_TIME_DATA_SERIALIZE.write(byteBuffer, t);
            } else if(o instanceof List) {
                List t = (List) o;
                LIST_DATA_SERIALIZE.write(byteBuffer, t);
            } else if(o instanceof Set) {
                Set t = (Set) o;
                SET_DATA_SERIALIZE.write(byteBuffer, t);
            } else if(o instanceof Map) {
                Map t = (Map) o;
                MAP_DATA_SERIALIZE.write(byteBuffer, t);
            }
        }
    }

    public static final BaseDataSerializeComponent BASE_DATA_SERIALIZE = new BaseDataSerializeComponent();
    public static final DataSerialize<String> AUTOSTRING_DATA_SERIALIZE = new AutoStringDataSerialize(StandardCharsets.UTF_8,1024);
    public static final DataSerialize<Integer> INTEGER_DATA_SERIALIZE = new DataSerialize<Integer>() {
        @Override
        public boolean canRead(byte type) {
            return NULL.getTypeValue() == type || INT32.getTypeValue() == type;
        }

        @Override
        public boolean canWrite(Object t,byte type) {
            return t instanceof Integer;
        }

        @Override
        public Integer read(ByteBuffer byteBuffer) {
            byte b = byteBuffer.get();
            if (b == INT32.getTypeValue())
                return byteBuffer.getInt();
            return null;
        }

        @Override
        public void write(ByteBuffer byteBuffer, Integer val) {
            if(val == null)
                byteBuffer.put(NULL.getTypeValue());
            else {
                byteBuffer.put(INT32.getTypeValue());
                byteBuffer.putInt(val);
            }
        }
    };
    public static final DataSerialize<Long> LONG_DATA_SERIALIZE = new DataSerialize<Long>() {
        @Override
        public boolean canRead(byte type) {
            return NULL.getTypeValue() == type || INT64.getTypeValue() == type;
        }

        @Override
        public boolean canWrite(Object t,byte type) {
            return t instanceof Long;
        }

        @Override
        public Long read(ByteBuffer byteBuffer) {
            byte b = byteBuffer.get();
            if(b == INT64.getTypeValue())
                return byteBuffer.getLong();
            return null;
        }

        @Override
        public void write(ByteBuffer byteBuffer, Long val) {
            if(val == null)
                byteBuffer.put(NULL.getTypeValue());
            else {
                byteBuffer.put(INT64.getTypeValue());
                byteBuffer.putLong(val);
            }
        }
    };
    public static final DataSerialize<Boolean> BOOLEAN_DATA_SERIALIZE = new DataSerialize<Boolean>() {
        @Override
        public boolean canRead(byte type) {
            return NULL.getTypeValue() == type || BOOL.getTypeValue() == type;
        }

        @Override
        public boolean canWrite(Object t,byte type) {
            return t instanceof Long;
        }

        @Override
        public Boolean read(ByteBuffer byteBuffer) {
            byte b = byteBuffer.get();
            if(b == BOOL.getTypeValue()) {
                byte bool = byteBuffer.get();
                return bool == 0x00 ? false : true;
            }
            return null;
        }

        @Override
        public void write(ByteBuffer byteBuffer, Boolean val) {
            if(val == null)
                byteBuffer.put(NULL.getTypeValue());
            else {
                byteBuffer.put(BOOL.getTypeValue());
                byteBuffer.put(val ? (byte) 0x01 : (byte) 0x00);
            }
        }
    };
    public static final DataSerialize<Double> DOUBLE_DATA_SERIALIZE = new DataSerialize<Double>() {
        @Override
        public boolean canRead(byte type) {
            return NULL.getTypeValue() == type || DOUBLE.getTypeValue() == type;
        }

        @Override
        public boolean canWrite(Object t,byte type) {
            return t instanceof Double;
        }

        @Override
        public Double read(ByteBuffer byteBuffer) {
            byte b = byteBuffer.get();
            if(b == DOUBLE.getTypeValue())
                return byteBuffer.getDouble();
            return null;
        }

        @Override
        public void write(ByteBuffer byteBuffer, Double val) {
            if(val == null)
                byteBuffer.put(NULL.getTypeValue());
            else {
                byteBuffer.put(DOUBLE.getTypeValue());
                byteBuffer.putDouble(val);
            }
        }
    };
    public static final DataSerialize<LocalTime> LOCAL_TIME_DATA_SERIALIZE = new DataSerialize<LocalTime>() {
        @Override
        public boolean canRead(byte type) {
            return NULL.getTypeValue() == type || TIME.getTypeValue() == type;
        }

        @Override
        public boolean canWrite(Object t,byte type) {
            return t instanceof LocalTime;
        }

        @Override
        public LocalTime read(ByteBuffer byteBuffer) {
            byte b = byteBuffer.get();
            if(b == TIME.getTypeValue()) {
                int anInt = byteBuffer.getInt();
                LocalTime localTime = LocalTime.ofSecondOfDay(anInt);
                return localTime;
            }
            return null;
        }

        @Override
        public void write(ByteBuffer byteBuffer, LocalTime val) {
            if(val == null)
                byteBuffer.put(NULL.getTypeValue());
            else {
                byteBuffer.put(TIME.getTypeValue());
                int i = val.toSecondOfDay();
                byteBuffer.putInt(i);
            }
        }
    };
    public static final DataSerialize<LocalDate> LOCAL_DATE_DATA_SERIALIZE = new DataSerialize<LocalDate>() {
        @Override
        public boolean canRead(byte type) {
            return NULL.getTypeValue() == type || DATE.getTypeValue() == type;
        }

        @Override
        public boolean canWrite(Object t,byte type) {
            return t instanceof LocalDate;
        }

        @Override
        public LocalDate read(ByteBuffer byteBuffer) {
            byte b = byteBuffer.get();
            if(b == DATE.getTypeValue()) {
                long aLong = byteBuffer.getLong();
                LocalDate localDate = LocalDate.ofEpochDay(aLong);
                return localDate;
            }
            return null;
        }

        @Override
        public void write(ByteBuffer byteBuffer, LocalDate val) {
            if(val == null)
                byteBuffer.put(NULL.getTypeValue());
            else {
                byteBuffer.put(DATE.getTypeValue());
                long i = val.toEpochDay();
                byteBuffer.putLong(i);
            }
        }
    };
    public static final DataSerialize<Short> SHORT_DATA_SERIALIZE = new DataSerialize<Short>() {
        @Override
        public boolean canRead(byte type) {
            return NULL.getTypeValue() == type || SHORT.getTypeValue() == type;
        }

        @Override
        public boolean canWrite(Object t,byte type) {
            return t instanceof Short;
        }

        @Override
        public Short read(ByteBuffer byteBuffer) {
            byte b = byteBuffer.get();
            if(b == SHORT.getTypeValue())
                return byteBuffer.getShort();
            return null;
        }

        @Override
        public void write(ByteBuffer byteBuffer, Short val) {
            if(val == null)
                byteBuffer.put(NULL.getTypeValue());
            else {
                byteBuffer.put(SHORT.getTypeValue());
                byteBuffer.putShort(val);
            }
        }
    };
    public static final DataSerialize<Float> FLOAT_DATA_SERIALIZE = new DataSerialize<Float>() {
        @Override
        public boolean canRead(byte type) {
            return NULL.getTypeValue() == type || FLOAT.getTypeValue() == type;
        }

        @Override
        public boolean canWrite(Object t,byte type) {
            return t instanceof Float;
        }

        @Override
        public Float read(ByteBuffer byteBuffer) {
            byte b = byteBuffer.get();
            if(b == FLOAT.getTypeValue()) {
                return byteBuffer.getFloat();
            }
            return null;
        }

        @Override
        public void write(ByteBuffer byteBuffer, Float val) {
            if(val == null)
                byteBuffer.put(NULL.getTypeValue());
            else {
                byteBuffer.put(FLOAT.getTypeValue());
                byteBuffer.putFloat(val);
            }
        }
    };
    public static final DataSerialize<LocalDateTime> LOCAL_DATE_TIME_DATA_SERIALIZE = new DataSerialize<LocalDateTime>() {
        @Override
        public boolean canRead(byte type) {
            return NULL.getTypeValue() == type || DATETIME.getTypeValue() == type;
        }

        @Override
        public boolean canWrite(Object t,byte type) {
            return t instanceof LocalDateTime;
        }

        @Override
        public LocalDateTime read(ByteBuffer byteBuffer) {
            byte b = byteBuffer.get();
            if(b == DATETIME.getTypeValue()) {
                long aLong = byteBuffer.getLong();
                LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(aLong, 0, ZoneOffset.UTC);
                return localDateTime;
            }
            return null;
        }

        @Override
        public void write(ByteBuffer byteBuffer, LocalDateTime val) {
            if(val == null)
                byteBuffer.put(NULL.getTypeValue());
            else {
                byteBuffer.put(DATETIME.getTypeValue());
                long l = val.toEpochSecond(ZoneOffset.UTC);
                byteBuffer.putLong(l);
            }
        }
    };
    public static final DataSerialize<Character> CHARACTER_DATA_SERIALIZE = new DataSerialize<Character>() {
        @Override
        public boolean canRead(byte type) {
            return NULL.getTypeValue() == type || CHAR.getTypeValue() == type;
        }

        @Override
        public boolean canWrite(Object t,byte type) {
            return t instanceof Charset;
        }

        @Override
        public Character read(ByteBuffer byteBuffer) {
            byte b = byteBuffer.get();
            if(b == CHAR.getTypeValue()) {
                return byteBuffer.getChar();
            }
            return null;
        }

        @Override
        public void write(ByteBuffer byteBuffer, Character val) {
            if(val == null)
                byteBuffer.put(NULL.getTypeValue());
            else {
                byteBuffer.put(CHAR.getTypeValue());
                byteBuffer.putChar(val);
            }
        }
    };
    public static final DataSerialize<byte[]> BYTES_DATA_SERIALIZE = new DataSerialize<byte[]>() {
        @Override
        public boolean canRead(byte type) {
            return NULL.getTypeValue() == type || BYTES.getTypeValue() == type;
        }

        @Override
        public boolean canWrite(Object t, byte type) {
            return t.getClass().isArray();
        }

        @Override
        public byte[] read(ByteBuffer byteBuffer) {
            byte b = byteBuffer.get();
            if(b == BYTES.getTypeValue()) {
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
                byteBuffer.put(NULL.getTypeValue());
            else {
                byteBuffer.put(BYTES.getTypeValue());
                byteBuffer.putInt(val.length);
                byteBuffer.put(val,0,val.length);
            }
        }
    };
    public static final DataSerialize<Void> VOID_DATA_SERIALIZER = new DataSerialize<Void>() {

        @Override
        public boolean canRead(byte type) {
            return NULL.getTypeValue() == type;
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
            byteBuffer.put(NULL.getTypeValue());
        }
    };
    public static final DataSerialize<Set<Object>> SET_DATA_SERIALIZE = new SetDataSerialize();
    public static final DataSerialize<List<Object>> LIST_DATA_SERIALIZE = new ListDataSerialize();
    public static final DataSerialize<Map<String,Object>> MAP_DATA_SERIALIZE = new MapDataSerialize();
    public static final DataSerialize<BigDecimal> BIGDECIMAL_DATA_SERIALIZE = new DataSerialize<BigDecimal>() {
        @Override
        public boolean canRead(byte type) {
            return NULL.getTypeValue() == type || DECIMAL.getTypeValue() == type;
        }

        @Override
        public boolean canWrite(Object t, byte type) {
            return t instanceof BigDecimal;
        }

        @Override
        public BigDecimal read(ByteBuffer byteBuffer) {
            byte b = byteBuffer.get();
            if(DECIMAL.getTypeValue() == b) {
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
                byteBuffer.put(NULL.getTypeValue());
                return;
            }
            short scale = (short) val.scale();
            byteBuffer.put(DECIMAL.getTypeValue());
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
