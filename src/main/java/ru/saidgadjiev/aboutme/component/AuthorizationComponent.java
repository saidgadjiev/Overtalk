package ru.saidgadjiev.aboutme.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.saidgadjiev.aboutme.domain.Role;
import ru.saidgadjiev.aboutme.model.CommentDetails;
import ru.saidgadjiev.aboutme.service.SecurityService;

@Component("authorization")
public class AuthorizationComponent {

    @Autowired
    private SecurityService securityService;

    public boolean canEditComment(CommentDetails commentDetails) {
        UserDetails userDetails = securityService.findLoggedInUser();

        if (userDetails == null) {
            return false;
        }
        for (GrantedAuthority authority: userDetails.getAuthorities()) {
            if (authority.getAuthority().equals(Role.ROLE_ADMIN)) {
                return true;
            }
        }

        return userDetails.getUsername().equals(commentDetails.getUser());
    }

    public boolean canDeleteComment(CommentDetails commentDetails) {
        return canEditComment(commentDetails);
    }
}
