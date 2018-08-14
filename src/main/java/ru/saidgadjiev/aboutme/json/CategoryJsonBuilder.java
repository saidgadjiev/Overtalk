package ru.saidgadjiev.aboutme.json;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Created by said on 14.08.2018.
 */
public class CategoryJsonBuilder {

    private JsonBuilder jsonBuilder = new JsonBuilder();

    public CategoryJsonBuilder id(int id) {
        jsonBuilder.add("id", id);

        return this;
    }

    public CategoryJsonBuilder name(String name) {
        jsonBuilder.add("name", name);

        return this;
    }

    public CategoryJsonBuilder description(String description) {
        jsonBuilder.add("description", description);

        return this;
    }

    public CategoryJsonBuilder postsCount(int postsCount) {
        jsonBuilder.add("postsCount", postsCount);

        return this;
    }

    public ObjectNode build() {
        return jsonBuilder.build();
    }
}
