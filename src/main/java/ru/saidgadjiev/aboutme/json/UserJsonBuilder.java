package ru.saidgadjiev.aboutme.json;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.RawValue;
import ru.saidgadjiev.aboutme.domain.Role;
import ru.saidgadjiev.aboutme.domain.UserRole;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by said on 14.08.2018.
 */
public class UserJsonBuilder {

    private final JsonBuilder jsonBuilder = new JsonBuilder();

    public UserJsonBuilder userName(String userName) {
        jsonBuilder.add("userName", userName);

        return this;
    }

    public UserJsonBuilder roles(Collection<UserRole> roles) {
        List<RawValue> values = new ArrayList<>();

        for (UserRole role: roles) {
            values.add(new RawValue(new RoleJsonBuilder().name(role.getRole().getName()).build()));
        }

        jsonBuilder.add("roles", values);

        return this;
    }

    public UserJsonBuilder nickName(String nickName) {
        jsonBuilder.add("nickName", nickName);

        return this;
    }

    public ObjectNode build() {
        return jsonBuilder.build();
    }
}
