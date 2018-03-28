package ru.saidgadjiev.overtalk.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import ru.saidgadjiev.orm.next.core.dao.Session;
import ru.saidgadjiev.orm.next.core.dao.SessionManager;
import ru.saidgadjiev.overtalk.application.configuration.OrmNextConfiguration;
import ru.saidgadjiev.overtalk.application.domain.UserProfile;
import ru.saidgadjiev.overtalk.application.domain.UserRole;
import ru.saidgadjiev.overtalk.application.model.ResponseMessage;
import ru.saidgadjiev.overtalk.application.model.UserDetails;
import ru.saidgadjiev.overtalk.application.utils.DTOUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by said on 25.03.2018.
 */
public class Test {

    public static void main(String[] args) throws Exception {
        SessionManager sessionManager = new OrmNextConfiguration().sessionManager();
        Session session = sessionManager.getCurrentSession();
        UserProfile userProfile = session.queryForId(UserProfile.class, 9);
        UserDetails userDetails = DTOUtils.convert(userProfile, UserDetails.class);

        System.out.println(userDetails);
    }
}
