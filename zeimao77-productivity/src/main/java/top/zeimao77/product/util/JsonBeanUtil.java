package top.zeimao77.product.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class JsonBeanUtil {

    private static Logger logger = LogManager.getLogger(JsonBeanUtil.class);
    public static final JsonBeanUtil DEFAULT = new JsonBeanUtil();

    public ObjectMapper objectMapper;

    public JsonBeanUtil(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private JsonBeanUtil(){
        objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class,new LocalDateTimeSerializer(LocalDateTimeUtil.STANDARDDATETIMEFORMATTER));
        javaTimeModule.addDeserializer(LocalDateTime.class,new LocalDateTimeDeserializer(LocalDateTimeUtil.STANDARDDATETIMEFORMATTER));
        javaTimeModule.addSerializer(LocalDate.class,new LocalDateSerializer(LocalDateTimeUtil.STANDARDDATEFORMATTER));
        javaTimeModule.addDeserializer(LocalDate.class,new LocalDateDeserializer(LocalDateTimeUtil.STANDARDDATEFORMATTER));
        javaTimeModule.addSerializer(LocalTime.class,new LocalTimeSerializer(LocalDateTimeUtil.STANDARDTIMEFORMATTER));
        javaTimeModule.addDeserializer(LocalTime.class,new LocalTimeDeserializer(LocalDateTimeUtil.STANDARDTIMEFORMATTER));
        objectMapper.registerModule(javaTimeModule);
    }

    /**
     * ?????????JSON?????????
     * @param obj ??????
     * @return JSON?????????
     */
    public String toJsonString(Object obj) {
        String s = null;
        try {
            s = objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error("BEAN??????JSON??????",e);
        }
        return s;
    }

    /**
     * JSON??????????????????
     * @param str JSON?????????
     * @param clazz ?????????
     * @param <T> ??????????????????
     * @return ????????????
     */
    public <T> T toBean(String str,Class<T> clazz) {
        T t = null;
        try {
            t = objectMapper.readValue(str,clazz);
        } catch (JsonProcessingException e) {
            logger.error("JSON??????BEAN??????",e);
        }
        return t;
    }

    public String read(String json,String path) {
        String result = null;
        try {
            JsonNode jsonNode = objectMapper.readTree(json);
            result = jsonNode.findPath(path).asText();
        } catch (JsonProcessingException e) {
            logger.error("JSON????????????",e);
        }
        return result;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

}
