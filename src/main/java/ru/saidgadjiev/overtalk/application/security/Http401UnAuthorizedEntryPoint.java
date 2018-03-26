package ru.saidgadjiev.overtalk.application.security;

import org.apache.log4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import ru.saidgadjiev.overtalk.application.utils.ResponseUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by said on 25.03.2018.
 */
public class Http401UnAuthorizedEntryPoint implements AuthenticationEntryPoint {

    private static final Logger LOGGER = Logger.getLogger(Http401UnAuthorizedEntryPoint.class);

    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        LOGGER.debug("User unauthorized");

        ResponseUtils.sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "User unauthorized");
    }
}
