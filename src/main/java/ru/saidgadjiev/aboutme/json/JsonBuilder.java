package ru.saidgadjiev.aboutme.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.RawValue;
import org.springframework.format.datetime.DateFormatter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by said on 14.08.2018.
 */
public class JsonBuilder {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private ObjectNode objectNode;

    private Map<String, DateTimeFormatter> formatterMap = new HashMap<>();

    public JsonBuilder() {
        this.objectNode = OBJECT_MAPPER.createObjectNode();
    }

    public JsonBuilder addDateFormat(String format) {
        formatterMap.put(format, DateTimeFormatter.ofPattern(format));

        return this;
    }

    public JsonBuilder add(String key, String value) {
        objectNode.put(key, value);

        return this;
    }

    public JsonBuilder add(String key, int value) {
        this.objectNode.put(key, value);

        return this;
    }

    public JsonBuilder add(String key, long value) {
        this.objectNode.put(key, value);

        return this;
    }

    public JsonBuilder add(String key, LocalDate localDate, String format) {
        this.objectNode.put(key, formatterMap.get(format).format(localDate));

        return this;
    }

    public JsonBuilder add(String key, LocalDateTime localDateTime, String format) {
        this.objectNode.put(key, formatterMap.get(format).format(localDateTime));

        return this;
    }

    public JsonBuilder add(String key, Collection<RawValue> values) {
        ArrayNode arrayNode = objectNode.putArray(key);

        values.forEach(arrayNode::addRawValue);

        return this;
    }

    public JsonBuilder add(String key, Object pojo) {
        objectNode.putPOJO(key, pojo);

        return this;
    }

    public JsonBuilder add(String key, boolean value) {
        objectNode.put(key, value);

        return this;
    }

    public JsonBuilder add(String key, ObjectNode objectNode) {
        this.objectNode.putRawValue(key, new RawValue(objectNode));

        return this;
    }

    public ObjectNode build() {
        return objectNode;
    }
}
