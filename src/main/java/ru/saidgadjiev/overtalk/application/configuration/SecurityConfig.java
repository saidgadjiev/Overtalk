package ru.saidgadjiev.overtalk.application.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import ru.saidgadjiev.overtalk.application.dao.UserDao;
import ru.saidgadjiev.overtalk.application.service.UserDetailsServiceImpl;
import ru.saidgadjiev.overtalk.application.service.UserService;

/**
 * Created by said on 18.03.2018.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.
                userDetailsService(new UserDetailsServiceImpl(userService));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable();
        http
                .formLogin()
                .loginPage("/api/user/signIn");
        http
                .logout()
                .logoutUrl("/api/user/signOut");
        http
                .authorizeRequests()
                .antMatchers("/api/user/signUp").permitAll();
    }
}
