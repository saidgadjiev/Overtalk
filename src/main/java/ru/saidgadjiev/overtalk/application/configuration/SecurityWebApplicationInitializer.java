package ru.saidgadjiev.overtalk.application.configuration;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * Created by said on 19.03.2018.
 */
public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {

    public SecurityWebApplicationInitializer() {
        super(SecurityConfig.class);
    }
}
