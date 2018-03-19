package ru.saidgadjiev.overtalk.application.model;

import javax.validation.constraints.NotNull;

/**
 * Created by said on 18.03.2018.
 */
public class UserDetails {

    @NotNull
    private String userName;

    @NotNull
    private String password;

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

}
