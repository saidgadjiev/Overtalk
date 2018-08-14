package ru.saidgadjiev.aboutme.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by said on 14.08.2018.
 */
public class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String toJson(Object object) throws IOException {
        StringWriter writer = new StringWriter();

        OBJECT_MAPPER.writeValue(writer, object);

        return writer.toString();
    }
}
