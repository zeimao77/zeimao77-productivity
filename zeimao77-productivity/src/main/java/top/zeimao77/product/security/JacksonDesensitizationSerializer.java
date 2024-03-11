package top.zeimao77.product.security;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;

public class JacksonDesensitizationSerializer extends JsonSerializer<String> implements ContextualSerializer {

    private JacksonDesensitization jacksonDesensitization;

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        this.jacksonDesensitization = property.getAnnotation(JacksonDesensitization.class);
        return this.jacksonDesensitization != null ? this : prov.findNullKeySerializer(property.getType(),property);
    }

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        int type = jacksonDesensitization.type();
        switch (type) {
            case JacksonDesensitization.MOBILEPHONE -> gen.writeString(DesensitizationUtil.mobilephone(value));
            case JacksonDesensitization.IDENTITYCARD -> gen.writeString(DesensitizationUtil.identityCard(value));
            case JacksonDesensitization.EMAIL -> gen.writeString(DesensitizationUtil.email(value));
            case JacksonDesensitization.NAME -> gen.writeString(DesensitizationUtil.name(value));
            case JacksonDesensitization.TELEPHONE -> gen.writeString(DesensitizationUtil.telephone(value));
            default -> gen.writeString(value);
        }
    }


}
