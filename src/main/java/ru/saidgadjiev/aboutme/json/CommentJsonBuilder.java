package ru.saidgadjiev.aboutme.json;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.LocalDateTime;

/**
 * Created by said on 14.08.2018.
 */
public class CommentJsonBuilder {

    private static final String DATE_TIME_FORMAT = "dd-MM-yyyy HH:mm";

    private final JsonBuilder jsonBuilder = new JsonBuilder();

    public CommentJsonBuilder() {
        jsonBuilder.addDateFormat(DATE_TIME_FORMAT);
    }

    public CommentJsonBuilder id(int id) {
        jsonBuilder.add("id", id);

        return this;
    }

    public CommentJsonBuilder content(String content) {
        jsonBuilder.add("content", content);

        return this;
    }

    public CommentJsonBuilder createdDate(LocalDateTime createdDate) {
        jsonBuilder.add("createdDate", createdDate, DATE_TIME_FORMAT);

        return this;
    }

    public CommentJsonBuilder nickname(String nickname) {
        jsonBuilder.add("nickname", nickname);

        return this;
    }

    public ObjectNode build() {
        return jsonBuilder.build();
    }
}
