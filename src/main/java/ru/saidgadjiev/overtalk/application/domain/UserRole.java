package ru.saidgadjiev.overtalk.application.domain;

import ru.saidgadjiev.orm.next.core.field.DBField;
import ru.saidgadjiev.orm.next.core.field.DataType;
import ru.saidgadjiev.orm.next.core.field.Getter;
import ru.saidgadjiev.orm.next.core.field.Setter;
import ru.saidgadjiev.orm.next.core.table.DBTable;
import ru.saidgadjiev.orm.next.core.table.Unique;

/**
 * Created by said on 18.03.2018.
 */
@DBTable(
        name = "user_role",
        uniqueConstraints = {
                @Unique(columns = {"userProfile", "role"})
        }
)
public class UserRole {

    @Getter(name = "getId")
    @Setter(name = "setId")
    @DBField(id = true, generated = true, dataType = 8)
    private Integer id;

    @Getter(name = "getUserProfile")
    @Setter(name = "setUserProfile")
    @DBField(foreign = true)
    private UserProfile userProfile;

    @Getter(name = "getRole")
    @Setter(name = "setRole")
    @DBField(foreign = true)
    private Role role;

    public UserRole() {
    }

    public UserRole(UserProfile userProfile, Role role) {
        this.userProfile = userProfile;
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
