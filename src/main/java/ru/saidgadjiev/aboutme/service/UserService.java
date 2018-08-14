package ru.saidgadjiev.aboutme.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.saidgadjiev.aboutme.dao.RoleDao;
import ru.saidgadjiev.aboutme.dao.UserDao;
import ru.saidgadjiev.aboutme.domain.Role;
import ru.saidgadjiev.aboutme.domain.UserProfile;
import ru.saidgadjiev.aboutme.domain.UserRole;
import ru.saidgadjiev.aboutme.model.UserRequest;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by said on 18.03.2018.
 */
@Service
public class UserService {

    private UserDao userDao;

    private RoleDao roleDao;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserDao userDao, RoleDao roleDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.passwordEncoder = passwordEncoder;
    }

    public Set<Role> create(UserRequest userRequest) throws SQLException {
        UserProfile userProfile = new UserProfile();
        Role userRole = roleDao.queryForId(1);
        Set<UserRole> userRoles = new HashSet<>();

        userProfile.setUserName(userRequest.getUserName());
        userProfile.setNickName(userRequest.getNickName());
        userProfile.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        userRoles.add(new UserRole(userProfile, userRole));
        userProfile.setUserRoles(userRoles);
        userDao.create(userProfile);

        return Collections.singleton(userRole);
    }

    public boolean isExistUserName(String userName) throws SQLException {
        return userDao.isExistUserName(userName);
    }

    public boolean isExistNickName(String nickName) throws SQLException {
        return userDao.isExistNickName(nickName);
    }

    public UserProfile getByUserName(String userName) throws SQLException {
        return userDao.getByUserName(userName);
    }

    public List<UserProfile> getAll(int limit, long offset) throws SQLException {
        return userDao.getList(limit, offset);
    }

    public long countOff() throws SQLException {
        return userDao.countOff();
    }
}
