package top.zeimao77.product.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import top.zeimao77.product.exception.BaseServiceRunException;
import static top.zeimao77.product.exception.ExceptionCodeDefinition.APPERR;
import top.zeimao77.product.util.CalendarDateUtil;
import top.zeimao77.product.util.JsonBeanUtil;
import top.zeimao77.product.util.LocalDateTimeUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.function.Function;

/**
 * JSON字符串的简单解析
 * @author zeimao77
 * @since 2.0.9
 */
public class Ijson {

    private JsonNode jsonNode;

    private Ijson(JsonNode jsonNode) {
        this.jsonNode = jsonNode;
    }

    public Ijson create(JsonNode jsonNode) {
        return new Ijson(jsonNode);
    }

    public static Ijson parse(String json) {
        return parse(JsonBeanUtil.DEFAULT.getObjectMapper(),json);
    }

    /**
     * 将JSON字符串解析到Ijson
     * @param objectMapper 解析器
     * @param json JSON字符串
     * @return ISON对象
     */
    public static Ijson parse(ObjectMapper objectMapper, String json) {
        Ijson instance = null;
        try {
            JsonNode jsonNode = objectMapper.readTree(json);
            instance = new Ijson(jsonNode);
        } catch (JsonProcessingException e) {
            throw new BaseServiceRunException(APPERR,"JSON解析错误",e);
        }
        return instance;
    }

    /**
     * @param fieldName 键
     * @return IJSON对象
     */
    public Ijson getJsonObject(String fieldName){
        if(this.jsonNode.get(fieldName).getNodeType()== JsonNodeType.OBJECT) {
            JsonNode jsonNode = this.jsonNode.get(fieldName);
            return new Ijson(jsonNode);
        }
        throw new BaseServiceRunException(APPERR,String.format("json字段类型错误:%s不可以转换对Obj",this.jsonNode.getNodeType().name()));
    }

    /**
     * @param index 下标
     * @return IJSON对象
     */
    public Ijson getJsonObject(Integer index){
        if(this.jsonNode.get(index).getNodeType()== JsonNodeType.OBJECT) {
            JsonNode jsonNode = this.jsonNode.get(index);
            return new Ijson(jsonNode);
        }
        throw new BaseServiceRunException(APPERR,String.format("json字段类型错误:%s不可以转换对Obj",this.jsonNode.getNodeType().name()));
    }

    /**
     * @param fieldName 键
     * @return IJSON对象
     */
    public Ijson getJsonArray(String fieldName) {
        if(this.jsonNode.get(fieldName).getNodeType()== JsonNodeType.ARRAY) {
            JsonNode jsonNode = this.jsonNode.get(fieldName);
            return new Ijson(jsonNode);
        }
        throw new BaseServiceRunException(APPERR,String.format("json字段类型错误:%s不可以转换对Arr",this.jsonNode.getNodeType().name()));
    }

    public long size(String fieldName) {
        if(this.jsonNode.get(fieldName).getNodeType()== JsonNodeType.ARRAY) {
            return this.jsonNode.get(fieldName).size();
        }
        return -1;
    }

    public long size() {
        if(this.jsonNode.getNodeType() == JsonNodeType.ARRAY) {
            return this.jsonNode.size();
        }
        return -1;
    }

    /**
     * @param fieldName 字段名
     * @return 长整形值
     */
    public long getLongValue(String fieldName) {
        Long aLong = getLong(fieldName);
        return aLong == null ? 0L : aLong;
    }

    /**
     * @param fieldName 键
     * @return 长整型
     */
    public Long getLong(String fieldName) {
        if(!this.jsonNode.has(fieldName)) {
            return null;
        }
        JsonNodeType nodeType = this.jsonNode.get(fieldName).getNodeType();
        return switch (nodeType) {
            case NULL -> null;
            case NUMBER -> {
                JsonNode jsonNode = this.jsonNode.get(fieldName);
                yield jsonNode.asLong();
            }
            default -> throw new BaseServiceRunException(APPERR,String.format("json字段类型错误:%s不可以转换对Lon",this.jsonNode.getNodeType().name()));
        };
    }

    /**
     * @param fieldName 字段名
     * @return 字符串
     */
    public String getString(String fieldName) {
        if(!this.jsonNode.has(fieldName)) {
            return null;
        }
        JsonNode jsonNode1 = this.jsonNode.get(fieldName);
        return switch (jsonNode1.getNodeType()) {
            case NULL -> null;
            case STRING -> jsonNode1.asText();
            case NUMBER -> String.valueOf(jsonNode1.asLong());
            case BOOLEAN -> String.valueOf(jsonNode1.asBoolean());
            default -> throw new BaseServiceRunException(APPERR,String.format("json字段类型错误:%s不可以转换对Str",jsonNode1.getNodeType().name()));
        };
    }

