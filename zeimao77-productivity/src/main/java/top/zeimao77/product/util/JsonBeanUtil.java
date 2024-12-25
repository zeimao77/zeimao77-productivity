package top.zeimao77.product.util;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class JsonBeanUtil {

    private static Logger logger = LoggerFactory.getLogger(JsonBeanUtil.class);
    public static final JsonBeanUtil DEFAULT = new JsonBeanUtil();

    public ObjectMapper objectMapper;

    public JsonBeanUtil(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private JsonBeanUtil(){
        objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class,new LocalDateTimeSerializer(LocalDateTimeUtil.STANDARDDATETIMEFORMATTER));
        javaTimeModule.addDeserializer(LocalDateTime.class,new JsonDeserializer<LocalDateTime>() {

            @Override
            public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
                String text = jsonParser.getText();
                return LocalDateTimeUtil.smartParse(text);
            }
        });
        javaTimeModule.addSerializer(LocalDate.class,new LocalDateSerializer(LocalDateTimeUtil.STANDARDDATEFORMATTER));
        javaTimeModule.addDeserializer(LocalDate.class,new LocalDateDeserializer(LocalDateTimeUtil.STANDARDDATEFORMATTER));
        javaTimeModule.addSerializer(LocalTime.class,new LocalTimeSerializer(LocalDateTimeUtil.STANDARDTIMEFORMATTER));
        javaTimeModule.addDeserializer(LocalTime.class,new LocalTimeDeserializer(LocalDateTimeUtil.STANDARDTIMEFORMATTER));
        javaTimeModule.addSerializer(StringOptional.class,new JsonSerializer<StringOptional>() {
            @Override
            public void serialize(StringOptional value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeString(value.isBlank() ? null : value.get());
            }
        });
        javaTimeModule.addDeserializer(StringOptional.class,new JsonDeserializer<StringOptional>() {
            @Override
            public StringOptional deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                if (p.hasToken(JsonToken.VALUE_STRING)) {
                    return new StringOptional(p.getText());
                }
                return StringOptional.empty();
            }
        });
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(javaTimeModule);
    }

    /**
     * 对象转JSON字符串
     * @param obj 对象
     * @return JSON字符串
     */
    public String toJsonString(Object obj) {
        String s = null;
        try {
            s = objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error("BEAN转换JSON出错",e);
        }
        return s;
    }

    /**
     * JSON字符串转对象
     * @param str JSON字符串
     * @param clazz 目标类
     * @param <T> 目标对象类型
     * @return 转换结果
     */
    public <T> T toBean(String str,Class<T> clazz) {
        T t = null;
        try {
            t = objectMapper.readValue(str,clazz);
        } catch (JsonProcessingException e) {
            logger.error("JSON转换BEAN出错",e);
        }
        return t;
    }

    public String read(String json,String path) {
        String result = null;
        try {
            JsonNode jsonNode = objectMapper.readTree(json);
            result = jsonNode.findPath(path).asText();
        } catch (JsonProcessingException e) {
            logger.error("JSON读取出错",e);
        }
        return result;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }




}
