package demo.api.server.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.commons.lang3.ObjectUtils;

import java.io.IOException;
import java.util.Map;

public class JsonUtils {
    public static SimpleModule allowMapNotNullJsonSerializer() {
        SimpleModule simpleModule = new SimpleModule("emptyFilter", new Version(1, 0, 0, null, null, null));

        simpleModule.addSerializer(Map.class, new JsonSerializer<Map>() {
            @Override
            public void serialize(Map value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeStartObject();

                for (Object key : value.keySet()) {
                    if (ObjectUtils.isNotEmpty(key) &&  ObjectUtils.isNotEmpty(value.get(key))) {
                        gen.writeObjectField((String)key, value.get(key));
                    }
                }

                gen.writeEndObject();
            }
        });
        return simpleModule;
    }
}
