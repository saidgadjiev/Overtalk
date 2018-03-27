package ru.saidgadjiev.overtalk.application.security;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import ru.saidgadjiev.overtalk.application.utils.ResponseUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RestAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private static final Logger LOGGER = Logger.getLogger(RestAuthenticationFailureHandler.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        LOGGER.debug("Authentication failure");
        LOGGER.error(exception.getMessage(), exception);

        ResponseUtils.sendResponseMessage(response, HttpServletResponse.SC_BAD_REQUEST, "Username or password wrong");
    }
}
