package ru.saidgadjiev.overtalk.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import ru.saidgadjiev.overtalk.application.domain.UserRole;
import ru.saidgadjiev.overtalk.application.model.ResponseMessage;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by said on 25.03.2018.
 */
public class Test {

    public static void main(String[] args) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        User user = new User("said1995", "test1", grantedAuthorities);
        ResponseMessage<User> responseMessage = new ResponseMessage<User>(200);
        System.out.println(objectMapper.writeValueAsString(responseMessage));
    }
}
