package top.zeimao77.product.converter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import top.zeimao77.product.exception.BaseServiceRunException;
import top.zeimao77.product.factory.BeanFactory;

import java.io.IOException;

public class JacksonConverterSerializer extends JsonSerializer<String> implements ContextualSerializer {

    private String propertyName;
    private JacksonConvertion dictDesensitization;

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(value);
        String converterBean = this.dictDesensitization.converterBean();
        String format = this.dictDesensitization.format();
        try {
            IConverter bean = BeanFactory.DEFAULT.getBean(converterBean, IConverter.class);
            if(bean != null) {
                String name = bean.getName(value);
                gen.writeFieldName(this.propertyName + "_name");
                if("simple".equalsIgnoreCase(format)) {
                    gen.writeString(name);
                } else if("full".equalsIgnoreCase(format)) {
                    gen.writeString(String.format("[%s]%s",value,name));
                }
            }
        }catch (BaseServiceRunException e) {
        }
    }

        @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        this.dictDesensitization = property.getAnnotation(JacksonConvertion.class);
        if(dictDesensitization != null)
            this.propertyName = property.getName();
        return this.dictDesensitization != null ? this : prov.findNullKeySerializer(property.getType(),property);
    }
}
