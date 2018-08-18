package ru.saidgadjiev.aboutme.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.saidgadjiev.aboutme.model.CommentDetails;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by said on 14.08.2018.
 */
public class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static<T> T fromJson(String json, Class<T> clazz) throws IOException {
        return OBJECT_MAPPER.readValue(json, clazz);
    }

    public static String toJson(Object object) throws IOException {
        StringWriter writer = new StringWriter();

        OBJECT_MAPPER.writeValue(writer, object);

        return writer.toString();
    }
}
