package ru.saidgadjiev.aboutme.domain;

import ru.saidgadjiev.ormnext.core.field.DatabaseColumn;
import ru.saidgadjiev.ormnext.core.field.ForeignCollectionField;
import ru.saidgadjiev.ormnext.support.datapersister.SerialTypeDataPersister;

import java.util.HashSet;
import java.util.Set;

public class UserProfile {

    @DatabaseColumn(id = true, generated = true, dataType = SerialTypeDataPersister.SERIAL)
    private Integer id;

    @DatabaseColumn(notNull = true, unique = true)
    private String userName;

    @DatabaseColumn(notNull = true, unique = true)
    private String nickName;

    @DatabaseColumn(notNull = true)
    private String password;

    @ForeignCollectionField(foreignFieldName = "user")
    private Set<UserRole> userRoles = new HashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Set<UserRole> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(Set<UserRole> userRoles) {
        this.userRoles = userRoles;
    }
}
