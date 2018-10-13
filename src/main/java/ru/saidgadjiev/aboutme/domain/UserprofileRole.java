package ru.saidgadjiev.aboutme.domain;

import ru.saidgadjiev.aboutme.dao.SerialTypeDataPersister;
import ru.saidgadjiev.ormnext.core.field.DatabaseColumn;
import ru.saidgadjiev.ormnext.core.field.ForeignColumn;
import ru.saidgadjiev.ormnext.core.table.DatabaseEntity;
import ru.saidgadjiev.ormnext.core.table.Unique;

@DatabaseEntity(
        uniqueConstraints = {
                @Unique(columns = {"user", "role"})
        }
)
public class UserprofileRole {

    @DatabaseColumn(id = true, generated = true, persisterClass = SerialTypeDataPersister.class)
    private Integer id;

    @DatabaseColumn(columnName = "userprofile_username", notNull = true)
    @ForeignColumn(foreignFieldName = "username")
    private Userprofile user;

    @DatabaseColumn(columnName = "role_name", notNull = true)
    @ForeignColumn(foreignFieldName = "name")
    private Role role;

    public UserprofileRole() {
    }

    public UserprofileRole(Userprofile user, Role role) {
        this.user = user;
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Userprofile getUser() {
        return user;
    }

    public void setUser(Userprofile user) {
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
        return "UserprofileRole{" +
                "id=" + id +
                ", role=" + (role == null ? null : role.getName()) +
                '}';
    }
}
