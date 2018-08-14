package ru.saidgadjiev.aboutme.json;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Created by said on 14.08.2018.
 */
public class SkillJsonBuilder {

    private JsonBuilder jsonBuilder = new JsonBuilder();

    public SkillJsonBuilder id(int id) {
        jsonBuilder.add("id", id);

        return this;
    }

    public SkillJsonBuilder name(String name) {
        jsonBuilder.add("name", name);

        return this;
    }

    public SkillJsonBuilder percentage(int percentage) {
        jsonBuilder.add("percentage", percentage);

        return this;
    }

    public ObjectNode build() {
        return jsonBuilder.build();
    }
}
