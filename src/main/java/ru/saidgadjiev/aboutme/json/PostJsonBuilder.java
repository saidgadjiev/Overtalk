package ru.saidgadjiev.aboutme.json;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.LocalDateTime;

/**
 * Created by said on 14.08.2018.
 */
public class PostJsonBuilder {

    private static final String DATE_TIME_FORMAT = "dd-MM-yyyy HH:mm";

    private final JsonBuilder jsonBuilder = new JsonBuilder();

    public PostJsonBuilder() {
        jsonBuilder.addDateFormat(DATE_TIME_FORMAT);
    }

    public PostJsonBuilder id(int id) {
        jsonBuilder.add("id", id);

        return this;
    }

    public PostJsonBuilder title(String title) {
        jsonBuilder.add("title", title);

        return this;
    }

    public PostJsonBuilder createdDate(LocalDateTime createdDate) {
        jsonBuilder.add("createdDate", createdDate, DATE_TIME_FORMAT);

        return this;
    }

    public PostJsonBuilder content(String content) {
        jsonBuilder.add("content", content);

        return this;
    }

    public PostJsonBuilder commentsCount(long commentsCount) {
        jsonBuilder.add("commentsCount", commentsCount);

        return this;
    }

    public PostJsonBuilder likesCount(long likesCount) {
        jsonBuilder.add("likesCount", likesCount);

        return this;
    }

    public PostJsonBuilder liked(boolean liked) {
        jsonBuilder.add("liked", liked);

        return this;
    }


    public ObjectNode build() {
        return jsonBuilder.build();
    }
}
