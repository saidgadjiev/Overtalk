package ru.saidgadjiev.overtalk.application.service;

import org.springframework.stereotype.Service;
import ru.saidgadjiev.overtalk.application.dao.UserDao;
import ru.saidgadjiev.overtalk.application.domain.UserProfile;
import ru.saidgadjiev.overtalk.application.model.UserDetails;
import ru.saidgadjiev.overtalk.application.utils.DTOUtils;

import java.sql.SQLException;

/**
 * Created by said on 18.03.2018.
 */
@Service
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

    public UserProfile getByUserName(String userName) throws SQLException {
        return userDao.getByUserName(userName);
    }
}
