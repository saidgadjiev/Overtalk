package ru.saidgadjiev.overtalk.application.security;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import ru.saidgadjiev.overtalk.application.utils.ResponseUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RestLogoutSuccessHandler implements LogoutSuccessHandler {

    private static final Logger LOGGER = Logger.getLogger(RestLogoutSuccessHandler.class);

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        LOGGER.debug("Logout success");

        if (authentication == null) {
            ResponseUtils.sendResponseMessage(httpServletResponse, HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            ResponseUtils.sendResponseMessage(httpServletResponse, HttpServletResponse.SC_OK);
        }
    }
}
