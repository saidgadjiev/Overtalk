package ru.saidgadjiev.overtalk.application.service;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * Created by said on 18.03.2018.
 */
public interface SecurityService {

    UserDetails findLoggedInUserName();

    void login(String userName, String password);
}
