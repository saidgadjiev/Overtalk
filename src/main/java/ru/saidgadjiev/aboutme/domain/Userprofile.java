package ru.saidgadjiev.aboutme.domain;

import ru.saidgadjiev.aboutme.dao.SerialTypeDataPersister;
import ru.saidgadjiev.ormnext.core.field.DatabaseColumn;
import ru.saidgadjiev.ormnext.core.field.FetchType;
import ru.saidgadjiev.ormnext.core.field.ForeignCollectionField;

import java.util.HashSet;
import java.util.Set;

public class Userprofile {

    @DatabaseColumn(id = true, generated = true, persisterClass = SerialTypeDataPersister.class)
    private Integer id;

    @DatabaseColumn(notNull = true, unique = true)
    private String username;

    @DatabaseColumn(notNull = true, unique = true)
    private String nickname;

    @DatabaseColumn(notNull = true)
    private String password;

    @ForeignCollectionField(foreignFieldName = "user", fetchType = FetchType.LAZY)
    private Set<UserprofileRole> userprofileRoles = new HashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Set<UserprofileRole> getUserprofileRoles() {
        return userprofileRoles;
    }

    public void setUserprofileRoles(Set<UserprofileRole> userprofileRoles) {
        this.userprofileRoles = userprofileRoles;
    }

    @Override
    public String toString() {
        return "Userprofile{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