    /**
     * @param fieldName 字段名
     * @return 整数
     */
    public Integer getIntValue(String fieldName) {
        Integer integer = getInteger(fieldName);
        return integer == null ? 0 : integer;
    }

    public Integer getInteger(String fieldName) {
        if(!this.jsonNode.has(fieldName)) {
            return null;
        }
        JsonNodeType nodeType = this.jsonNode.get(fieldName).getNodeType();
        return switch (nodeType) {
            case NULL -> null;
            case NUMBER -> {
                JsonNode jsonNode = this.jsonNode.get(fieldName);
                yield jsonNode.asInt();
            }
            default -> throw new BaseServiceRunException(APPERR,String.format("json字段类型错误:%s不可以转换对Int",this.jsonNode.getNodeType().name()));
        };
    }

    public Double getDouble(String fieldName) {
        if(!this.jsonNode.has(fieldName)) {
            return null;
        }
        JsonNodeType nodeType = this.jsonNode.get(fieldName).getNodeType();
        return switch (nodeType) {
            case NULL -> null;
            case NUMBER -> {
                JsonNode jsonNode = this.jsonNode.get(fieldName);
                yield jsonNode.asDouble();
            }
            default -> throw new BaseServiceRunException(APPERR,String.format("json字段类型错误:%s不可以转换对Dou",this.jsonNode.getNodeType().name()));
        };
    }

    /**
     * @param fieldName 字段名
     * @return 浮点型
     */
    public Float getFloat(String fieldName) {
        Double aDouble = getDouble(fieldName);
        return aDouble == null?null:Float.valueOf(aDouble.floatValue());
    }

    /**
     * @param fieldName 字段名
     * @return 浮点型值
     */
    public float getFloatValue(String fieldName) {
        Float aFloat = getFloat(fieldName);
        return aFloat==null?0F:aFloat.floatValue();
    }

    /**
     * @param fieldName 字段名
     * @return boolean值
     */
    public boolean getBooleanValue(String fieldName) {
        Boolean aBoolean = getBoolean(fieldName);
        return aBoolean == null ? false : aBoolean;
    }

    /**
     * @param fieldName 字段名
     * @return Boolean对象
     */
    public Boolean getBoolean(String fieldName) {
        if(!this.jsonNode.has(fieldName)) {
            return null;
        }
        JsonNodeType nodeType = this.jsonNode.get(fieldName).getNodeType();
        return switch (nodeType) {
            case NULL -> null;
            case BOOLEAN -> {
                JsonNode jsonNode = this.jsonNode.get(fieldName);
                yield jsonNode.asBoolean();
            }
            default -> throw new BaseServiceRunException(APPERR,String.format("json字段类型错误:%s不可以转换对Bool",this.jsonNode.getNodeType().name()));
        };
    }

    public double getDoubleValue(String fieldName) {
        Double aDouble = getDouble(fieldName);
        return aDouble == null ? 0D : aDouble;
    }

    public BigDecimal getDecimal(String fieldName) {
        Double aDouble = getDouble(fieldName);
        return aDouble == null ? null : BigDecimal.valueOf(aDouble);
    }

    public LocalDateTime getLocalDateTime(String fieldName) {
        String string = getString(fieldName);
        return LocalDateTimeUtil.parseDateTime(string);
    }

    /**
     * @param fieldName 字段名
     * @return LocalDate对象
     */
    public LocalDate getLocalDate(String fieldName) {
        String string = getString(fieldName);
        return LocalDateTimeUtil.parseDate(string);
    }

    /**
     * @param fieldName 字段名
     * @return LocalTime对象
     */
    public LocalTime getLocalTime(String fieldName) {
        String string = getString(fieldName);
        return LocalDateTimeUtil.parseTime(string);
    }

    /**
     * @param fieldName 字段名
     * @return Date对象
     */
    public Date getDate(String fieldName) {
        String string = getString(fieldName);
        return CalendarDateUtil.parseDateTime(string);
    }

    /**
     * 获取一个字符串，手动转换类型
     * @param fieldName 字符名
     * @param fun 转换函数
     * @param <T> 返回类型
     * @return 转换后的结果
     */
    public <T> T getT(String fieldName, Function<String,T> fun) {
        String string = getString(fieldName);
        return fun.apply(string);
    }

    /**
     * 返回字符串结果
     * @param pretty 是否需要换行美化
     * @return 结果字符串
     */
    public String toJsonString(boolean pretty) {
        return pretty?jsonNode.toPrettyString():jsonNode.toString();
    }

}
