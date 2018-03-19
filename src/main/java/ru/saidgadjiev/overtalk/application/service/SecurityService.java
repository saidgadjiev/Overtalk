package ru.saidgadjiev.overtalk.application.service;

/**
 * Created by said on 18.03.2018.
 */
public interface SecurityService {

    String findLoggedInUserName();

    void login(String userName, String password);
}
