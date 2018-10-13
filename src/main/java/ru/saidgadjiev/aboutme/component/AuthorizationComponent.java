package ru.saidgadjiev.aboutme.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.saidgadjiev.aboutme.dao.CommentDao;
import ru.saidgadjiev.aboutme.domain.Comment;
import ru.saidgadjiev.aboutme.domain.Role;
import ru.saidgadjiev.aboutme.service.SecurityService;

import java.sql.SQLException;

@Component("authorization")
public class AuthorizationComponent {

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private SecurityService securityService;

    public boolean canEditComment(Integer id) throws SQLException {
        Comment comment = commentDao.getById(id);

        UserDetails userDetails = securityService.findLoggedInUser();

        if (userDetails == null) {
            return false;
        }
        for (GrantedAuthority authority: userDetails.getAuthorities()) {
            if (authority.getAuthority().equals(Role.ROLE_ADMIN)) {
                return true;
            }
        }

        return userDetails.getUsername().equals(comment.getUser().getUsername());
    }

    public boolean canDeleteComment(Integer id) throws SQLException {
        return canEditComment(id);
    }
}
