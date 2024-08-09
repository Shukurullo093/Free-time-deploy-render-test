package com.time.demo.dto;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class CustomSerializer extends StdSerializer<Object> {

    public CustomSerializer() {
        super(Object.class);
    }

    @Override
    public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        String fieldName = value.getClass().getName(); // Dynamically determine field name
        jgen.writeObjectField(fieldName, value);
        jgen.writeEndObject();
    }
}
