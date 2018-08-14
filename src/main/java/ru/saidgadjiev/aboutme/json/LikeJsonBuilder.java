package ru.saidgadjiev.aboutme.json;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.RawValue;
import ru.saidgadjiev.aboutme.domain.Skill;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by said on 14.08.2018.
 */
public class LikeJsonBuilder {

    private final JsonBuilder jsonBuilder = new JsonBuilder();

    public LikeJsonBuilder postId(int postId) {
        jsonBuilder.add("postId", postId);

        return this;
    }

    public LikeJsonBuilder likesCount(long likesCount) {
        jsonBuilder.add("likesCount", likesCount);

        return this;
    }

    public LikeJsonBuilder liked(boolean liked) {
        jsonBuilder.add("liked", liked);

        return this;
    }

    public ObjectNode build() {
        return jsonBuilder.build();
    }
}
