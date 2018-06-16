package ru.saidgadjiev.aboutme.service;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * Created by said on 18.03.2018.
 */
public interface SecurityService {

    UserDetails findLoggedInUser();

    void login(String userName, String password);
}
