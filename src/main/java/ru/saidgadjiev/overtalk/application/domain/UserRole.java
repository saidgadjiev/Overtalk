package ru.saidgadjiev.overtalk.application.domain;

import ru.saidgadjiev.orm.next.core.field.DBField;
import ru.saidgadjiev.orm.next.core.field.Getter;
import ru.saidgadjiev.orm.next.core.field.Setter;
import ru.saidgadjiev.orm.next.core.table.DBTable;
import ru.saidgadjiev.orm.next.core.table.Unique;
import ru.saidgadjiev.overtalk.application.common.Constants;

/**
 * Created by said on 18.03.2018.
 */
@DBTable(
        name = "user_role",
        uniqueConstraints = {
                @Unique(columns = {"user", "role"})
        }
)
public class UserRole {

    @Getter(name = "getId")
    @Setter(name = "setId")
    @DBField(id = true, generated = true, dataType = Constants.PK_TYPE)
    private Integer id;

    @Getter(name = "getUser")
    @Setter(name = "setUser")
    @DBField(foreign = true)
    private UserProfile user;

    @Getter(name = "getRole")
    @Setter(name = "setRole")
    @DBField(foreign = true)
    private Role role;

    public UserRole() {
    }

    public UserRole(UserProfile user, Role role) {
        this.user = user;
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserProfile getUser() {
        return user;
    }

    public void setUser(UserProfile user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
