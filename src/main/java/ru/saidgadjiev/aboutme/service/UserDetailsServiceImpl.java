package ru.saidgadjiev.aboutme.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.saidgadjiev.aboutme.domain.Userprofile;
import ru.saidgadjiev.aboutme.domain.UserprofileRole;
import ru.saidgadjiev.aboutme.model.ExtSpringUser;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by said on 18.03.2018.
 */

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserService userService;

    @Autowired
    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        try {
            Userprofile userprofile = userService.getByUserName(s);
            Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

            for (UserprofileRole userprofileRole : userprofile.getUserprofileRoles()) {
                grantedAuthorities.add(new SimpleGrantedAuthority(userprofileRole.getRole().getName()));
            }

            return new ExtSpringUser(
                    userprofile.getNickname(),
                    userprofile.getUsername(),
                    userprofile.getPassword(),
                    grantedAuthorities
            );
        } catch (SQLException ex) {
            throw new UsernameNotFoundException(ex.getMessage(), ex);
        }
    }
}
