package ru.saidgadjiev.overtalk.application.service;

import ru.saidgadjiev.overtalk.application.dao.UserDao;
import ru.saidgadjiev.overtalk.application.domain.UserProfile;
import ru.saidgadjiev.overtalk.application.model.UserDetails;
import ru.saidgadjiev.overtalk.application.utils.DTOUtils;

/**
 * Created by said on 18.03.2018.
 */
public class UserService {

    private UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void create(UserDetails userDetails) {
        userDao.create(DTOUtils.convert(userDetails, UserProfile.class));
    }

    public boolean isExists(String userName) {
        return userDao.isExists(userName);
    }
}
