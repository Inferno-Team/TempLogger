package site.inferno_team.TempLogger.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.util.Map;

public class MapSerializer extends JsonSerializer<Map<?, ?>> {

    @Override
    public void serialize(Map<?, ?> map, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        jsonGenerator.writeStartArray();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeFieldName("left");
            jsonGenerator.writeObject(entry.getKey());
            jsonGenerator.writeFieldName("right");
            jsonGenerator.writeObject(entry.getValue());
            jsonGenerator.writeEndObject();
        }
        jsonGenerator.writeEndArray();
    }
}
