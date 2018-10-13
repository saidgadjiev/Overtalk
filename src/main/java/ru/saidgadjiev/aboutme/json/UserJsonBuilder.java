package ru.saidgadjiev.aboutme.json;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.RawValue;
import ru.saidgadjiev.aboutme.domain.UserprofileRole;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by said on 14.08.2018.
 */
public class UserJsonBuilder {

    private final JsonBuilder jsonBuilder = new JsonBuilder();

    public UserJsonBuilder username(String username) {
        jsonBuilder.add("username", username);

        return this;
    }

    public UserJsonBuilder roles(Collection<UserprofileRole> roles) {
        List<RawValue> values = new ArrayList<>();

        for (UserprofileRole role: roles) {
            values.add(new RawValue(new RoleJsonBuilder().name(role.getRole().getName()).build()));
        }

        jsonBuilder.add("roles", values);

        return this;
    }

    public UserJsonBuilder nickname(String nickname) {
        jsonBuilder.add("nickname", nickname);

        return this;
    }

    public ObjectNode build() {
        return jsonBuilder.build();
    }
}
