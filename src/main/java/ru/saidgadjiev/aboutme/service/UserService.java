package ru.saidgadjiev.aboutme.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.saidgadjiev.aboutme.dao.RoleDao;
import ru.saidgadjiev.aboutme.dao.UserDao;
import ru.saidgadjiev.aboutme.domain.Role;
import ru.saidgadjiev.aboutme.domain.UserProfile;
import ru.saidgadjiev.aboutme.domain.UserRole;
import ru.saidgadjiev.aboutme.model.UserDetails;
import ru.saidgadjiev.aboutme.utils.DTOUtils;

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

    public Set<Role> create(UserDetails userDetails) throws SQLException {
        UserProfile userProfile = DTOUtils.convert(userDetails, UserProfile.class);
        Role userRole = roleDao.queryForId(1);
        Set<UserRole> userRoles = new HashSet<>();

        userProfile.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        userRoles.add(new UserRole(userProfile, userRole));
        userProfile.setUserRoles(userRoles);
        userDao.create(userProfile);

        return Collections.singleton(userRole);
    }

    public boolean isExists(String userName) throws SQLException {
        return userDao.isExists(userName);
    }

    public UserProfile getByUserName(String userName) throws SQLException {
        return userDao.getByUserName(userName);
    }

    public Page<UserDetails> getAll(Pageable pageable) throws SQLException {
        long totalCount = userDao.countOff();
        List<UserProfile> userProfiles = userDao.getAll(pageable.getPageSize(), pageable.getOffset());

        return new PageImpl<>(DTOUtils.convert(userProfiles, UserDetails.class), pageable, totalCount);
    }
}
