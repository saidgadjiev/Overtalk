package ru.saidgadjiev.aboutme.filter;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.saidgadjiev.ormnext.core.dao.Session;
import ru.saidgadjiev.ormnext.core.dao.SessionManager;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@Component
public class OpenSessionInViewFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = Logger.getLogger(OpenSessionInViewFilter.class);

    @Autowired
    private SessionManager sessionManager;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        LOGGER.debug("Start open session in view");

        try(Session ignored = sessionManager.currentSession()) {
            filterChain.doFilter(request, response);
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
        LOGGER.debug("Session in view closed");
    }
}
