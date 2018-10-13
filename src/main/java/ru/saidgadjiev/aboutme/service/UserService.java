package ru.saidgadjiev.aboutme.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.saidgadjiev.aboutme.dao.RoleDao;
import ru.saidgadjiev.aboutme.dao.UserProfileDao;
import ru.saidgadjiev.aboutme.domain.Role;
import ru.saidgadjiev.aboutme.domain.Userprofile;
import ru.saidgadjiev.aboutme.domain.UserprofileRole;
import ru.saidgadjiev.aboutme.model.UserRequest;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by said on 18.03.2018.
 */
@Service
public class UserService {

    private UserProfileDao userProfileDao;

    private RoleDao roleDao;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserProfileDao userProfileDao, RoleDao roleDao, PasswordEncoder passwordEncoder) {
        this.userProfileDao = userProfileDao;
        this.roleDao = roleDao;
        this.passwordEncoder = passwordEncoder;
    }

    public void create(UserRequest userRequest) throws SQLException {
        Userprofile userprofile = new Userprofile();
        Role userRole = roleDao.queryForId(1);
        Set<UserprofileRole> userprofileRoles = new HashSet<>();

        userprofile.setUsername(userRequest.getUsername());
        userprofile.setNickname(userRequest.getNickname());
        userprofile.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        userprofileRoles.add(new UserprofileRole(userprofile, userRole));
        userprofile.setUserprofileRoles(userprofileRoles);
        userProfileDao.create(userprofile);
    }

    public boolean isExistUserName(String username) throws SQLException {
        return userProfileDao.isExistUsername(username);
    }

    public boolean isExistNickName(String nickname) throws SQLException {
        return userProfileDao.isExistNickName(nickname);
    }

    public Userprofile getByUserName(String username) throws SQLException {
        return userProfileDao.getByUserName(username);
    }

    public List<Userprofile> getAll(int limit, long offset) throws SQLException {
        return userProfileDao.getList(limit, offset);
    }

    public long countOff() throws SQLException {
        return userProfileDao.countOff();
    }
}
