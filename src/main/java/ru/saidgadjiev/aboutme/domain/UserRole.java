package ru.saidgadjiev.aboutme.domain;

import ru.saidgadjiev.aboutme.dao.SerialTypeDataPersister;
import ru.saidgadjiev.ormnext.core.field.DatabaseColumn;
import ru.saidgadjiev.ormnext.core.field.FetchType;
import ru.saidgadjiev.ormnext.core.field.ForeignColumn;
import ru.saidgadjiev.ormnext.core.table.DatabaseEntity;
import ru.saidgadjiev.ormnext.core.table.Unique;

@DatabaseEntity(
        name = "user_role",
        uniqueConstraints = {
                @Unique(columns = {"user", "role"})
        }
)
public class UserRole {

    @DatabaseColumn(id = true, generated = true, persisterClass = SerialTypeDataPersister.class)
    private Integer id;

    @DatabaseColumn(notNull = true)
    @ForeignColumn(foreignFieldName = "userName")
    private UserProfile user;

    @DatabaseColumn(notNull = true)
    @ForeignColumn(foreignFieldName = "name")
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

    @Override
    public String toString() {
        return "UserRole{" +
                "id=" + id +
                ", role=" + (role == null ? null : role.getName()) +
                '}';
    }
}
