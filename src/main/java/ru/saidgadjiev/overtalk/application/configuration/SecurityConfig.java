package ru.saidgadjiev.overtalk.application.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import ru.saidgadjiev.overtalk.application.dao.UserDao;
import ru.saidgadjiev.overtalk.application.service.UserDetailsServiceImpl;

/**
 * Created by said on 18.03.2018.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private UserDao userDao;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.
                userDetailsService(new UserDetailsServiceImpl(userDao));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin()
                .loginPage("/api/user/signIn");

        http
                .logout()
                .logoutUrl("/api/user/signOut");
    }
}
