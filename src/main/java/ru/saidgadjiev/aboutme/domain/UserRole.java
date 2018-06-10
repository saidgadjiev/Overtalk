package ru.saidgadjiev.aboutme.domain;

import ru.saidgadjiev.ormnext.core.field.DatabaseColumn;
import ru.saidgadjiev.ormnext.core.field.ForeignColumn;
import ru.saidgadjiev.ormnext.core.table.DatabaseEntity;
import ru.saidgadjiev.ormnext.core.table.Unique;
import ru.saidgadjiev.ormnext.support.datapersister.SerialTypeDataPersister;

/**
 * Created by said on 18.03.2018.
 */
@DatabaseEntity(
        name = "user_role",
        uniqueConstraints = {
                @Unique(columns = {"user", "role"})
        }
)
public class UserRole {

    @DatabaseColumn(id = true, generated = true, dataType = SerialTypeDataPersister.SERIAL)
    private Integer id;

    @ForeignColumn
    private UserProfile user;

    @ForeignColumn
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
