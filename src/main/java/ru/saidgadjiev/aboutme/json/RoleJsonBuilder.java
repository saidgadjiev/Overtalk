package ru.saidgadjiev.aboutme.json;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Created by said on 14.08.2018.
 */
public class RoleJsonBuilder {

    private JsonBuilder jsonBuilder = new JsonBuilder();

    public RoleJsonBuilder name(String name) {
        jsonBuilder.add("name", name);

        return this;
    }

    public ObjectNode build() {
        return jsonBuilder.build();
    }
}
